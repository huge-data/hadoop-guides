package cc.pp.hadoop.merge;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class MergeSort {

	public static class MergeSortMapper extends Mapper<Text, Text, Text, Text> {

		@Override
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
		}
	}

	public static class MergeSortReducer extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text value : values) {
				context.write(key, value);
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		if (args.length != 2) {
			System.err.println("Usage: MergeSort <intput path> <output path>");
			System.exit(-1);
		}

		Configuration conf = new Configuration();
		conf.set("mapred.reduce.tasks", "1");

		Job job = new Job(conf);
		job.setJarByClass(MergeSort.class);
		job.setJobName("MergeSort");

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(MergeSortMapper.class);
		job.setReducerClass(MergeSortReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(IgnoreEofSequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
