package cc.pp.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.util.ReflectionUtils;

public class PooledStreamCompressor {

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("Usage: PooledStreamCompressor <input codecClasssName>");
			System.exit(-1);
		}

		String codecClassName = args[0];
		Class<?> codecClass = Class.forName(codecClassName);
		Configuration conf = new Configuration();
		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);

		Compressor compressor = null;
		try {
			compressor = CodecPool.getCompressor(codec);
			CompressionOutputStream out = codec.createOutputStream(System.out, compressor);
			IOUtils.copyBytes(System.in, out, 4096, false);
			out.flush();
		} finally {
			CodecPool.returnCompressor(compressor);
		}
	}

}
