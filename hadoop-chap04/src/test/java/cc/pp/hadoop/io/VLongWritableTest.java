package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.io.VLongWritable;

public class VLongWritableTest extends WritableTestBase {

	public void test() throws IOException {

		/**
		 * 1个字节
		 */
		assertThat(serializeToString(new VLongWritable(1)), is("01"));
		assertThat(serializeToString(new VLongWritable(127)), is("7f"));
		/**
		 * 2个字节
		 */
		assertThat(serializeToString(new VLongWritable(128)), is("8f80"));
		assertThat(serializeToString(new VLongWritable(163)), is("8fa3"));
		/**
		 * 9个字节
		 */
		assertThat(serializeToString(new VLongWritable(Long.MAX_VALUE)), is("887fffffffffffffff"));
		assertThat(serializeToString(new VLongWritable(Long.MIN_VALUE)), is("807fffffffffffffff"));

	}

}
