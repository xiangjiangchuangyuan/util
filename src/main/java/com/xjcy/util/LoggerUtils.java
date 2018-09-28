package com.xjcy.util;

import org.apache.log4j.Logger;

public class LoggerUtils {

	private Logger logger;

	private <T> LoggerUtils(Class<T> class1) {
		logger = Logger.getLogger(class1);
	}

	public static <T> LoggerUtils from(Class<T> class1) {
		return new LoggerUtils(class1);
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void error(String message) {
		logger.error(message);
	}

	public void error(String message, Throwable e) {
		logger.error(message, e);
	}
	
}
