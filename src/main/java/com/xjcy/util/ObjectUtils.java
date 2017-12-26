package com.xjcy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	public static String input2String(InputStream input)
	{
		StringBuilder sb = new StringBuilder();
		String line = null;
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(input);
		}
		return sb.toString();
	}

	private static void close(InputStream input)
	{
		try
		{
			input.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
