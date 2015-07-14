package com.myapp.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity{
	public String TAG=this.getClass().getSimpleName();
	public abstract void initView();
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initView();
		AppManager.getAppManager().addActivity(this);
//		initView();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity();
	}
}
