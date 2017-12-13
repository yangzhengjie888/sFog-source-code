package com.example.androidtemplate.common;

import android.content.Context;
import android.widget.Toast;


/**
 * 调试输出
 * 
 */
public class D {

	public static boolean DEBUG_SWITCH = true;

	public static void out(Exception e) {
		if (DEBUG_SWITCH) {
			e.printStackTrace();
		}
	}

	public static void out(Object obj) {
		if (DEBUG_SWITCH) {
			String str = obj.toString();
			if(str.length() > 2000) {
				int cnt = 0;
				while (cnt < str.length()) {
					int end = (cnt + 2000) > str.length() ? str.length() : cnt + 2000;
					System.out.println(str.substring(cnt, end));
					cnt += 2000;
				}
			} else {
				System.out.println(obj);
			}
		}
	}
	
	public static void toastLong(Context context, Object obj) {
		Toast.makeText(context, obj.toString(), Toast.LENGTH_LONG).show();
	}


}
