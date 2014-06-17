package cc.pp.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import cc.pp.mapreduce.MaxTemperature;
import cc.pp.mapreduce.MaxTemperatureMapper;
import cc.pp.mapreduce.MaxTemperatureReducer;

public class MaxTemperatureWithMapOutputCompression {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: maxTemperatureWithMapOutputCompression <input path> <output path>");
			System.exit(-1);
		}

		Configuration conf = new Configuration();
		conf.setBoolean("mapred.compress.map.output", true); // 开起map输出压缩
		conf.setClass("mapred.map.output.compression.codec", GzipCodec.class, CompressionCodec.class); // 设置压缩算法

		Job job = new Job(conf);
		job.setJarByClass(MaxTemperature.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(MaxTemperatureMapper.class);
		job.setCombinerClass(MaxTemperatureReducer.class);
		job.setReducerClass(MaxTemperatureReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
