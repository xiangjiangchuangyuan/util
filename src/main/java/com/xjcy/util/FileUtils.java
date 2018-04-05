package com.xjcy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.log4j.Logger;

/**
 * 文件存储
 * @author YYDF
 *
 */
public class FileUtils
{
	private static final Logger logger = Logger.getLogger(FileUtils.class);
			
	static final int BYTE_LEN = 4096;
	
	public static boolean saveFile(File dest, InputStream input)
	{
		if (input == null) return false;
		try
		{
			FileOutputStream fos = new FileOutputStream(dest);
			byte[] buffer = new byte[BYTE_LEN];
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

	public static void saveFile(File dest, String txt) {
		if(txt == null) 
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
}
