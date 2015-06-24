package com.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.myapp.base.BaseActivity;
import com.myapplicationdemo.R;

public class WelocomeActivity extends BaseActivity{
	private SharedPreferences share;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welocome);
		share=WelocomeActivity.this.getSharedPreferences("isStart", Activity.MODE_PRIVATE);
		if(share.getInt("start", 0)==1){
			startActivity(new Intent(WelocomeActivity.this,MyViewpagerTest.class));
			WelocomeActivity.this.finish();
		}else{
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					share.edit().putInt("start", 1).commit();
					startActivity(new Intent(WelocomeActivity.this,MainActivity.class));
					WelocomeActivity.this.finish();
				}
			}, 3000);
		}
	}
	@Override
	public void initView() {
		
	}
}
