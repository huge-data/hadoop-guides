package cc.pp.hadoop.hdfs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * 文件拷贝
 * @author wgybzb
 *
 */
public class FileCopyWithProgress {

	public static void main(String[] args) throws Exception {

		String localSrc = args[0];
		String dst = args[1];

		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		OutputStream out = fs.create(new Path(dst));
		//		OutputStream out = fs.create(new Path(dst), new Progressable() {
		//			@Override
		//			public void progress() {
		//				System.out.println(".");
		//			}
		//		});

		IOUtils.copyBytes(in, out, 4096, true);
	}

}
