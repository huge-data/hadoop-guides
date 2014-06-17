package cc.pp.mapreduce;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class OldMaxTemperature {

	public static class OldMaxTemperatureMapper extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, IntWritable> {

		private static final int MISSING = 9999;

		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter report)
				throws IOException {

			String line = value.toString();
			String year = line.substring(15, 19);
			int airTemperature;
			if (line.charAt(87) == '+') {
				airTemperature = Integer.parseInt(line.substring(88, 92));
			} else {
				airTemperature = Integer.parseInt(line.substring(87, 92));
			}
			String quality = line.substring(92, 93);
			if (airTemperature != MISSING && quality.matches("[01459]")) {
				output.collect(new Text(year), new IntWritable(airTemperature));
			}
		}
	}

	public static class OldMaxTemperatureReducer extends MapReduceBase implements
			Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output,
				Reporter report)
				throws IOException {

			int maxValue = Integer.MIN_VALUE;
			/**
			 * 不能使用for(IntWritable value : values)
			 */
			while (values.hasNext()) {
				maxValue = Math.max(maxValue, values.next().get());
			}
			output.collect(key, new IntWritable(maxValue));
		}
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.err.println("Usage: OldMaxTemperature <input path> <output path>");
			System.exit(-1);
		}

		JobConf conf = new JobConf(OldMaxTemperature.class);
		conf.setJobName("Old Max Temperature");

		FileInputFormat.addInputPath(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		conf.setMapperClass(OldMaxTemperatureMapper.class);
		conf.setReducerClass(OldMaxTemperatureReducer.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		JobClient.runJob(conf);
	}

}
