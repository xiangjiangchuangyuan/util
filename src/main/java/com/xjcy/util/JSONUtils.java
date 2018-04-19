package com.xjcy.util;

public class JSONUtils
{
	public static String getString(String json, String jsonKey)
	{
		return getString(json, jsonKey, false);
	}

	public static String getString(String json, String jsonKey, boolean fromLast)
	{
		if(StringUtils.isEmpty(json))
			return null;
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
		if(StringUtils.isEmpty(json))
			return null;
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
	
	public static void main(String[] args)
	{
		String json = "{\"greet\":\"您好，非常高兴能够为您解答疑惑，希望我的回答对您有所帮助。\",\"qualitative\":\"您咨询的是　相关法律问题\",\"analysis\":\"您咨询的是　相关法律问题5\",\"suggest\":\"您咨询的是　相关法律问5\",\"laws\":\"您咨询的是　相关法律问题5\"}";
		System.out.println(getString(json, "suggest"));
	}
}
