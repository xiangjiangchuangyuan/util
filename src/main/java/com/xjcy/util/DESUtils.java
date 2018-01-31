package com.xjcy.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class DESUtils
{
	private final static String DES = "DES";
	private final static String CHARSET = "utf-8";

	/**
	 * 加密 add zhongsanmu 20160831
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception
	{
		if (data == null)
			return null;
		byte[] bt = encrypt(data.getBytes(CHARSET), key.getBytes(CHARSET));
		return Base64.encodeBase64String(bt);
	}

	/**
	 * 解密 add zhongsanmu 20160831
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception
	{
		if (data == null)
			return null;
		byte[] buf = Base64.decodeBase64(data);// update zhongsanmu 20161009
		return new String(decrypt(buf, key.getBytes(CHARSET)), CHARSET);
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception
	{
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception
	{
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	public static void main(String[] args) throws Exception
	{
		String str = encrypt("31", "12345678");
		System.out.println(str);
		System.out.println(decrypt(str, "12345678"));
	}
}
