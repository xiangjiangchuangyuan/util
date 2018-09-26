package com.xjcy.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

public class ObjectUtils {
	private static final Logger logger = Logger.getLogger(ObjectUtils.class);
	private static MessageDigest sha1MD;

	public static String byte2String(byte[] data, String charset) {
		if (data == null)
			return null;
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static byte[] string2Byte(String str, String charset) {
		if (str == null)
			return null;
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 转换二进制数据为字符串
	 * 
	 * @param hash
	 *            二进制数据
	 * @return 说明返回值含义
	 */
	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static String input2String(InputStream input) {
		return input2String(input, false);
	}

	public static String input2String(InputStream input, boolean isGzip) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			if (isGzip)
				input = new GZIPInputStream(input);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(input);
		}
		return sb.toString();
	}

	public static byte[] input2Data(InputStream input) throws IOException {
		if (input == null)
			return null;
		byte[] temp = new byte[1024];
		ByteArrayOutputStream swapStream = null;
		try {
			swapStream = new ByteArrayOutputStream();
			int rc = 0;
			while ((rc = input.read(temp, 0, temp.length)) > 0) {
				swapStream.write(temp, 0, rc);
			}
			return swapStream.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			input.close();
			if (swapStream != null)
				swapStream.close();
		}
	}

	private static void close(InputStream input) {
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String SHA1(String text) {
		if (null == sha1MD) {
			try {
				sha1MD = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
		}
		try {
			sha1MD.update(text.getBytes("utf-8"), 0, text.length());
		} catch (UnsupportedEncodingException e) {
			sha1MD.update(text.getBytes(), 0, text.length());
		}
		return byteToHex(sha1MD.digest());
	}

	public static String decryptData(byte[] data, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			return new String(cipher.doFinal(data));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			logger.error("Decrypt data faild:", e);
		}
		return null;
	}

	public static String decryptData(byte[] data, byte[] iv, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			Key secretKeySpec = new SecretKeySpec(key, "AES");
			AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
			params.init(new IvParameterSpec(iv));
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, params);
			return new String(cipher.doFinal(data));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException | InvalidParameterSpecException e) {
			logger.error("Decrypt data faild:", e);
		}
		return null;
	}

	/**
	 * 合并数组
	 * 
	 * @param objs1
	 *            数组1
	 * @param objs2
	 *            数组2
	 * @return 合并后的数组
	 */
	public static Object[] mergeArray(Object[] objs1, Object[] objs2) {
		if (objs1.length == 0 && objs2.length == 0)
			return new Object[0];
		if (objs1.length == 0)
			return objs2;
		if (objs2.length == 0)
			return objs1;
		Object[] temp = new Object[objs1.length + objs2.length];
		System.arraycopy(objs1, 0, temp, 0, objs1.length);
		System.arraycopy(objs2, 0, temp, objs1.length, objs2.length);
		return temp;
	}

	/**
	 * 移除list中的相同项
	 * 
	 * @param strList
	 *            字符串集合
	 * @return 去重后的集合
	 */
	public static List<String> removeDuplicate(List<String> strList) {
		if (strList.isEmpty())
			return strList;
		List<String> listTemp = new ArrayList<>();
		for (int i = 0; i < strList.size(); i++) {
			if (!listTemp.contains(strList.get(i))) {
				listTemp.add(strList.get(i));
			}
		}
		return listTemp;
	}
}
