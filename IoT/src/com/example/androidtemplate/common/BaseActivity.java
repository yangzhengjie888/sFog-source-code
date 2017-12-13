package com.example.androidtemplate.common;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.androidtemplate.R;
import com.example.androidtemplate.manager.ManagerApp;

/**
 * 基础Activity,规范activity编程
 */
public abstract class BaseActivity extends Activity {
	
	
	protected BaseActivity this_;
	protected abstract void initData(); // 初始化数据

	protected abstract void recycle(); // 资源回收


	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this_ = this;
		ManagerApp.addActivity(this_);
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {   
		super.onStop();  
	}
	
	@Override
	protected void onDestroy() {
		recycle();
		super.onDestroy();
	}
	
	public void topLeftCorner(){
		overridePendingTransition(R.anim.scale_translate,
				R.anim.my_alpha_action);
	}

	public void leftInOut(){
		overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	public boolean isFail(String state){
		if(!TextUtils.isEmpty(state) && state.contains(Constants.FAIL_STATE)){
			return true;
		}else{
			return false;
		}
	}

}
