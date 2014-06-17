package cc.pp.hadoop.driver;

import org.apache.hadoop.util.ProgramDriver;

import cc.pp.hadoop.io.StreamCompressor;

public class Driver {

	public static void main(String argv[]) {

		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("streamCompressor", StreamCompressor.class, "数据流压缩");
			pgd.driver(argv);

			exitCode = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.exit(exitCode);
	}

}
