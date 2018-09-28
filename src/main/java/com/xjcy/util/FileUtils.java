package com.xjcy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

/**
 * 文件存储
 * 
 * @author YYDF
 *
 */
public class FileUtils {
	private static final LoggerUtils logger = LoggerUtils.from(FileUtils.class);

	static final int BYTE_LEN = 4096;

	public static boolean saveFile(File dest, InputStream input) {
		if (input == null)
			return false;
		try {
			if(!dest.getParentFile().exists())
				dest.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(dest);
			byte[] buffer = new byte[BYTE_LEN];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				fos.write(buffer, 0, n);
			}
			// 关闭输入流等（略）
			fos.close();
			return true;
		} catch (IOException e) {
			logger.error("保存文件失败", e);
		}
		return false;
	}

	public static void saveFile(File dest, String txt) {
		if (txt == null)
			return;
		try {
			dest.createNewFile();
			Writer out2 = new FileWriter(dest);
			out2.write(txt);
			out2.close();
		} catch (Exception e) {
			logger.error("保存文件失败", e);
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				if (!deleteDir(new File(dir, children[i]))) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
}
