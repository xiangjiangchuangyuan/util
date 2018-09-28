package com.xjcy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 执行脚本函数
 * @author YYDF
 * 2018-02-04
 */
public class CmdUtils
{
	private static final LoggerUtils logger = LoggerUtils.from(CmdUtils.class);
	
	public static void execute(String script)
	{
		try
		{
			Process proc = Runtime.getRuntime().exec(script);
			proc.waitFor(); // 阻塞，直到上述命令执行完
			proc = Runtime.getRuntime().exec(script);
			// 注意下面的操作
			String ls_1 = null;
			BufferedReader buffer = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((ls_1 = buffer.readLine()) != null)
				;
			buffer.close();
			proc.waitFor();
			if (logger.isDebugEnabled())
				logger.debug("执行命令结果：" + ls_1);
		}
		catch (IOException | InterruptedException e)
		{
			logger.error("执行命令" + script + "失败", e);
		}
	}
}
