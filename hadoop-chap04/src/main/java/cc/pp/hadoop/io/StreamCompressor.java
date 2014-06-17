package cc.pp.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

public class StreamCompressor {

	public static void main(String[] args) throws Exception {

		String codecClassname = args[0];
		Class<?> codecClass = Class.forName(codecClassname);
		Configuration conf = new Configuration();
		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
		CompressionOutputStream out = codec.createOutputStream(System.out);
		IOUtils.copyBytes(System.in, out, 4096, false);
		out.finish();

		//		String inputFile = "compression/1902";
		//		String outputFile = "compression/comtestfile";
		//		Configuration conf = new Configuration();
		//		FileSystem fs = FileSystem.get(URI.create(inputFile), conf);
		//		String codecClassname = "org.apache.hadoop.io.compress.GzipCodec";
		//		Class<?> codecClass = Class.forName(codecClassname);
		//		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
		//		InputStream in = null;
		//		CompressionOutputStream out = null;
		//		try {
		//			in = codec.createInputStream(fs.open(new Path(inputFile)));
		//			out = codec.createOutputStream(fs.create(new Path(outputFile)));
		//			IOUtils.copyBytes(in, out, conf);
		//		} finally {
		//			IOUtils.closeStream(in);
		//			IOUtils.closeStream(out);
		//		}
		//		System.out.println("Finish!");

	}

}
