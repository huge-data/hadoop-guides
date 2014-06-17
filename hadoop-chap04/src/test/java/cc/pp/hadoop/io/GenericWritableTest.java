package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableUtils;

public class GenericWritableTest extends WritableTestBase {

	@SuppressWarnings("deprecation")
	public void test() throws IOException {

		BinaryOrTextWritable src = new BinaryOrTextWritable();
		src.set(new Text("text"));
		BinaryOrTextWritable dst = new BinaryOrTextWritable();
		WritableUtils.cloneInto(dst, src);
		assertThat((Text) dst.get(), is(new Text("text")));

		src.set(new BytesWritable(new byte[] { 3, 5 }));
		WritableUtils.cloneInto(dst, src);
		assertThat(((BytesWritable) dst.get()).getLength(), is(2));
	}

}
