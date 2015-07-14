package com.myapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.myapp.base.BaseActivity;
import com.myapplicationdemo.R;
import com.ypy.eventbus.EventBus;

public class ResultActivity extends BaseActivity{
	private TextView tvResult;
	private String result;
	private Button bt_click;
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.http_resutl_activity);
		super.onCreate(arg0);
	}
	@Override
	public void initView() {
		tvResult=(TextView) findViewById(R.id.result_activity_tv);
		bt_click=(Button) findViewById(R.id.bt_click);
		result=getIntent().getStringExtra("result");
		tvResult.setText(result);
		tvResult.setHorizontallyScrolling(true);
		bt_click.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post("eventBus  时间总线");
				bt_click.setText("click");
//				ResultActivity.this.finish();
			}
		});
	}
}
