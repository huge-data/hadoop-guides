package cc.pp.hadoop.hdfs;

import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

/**
 * 注意：输入参数必须是完整的路径，如hdfs://localhost/user/wgybzb/input/cp.txt
 * @author wgybzb
 *
 */
public class URLCat {

	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("Usage: URLCat <input file>");
			System.exit(-1);
		}

		InputStream in = null;
		try {
			in = new URL(args[0]).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally {
			IOUtils.closeStream(in);
		}
	}

}
