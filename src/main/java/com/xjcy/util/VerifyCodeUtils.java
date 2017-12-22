package com.xjcy.util;

import java.io.OutputStream;
import java.util.Random;

public class VerifyCodeUtils
{
	private static final Random d = new Random();
	
	public static String generateCode(int len)
	{
		String str = "";
		for (int i = 0; i < 4; i++)
		{
			int num = d.nextInt(10);
			str += num + "";
		}
		return str;
	}
	
	public static void generateImage(int width, int height, OutputStream os, String code)
	{
		
	}
}
