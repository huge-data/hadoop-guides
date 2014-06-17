package cc.pp.java.utils;

import org.apache.hadoop.conf.Configuration;

public class XmlReader {

	public static void main(String[] args) {

		Configuration conf = new Configuration();
		conf.addResource("src/main/conf/core-site.xml");
		conf.addResource("src/main/conf/hdfs-site.xml");
		System.out.println(conf.get("fs.default.name"));
		System.out.println(conf.get("dfs.replication"));
	}

}
