package com.xjcy.util;

public class StringUtils
{

	public static boolean isEmpty(String str)
	{
		return str == null || str.length() == 0;
	}

	public static boolean isNotBlank(String str)
	{
		return !isEmpty(str);
	}

}
