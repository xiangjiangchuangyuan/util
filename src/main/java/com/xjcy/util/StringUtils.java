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

	public static String padLeft(Object orgin, String str, int len)
	{
		int length = orgin.toString().length();
		int remaining = len - length;
		if (remaining > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < remaining; i++)
			{
				sb.append(str);
			}
			sb.append(orgin);
			return sb.toString();
		}
		return orgin.toString();
	}
}
