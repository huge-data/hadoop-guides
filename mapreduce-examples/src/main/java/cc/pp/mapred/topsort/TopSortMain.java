package cc.pp.mapred.topsort;

import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class TopSortMain {

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: Top10PswdCSDN <in> <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "csdn");
		job.setJarByClass(TopSortMain.class);
		job.setMapperClass(CountMapper.class);
		job.setCombinerClass(CountReducer.class);
		job.setReducerClass(CountReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		Path tempDir = new Path("csdn-temp-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
		FileOutputFormat.setOutputPath(job, tempDir);

		if (job.waitForCompletion(true)) {
			Job sortJob = Job.getInstance(conf, "csdnsort");
			sortJob.setJarByClass(TopSortMain.class);

			FileInputFormat.addInputPath(sortJob, tempDir);
			FileOutputFormat.setOutputPath(sortJob, new Path(otherArgs[1]));

			sortJob.setMapperClass(SortMapper.class);

			sortJob.setOutputKeyClass(IntWritable.class);
			sortJob.setOutputValueClass(Text.class);

			sortJob.setSortComparatorClass(IntDecreasingComparator.class);

			FileSystem.get(conf).deleteOnExit(tempDir);

			System.exit(sortJob.waitForCompletion(true) ? 0 : 1);
		}

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
