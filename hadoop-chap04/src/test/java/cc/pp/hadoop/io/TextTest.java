package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.junit.Test;

public class TextTest extends WritableTestBase {

	@Test
	public void test() {

		Text t = new Text("hadoop");
		assertThat(t.getLength(), is(6)); // 字符串长度
		assertThat(t.getBytes().length, is(6)); // 字节长度
		assertThat(t.charAt(2), is(100));
		assertThat(t.charAt(2), is((int) 'd'));
		assertThat("Out of bounds", t.charAt(100), is(-1));
	}

	@Test
	public void find() {

		Text t = new Text("hadoop");
		assertThat("Find a substring", t.find("do"), is(2));
		assertThat("Find first 'o'", t.find("o"), is(3));
		assertThat("Finds 'o' from position 4 or later", t.find("o", 4), is(4));
		assertThat("No match", t.find("pig"), is(-1));
	}

	@Test
	public void mutability() {

		Text t = new Text("hadoop");
		t.set("pig");
		assertThat(t.getLength(), is(3)); // 字符串长度
		assertThat(t.getBytes().length, is(3)); // 字节长度
	}

	@Test
	public void byteArrayNotShortened() {

		Text t = new Text("hadoop");
		/**
		 * set参数为Text对象时，值变了，但是字节长度未变
		 */
		t.set(new Text("pig"));
		assertThat(t.getLength(), is(3));
		assertThat("Byte length not shortened", t.getBytes().length, is(6));
	}

	@Test
	public void toStringMethod() {
		assertThat(new Text("hadoop").toString(), is("hadoop"));
	}

	@Test
	public void comparison() {

		assertThat("\ud800\udc00".compareTo("\ue000"), lessThan(0));
		assertThat(new Text("\ud800\udc00").compareTo(new Text("\ue000")), greaterThan(0));
	}

	@Test
	public void withSupplementaryCharacters() throws IOException {

		String s = "\u0041\u00DF\u6771\uD801\uDC00";
		assertThat(s.length(), is(5)); // 5个字符
		assertThat(s.getBytes("utf-8").length, is(10)); // UTF-8编码长度
		assertThat("\u0041".getBytes("utf-8").length, is(1));
		assertThat("\u00DF".getBytes("utf-8").length, is(2));
		assertThat("\u6771".getBytes("utf-8").length, is(3));
		assertThat("\uD801\uDC00".getBytes("utf-8").length, is(4));

		assertThat(s.indexOf("\u0041"), is(0));
		assertThat(s.indexOf("\u00DF"), is(1));
		assertThat(s.indexOf("\u6771"), is(2));
		assertThat(s.indexOf("\uD801"), is(3));
		assertThat(s.indexOf("\uDC00"), is(4));

		assertThat(s.charAt(0), is('\u0041'));
		assertThat(s.charAt(1), is('\u00DF'));
		assertThat(s.charAt(2), is('\u6771'));
		assertThat(s.charAt(3), is('\uD801'));
		assertThat(s.charAt(4), is('\uDC00'));

		Text t = new Text("\u0041\u00DF\u6771\uD801\uDC00");

		assertThat(t.charAt(t.find("\u0041")), is(0x0041)); // 1个字节
		assertThat(t.charAt(t.find("\u00DF")), is(0x00DF)); // 2个字节
		assertThat(t.charAt(t.find("\u6771")), is(0x6771)); // 13个字节
		assertThat(t.charAt(t.find("\uD801\uDC00")), is(0x10400)); // 4个字节
	}

}
