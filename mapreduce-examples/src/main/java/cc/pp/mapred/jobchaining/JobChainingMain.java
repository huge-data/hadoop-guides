package cc.pp.mapred.jobchaining;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.GenericOptionsParser;

public class JobChainingMain {

	public static final String AVERAGE_CALC_GROUP = "AverageCalculation";
	public static final String MULTIPLE_OUTPUTS_ABOVE_NAME = "aboveavg";
	public static final String MULTIPLE_OUTPUTS_BELOW_NAME = "belowavg";

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		if (otherArgs.length != 3) {
			System.err.println("Usage: JobChainingDriver <posts> <users> <out>");
			System.exit(2);
		}

		Path postInput = new Path(otherArgs[0]);
		Path userInput = new Path(otherArgs[1]);
		Path outputDirIntermediate = new Path(otherArgs[2] + "_int");
		Path outputDir = new Path(otherArgs[2]);

		// Setup first job to counter user posts
		Job countingJob = new Job(conf, "JobChaining-Counting");
		countingJob.setJarByClass(JobChainingMain.class);

		// Set our mapper and reducer, we can use the API's long sum reducer for
		// a combiner!
		countingJob.setMapperClass(UserIdCountMapper.class);
		countingJob.setCombinerClass(LongSumReducer.class);
		countingJob.setReducerClass(UserIdSumReducer.class);

		countingJob.setOutputKeyClass(Text.class);
		countingJob.setOutputValueClass(LongWritable.class);

		countingJob.setInputFormatClass(TextInputFormat.class);

		TextInputFormat.addInputPath(countingJob, postInput);

		countingJob.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(countingJob, outputDirIntermediate);

		// Execute job and grab exit code
		int code = countingJob.waitForCompletion(true) ? 0 : 1;

		if (code == 0) {
			// Calculate the average posts per user by getting counter values
			double numRecords = countingJob.getCounters()
					.findCounter(AVERAGE_CALC_GROUP, UserIdCountMapper.RECORDS_COUNTER_NAME).getValue();
			double numUsers = countingJob.getCounters()
					.findCounter(AVERAGE_CALC_GROUP, UserIdSumReducer.USERS_COUNTER_NAME).getValue();

			double averagePostsPerUser = numRecords / numUsers;

			// Setup binning job
			Job binningJob = new Job(new Configuration(), "JobChaining-Binning");
			binningJob.setJarByClass(JobChainingMain.class);

			// Set mapper and the average posts per user
			binningJob.setMapperClass(UserIdBinningMapper.class);
			UserIdBinningMapper.setAveragePostsPerUser(binningJob, averagePostsPerUser);

			binningJob.setNumReduceTasks(0);

			binningJob.setInputFormatClass(TextInputFormat.class);
			TextInputFormat.addInputPath(binningJob, outputDirIntermediate);

			// Add two named outputs for below/above average
			MultipleOutputs.addNamedOutput(binningJob, MULTIPLE_OUTPUTS_BELOW_NAME, TextOutputFormat.class, Text.class,
					Text.class);

			MultipleOutputs.addNamedOutput(binningJob, MULTIPLE_OUTPUTS_ABOVE_NAME, TextOutputFormat.class, Text.class,
					Text.class);
			MultipleOutputs.setCountersEnabled(binningJob, true);

			TextOutputFormat.setOutputPath(binningJob, outputDir);

			// Add the user files to the DistributedCache
			FileStatus[] userFiles = FileSystem.get(conf).listStatus(userInput);
			for (FileStatus status : userFiles) {
				DistributedCache.addCacheFile(status.getPath().toUri(), binningJob.getConfiguration());
			}

			// Execute job and grab exit code
			code = binningJob.waitForCompletion(true) ? 0 : 1;
		}

		// Clean up the intermediate output
		FileSystem.get(conf).delete(outputDirIntermediate, true);

		System.exit(code);
	}

}
