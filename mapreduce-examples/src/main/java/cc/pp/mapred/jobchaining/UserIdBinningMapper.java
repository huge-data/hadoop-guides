package cc.pp.mapred.jobchaining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import cc.pp.mapred.utils.MapRedUtils;

public class UserIdBinningMapper extends Mapper<Object, Text, Text, Text> {

	public static final String AVERAGE_POSTS_PER_USER = "avg.posts.per.user";

	public static void setAveragePostsPerUser(Job job, double avg) {
		job.getConfiguration().set(AVERAGE_POSTS_PER_USER, Double.toString(avg));
	}

	public static double getAveragePostsPerUser(Configuration conf) {
		return Double.parseDouble(conf.get(AVERAGE_POSTS_PER_USER));
	}

	private double average = 0.0;
	private MultipleOutputs<Text, Text> mos = null;
	private final Text outkey = new Text(), outvalue = new Text();
	private final HashMap<String, String> userIdToReputation = new HashMap<String, String>();

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		average = getAveragePostsPerUser(context.getConfiguration());
		mos = new MultipleOutputs<Text, Text>(context);

		try {
			Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration());

			if (files == null || files.length == 0) {
				throw new RuntimeException("User information is not set in DistributedCache");
			}

			// Read all files in the DistributedCache
			for (Path p : files) {
				BufferedReader rdr = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(
						new File(p.toString())))));

				String line;
				// For each record in the user file
				while ((line = rdr.readLine()) != null) {

					// Get the user ID and reputation
					Map<String, String> parsed = MapRedUtils.transformXmlToMap(line);
					String userId = parsed.get("Id");
					String reputation = parsed.get("Reputation");

					if (userId != null && reputation != null) {
						// Map the user ID to the reputation
						userIdToReputation.put(userId, reputation);
					}
				}
				rdr.close();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		String[] tokens = value.toString().split("\t");

		String userId = tokens[0];
		int posts = Integer.parseInt(tokens[1]);

		outkey.set(userId);
		outvalue.set((long) posts + "\t" + userIdToReputation.get(userId));

		if (posts < average) {
			mos.write(JobChainingMain.MULTIPLE_OUTPUTS_BELOW_NAME, outkey, outvalue,
					JobChainingMain.MULTIPLE_OUTPUTS_BELOW_NAME + "/part");
		} else {
			mos.write(JobChainingMain.MULTIPLE_OUTPUTS_ABOVE_NAME, outkey, outvalue,
					JobChainingMain.MULTIPLE_OUTPUTS_ABOVE_NAME + "/part");
		}

	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		mos.close();
	}

}
