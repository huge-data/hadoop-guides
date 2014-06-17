package cc.pp.hadoop.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MyWritableComparable implements WritableComparable<MyWritableComparable> {

	private int counter;
	private long timestamp;

	public MyWritableComparable() {
		//
	}

	public MyWritableComparable(int counter, long timestamp) {
		set(counter, timestamp);
	}

	public void set(int counter, long timestamp) {
		this.counter = counter;
		this.timestamp = timestamp;
	}

	public int getCounter() {
		return counter;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(counter);
		out.writeLong(timestamp);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		counter = in.readInt();
		timestamp = in.readInt();
	}

	@Override
	public int compareTo(MyWritableComparable w) {

		int cmp = compareInt(counter, w.getCounter());
		if (cmp != 0) {
			return cmp;
		}
		return compareLong(timestamp, w.getTimestamp());
	}

	private int compareInt(int c1, int c2) {
		return (c1 < c2) ? -1 : (c1 == c2 ? 0 : 1);
	}

	private int compareLong(Long c1, Long c2) {
		return (c1 < c2) ? -1 : (c1 == c2 ? 0 : 1);
	}

}
