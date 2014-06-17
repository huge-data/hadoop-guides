package cc.pp.hadoop.mapred.v5;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

public class MaxTemperatureMapperTest {

	@Test
	public void testParsesValidRecord() {

		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+038299999"
				+ "V0203201N00261220001CN9999999N9-00111+99999999999");
		new MapDriver<LongWritable, Text, Text, IntWritable>().withMapper(new MaxTemperatureMapper())
				.withInputValue(value).withOutput(new Text("1950"), new IntWritable(-11)).runTest();
	}

	@Test
	public void testParsesMissingTemperature() {

		Text value = new Text("0043011990999991950051518004+68750+023550FM-12+038299999"
				+ "V0203201N00261220001CN9999999N9+99991+99999999999");
		new MapDriver<LongWritable, Text, Text, IntWritable>().withMapper(new MaxTemperatureMapper())
				.withInputValue(value).runTest();
	}

	@Test
	public void testParsesMalformedTemperature() {

		Text value = new Text("0335999999433181957042302005+37950+139117SAO  +0004RJSN V"
				+ "02011359003150070356999999433201957010100005+353");
		Counters counters = new Counters();
		new MapDriver<LongWritable, Text, Text, IntWritable>().withMapper(new MaxTemperatureMapper())
				.withMapper(new MaxTemperatureMapper()).withInputValue(value).withCounters(counters).runTest();
		Counter c = counters.findCounter(MaxTemperatureMapper.Temperature.MALFORMED);
		assertThat(c.getValue(), is(1L));
	}

}
