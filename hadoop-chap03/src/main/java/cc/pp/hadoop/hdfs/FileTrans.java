package cc.pp.hadoop.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileTrans {

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Usage: FileTrans <input dir>");
			System.exit(-1);
		}
		String uri = args[0];

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		fs.mkdirs(new Path(uri));

	}

}
