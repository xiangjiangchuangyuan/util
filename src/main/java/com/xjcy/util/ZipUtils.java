package com.xjcy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	private static final LoggerUtils logger = LoggerUtils.from(ZipUtils.class);

	/**
	 * 压缩指定的文件到同一个压缩包
	 * 
	 * @param zipFileName 压缩后文件名
	 * @param sourceFiles 压缩的File路径数组
	 * @return 压缩结果
	 */
	public static boolean Compress(String zipFileName, String[] sourceFiles) {
		try {
			// 创建zip输出流
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
			for (String sourceFile : sourceFiles) {
				// 调用函数
				compress(out, sourceFile);
			}
			out.close();
			return true;
		} catch (IOException e) {
			logger.error("压缩失败", e);
			return false;
		}
	}

	public static boolean Compress(OutputStream os, Map<String, List<String[]>> sourceFiles) {
		try {
			// 创建zip输出流
			ZipOutputStream out = new ZipOutputStream(os);
			Set<String> paths = sourceFiles.keySet();
			for (String path : paths) {
				// 调用函数
				compress(out, path, sourceFiles.get(path));
			}
			out.close();
			return true;
		} catch (IOException e) {
			logger.error("压缩失败", e);
			return false;
		}
	}

	private static void compress(ZipOutputStream out, String path, List<String[]> sourceFiles) throws IOException {
		InputStream is;
		for (String[] file : sourceFiles) {
			out.putNextEntry(new ZipEntry(path + "/" + file[0]));
			is = new URL(file[1]).openStream();
			int tag;
			while ((tag = is.read()) != -1) {
				out.write(tag);
			}
			is.close();
			out.closeEntry();
		}
	}

	private static void compress(ZipOutputStream out, String sourceFile) throws IOException {
		File file = new File(sourceFile);
		out.putNextEntry(new ZipEntry(file.getName()));
		FileInputStream fos = new FileInputStream(file);
		int tag;
		while ((tag = fos.read()) != -1) {
			out.write(tag);
		}
		fos.close();
		out.closeEntry();
	}
}
