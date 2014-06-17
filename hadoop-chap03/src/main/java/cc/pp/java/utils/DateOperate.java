package cc.pp.java.utils;

import java.util.Date;

public class DateOperate {

	public static void main(String[] args) {

		Date start = new Date(1384222739145_000L);
		Date end = new Date(1384223739145_000L);

		Date now = new Date(1384222739146_000L);

		System.out.println(now.before(start));
		System.out.println(now.after(end));
	}

}
