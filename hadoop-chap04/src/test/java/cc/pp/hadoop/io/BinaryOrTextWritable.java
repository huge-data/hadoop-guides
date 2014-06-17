package cc.pp.hadoop.io;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.GenericWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class BinaryOrTextWritable extends GenericWritable {

	@SuppressWarnings("rawtypes")
	private static Class[] TYPES = { BytesWritable.class, Text.class };

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends Writable>[] getTypes() {
		return TYPES;
	}

}
