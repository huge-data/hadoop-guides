package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.io.VIntWritable;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

public class VIntWritableTest extends WritableTestBase {

	@Test
	public void test() throws IOException {

		byte[] data = serialize(new VIntWritable(163));
		assertThat(StringUtils.byteToHexString(data), is("8fa3"));
	}

	@Test
	public void testSizes() throws IOException {

		/**
		 * 1个字节
		 */
		assertThat(serializeToString(new VIntWritable(1)), is("01"));
		assertThat(serializeToString(new VIntWritable(-112)), is("90"));
		assertThat(serializeToString(new VIntWritable(127)), is("7f"));
		/**
		 * 2个字节
		 */
		assertThat(serializeToString(new VIntWritable(128)), is("8f80"));
		assertThat(serializeToString(new VIntWritable(163)), is("8fa3"));
		/**
		 * 5个字节
		 */
		assertThat(serializeToString(new VIntWritable(Integer.MAX_VALUE)), is("8c7fffffff"));
		assertThat(serializeToString(new VIntWritable(Integer.MIN_VALUE)), is("847fffffff"));
	}

}
