package cc.pp.mapred.topsort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SortMapper extends Mapper<Object, Text, IntWritable, Text> {

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		IntWritable times = new IntWritable(1);
		Text password = new Text();

		String eachline = value.toString();
		String[] eachterm = eachline.split("	");

		if (eachterm.length == 2) {
			password.set(eachterm[0]);
			times.set(Integer.parseInt(eachterm[1]));

			// key is times, value is the password
			context.write(times, password);
		} else {
			password.set("errorpassword");
			context.write(times, password);
		}
	}

}
