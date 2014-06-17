package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SequenceFileSeekAndSyncTest {

	private static final String SF_URL = "test.numbers.seq";
	private FileSystem fs;
	private SequenceFile.Reader reader;
	private Writable key;
	private Writable value;

	@Before
	public void setUp() throws IOException {

		SequenceFileWriteDemo.main(new String[] { SF_URL });
		Configuration conf = new Configuration();
		fs = FileSystem.get(URI.create(SF_URL), conf);
		Path path = new Path(SF_URL);

		reader = new SequenceFile.Reader(fs, path, conf);
		key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
	}

	@After
	public void tearDown() throws IOException {
		fs.delete(new Path(SF_URL), true);
	}

	@Test
	public void seekToRecordBoundary() throws IOException {

		reader.getPosition();
		assertThat(reader.next(key, value), is(true));
		assertThat(((IntWritable) key).get(), is(100));

		reader.seek(359);
		assertThat(reader.next(key, value), is(true));
		assertThat(((IntWritable) key).get(), is(95));
	}

	/**
	 * 如果seek的输入值（偏移量）在序列化文件中不存在，会抛出异常
	 */
	@Test(expected = IOException.class)
	public void seekToNonRecordBoundary() throws IOException {

		reader.seek(360);
		reader.next(key, value);
	}

	@Test
	public void syncFromNonRecordBoundary() throws IOException {

		reader.sync(360);
		assertThat(reader.getPosition(), is(2021L));
		assertThat(reader.next(key, value), is(true));
		assertThat(((IntWritable) key).get(), is(59));
	}

	/**
	 * 当没有下一个同步标记点时，直接返回文件结尾位置
	 */
	@Test
	public void synAfterLastSyncPoint() throws IOException {

		reader.sync(4557);
		assertThat(reader.getPosition(), is(4788L));
		assertThat(reader.next(key, value), is(false));
	}

}
