package com.myapp.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myapp.http.AbHttpUtil;
import com.myapp.utils.AbAppUtil;
import com.myapplicationdemo.R;
import com.ypy.eventbus.EventBus;

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
	private RequestQueue mQueue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		 EventBus.getDefault().register(MainActivity.this); 
		button=(Button) findViewById(R.id.dummy_button);
		mQueue = Volley.newRequestQueue(this);
		//JsonObjectRequest
//	    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,  
//	            new Response.Listener<JSONObject>() {  
//	                @Override  
//	                public void onResponse(JSONObject response) {  
//	                    Log.d("TAG", response.toString());  
//	                }  
//	            }, new Response.ErrorListener() {  
//	                @Override  
//	                public void onErrorResponse(VolleyError error) {  
//	                    Log.e("TAG", error.getMessage(), error);  
//	                }  
//	            });  

		
		
	      //获取Http工具类
//        mAbHttpUtil = AbHttpUtil.getInstance(this);
//        mAbHttpUtil.setDebug(true);
//        final AbRequestParams params = new AbRequestParams();
//        final String url="http://www.kuaiditu.com/kuaidituappprot/logisticsData/getLogistics";
//		params.put("strOrderCode", "560031072312");
//		params.put("strCpay", "圆通速递");
//		params.put("identifying", "Android");
//		params.put("strMobile", "15618535360");
//		params.put("strOpenID", "");
 button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringRequest stringRequest = new StringRequest(Method.POST, "http://www.kuaiditu.com/kuaidituappprot/logisticsData/getLogistics",  
						new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {
								Log.e(TAG, response);
								Intent intent=new Intent(MainActivity.this,ResultActivity.class);
				        		intent.putExtra("result", response);
				        		startActivity(intent);
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("TAG", error.getMessage(), error);
								Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}) {
					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> params = new HashMap<String, String>();
						params.put("strOrderCode", "560031072312");
						params.put("strCpay", "圆通速递");
						params.put("identifying", "Android");
						params.put("strMobile", "15618535360");
						params.put("strOpenID", "");
						return params;
					}
				};
				mQueue.add(stringRequest);
				 
//				String url="http://www.kuaiditu.com/kuaidituappprot/logisticsData/getkudcouriercompany?";
				// 绑定参数
//		        AbRequestParams params = new AbRequestParams(); 
//		        params.put("orderCode", "560031072312");
//		        mAbHttpUtil.post(url,params, new AbStringHttpResponseListener() {
//		        	
//		        	// 获取数据成功会调用这里
//		        	@Override
//		        	public void onSuccess(int statusCode, String content) {
//		        		Log.d(TAG, "onSuccess"+content);
//		        		Intent intent=new Intent(MainActivity.this,ResultActivity.class);
//		        		intent.putExtra("result", content);
//		        		startActivity(intent);
//		            };
//		            
//		            // 开始执行前
//		            @Override
//					public void onStart() {
//		            	Log.d(TAG, "onStart");
//					}
//		            
//		            // 失败，调用
//		            @Override
//					public void onFailure(int statusCode, String content,
//							Throwable error) {
//		            	Log.e(TAG, statusCode+content);
//		            	Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();;
//		            	onRetry();
//					}
//
//					// 完成后调用，失败，成功
//		            @Override
//		            public void onFinish() { 
//		            	Log.d(TAG, "onFinish");
//		            };
//		            
//		        });
			}
		});
//		
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
//    public void onEventMainThread(String event) {  
//        String msg = "onEventMainThread收到了消息：" + event;  
//        button.setText(msg);
////        tv.setText(msg);  
//        Log.e(TAG, "eventBus接收到的参数："+msg);
//        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
//    }  
//    
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		EventBus.getDefault().unregister(this);
//	}
//	  /**
//     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse} .
//     *
//     * @param response The network response to parse headers from
//     * @return a cache entry for the given response, or null if the response is not cacheable.
//     */
//    public static Cache.Entry parseCacheHeaders(NetworkResponse response) {
//        long now = System.currentTimeMillis();
//
//        Map<String, String> headers = response. headers;
//
//        long serverDate = 0;
//        long serverExpires = 0;
//        long softExpire = 0;
//        long maxAge = 0;
//        boolean hasCacheControl = false;
//
//        String serverEtag = null;
//        String headerValue;
//
//        headerValue = headers.get( "Date");
//        if (headerValue != null) {
//            serverDate = parseDateAsEpoch(headerValue);
//        }
//
//        headerValue = headers.get( "Cache-Control");
//        if (headerValue != null) {
//            hasCacheControl = true;
//            String[] tokens = headerValue.split( ",");
//            for ( int i = 0; i < tokens. length; i++) {
//                String token = tokens[i].trim();
//                if (token.equals( "no-cache") || token.equals("no-store")) {
//                    return null;
//                } else if (token.startsWith( "max-age=")) {
//                    try {
//                        maxAge = Long. parseLong(token.substring(8));
//                    } catch (Exception e) {
//                    }
//                } else if (token.equals( "must-revalidate") || token.equals("proxy-revalidate" )) {
//                    maxAge = 0;
//                }
//            }
//        }
//
//        headerValue = headers.get( "Expires");
//        if (headerValue != null) {
//            serverExpires = parseDateAsEpoch(headerValue);
//        }
//
//        serverEtag = headers.get( "ETag");
//
//        // Cache-Control takes precedence over an Expires header, even if both exist and Expires
//        // is more restrictive.
//        if (hasCacheControl) {
//            softExpire = now + maxAge * 1000;
//        } else if (serverDate > 0 && serverExpires >= serverDate) {
//            // Default semantic for Expire header in HTTP specification is softExpire.
//            softExpire = now + (serverExpires - serverDate);
//        }
//
//        Cache.Entry entry = new Cache.Entry();
//        entry. data = response. data;
//        entry. etag = serverEtag;
//        entry. softTtl = softExpire;
//        entry. ttl = entry. softTtl;
//        entry. serverDate = serverDate;
//        entry. responseHeaders = headers;
//
//        return entry;
//    }
}

