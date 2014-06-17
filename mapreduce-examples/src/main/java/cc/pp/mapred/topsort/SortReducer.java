package cc.pp.mapred.topsort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SortReducer extends Reducer<IntWritable, Text, IntWritable, Text> {

	private final Text password = new Text();

	@Override
	public void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException,
			InterruptedException {

		// different passwords may have the same counts
		for (Text val : values) {
			password.set(val);
			context.write(key, password);
		}
	}

}
