package com.myapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.myapp.base.BaseActivity;
import com.myapplicationdemo.R;

public class ResultActivity extends BaseActivity{
	private TextView tvResult;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.http_resutl_activity);
	}
	@Override
	public void initView() {
		tvResult=(TextView) findViewById(R.id.result_activity_tv);
		tvResult.setText(getIntent().getStringExtra("result"));
	}
}
