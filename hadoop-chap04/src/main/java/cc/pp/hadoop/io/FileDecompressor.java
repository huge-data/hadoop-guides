package cc.pp.hadoop.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

public class FileDecompressor {

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("Usage: FileDecompressor <input file>");
			System.exit(-1);
		}
		String uri = args[0];
		//		String uri = "compression/comtestfile";

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		Path inputFile = new Path(uri);
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		CompressionCodec codec = factory.getCodec(inputFile);
		if (codec == null) {
			System.err.println("No codec found for " + uri);
			System.exit(1);
		}

		String outputFile = CompressionCodecFactory.removeSuffix(uri, codec.getDefaultExtension());
		InputStream in = null;
		OutputStream out = null;
		try {
			in = codec.createInputStream(fs.open(inputFile));
			out = fs.create(new Path(outputFile));
			IOUtils.copyBytes(in, out, conf);
		} finally {
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}

	}

}
