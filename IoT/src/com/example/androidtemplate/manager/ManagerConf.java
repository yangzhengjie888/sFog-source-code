package com.example.androidtemplate.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 软件配置管理器
 */
public class ManagerConf {
	private static final String DATA_NAME = "template_common.options";

	private static SharedPreferences sp;

	/**
	 * 私有构造器，配合单例模式
	 * 
	 * @param context
	 */
	private ManagerConf(Context context) {

	}

	/**
	 * 初始化管理器
	 * 
	 * @param context
	 */
	public static void initManagerConf(Context context) {
		sp = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 保存到本地
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void saveToLocal(String key, String value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();

	}

	/**
	 * 从本地读取数据 读取值皆为String类型，可能需要类型转换
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static String readFromLocal(String key) {
		return sp.getString(key, null);

	}

	/**
	 * 从本地读取数据 读取值皆为String类型，可能需要类型转换
	 * 
	 * @param key
	 *            键
	 * @param defaultValue
	 *            返回的默认值
	 * @return 值
	 */
	public static String readFromLocal(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

}
