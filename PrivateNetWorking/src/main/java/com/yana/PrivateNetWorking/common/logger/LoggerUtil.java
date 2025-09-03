package com.yana.PrivateNetWorking.common.logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
	private static final String LOG_DIR = "log/";
	private static final String LINE_SEPALATOR = System.lineSeparator();
	private static LoggerUtil loggerUtil;
	private Logger logger;
	private LoggerUtil(Logger logger) {
		this.logger = logger;
	}

	public static synchronized void setLogger(String loggerName, String logName) throws IOException {
		if(loggerUtil != null) {
			return;
		}
		Logger logger = Logger.getLogger(loggerName);
		boolean isConsoleOut = Boolean.parseBoolean(System.getProperty("logOutConsole"));
		Handler handler;
		if(isConsoleOut) {
			handler = new ConsoleHandler();
		} else {
			handler = new FileHandler(LOG_DIR + logName);
		}
		logger.addHandler(handler);
		handler.setFormatter(new SimpleFormatter());
		logger.setLevel(Level.INFO);
		loggerUtil = new LoggerUtil(logger);
	}

	public static void info(String message) {
		loggerUtil.logger.info(message);
	}

	public static void warn(Throwable e) {
		String stMess = getStacTraceInfo(e);
		if(stMess.isEmpty()) {
			loggerUtil.logger.warning(e.getMessage());
		} else {
			loggerUtil.logger.warning(e.getMessage() + LINE_SEPALATOR + stMess);
		}
	}

	private static String getStacTraceInfo(Throwable e) {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] sts = e.getStackTrace();
		if(sts.length == 0) {
			return "";
		}
		sb.append(sts[0].toString());
		sb.append(LINE_SEPALATOR);
		for(int i = 1; i < sts.length; i++) {
			sb.append("\t" + sts[i].toString());
			sb.append(LINE_SEPALATOR);
		}
		String stMess = sb.toString();
		return stMess.substring(0, stMess.length() - LINE_SEPALATOR.length());
	}
}
