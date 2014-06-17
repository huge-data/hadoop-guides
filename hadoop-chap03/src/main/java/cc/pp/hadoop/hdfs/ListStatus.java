package cc.pp.hadoop.hdfs;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class ListStatus {

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("Usage: ListStatus <input path>");
			System.exit(-1);
		}

		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		Path[] paths = new Path[args.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path(args[i]);
		}

		FileStatus[] status = fs.listStatus(paths);
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listedPaths) {
			System.out.println(p);
		}
	}

}
