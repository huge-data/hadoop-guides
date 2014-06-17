package cc.pp.hadoop.mapred.driver;

import org.apache.hadoop.util.ProgramDriver;

import cc.pp.hadoop.conf.log.LoggingDriver;
import cc.pp.hadoop.mapred.v2.MaxTemperatureDriverV2;
import cc.pp.hadoop.mapred.v3.MaxTemperatureDriverV3;
import cc.pp.hadoop.mapred.v4.MaxTemperatureDriverV4;
import cc.pp.hadoop.mapred.v5.MaxTemperatureDriverV5;
import cc.pp.hadoop.mapred.v6.MaxTemperatureDriverV6;
import cc.pp.hadoop.mapred.v7.MaxTemperatureDriverV7;

public class Driver {

	public static void main(String[] args) {

		int exitCode = -1;

		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("loggingDriver", LoggingDriver.class, "MapReduce日志打印");
			pgd.addClass("maxTemperatureDriverV2", MaxTemperatureDriverV2.class, "错误数据处理");
			pgd.addClass("maxTemperatureDriverV3", MaxTemperatureDriverV3.class, "错误数据处理，独立出来");
			pgd.addClass("maxTemperatureDriverV4", MaxTemperatureDriverV4.class, "错误数据处理，并计数");
			pgd.addClass("maxTemperatureDriverV5", MaxTemperatureDriverV5.class, "异常温度数据处理，并计数");
			pgd.addClass("maxTemperatureDriverV6", MaxTemperatureDriverV6.class, "使用profile性能调优工具，纪录任务性能数据");
			pgd.addClass("maxTemperatureDriverV7", MaxTemperatureDriverV7.class, "对象重用");
			pgd.driver(args);

			exitCode = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println(exitCode);
	}

}
