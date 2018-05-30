package com.xjcy.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Formatter;
import java.util.zip.GZIPInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ObjectUtils {
	private static final Logger logger = Logger.getLogger(ObjectUtils.class);
	private static MessageDigest sha1MD;

	public static String byte2String(byte[] data, String charset) {
		if(data == null)
			return null;
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static byte[] string2Byte(String str, String charset) {
		if(str == null)
			return null;
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 方法名：byteToHex</br>
	 * 详述：字符串加密辅助方法 </br>
	 * 开发人员：souvc </br>
	 * 创建时间：2016-1-5 </br>
	 * 
	 * @param hash
	 * @return 说明返回值含义
	 * @throws 说明发生此异常的条件
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
		return decryptData(data, null, key);
	}

	public static String decryptData(byte[] data, byte[] iv, byte[] key) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			if (iv != null)
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
			else
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			return new String(cipher.doFinal(data));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			logger.error("Decrypt data faild:", e);
		}
		return null;
	}
}
