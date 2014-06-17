package cc.pp.hadoop.mapred.v7;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cc.pp.hadoop.mapred.v5.NcdcRecordParser;

/**
 * 对象重用
 * @author wgybzb
 *
 */
public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	enum Temperature {
		MALFORMED
	}

	private final NcdcRecordParser parser = new NcdcRecordParser();

	private final Text year = new Text();
	private final IntWritable temp = new IntWritable();

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		parser.parse(value);
		if (parser.isValidTemperature()) {
			year.set(parser.getYear());
			temp.set(parser.getAirTemperature());
			context.write(year, temp);
		} else if (parser.isMalformedTemperature()) {
			System.err.println("Ignoring possibly corrupt input: " + value);
			context.getCounter(Temperature.MALFORMED).increment(1);
		}

	}

}
