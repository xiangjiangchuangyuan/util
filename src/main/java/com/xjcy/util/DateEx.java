package com.xjcy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateEx
{
	private static Calendar cal = null;
	static final SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");

	static
	{
		cal = Calendar.getInstance();
	}

	/**
	 * 转换字符串为date，支持多种格式
	 * 
	 * @param str
	 * @return
	 */
	public static Date toDate(String str)
	{
		if (StringUtils.isEmpty(str))
			return null;
		String format = null;
		if (str.length() == 8) // 20111212
			format = "yyyyMMdd";
		else if (str.length() == 10)
		{
			if (str.contains("-"))
				format = "yyyy-MM-dd";
			else if (str.contains(":"))
				format = "HH:mm:ss";
			else
				return toDate(Long.parseLong(str));
		}
		else if (str.length() == 14) // 20170707113005
			format = "yyyyMMddHHmmss";
		else if (str.length() == 19)
			format = "yyyy-MM-dd HH:mm:ss";
		if (format == null)
			return null;
		try
		{
			return new SimpleDateFormat(format).parse(str);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static Date toDate(long time)
	{
		return new Date(time * 1000);
	}

	public static int getDay()
	{
		return getDay(0);
	}

	public static int getDay(int day)
	{
		cal.setTime(new Date());
		if (day != 0)
			cal.add(Calendar.DATE, day);
		return cal.get(Calendar.DATE);
	}
	
	public static Date addDay(String date, int day)
	{
		cal.setTime(toDate(date));
		if (day != 0)
			cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	public static String addDay(int day)
	{
		cal.setTime(new Date());
		if (day != 0)
			cal.add(Calendar.DATE, day);
		return shortSdf.format(cal.getTime());
	}

	public static String today()
	{
		return today(null, false);
	}
	
	public static String today(String join)
	{
		return today(join, false);
	}

	public static String today(String join, boolean withTime)
	{
		String format;
		if (withTime)
			format = (join == null) ? "yyyy年MM月dd日 HH时mm分ss秒" : ("yyyy" + join + "MM" + join + "dd " + "HH:mm:ss");
		else
			format = (join == null) ? "yyyy年MM月dd日" : ("yyyy" + join + "MM" + join + "dd");
		return new SimpleDateFormat(format).format(new Date());
	}

	public static int getMonth()
	{
		return getMonth(0);
	}

	public static int getMonth(int month)
	{
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, month);
		return cal.get(Calendar.MONTH);
	}

	public static int getYear()
	{
		return getYear(0);
	}

	public static int getYear(int year)
	{
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, year);
		return cal.get(Calendar.YEAR);
	}
	
	public static Date addYear(String date, int year)
	{
		cal.setTime(toDate(date));
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}
	
	public static Date addMonth(Date date, int month)
	{
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}
	
	static long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数    
	static long nh = 1000 * 60 * 60;// 一小时的毫秒数    
	static long nm = 1000 * 60;// 一分钟的毫秒数    
	static long ns = 1000;// 一秒钟的毫秒数    
	
	/**
	 * 获取时间差
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String getDiff(String startTime, String endTime) {  
        long diff = toDate(endTime).getTime() - toDate(startTime).getTime();  
        long day = diff / nd;// 计算差多少天    
        long hour = diff % nd / nh + day * 24;// 计算差多少小时    
        long min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟    
        long sec = diff % nd % nh % nm / ns;// 计算差多少秒    
        // 输出结果    
        return day + "天" + (hour - day * 24) + "小时"  
                + (min - day * 24 * 60) + "分钟" + sec + "秒";
    }  
}
