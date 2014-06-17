package cc.pp.hadoop.io;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

public class MapFileReaderDemo {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: MapFileReaderDemo <input map_file_path> <input key>");
			System.exit(-1);
		}
		String uri = args[0];
		WritableComparable<?> key1 = new LongWritable(Long.parseLong(args[1]));
		//		String uri = "seqfile/sorted.map";
		//		IntWritable key1 = new IntWritable(2);

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		MapFile.Reader reader = new MapFile.Reader(fs, uri, conf);
		WritableComparable<?> key = (WritableComparable<?>) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);

		// 查找某个key的数据
		//		reader.get(key1, value);
		//		System.out.println(value.toString());

		/**
		 * 从某个key开始顺序查找，当当前key没找到对应value时，搜索key下一个存在的键值对。
		 */
		//		reader.seek(key1);
		//		reader.next(key, value);
		//		System.out.println(((IntWritable) key).get() + "\t" + ((Text) value).toString());

		/**
		 * 查找所有key1值
		 */
		reader.get(key1, value);
		int i = 1;
		System.out.println(i++ + "\t" + ((LongWritable) key1).get() + "\t" + ((Text) value).toString());
		while (reader.next(key, value)) {
			if (key.toString().equals(key1.toString())) {
				System.out.println(i++ + "\t" + ((LongWritable) key).get() + "\t" + ((Text) value).toString());
			} else {
				break;
			}
		}
		reader.next(key, value);
		System.out.println(i++ + "\t" + ((LongWritable) key).get() + "\t" + ((Text) value).toString());

		//		fs.delete(new Path(uri), true);
	}

}
