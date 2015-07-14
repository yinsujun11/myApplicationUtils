package com.test.dao;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.util.Log;

public class BaseRequest {
	public static String host;
	public static String baseURL;
	public static String baseImageURL;
	public String rawUrl;
	private String apiName;
	private Map<String, Object> parameters;
	private HashMap<String, File> fileBody;
	private HttpPost mHttpPost = null;
	private static String privateKey = "i5KppyKnGinSGGhFrVBsJz5XQhI14lmdel+QdN5DRkp7PlsdXFON3+4mpL21Ep8TgkT6iD";
	private static String publicKey = "vs1KJnwKu6BUnFO9MWvpORfMhNJkMzc4dZyjd1NaKRRWiY55tBIo3MBBrwOjczVXWTknmU";

	static {
		// set device info
		//        deviceInfo = new HashMap<String, String>();
		//        deviceInfo.put("dev", "a");
	}

	public void setRawUrl(String rawUrl) {
		this.rawUrl = rawUrl;
	}

	public BaseRequest(String apiName) {
		this.apiName = apiName;
	}

	public BaseRequest() {
	}

	private static void setBaseUrl() {
		baseURL = host+"/kuaidituInphone/v9/";
//		baseURL = host + "/kuaidituInphone/v2/";
		baseImageURL = host + "/kuaidituInphone";
	}

	public static void configure() {
//		if (MyApplication.isDebug()) {
//			host = getHostFromLocal();
//			//			host="http://115.29.240.85";
//			//			host="http://192.168.1.109:8080";
////						host="http://115.29.206.228";
//		} else {
//			host = "http://115.29.206.228";//签过名后使用这个host，程序会自动修改
//			//            host = "http://115.29.240.85";//签过名后使用这个host，程序会自动修改
//		}
		setBaseUrl();
	}

	private static String getHostFromLocal() {
//		SharedPreferences share = MyApplication.getInstance().getSharedPreferences("HostConfig", Context.MODE_PRIVATE);
		return "http://115.29.240.85";
	}

	public HttpResponse execute() {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		if (rawUrl != null) {
			mHttpPost = new HttpPost(this.rawUrl);
		} else {
			mHttpPost = new HttpPost(BaseRequest.baseURL + this.apiName);
		}

		MultipartEntity entity = new MultipartEntity();
		String sign = "";
		// add data
		try {
			entity.addPart("publicKey", new StringBody(BaseRequest.publicKey, Charset.forName("UTF-8")));
			entity.addPart("sign", new StringBody(sign, Charset.forName("UTF-8")));
			for (String key : this.parameters.keySet()) {
				entity.addPart(key, new StringBody(String.valueOf(this.parameters.get(key)), Charset.forName("UTF-8")));
			}
			if (fileBody != null) {
				for (String key : fileBody.keySet()) {
					FileBody bodyTemp = new FileBody(fileBody.get(key));
					entity.addPart(key, bodyTemp);
				}
			}
			mHttpPost.setEntity(entity);
			HttpResponse response = client.execute(mHttpPost);
			return response;
		} catch (UnsupportedEncodingException exception) {
			Log.e("exception", exception.toString());
			return null;
		} catch (ClientProtocolException exception) {
			Log.e("exception", exception.toString());
			return null;
		} catch (IOException exception) {
			Log.e("exception", exception.toString());
			return null;
		}

	}

	public void cancel() {
		if (mHttpPost != null) {
			mHttpPost.abort();
		}
	}

	public String getApiName() {
		return baseURL + apiName;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public HashMap<String, File> getFileBody() {
		return fileBody;
	}

	public void setFileBody(HashMap<String, File> fileBody) {
		this.fileBody = fileBody;
	}
}
