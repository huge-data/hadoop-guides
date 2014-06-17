package cc.pp.java.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternStrs {

	public static void main(String[] args) {

		String str = "-1-2ab333nk4&";
		// "^\\d+$"　　非负整数（正整数 + 0）
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(str);
		System.out.println(matcher.matches());
		while (matcher.matches()) {
			System.out.println(matcher.group(1));
		}

	}

}
