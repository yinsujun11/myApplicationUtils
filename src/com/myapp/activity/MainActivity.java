package com.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myapp.http.AbHttpUtil;
import com.myapp.http.AbRequestParams;
import com.myapp.http.AbStringHttpResponseListener;
import com.myapp.utils.AbAppUtil;
import com.myapplicationdemo.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	private AbHttpUtil mAbHttpUtil = null;
	private Button button;
	private String TAG=MainActivity.this.getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button=(Button) findViewById(R.id.dummy_button);
	      //获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setDebug(true);
        
        final AbRequestParams params = new AbRequestParams();
        final String url="http://www.kuaiditu.com/kuaidituappprot/logisticsData/getLogistics";
		params.put("strOrderCode", "560031072312");
		params.put("strCpay", "圆通速递");
		params.put("identifying", "Android");
		params.put("strMobile", "15618535360");
		params.put("strOpenID", "");
 button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				String url="http://www.kuaiditu.com/kuaidituappprot/logisticsData/getkudcouriercompany?";
				// 绑定参数
//		        AbRequestParams params = new AbRequestParams(); 
//		        params.put("orderCode", "560031072312");
		        mAbHttpUtil.post(url,params, new AbStringHttpResponseListener() {
		        	
		        	// 获取数据成功会调用这里
		        	@Override
		        	public void onSuccess(int statusCode, String content) {
		        		Log.d(TAG, "onSuccess"+content);
		        		Intent intent=new Intent(MainActivity.this,ResultActivity.class);
		        		intent.putExtra("result", content);
		        		startActivity(intent);
		            };
		            
		            // 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
					}
		            
		            // 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	Log.e(TAG, statusCode+content);
		            	Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();;
		            	onRetry();
					}

					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            };
		            
		        });
			}
		});
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		
	}
    // 退出
    private long touchTime;
    private long waitTime = 1500;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			 long currentTime = System.currentTimeMillis();
	            if ((currentTime - touchTime) >= waitTime) {
	                touchTime = currentTime;
	                AbAppUtil.showTextToast(this, "再按一次退出");
	            } else {
	                //第二次退出应用程序
//	               AppManager.getAppManager().AppExit(this);
//	            	AppManager.getAppManager().finishAllActivity();
	            	System.exit(0);
	            }
	            return true;
		}
		return false;
	}
}
