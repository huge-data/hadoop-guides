package cc.pp.hdfs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CoherencyModelTest {

	/**
	 * 使用进程内的HDFS集群测试
	 */
	private MiniDFSCluster cluster;

	private FileSystem fs;

	@Before
	public void setUp() throws IOException {

		Configuration conf = new Configuration();
		if (System.getProperty("test.build.data") == null) {
			System.setProperty("test.build.data", "/tmp");
		}
		cluster = new MiniDFSCluster(conf, 1, true, null);
		fs = cluster.getFileSystem();
	}

	@After
	public void tearDown() throws IOException {
		if (fs != null) {
			fs.close();
		}
		if (cluster != null) {
			cluster.shutdown();
		}
	}

	@Test
	public void fileExistsImmediatelyAfterCreation() throws IOException {

		Path p = new Path("wgybzb");
		fs.create(p);
		assertThat(fs.exists(p), is(true));
		assertThat(fs.delete(p, true), is(true));
	}

	@Test
	public void fileContentIsNotVisibleAfterFlush() throws IOException {

		Path p = new Path("wgybzb");
		OutputStream out = fs.create(p);
		out.write("wanggang".getBytes("utf-8"));
		System.out.println(fs.getFileStatus(p).getLen());
		out.flush();
		assertThat(fs.getFileStatus(p).getLen(), is(0L));
		out.close();
		assertThat(fs.delete(p, true), is(true));
	}

	@Test
	@Ignore("See https://issues.apache.org/jira/browse/HADOOP-4379")
	public void fileContentIsVisibleAfterFlushAndSync() throws IOException {

		Path p = new Path("wgybzb");
		FSDataOutputStream out = fs.create(p);
		out.write("wgybzb".getBytes("utf-8"));
		out.flush();
		out.sync();
		assertThat(fs.getFileStatus(p).getLen(), is(((long) "wgybzb".length())));
		out.close();
		assertThat(fs.delete(p, true), is(true));
	}

	@Test
	public void localFileContentIsVisibleAfterFlushAndSync() throws IOException {

		File localFile = File.createTempFile("tmp", "");
		assertThat(localFile.exists(), is(true));
		FileOutputStream out = new FileOutputStream(localFile);
		out.write("wgybzb".getBytes("UTF-8"));
		out.flush(); // 刷新到操作系统
		out.getFD().sync(); // 同步到磁盘
		assertThat(localFile.length(), is((long) "wgybzb".length()));
		out.close();
		assertThat(localFile.delete(), is(true));
	}

	@Test
	public void fileContentIsVisibleAfterClose() throws IOException {

		Path p = new Path("wgybzb");
		OutputStream out = fs.create(p);
		out.write("wgybzb".getBytes("utf-8"));
		out.close();
		assertThat(fs.getFileStatus(p).getLen(), is(((long) "wgybzb".length())));
		assertThat(fs.delete(p, true), is(true));
	}

}
