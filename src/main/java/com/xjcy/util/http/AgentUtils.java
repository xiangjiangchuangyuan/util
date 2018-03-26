package com.xjcy.util.http;

public class AgentUtils {

	public static boolean isWeixin(String userAgent) {
		if (userAgent == null)
			return false;
		if (userAgent.toLowerCase().indexOf("micromessenger") != -1)
			return true;
		return false;
	}
}
