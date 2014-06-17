package cc.pp.hadoop.io;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;

public class TextArrayWritable extends ArrayWritable {

  public TextArrayWritable() {
    super(Text.class);
  }

}
