package com.css.utils;


import java.io.*;
import java.util.*;

/**
 * 读取Properties文件的工具类
 */
public class PropertiesUtils {

	public static final String APP_CONFIG = "application.properties";

	private PropertiesUtils() {
		super();
	}

	public static Properties getProperties(final String fileName) {
		final Properties props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_CONFIG));
		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
		return props;
	}

	public static HashMap<String, String> getMapByFileName(String fileName) {
		final HashMap<String, String> result = new HashMap<String, String>();
		final Properties pros = getProperties(fileName);
		final Set<Object> keys = pros.keySet();
		for (final Object o : keys) {
			result.put(o.toString(), pros.getProperty(o.toString()));
		}
		return result;
	}

	public static HashMap<String, String> getAppConf(){
		return getMapByFileName(APP_CONFIG);
	}
}
