package cc.pp.hadoop.mapred.v5;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	enum Temperature {
		MALFORMED
	}

	private final NcdcRecordParser parser = new NcdcRecordParser();

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		parser.parse(value);
		if (parser.isValidTemperature()) {
			int airTemperature = parser.getAirTemperature();
			context.write(new Text(parser.getYear()), new IntWritable(airTemperature));
		} else if (parser.isMalformedTemperature()) {
			System.err.println("Ignoring possibly corrupt input: " + value);
			context.getCounter(Temperature.MALFORMED).increment(1);
		}
	}

}
