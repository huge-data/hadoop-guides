package cc.pp.hdfs;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

public class FileSystemDeleteTest {

	private FileSystem fs;

	@Before
	public void setUp() throws IOException {

		Configuration conf = new Configuration();
		fs = FileSystem.get(conf);
		Path p = new Path("wg/ybzb");
		FSDataOutputStream out = fs.create(p);
		out.close();
	}

	@Test
	public void deleteFile() throws IOException {

		/**
		 * delete(p1,p2)的第二个参数表示是否递归删除
		 */
		assertThat(fs.delete(new Path("wg/ybzb"), false), is(true));
		assertThat(fs.exists(new Path("wg/ybzb")), is(false));
		assertThat(fs.exists(new Path("wg")), is(true));
		assertThat(fs.delete(new Path("wg"), false), is(true));
		assertThat(fs.exists(new Path("wg")), is(false));
	}

	/**
	 * 使用非递归方式删除非空目录（含有子目录），要删除含有子目录的目录必须用递归删除
	 */
	@Test
	public void deleteNonEmptyDirectoryNonRecursivelyFails() {

		try {
			//			System.out.println(fs.exists(new Path("wg/ybzb")));
			//			System.out.println(fs.delete(new Path("wg"), true));
			fs.delete(new Path("wg"), false);
			fail("Shouldn't delete non-empty directory");
		} catch (IOException e) {
			//			e.printStackTrace();
		}
	}

	@Test
	public void deleteDirectory() throws IOException {

		assertThat(fs.delete(new Path("wg"), true), is(true));
		assertThat(fs.exists(new Path("wg")), is(false));
	}

}
