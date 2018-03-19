package com.xjcy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipUtils
{
	private static final Logger logger = Logger.getLogger(ZipUtils.class);

	/**
	 * 压缩指定的文件到同一个压缩包
	 * @param zipFileName
	 * @param sourceFiles
	 * @return
	 */
	public static boolean Compress(String zipFileName, String[] sourceFiles)
	{
		try
		{
			// 创建zip输出流
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
			for (String sourceFile : sourceFiles)
			{
				// 调用函数
				compress(out, sourceFile);
			}
			out.close();
			return true;
		}
		catch (IOException e)
		{
			logger.error("压缩失败", e);
			return false;
		}
	}

	private static void compress(ZipOutputStream out, String sourceFile) throws IOException
	{
		File file = new File(sourceFile);
		out.putNextEntry(new ZipEntry(file.getName()));
		FileInputStream fos = new FileInputStream(file);
		int tag;
		while ((tag = fos.read()) != -1)
		{
			out.write(tag);
		}
		fos.close();
		out.closeEntry();
	}
}
