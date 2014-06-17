package cc.pp.mapred.logprocess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class LogWritable implements Writable {

	private Text userIP;
	private final Text timestamp;
	private final Text request;
	private final IntWritable responseSize, status;

	public LogWritable() {
		this.userIP = new org.apache.hadoop.io.Text();
		this.userIP = new Text();
		this.timestamp = new Text();
		this.request = new Text();
		this.responseSize = new IntWritable();
		this.status = new IntWritable();
	}

	public LogWritable(Text userIP, Text timestamp, Text request, IntWritable responseSize, IntWritable status) {
		this.userIP = userIP;
		this.timestamp = timestamp;
		this.request = request;
		this.responseSize = responseSize;
		this.status = status;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		userIP.readFields(in);
		timestamp.readFields(in);
		request.readFields(in);
		responseSize.readFields(in);
		status.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		userIP.write(out);
		timestamp.write(out);
		request.write(out);
		responseSize.write(out);
		status.write(out);
	}

}
