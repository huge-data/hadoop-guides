package cc.pp.hadoop.io;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

public class MapFileReader {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.err.println("Usage: MapFileReaderDemo <input map_file_path> <input key>");
			System.exit(-1);
		}
		String uri = args[0];
		WritableComparable<?> query_key = new Text(args[1]);

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		MapFile.Reader reader = new MapFile.Reader(fs, uri, conf);
		WritableComparable<?> key = (WritableComparable<?>) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);

		/**
		 * 查找所有key1的值
		 */
		reader.get(query_key, value);
		int i = 1;
		System.out.println(i++ + "\t" + ((Text) query_key).toString() + "\t" + ((Text) value).toString());
		while (reader.next(key, value)) {
			if (key.toString().equals(query_key.toString())) {
				System.out.println(i++ + "\t" + ((Text) key).toString() + "\t" + ((Text) value).toString());
			} else {
				break;
			}
		}

	}

}
