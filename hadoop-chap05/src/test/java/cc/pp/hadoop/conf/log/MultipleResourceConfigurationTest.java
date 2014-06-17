package cc.pp.hadoop.conf.log;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.hadoop.conf.Configuration;
import org.junit.Test;

public class MultipleResourceConfigurationTest {

	@Test
	public void testConf() {

		Configuration conf = new Configuration();
		conf.addResource("configuration-1.xml");
		conf.addResource("configuration-2.xml");

		assertThat(conf.get("color"), is("yellow"));
		/**
		 * 如果size值不存在，使用默认值0
		 */
		assertThat(conf.getInt("size", 0), is(12));
		assertThat(conf.get("weight"), is("heavy"));
		assertThat(conf.get("size-weight"), is("12,heavy"));

		/**
		 * 如果属性存在配置文件中，则系统设置起作用
		 */
		System.setProperty("size", "14");
		assertThat(conf.get("size-weight"), is("14,heavy"));

		System.setProperty("length", "2");
		assertThat(conf.get("length"), is((String) null));

	}

}
