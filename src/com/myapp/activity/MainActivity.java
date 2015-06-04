package com.myapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myapp.http.AbHttpUtil;
import com.myapp.http.AbRequestParams;
import com.myapp.http.AbStringHttpResponseListener;
import com.myapp.utils.AbAppUtil;
import com.myapplicationdemo.R;
import com.myapplicationdemo.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	
	private AbHttpUtil mAbHttpUtil = null;
	private Button button;
	private String TAG=MainActivity.this.getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
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
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
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
