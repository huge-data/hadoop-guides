package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.junit.Test;

public class ArrayWritableTest extends WritableTestBase {

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws IOException {

		ArrayWritable writable = new ArrayWritable(Text.class);
		writable.set(new Text[] { new Text("cat"), new Text("dog") });

		TextArrayWritable dest = new TextArrayWritable();
		WritableUtils.cloneInto(dest, writable);
		assertThat(dest.get().length, is(2));

		assertThat((Text) dest.get()[0], is(new Text("cat")));
		assertThat((Text) dest.get()[1], is(new Text("dog")));

		Text[] copy = (Text[]) dest.toArray();
		assertThat(copy[0], is(new Text("cat")));
		assertThat(copy[1], is(new Text("dog")));
	}

	@Test
	public void testArrayWritable1() {

		ArrayWritable writable = new ArrayWritable(Text.class);
		writable.set(new Text[] { new Text("cat"), new Text("dog") });
		assertThat((Text) writable.get()[0], is(new Text("cat")));
		assertThat((Text) writable.get()[1], is(new Text("dog")));
	}

	@Test
	public void testArrayWritable2() {

		ArrayWritable writable = new ArrayWritable(Text.class, new Text[] { new Text("cat"), new Text("dog") });
		assertThat((Text) writable.get()[0], is(new Text("cat")));
		assertThat((Text) writable.get()[1], is(new Text("dog")));
	}

	@Test
	public void testArrayWritable3() {

		ArrayWritable writable = new ArrayWritable(new String[] { "cat", "dog" });
		//		String[] values = writable.toStrings();
		Writable[] values = writable.get();
		assertThat(values[0].toString(), is("cat"));
		assertThat(values[1].toString(), is("dog"));
	}

}
