package com.xjcy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotBlank(String str) {
		return !isEmpty(str);
	}

	public static String padLeft(Object orgin, String str, int len) {
		int length = orgin.toString().length();
		int remaining = len - length;
		if (remaining > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < remaining; i++) {
				sb.append(str);
			}
			sb.append(orgin);
			return sb.toString();
		}
		return orgin.toString();
	}

	/**
	 * 将逗号分隔字符串转换为List
	 * @param str
	 * @return
	 */
	public static List<String> toList(String str) {
		List<String> strList = new ArrayList<>();
		if (isNotBlank(str)) {
			if (str.contains(","))
				strList = Arrays.asList(str.split(","));
			else
				strList.add(str);
		}
		return strList;
	}
}
