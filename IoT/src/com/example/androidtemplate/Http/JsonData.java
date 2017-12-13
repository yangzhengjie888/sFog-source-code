package com.example.androidtemplate.Http;

import com.example.androidtemplate.common.D;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Json类型数据
 *
 */
public class JsonData {
	public static final int STATUS_OK = 0;
	public static final int STATUS_ERROR = 1;

	protected JSONObject jsonData;

	private boolean returnStatus;

	public JsonData(String gsonStr) {
		try {
			if(gsonStr==null){
				jsonData = new JSONObject("");
			}else{
				jsonData = new JSONObject(gsonStr);
			}
		} catch (JSONException e) {
			D.out(e);
		}
	}

	public boolean returnOk() {
		if (jsonData == null || !jsonData.has("success")) {
			return false;
		}

		returnStatus = jsonData.optString("success","false").equalsIgnoreCase("true") ? true : false;
		return returnStatus;
	}

	public String getMsg() {
		if (jsonData == null || !jsonData.has("success")) {
			return null;
		}

		if(jsonData.has("message")) {
			return jsonData.optString("message", "出现未知错误！");
		}

		return "出现未知错误！";
	}

	public JSONObject getJsonData() {
		return jsonData;
	}

	public String getData(){
		return jsonData.optString("rows");
	}

	@Override
	public String toString() {
		return jsonData.toString();
	}
}
