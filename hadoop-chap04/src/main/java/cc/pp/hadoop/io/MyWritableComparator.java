package cc.pp.hadoop.io;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MyWritableComparator extends WritableComparator {

	@SuppressWarnings("rawtypes")
	protected MyWritableComparator(Class<? extends WritableComparable> c) {
		super(c);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		return 20;
	}

	public WritableComparator get() {
		return new MyWritableComparator(IntWritable.class);
	}

}
