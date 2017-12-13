package com.example.androidtemplate.manager;


import android.os.Handler;

import com.example.androidtemplate.mo.User;
import com.example.androidtemplate.mo.WifiInfoc;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * 静态变量管理
 */
public class ManagerComm {

	public static DisplayImageOptions displayImageOptions;
//	public static String selectImageTempPath = Environment.getExternalStorageDirectory() + "/img.jpg";
//	public static String imageTempPath = Environment.getExternalStorageDirectory() + "/temp.jpg";
	public static User loginUser;

	public static List<WifiInfoc> wifiInfoList = new ArrayList<WifiInfoc>();
	public static Handler handler;

	public static String gatewayIp;
	public static ArrayList<com.example.androidtemplate.mo.Time> timeList;
	public static int countflag =0;

}
