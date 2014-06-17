package cc.pp.hadoop.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

public class StreamCompressorDemo {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		String outputFile = "compression/compressedfile";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		String codecClassname = "org.apache.hadoop.io.compress.GzipCodec";
		Class<?> codecClass = Class.forName(codecClassname);
		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);

		InputStream in = null;
		CompressionOutputStream out = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("file");
			out = codec.createOutputStream(fs.create(new Path(outputFile)));
			IOUtils.copyBytes(in, out, conf);
		} finally {
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
		System.out.println("Finish!");

	}

}
