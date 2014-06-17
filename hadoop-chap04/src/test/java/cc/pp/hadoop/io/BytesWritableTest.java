package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

public class BytesWritableTest extends WritableTestBase {

	@Test
	public void test() throws IOException {

		BytesWritable b = new BytesWritable(new byte[] { 3, 5 });
		byte[] bytes = serialize(b);
		assertThat(bytes.length, is(6));
		assertThat(StringUtils.byteToHexString(bytes), is("000000020305")); // 12位

		assertThat(b.getLength(), is(2)); // 当前缓存区的大小
		b.setCapacity(11); // 改变缓存区的存储容量
		assertThat(b.getLength(), is(2)); // 当前缓存区的大小
		assertThat(b.getBytes().length, is(11));
	}

}
