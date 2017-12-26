package com.xjcy.util;

public class JSONUtils
{
	public final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String getString(String json, String jsonKey)
	{
		return getString(json, jsonKey, false);
	}

	public static String getString(String json, String jsonKey, boolean fromLast)
	{
		int startIndex;
		if (fromLast) startIndex = json.lastIndexOf(jsonKey);
		else startIndex = json.indexOf(jsonKey);
		if (startIndex == -1) return null;
		int start = json.indexOf("\"", startIndex + jsonKey.length() + 2);
		int end = json.indexOf("\"", start + 1);
		return json.substring(start + 1, end);
	}

	public static Integer getInteger(String json, String jsonKey)
	{
		return getInteger(json, jsonKey, false);
	}

	public static Integer getInteger(String json, String jsonKey, boolean fromLast)
	{
		int startIndex;
		if (fromLast) startIndex = json.lastIndexOf(jsonKey);
		else startIndex = json.indexOf(jsonKey);
		if (startIndex == -1) return null;
		int start = startIndex + jsonKey.length() + 2;
		int end = json.indexOf(",", start + 1);
		if (end == -1)
		{
			end = json.indexOf("}", start + 1);
			if (end == -1) 
				end = json.indexOf("]", start + 1);
		}
		return Integer.parseInt(json.substring(start + 1, end));
	}
}
