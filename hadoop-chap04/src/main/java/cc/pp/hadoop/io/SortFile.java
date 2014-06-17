package cc.pp.hadoop.io;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

public class SortFile {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

		String uri = "seqfile/sorted.map";

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		Path inFile = new Path(uri + "/unsortedfile");
		Path outFile = new Path(uri + "/data");

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, inFile, conf);
		WritableComparable<?> key = (WritableComparable<?>) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
		SequenceFile.Sorter sorter = new SequenceFile.Sorter(fs, key.getClass(), value.getClass(), conf);

		sorter.sort(inFile, outFile);

		System.out.println("Finish!");

	}

}
