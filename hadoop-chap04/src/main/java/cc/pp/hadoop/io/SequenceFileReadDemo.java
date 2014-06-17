package cc.pp.hadoop.io;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

public class SequenceFileReadDemo {

	public static void main(String[] args) throws Exception {

		//		if (args.length != 1) {
		//			System.err.println("Usage: SequenceFileReadDemo <input path>");
		//			System.exit(-1);
		//		}
		//		String uri = args[0];
		String uri = "seqfile/unsortedfile";

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		Path path = new Path(uri);

		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(fs, path, conf);
			Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
			Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
			long position = reader.getPosition();
			while (reader.next(key, value)) {
				String syncSeen = reader.syncSeen() ? "*" : "";
				System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key, value);
				position = reader.getPosition();
			}
		} finally {
			IOUtils.closeStream(reader);
		}
	}

}
