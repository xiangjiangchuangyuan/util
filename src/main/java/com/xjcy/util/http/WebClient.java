package com.xjcy.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

import com.xjcy.util.ObjectUtils;

public class WebClient
{
	private static final Logger logger = Logger.getLogger(WebClient.class);
	
	private static final String CHARSET_UTF8 = "utf-8";
	
	public static String downloadString(String url)
	{
		byte[] data = downloadData(url);
		return ObjectUtils.byte2String(data, CHARSET_UTF8);
	}
	
	public static byte[] downloadData(String url)
	{
		byte[] data = null;
		try
		{
			// 将返回的输入流转换成字符串
			InputStream is = new URL(url).openStream();
			byte[] temp = new byte[1024];
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			int rc = 0;
			while ((rc = is.read(temp, 0, 100)) > 0)
			{
				swapStream.write(temp, 0, rc);
			}
			data = swapStream.toByteArray();
			is.close();
			swapStream.close();
		}
		catch (IOException e)
		{
			logger.error("调用downloadData失败", e);
		}
		return data;
	}

}
