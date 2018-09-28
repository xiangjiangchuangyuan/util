package com.xjcy.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.xjcy.util.LoggerUtils;
import com.xjcy.util.ObjectUtils;
import com.xjcy.util.STR;

public class WebClient {
	private static final LoggerUtils logger = LoggerUtils.from(WebClient.class);

	public static SSLSocketFactory getSSLSocketFactory(String p12, String p12Pass) {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			char[] password = p12Pass.toCharArray();
			InputStream inputStream = WebClient.class.getClassLoader().getResourceAsStream(p12);
			ks.load(inputStream, password);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, password);
			SSLContext ssl = SSLContext.getInstance("TLS");
			ssl.init(kmf.getKeyManagers(), null, null);
			return ssl.getSocketFactory();
		} catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException | UnrecoverableKeyException e) {
			logger.error("加载证书失败" + p12, e);
			return null;
		}
	}

	public static String downloadString(String url) {
		byte[] data = downloadData(url);
		return ObjectUtils.byte2String(data, STR.ENCODING_UTF8);
	}

	public static byte[] downloadData(String url) {
		try {
			// 将返回的输入流转换成字符串
			return ObjectUtils.input2Data(new URL(url).openStream());
		} catch (IOException e) {
			logger.error("调用downloadData失败", e);
		}
		return null;
	}

	public static String uploadString(String url, String postStr) {
		byte[] data = ObjectUtils.string2Byte(postStr, STR.ENCODING_UTF8);
		byte[] result = uploadData(url, data);
		return ObjectUtils.byte2String(result, STR.ENCODING_UTF8);
	}

	public static byte[] uploadData(String url, byte[] data) {
		return uploadData(url, data, null, null, null);
	}

	public static String uploadData(String url, byte[] data, SSLSocketFactory ssl) {
		byte[] result = uploadData(url, data, null, null, ssl);
		return ObjectUtils.byte2String(result, STR.ENCODING_UTF8);
	}

	public static String uploadData(String url, byte[] data, Map<String, String> headers, Cookie cookie) {
		byte[] result = uploadData(url, data, headers, cookie, null);
		return ObjectUtils.byte2String(result, STR.ENCODING_UTF8);
	}

	public static byte[] uploadData(String url, byte[] data, Map<String, String> headers, Cookie cookie,
			SSLSocketFactory ssl) {
		try {
			URLConnection conn;
			if (ssl == null)
				conn = new URL(url).openConnection();
			else {
				HttpsURLConnection conn2 = (HttpsURLConnection) new URL(url).openConnection();
				conn2.setSSLSocketFactory(ssl);
				conn = conn2;
			}

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			if (headers != null) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					conn.addRequestProperty(key, headers.get(key));
				}
			}

			conn.connect();

			// 发送请求参数
			if (data != null) {
				OutputStream os = conn.getOutputStream();
				os.write(data);
				os.flush();
				os.close();
			}
			// 获取请求结果
			if (cookie != null)
				cookie.getCookie(conn.getHeaderField("Set-Cookie"));
			return ObjectUtils.input2Data(conn.getInputStream());
		} catch (IOException e) {
			logger.error("调用uploadData失败", e);
		}
		return null;
	}

	public static String downloadData(String url, Cookie cookie) {
		return uploadData(url, null, null, cookie);
	}
}
