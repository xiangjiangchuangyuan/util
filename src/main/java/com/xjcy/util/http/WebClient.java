package com.xjcy.util.http;

import java.io.ByteArrayOutputStream;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;

import com.xjcy.util.ObjectUtils;

public class WebClient {
	private static final Logger logger = Logger.getLogger(WebClient.class);

	private static final String CHARSET_UTF8 = "utf-8";

	public static final Map<String, String> DEFAULT_HEADERS = new HashMap<String, String>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;
		{
			put("Content-Type", "application/json; charset=UTF-8");
			put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
		}
	};

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
		return ObjectUtils.byte2String(data, CHARSET_UTF8);
	}

	public static byte[] downloadData(String url) {
		byte[] data = null;
		try {
			// 将返回的输入流转换成字符串
			InputStream is = new URL(url).openStream();
			byte[] temp = new byte[1024];
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			int rc = 0;
			while ((rc = is.read(temp, 0, 100)) > 0) {
				swapStream.write(temp, 0, rc);
			}
			data = swapStream.toByteArray();
			is.close();
			swapStream.close();
		} catch (IOException e) {
			logger.error("调用downloadData失败", e);
		}
		return data;
	}

	public static String uploadString(String url, String postStr) {
		byte[] data = ObjectUtils.string2Byte(postStr, CHARSET_UTF8);
		if (data != null) {
			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", "application/json; charset=UTF-8");
			headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
			return uploadData(url, data, null, null);
		}
		return null;
	}

	public static String uploadData(String url, byte[] data, SSLSocketFactory ssl) {
		return uploadData(url, data, null, null, ssl);
	}

	public static String uploadData(String url, byte[] data, Map<String, String> headers, Cookie cookie) {
		return uploadData(url, data, headers, cookie, null);
	}

	public static String uploadData(String url, byte[] data, Map<String, String> headers, Cookie cookie,
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
			String result = ObjectUtils.input2String(conn.getInputStream());
			if (cookie != null)
				cookie.getCookie(conn.getHeaderField("Set-Cookie"));
			return result;
		} catch (IOException e) {
			logger.error("调用uploadData失败", e);
		}
		return null;
	}

	public static String downloadData(String url, Cookie cookie) {
		return uploadData(url, null, null, cookie);
	}
}
