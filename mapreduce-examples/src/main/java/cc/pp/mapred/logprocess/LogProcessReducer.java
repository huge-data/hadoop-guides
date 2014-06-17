package cc.pp.mapred.logprocess;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class LogProcessReducer extends MapReduceBase implements Reducer<Text, LogWritable, Text, IntWritable> {

	@Override
	public void reduce(Text key, Iterator<LogWritable> values, OutputCollector<Text, IntWritable> output,
			Reporter reporter) throws IOException {
		//
	}

}