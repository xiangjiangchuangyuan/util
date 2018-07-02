package com.xjcy.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/***
 * 通过jsonKey获取值
 * 
 * @author YYDF 2018-04-23
 */
public class JSONUtils {

	private static final Logger logger = Logger.getLogger(JSONUtils.class);

	public static String getString(String json, String jsonKey) {
		return getString(json, jsonKey, false);
	}

	public static String getString(String json, String jsonKey, boolean fromLast) {
		if (StringUtils.isEmpty(json))
			return null;
		int startIndex;
		if (fromLast)
			startIndex = json.lastIndexOf(jsonKey);
		else
			startIndex = json.indexOf(jsonKey);
		if (startIndex == -1)
			return null;
		int start = json.indexOf("\"", startIndex + jsonKey.length() + 2);
		int end = json.indexOf("\"", start + 1);
		return json.substring(start + 1, end);
	}

	public static Integer getInteger(String json, String jsonKey) {
		return getInteger(json, jsonKey, false);
	}

	public static Integer getInteger(String json, String jsonKey, boolean fromLast) {
		if (StringUtils.isEmpty(json))
			return null;
		int startIndex;
		if (fromLast)
			startIndex = json.lastIndexOf(jsonKey);
		else
			startIndex = json.indexOf(jsonKey);
		if (startIndex == -1)
			return null;
		int start = startIndex + jsonKey.length() + 2;
		int end = json.indexOf(",", start);
		if (end == -1) {
			end = json.indexOf("}", start);
			if (end == -1)
				end = json.indexOf("]", start);
		}
		json = json.substring(start, end).replace("\"", "").replace("\'", "");
		return Integer.parseInt(json);
	}

	public static Long getLong(String json, String jsonKey) {
		return getLong(json, jsonKey, false);
	}

	public static Long getLong(String json, String jsonKey, boolean fromLast) {
		if (StringUtils.isEmpty(json))
			return null;
		int startIndex;
		if (fromLast)
			startIndex = json.lastIndexOf(jsonKey);
		else
			startIndex = json.indexOf(jsonKey);
		if (startIndex == -1)
			return null;
		int start = startIndex + jsonKey.length() + 2;
		int end = json.indexOf(",", start);
		if (end == -1) {
			end = json.indexOf("}", start);
			if (end == -1)
				end = json.indexOf("]", start);
		}
		return Long.parseLong(json.substring(start, end));
	}

	public static <T> List<T> toList(String str, Class<T> t) {
		List<T> list = new ArrayList<>();
		if(StringUtils.isEmpty(str))
			return list;
		try {
			T tt;
			Field[] fields = t.getDeclaredFields();
			int len = StringUtils.find(str, "}");
			for (int i = 0; i < len; i++) {
				tt = t.newInstance();
				fill(tt, fields, str.substring(0, str.indexOf("}") + 1));
				list.add(tt);
				str = str.substring(str.indexOf("}") + 1);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Json to list faild", e);
		}
		return list;
	}

	private static final String STR_VERSION_UID = "serialVersionUID";

	private static <T> void fill(T tt, Field[] fields, String str)
			throws IllegalArgumentException, IllegalAccessException {
		for (Field field : fields) {
			if (field.getName().equals(STR_VERSION_UID))
				continue;
			field.setAccessible(true);
			if (field.getGenericType() == String.class)
				field.set(tt, getString(str, field.getName()));
			else if (field.getGenericType() == Integer.class)
				field.set(tt, getInteger(str, field.getName()));
			else if (field.getGenericType() == Long.class)
				field.set(tt, getLong(str, field.getName()));
			else if (field.getGenericType() == Date.class)
				field.set(tt, DateEx.toDate(getString(str, field.getName())));
			else
				throw new IllegalAccessException("Unknown generic type " + field.getGenericType());
			field.setAccessible(false);
		}
	}

	public static void main(String[] args) {
		// String json =
		// "[{\"greet\":\"您好，非常高兴能够为您解答疑惑，希望我的回答对您有所帮助。\",\"qualitative\":\"您咨询的是
		// 相关法律问题\",\"analysis\":\"您咨询的是 相关法律问题5\",\"suggest\":\"您咨询的是
		// 相关法律问5\",\"laws\":\"您咨询的是
		// 相关法律问题5\"},{\"greet\":\"您好，非常高兴能够为您解答疑惑，希望我的回答对您有所帮助。\",\"qualitative\":\"您咨询的是
		// 相关法律问题\",\"analysis\":\"您咨询的是 相关法律问题5\",\"suggest\":\"您咨询的是
		// 相关法律问5\",\"laws\":\"您咨询的是 相关法律问题5\"}]";
		String json = "[{\"fileName\":\"u=1390033421,4224470731&fm=27&gp=0.jpg\",\"fileUrl\":\"http://klbm.oss-cn-beijing.aliyuncs.com/15529923865809346.jpg\"}]";
		System.out.println(getString(json, "fileUrl"));
		// List<aa> list = toList(json, aa.class);
		// System.out.println(list.size());
	}
}
