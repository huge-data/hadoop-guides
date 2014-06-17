package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.WritableUtils;
import org.junit.Test;

public class ObjectWritableTest extends WritableTestBase {

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws IOException {

		ObjectWritable src = new ObjectWritable(Integer.TYPE, 163);
		ObjectWritable dest = new ObjectWritable();
		WritableUtils.cloneInto(dest, src);
		assertThat((Integer) (dest.get()), is(163));
	}

}
