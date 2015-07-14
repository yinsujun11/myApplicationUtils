


package com.test.dao;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseDAO implements Runnable,Handler.Callback{

	public final static Integer ERR_NONE = 0x100;
	public final static Integer ERR_REQUEST_FAILED = 901;
	public final static Integer ERR_JSON_PARSE = 902;
	public final static Integer ERR_STREAM_READ = 903;
	public final static Integer ERR_NO_NET= 904;
	protected String TAG=this.getClass().getSimpleName();

	private Integer errorCode;
	private String errorMessage;

	private BaseDAOListener mResultListener;
	private BaseRequest mRequest;
	private HttpResponse mResponse;
	private Thread mThread;
	private Handler mHandler;
	private JSONObject mJsonContent;
	private String jsonString;
	private String requestParams;
	private String className;
	public BaseDAO() {
		this.errorCode = BaseDAO.ERR_NONE;
		this.errorMessage = "";
		this.setHandler(new Handler(this));
		className=this.getClass().getSimpleName();
	}

	public String getJsonString() {
		return jsonString;
	}
	public void cancel() {
		if (mRequest != null) {
			mRequest.cancel();
		}
	}
	protected abstract String getActionName();
	public void request(NameValue ... parameters){
		Map<String,Object> params=new HashMap<String,Object>();
		for(NameValue nameValue:parameters){
			params.put(nameValue.getName(), nameValue.getValue());
		}
		loadData(params);
	}
	protected void loadData(Map<String, Object> parameters) {
		mThread = new Thread(this);
		mRequest = new BaseRequest(getActionName());
		mRequest.setParameters(parameters);
		requestParams=parameters.toString();
		Log.i("request api:("+className+")", mRequest.getApiName());
		Log.i("request params:("+className+")", requestParams);
		mThread.start();
	}

	protected void loadData(HashMap<String, Object> parameters, HashMap<String, File> fileBody) {
		mThread = new Thread(this);
		mRequest = new BaseRequest(getActionName());
		mRequest.setParameters(parameters);
		requestParams=parameters.toString();
		Log.i("request api:("+className+")", mRequest.getApiName());
		Log.i("request params:("+className+")", requestParams);
		mRequest.setFileBody(fileBody);
		mThread.start();
	}
	/**
	 * apiName为整个url,包括"http://"
	 */
	protected void loadDataByRaw(Map<String, Object> parameters) {
		mThread = new Thread(this);
		mRequest = new BaseRequest();
		mRequest.setRawUrl(getActionName());
		mRequest.setParameters(parameters);
		requestParams=parameters.toString();
		Log.i("request api:("+className+")", getActionName());
		Log.i("request params:("+className+")", requestParams);
		mThread.start();
	}
	protected JSONObject parseEntity(HttpEntity entity) {
		try {
			//			String chunk = entity.isChunked() ? "chunked" : "d==";
			//			String stream = entity.isStreaming() ? "stream" : "d==";
			//			Log.v("entity log", entity.getClass().toString() + chunk + stream);
//			if(entity.)
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			entity.writeTo(outputStream);
//			EntityUtils.toString(entity, "UTF-8");
//			jsonString = new String(outputStream.toByteArray());
			if(entity==null){
				setErrorCode(ERR_STREAM_READ);
				setErrorMessage("stream null error");
				return null;
			}
			jsonString=EntityUtils.toString(entity, "UTF-8");
			Log.i("response string:("+className+")", jsonString);
			JSONObject object = new JSONObject(jsonString);
			Log.i("response json:("+className+")", object.toString());
			return object;
		} catch (IOException e) {
			System.out.println("IOException:"+e.getMessage());
			e.printStackTrace();
			setErrorCode(ERR_STREAM_READ);
			setErrorMessage("stream read error");
			return null;
		} catch (JSONException e) {
			setErrorCode(ERR_JSON_PARSE);
			setErrorMessage("response is not json string");
			e.printStackTrace();
			return null;
		}catch(IllegalStateException e){
			setErrorCode(ERR_STREAM_READ);
			setErrorMessage("stream read error [content has bean consumed]");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 检查是否符合success+result+message结构
	 * @param object
	 * @return
	 */
	protected boolean hasError(JSONObject object) {
		String status = null;
		if (object == null) {
			return true;
		}
		if (object.has("success")) {
			status = object.optString("success");
			if(status.equals("true")){
				setErrorCode(ERR_NONE);
				return false;
			}else if (status.equals("false")) {
				//				JSONObject firstErr = object.optJSONArray("errors").optJSONObject(0);
				//				Integer errid = (Integer)firstErr.optInt("errid");
				//				String errMsg = firstErr.optString("errdesc");
				this.setErrorCode(BaseDAO.ERR_REQUEST_FAILED);
				this.setErrorMessage(object.optString("message"));
				return true;
			} else {
				return false;
			}
		}
		return false;

	}
	@Override
	public void run() {
		Message msg = new Message();
		mResponse = mRequest.execute();
		if (mResponse != null) {
			msg.what = mResponse.getStatusLine().getStatusCode();
			mJsonContent = parseEntity(mResponse.getEntity());
			hasError(mJsonContent);
		} else {
			msg.what = 0;//net unable
			this.setErrorCode(BaseDAO.ERR_NO_NET);
			this.setErrorMessage("请检查网络");
		}

		if (mHandler != null) {
			mHandler.sendMessage(msg);
		}
	}
	public void setResultListener(BaseDAOListener mResultListener) {
		this.mResultListener = mResultListener;
	}

	public BaseDAOListener getResultListener() {
		return mResultListener;
	}

	public Handler getHandler() {
		return mHandler;
	}

	protected void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer code) {
		this.errorCode = code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public BaseRequest getRequest() {
		return mRequest;
	}

	public HttpResponse getResponse() {
		return mResponse;
	}

	public Thread getThread() {
		return mThread;
	}
	public void loadDataFinish() {
		if (mResultListener != null) {
			mResultListener.onDataLoaded(this);
		}
	}
	public void loadDataFail() {
		if (mResultListener != null) {
			mResultListener.onDataFailed(this);
		}
	}
	public abstract void dealJsonResult(JSONObject result) throws Exception;
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case 0:
			loadDataFail();//网络问题
			break;
		case HttpStatus.SC_OK:
			if (getErrorCode() == BaseDAO.ERR_NONE) {
				try {
					dealJsonResult(this.mJsonContent);
				}  catch (Exception e) {
					e.printStackTrace();
					setErrorCode(ERR_JSON_PARSE);
					setErrorMessage("解析数据出错");
					loadDataFail();
					return false;
				}
				loadDataFinish();
			} else {
				try {
					dealJsonResult(this.mJsonContent);
				}  catch (Exception e) {
					Log.e(TAG, "解析false数据出错:"+e.getMessage());
				}
				loadDataFail();
			}
			break;
		default:
			this.setErrorCode(msg.what);
			this.setErrorMessage("HTTP ERROR CODE:"+msg.what);
			loadDataFail();
			break;
		}
		return true;

	}
}
