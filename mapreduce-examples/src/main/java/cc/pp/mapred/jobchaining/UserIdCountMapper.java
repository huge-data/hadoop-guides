package cc.pp.mapred.jobchaining;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cc.pp.mapred.utils.MapRedUtils;

public class UserIdCountMapper extends Mapper<Object, Text, Text, LongWritable> {

	public static final String RECORDS_COUNTER_NAME = "Records";

	private static final LongWritable ONE = new LongWritable(1);
	private final Text outkey = new Text();

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		// Parse the input into a nice map.
		Map<String, String> parsed = MapRedUtils.transformXmlToMap(value.toString());

		// Get the value for the OwnerUserId attribute
		String userId = parsed.get("OwnerUserId");

		if (userId != null) {
			outkey.set(userId);
			context.write(outkey, ONE);
			context.getCounter(JobChainingMain.AVERAGE_CALC_GROUP, RECORDS_COUNTER_NAME).increment(1);
		}
	}

}
