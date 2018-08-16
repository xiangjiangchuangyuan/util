package com.xjcy.util;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

public class MD5
{
	/**
	 * 对字符串进行MD5编码
	 * @param str 字符串
	 * @return MD5后的字符串
	 */
	public static String encodeByMD5(String str)
	{
		if (StringUtils.isEmpty(str))
			return str;
		try
		{
			// 创建具有指定算法名称的信息摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
			byte[] results = md.digest(str.getBytes("utf-8"));
			// 将得到的字节数组变成字符串返回
			return ObjectUtils.byteToHex(results);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	public static String getRandomStr()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	static final Random r = new Random();
	
	public static synchronized long getRandomId() {
		return Long.parseLong(System.nanoTime() + "" + r.nextInt(10));
	}
}
