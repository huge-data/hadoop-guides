package cc.pp.mapred.jobchaining;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class UserIdSumReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

	public static final String USERS_COUNTER_NAME = "Users";
	private final LongWritable outvalue = new LongWritable();

	@Override
	public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException,
			InterruptedException {

		// Increment user counter, as each reduce group represents one user
		context.getCounter(JobChainingMain.AVERAGE_CALC_GROUP, USERS_COUNTER_NAME).increment(1);

		int sum = 0;

		for (LongWritable value : values) {
			sum += value.get();
		}

		outvalue.set(sum);
		context.write(key, outvalue);
	}

}
