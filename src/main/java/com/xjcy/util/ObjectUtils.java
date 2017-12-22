package com.xjcy.util;

import java.io.UnsupportedEncodingException;
import java.util.Formatter;

public class ObjectUtils
{

	public static String byte2String(byte[] data, String charset)
	{
		try
		{
			return new String(data, charset);
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}

	/**
	 * 方法名：byteToHex</br>
	 * 详述：字符串加密辅助方法 </br>
	 * 开发人员：souvc </br>
	 * 创建时间：2016-1-5 </br>
	 * 
	 * @param hash
	 * @return 说明返回值含义
	 * @throws 说明发生此异常的条件
	 */
	public static String byteToHex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
