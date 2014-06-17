package cc.pp.hdfs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.pp.hadoop.hdfs.DateRangePathFilter;
import cc.pp.hadoop.hdfs.RegexExcludePathFilter;
import cc.pp.hadoop.hdfs.RegexPathFilter;

public class FileSystemGlobTest {

	private static final String BASE_PATH = "/tmp/" + FileSystemGlobTest.class.getSimpleName();

	private FileSystem fs;

	@Before
	public void setUp() throws IOException {

		fs = FileSystem.get(new Configuration());
		fs.mkdirs(new Path(BASE_PATH, "2012/12/30"));
		fs.mkdirs(new Path(BASE_PATH, "2012/12/31"));
		fs.mkdirs(new Path(BASE_PATH, "2013/01/01"));
		fs.mkdirs(new Path(BASE_PATH, "2013/01/02"));
	}

	@After
	public void tearDown() throws IOException {
		if (fs != null) {
			fs.delete(new Path(BASE_PATH), true);
		}
	}

	@Test
	public void glob() throws IOException {

		assertThat(glob("/*"), is(paths("/2012", "/2013")));
		assertThat(glob("/*/*"), is(paths("/2012/12", "/2013/01")));
		assertThat(glob("/*/12/*"), is(paths("/2012/12/30", "/2012/12/31")));

		assertThat(glob("/201?"), is(paths("/2012", "/2013")));
		assertThat(glob("/201[23]"), is(paths("/2012", "/2013")));
		assertThat(glob("/201[2-3]"), is(paths("/2012", "/2013")));
		assertThat(glob("/201[^01456789]"), is(paths("/2012", "/2013")));

		assertThat(glob("/*/*/{31,01}"), is(paths("/2012/12/31", "/2013/01/01")));
		assertThat(glob("/*/*/3{0,1}"), is(paths("/2012/12/30", "/2012/12/31")));

		assertThat(glob("/*/{12/31,01/01}"), is(paths("/2012/12/31", "/2013/01/01")));
	}

	@Test
	public void regexInclude() throws IOException {

		assertThat(glob("/*", new RegexPathFilter("^.*/2012$")), is(paths("/2012")));
		/**
		 * hadoop的Bug，新版本已修复，可以参考官方jira
		 */
		assertThat(glob("/*/*/*", new RegexPathFilter("^.*/2012/12/31$")), is(paths("/2012/12/31")));
		assertThat(glob("/*/*/*", new RegexPathFilter("^.*/2012(/12(/31)?)?$")), is(paths("/2012/12/31")));
	}

	@Test
	public void regexExcludes() throws IOException {

		assertThat(glob("/*", new RegexPathFilter("^.*/2012$", false)), is(paths("/2013")));
		assertThat(glob("/2012/*/*", new RegexPathFilter("^.*/2012/12/31$", false)), is(paths("/2012/12/30")));
	}

	@Test
	public void regexExcludesWithRegexExcludePathFilter() throws IOException {

		assertThat(glob("/*", new RegexExcludePathFilter("^.*/2012$")), is(paths("/2013")));
		assertThat(glob("/2012/*/*", new RegexExcludePathFilter("^.*/2012/12/31$")), is(paths("/2012/12/30")));
	}

	@Test
	public void testDateRange() throws ParseException, IOException {

		DateRangePathFilter filter = new DateRangePathFilter(date("2012/12/31"), date("2013/01/01"));
		assertThat(glob("/*/*/*", filter), is(paths("/2012/12/31", "/2013/01/01")));
	}

	private Path[] glob(String pattern) throws IOException {
		return FileUtil.stat2Paths(fs.globStatus(new Path(BASE_PATH + pattern)));
	}

	private Path[] glob(String pattern, PathFilter pathFilter) throws IOException {
		return FileUtil.stat2Paths(fs.globStatus(new Path(BASE_PATH + pattern), pathFilter));
	}

	private Path[] paths(String... pathStrings) {

		Path[] paths = new Path[pathStrings.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path("file:" + BASE_PATH + pathStrings[i]);
		}
		return paths;
	}

	private Date date(String date) throws ParseException {
		return new SimpleDateFormat("yyyy/MM/dd").parse(date);
	}

}
