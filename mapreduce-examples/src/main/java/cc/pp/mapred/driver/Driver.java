package cc.pp.mapred.driver;

import org.apache.hadoop.util.ProgramDriver;

import cc.pp.mapred.jobchaining.JobChainingMain;

public class Driver {

	public static void main(String[] args) {

		int exitCode = -1;

		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("jobChainingMain", JobChainingMain.class, "MapReduce作业链");
			pgd.driver(args);

			exitCode = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println(exitCode);
	}

}
