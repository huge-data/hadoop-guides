package cc.pp.hadoop.io;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import cc.pp.mapreduce.MaxTemperatureMapper;
import cc.pp.mapreduce.MaxTemperatureReducer;

public class MaxTemperatureWithCompression {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: MaxTemperatureWithCompression <input path> <output path>");
			System.exit(-1);
		}

		Job job = new Job();
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		/**
		 * 设置输出压缩
		 */
		FileOutputFormat.setCompressOutput(job, true);
		FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);

		job.setMapperClass(MaxTemperatureMapper.class);
		job.setCombinerClass(MaxTemperatureReducer.class);
		job.setReducerClass(MaxTemperatureReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
