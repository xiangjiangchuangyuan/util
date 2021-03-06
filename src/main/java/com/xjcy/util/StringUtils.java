package com.xjcy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StringUtils {
	static Random random = new Random();// 随机类初始化

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
	 * 
	 * @param str 逗号分隔的字符串
	 * @return 字符串集合
	 */
	public static List<String> toList(String str) {
		List<String> strList = new ArrayList<>();
		if (isNotBlank(str)) {
			if (str.contains(","))
				strList = Arrays.asList(str.split(","));
			else
				strList.add(str);
		}
		return ObjectUtils.removeDuplicate(strList);
	}

	public static String toString(List<String> strList) {
		if (strList == null || strList.isEmpty())
			return null;
		StringBuffer buffer = new StringBuffer();
		for (String str : strList) {
			buffer.append(str);
			buffer.append(",");
		}
		buffer.delete(buffer.length() - 1, buffer.length());
		return buffer.toString();
	}

	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";// 含有字符和数字的字符串

		StringBuffer sb = new StringBuffer("_");// StringBuffer类生成，为了拼接字符串
		for (int i = 0; i < length; ++i) {
			sb.append(str.charAt(random.nextInt(62)));
		}
		return sb.toString();
	}

	public static int find(String str, String findStr) {
		if(StringUtils.isEmpty(str) || StringUtils.isEmpty(findStr) )
			return 0;
		int n = 0;// 计数器
		int index = 0;// 指定字符的长度
		index = str.indexOf(findStr);
		while (index != -1) {
			n++;
			index = str.indexOf(findStr, index + 1);
		}
		return n;
	}
}
