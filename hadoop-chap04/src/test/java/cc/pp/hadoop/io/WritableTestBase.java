package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

public class WritableTestBase {

	@Test
	public void testWritableTestBase() throws IOException {

		BooleanWritable src = new BooleanWritable(true);
		BooleanWritable dest = new BooleanWritable();
		assertThat(serializeToString(src), is("01"));
		assertThat(writeTo(src, dest), is("01"));
		assertThat(dest.get(), is(src.get()));
	}

	/**
	 * 序列化文件，并返回结果到字节数组中
	 */
	public static byte[] serialize(Writable writable) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		writable.write(dataOut);
		dataOut.close();

		return out.toByteArray();
	}

	/**
	 * 反序列化文件，并返回结果到字节数组中
	 */
	public static byte[] deserialize(Writable writable, byte[] bytes) throws IOException {

		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		DataInputStream dataIn = new DataInputStream(in);
		writable.readFields(dataIn);
		dataIn.close();

		return bytes;
	}

	/**
	 * 序列化文件，并返回序列化文件的十六进制值
	 */
	public static String serializeToString(Writable src) throws IOException {
		return StringUtils.byteToHexString(serialize(src));
	}

	/**
	 * 写序列化文件，并返回反序列化文件的十六进制值
	 */
	public static String writeTo(Writable src, Writable dest) throws IOException {
		byte[] data = deserialize(dest, serialize(src));
		return StringUtils.byteToHexString(data);
	}

}
