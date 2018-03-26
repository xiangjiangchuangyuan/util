package com.xjcy.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.xjcy.util.http.WebClient;

/**
 * 获取短链接
 * @author YYDF
 * 2018-02-03
 */
public class UrlUtils
{
	static final String SINA_URL = "http://api.t.sina.com.cn/short_url/shorten.json?source=3271760578&url_long=%s";
	static final String NI2_URL = "http://ni2.org/api/create.json?url=%s";
	static final String MRW_URL = "http://mrw.so/api.php?url=%s";
	

	public static String getShortUrlBySina(String url)
	{
		url = encodeUrl(url);
		String json = WebClient.downloadString(String.format(SINA_URL, url));
		return JSONUtils.getString(json, "url_short");
	}

	public static String getShortUrlByNi2(String url)
	{
		url = encodeUrl(url);
		String json = WebClient.downloadString(String.format(NI2_URL, url));
		return JSONUtils.getString(json, "url");
	}

	public static String getShortUrlByMrw(String url)
	{
		url = encodeUrl(url);
		return WebClient.downloadString(String.format(MRW_URL, url));
	}

	private static String encodeUrl(String url)
	{
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}
}
