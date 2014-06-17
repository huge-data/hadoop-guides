package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;
import org.junit.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MyWritableComparatorTest {

	@Test
	public void test() {

		IntWritable w1 = new IntWritable(12);
		IntWritable w2 = new IntWritable(13);
		RawComparator<IntWritable> comparator1 = MyWritableComparator.get(IntWritable.class);
		assertThat(comparator1.compare(w1, w2), is(-1));
		RawComparator<IntWritable> comparator2 = new MyWritableComparator(IntWritable.class);
		assertThat(comparator2.compare(w1, w2), is(20));
	}

	@Test
	public void testMyWritableComparable() {

		MyWritableComparable w1 = new MyWritableComparable(12, 14L);
		MyWritableComparable w2 = new MyWritableComparable(12, 11L);
		RawComparator comparator = WritableComparator.get(MyWritableComparable.class);
		assertThat(comparator.compare(w1, w2), is(1));
	}

	@Test
	public void testTextPair() {

		TextPair tp1 = new TextPair("a", "b");
		TextPair tp2 = new TextPair("b", "a");
		RawComparator comparator = WritableComparator.get(TextPair.class);
		assertThat(comparator.compare(tp1, tp2), is(-1));
	}

	@Test
	public void testIntPair() {

		IntPair ip1 = new IntPair(12, 14);
		IntPair ip2 = new IntPair(13, 11);
		RawComparator comparator = WritableComparator.get(IntPair.class);
		assertThat(comparator.compare(ip1, ip2), is(-1));
	}

}
