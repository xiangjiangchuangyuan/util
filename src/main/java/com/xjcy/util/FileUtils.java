package com.xjcy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class FileUtils
{
	private static final Logger logger = Logger.getLogger(FileUtils.class);
			
	public static boolean saveFile(String fileName, InputStream input)
	{
		if (input == null) return false;
		try
		{
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer)))
			{
				fos.write(buffer, 0, n);
			}
			// 关闭输入流等（略）
			fos.close();
			return true;
		}
		catch (IOException e)
		{
			logger.error("保存文件失败", e);
		}
		return false;
	}
}
