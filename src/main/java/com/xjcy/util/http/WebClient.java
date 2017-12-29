package com.xjcy.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

	public static String uploadString(String url, String postStr)
	{
		byte[] data = ObjectUtils.string2Byte(postStr, CHARSET_UTF8);
		if (data != null)
		{
			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json; charset=UTF-8");
			headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
			return uploadData(url, data, null, null);
		}
		return null;
	}

	private static String uploadData(String url, byte[] data, Map<String, String> headers, Cookie cookie)
	{
		try
		{
			URLConnection conn = new URL(url).openConnection();

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			if (headers != null)
			{
				Set<String> keys = headers.keySet();
				for (String key : keys)
				{
					conn.addRequestProperty(key, headers.get(key));
				}
			}

			conn.connect();

			// 发送请求参数
			if (data != null)
			{
				OutputStream os = conn.getOutputStream();
				os.write(data);
				os.flush();
				os.close();
			}
			// 获取请求结果
			String result = ObjectUtils.input2String(conn.getInputStream());
			if(cookie != null)
				cookie.getCookie(conn.getHeaderField("Set-Cookie"));
			return result;
		}
		catch (IOException e)
		{
			logger.error("调用uploadData失败", e);
		}
		return null;
	}

	public static String downloadData(String url, Cookie cookie)
	{
		return uploadData(url, null, null, cookie);
	}
}
