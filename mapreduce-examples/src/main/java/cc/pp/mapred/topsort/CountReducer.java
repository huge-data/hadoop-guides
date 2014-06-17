package cc.pp.mapred.topsort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	private final IntWritable total = new IntWritable();

	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException,
			InterruptedException {

		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		total.set(sum);

		// key is password, total is counts
		context.write(key, total);
	}

}
