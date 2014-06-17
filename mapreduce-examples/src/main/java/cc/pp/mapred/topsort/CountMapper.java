package cc.pp.mapred.topsort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CountMapper extends Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private final Text password = new Text();

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String eachline = value.toString();
		String[] eachterm = eachline.split("#");
		password.set(eachterm[1].trim());
		context.write(password, one);
	}

}
