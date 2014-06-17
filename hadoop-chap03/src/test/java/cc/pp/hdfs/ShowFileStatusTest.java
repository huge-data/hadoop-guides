package cc.pp.hdfs;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShowFileStatusTest {

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
		OutputStream out = fs.create(new Path("/wgybzb/file"));
		out.write("wgybzb".getBytes("utf-8"));
		out.close();
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
	public void throwsFileNotFoundForNonExistentFile_通过fail实现() {
		try {
			fs.getFileStatus(new Path("no-such-file"));
			fail("No such file.");
		} catch (IOException e) {
			//			e.printStackTrace();
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void throwsFileNotFoundForNonExistentFile_通过expected实现() throws IOException {
		fs.getFileStatus(new Path("没有这个文件名"));
	}

	@Test
	public void fileStatusForFile() throws IOException {

		Path f = new Path("/wgybzb/file");
		FileStatus status = fs.getFileStatus(f);
		assertThat(status.getPath().toUri().getPath(), is("/wgybzb/file"));
		assertThat(status.isDir(), is(false));
		assertThat(status.getLen(), is(6L));
		assertThat(status.getModificationTime(), is(lessThanOrEqualTo(System.currentTimeMillis())));
		assertThat(status.getReplication(), is((short) 1));
		assertThat(status.getBlockSize(), is(64 * 1024 * 1024L));
		assertThat(status.getOwner(), is(System.getProperty("user.name")));
		assertThat(status.getGroup(), is("supergroup"));
		assertThat(status.getPermission().toString(), is("rw-r--r--"));
	}

	@Test
	public void fileStatusForDirectory() throws IOException {

		Path dir = new Path("/wgybzb");
		FileStatus status = fs.getFileStatus(dir);
		assertThat(status.getPath().toUri().getPath(), is("/wgybzb"));
		assertThat(status.isDir(), is(true));
		assertThat(status.getLen(), is(0L));
		assertThat(status.getModificationTime(), is(lessThanOrEqualTo(System.currentTimeMillis())));
		assertThat(status.getReplication(), is((short) 0));
		assertThat(status.getBlockSize(), is(0L));
		assertThat(status.getOwner(), is(System.getProperty("user.name")));
		assertThat(status.getGroup(), is("supergroup"));
		assertThat(status.getPermission().toString(), is("rwxr-xr-x"));
	}

}
