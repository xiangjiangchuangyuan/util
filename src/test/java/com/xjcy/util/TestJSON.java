package com.xjcy.util;

public class TestJSON
{
	public static void main(String[] args)
	{
		System.out.println(JSONUtils.getInteger("{\"auth\": 1}", "auth"));
	}
}
