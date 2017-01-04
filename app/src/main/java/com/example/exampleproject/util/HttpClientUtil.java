package com.example.exampleproject.util;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.example.exampleproject.app.MyApplication;
import com.example.exampleproject.listener.OnFileUploadListener;
import com.example.exampleproject.util.CustomMultipartEntity.ProgressListener;
import com.loopj.android.http.PersistentCookieStore;

/**
 * http请求工具类
 * 
 * @author chang
 * 
 */
public class HttpClientUtil {

	private final static String TAG = HttpClientUtil.class.getSimpleName();
	/** http 请求头参数 **/
	private HttpParams httpParams;
	/** httpClient 对象 **/
	private static DefaultHttpClient httpClient = null;
	/** 得到上下文 **/
	private Context mContext;
	/** 连接连接池的管理器超时参数 **/
	private int timeOut = 10 * 1000;
	/** 连接超时参数 **/
	private int connectionTimeOut = 10 * 1000;
	/** Socket超时参数 **/
	private int soTimeOut = 10 * 1000;
	/** Socket缓存大小参数 **/
	private int socketBufferSize = 8192;
	/** 上传文件大小 */
	private long totalSize;

	private MyApplication li = null;
	private final static Object syncObj = new Object();
	private static HttpClientUtil instance;
	private OnFileUploadListener mFileUploadListener;
	/** 返回连接失败信息 **/
	private String mStrResult = "{\"error_code\":1000,\"error_msg\":\"服务器无法连接，请稍后再试\"}";

	public HttpClientUtil(Context context) {
		mContext = context;
		getHttpClient();
	}

	public static HttpClientUtil getInstance(Context context) {
		if (null == instance) {
			synchronized (syncObj) {
				instance = new HttpClientUtil(context);
			}
			return instance;
		}
		return instance;
	}

	/**
	 * 设置文件上传监听器
	 * 
	 * @param listener
	 */
	public void setOnFileUploadListener(OnFileUploadListener listener) {
		mFileUploadListener = listener;
	}

	public String httpPost(String url, HashMap<String, String> params) {
		try {
			HttpPost httpRequest = new HttpPost(url);
			if (params != null) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						params.size());
				for (String param : params.keySet()) {
					nameValuePairs.add(new BasicNameValuePair(param, params
							.get(param)));
				}
				httpRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"UTF-8"));
			}
			/** 保持会话Session **/
			/** 设置Cookie **/
			PersistentCookieStore cookieStore = li.getPersistentCookieStore();
			httpClient.setCookieStore(cookieStore);

			/** 保持会话Session end **/
			HttpResponse response = httpClient.execute(httpRequest);
			HttpEntity entity = response.getEntity();//
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				PersistentCookieStore myCookieStore = li
						.getPersistentCookieStore();
				List<Cookie> cookies = httpClient.getCookieStore().getCookies();
				for (Cookie cookie : cookies) {
					myCookieStore.addCookie(cookie);
				}
				return EntityUtils.toString(response.getEntity());
			}
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mStrResult;
	}

	/** 得到 apache http HttpClient对象 **/
	public DefaultHttpClient getHttpClient() {

		if (httpClient == null) {
			/** 创建 HttpParams 以用来设置 HTTP 参数 **/
			httpParams = new BasicHttpParams();

			/** 设置连接连接池的管理器超时、连接超时和 Socket 超时，以及 Socket 缓存大小 **/
			ConnManagerParams.setTimeout(httpParams, timeOut);
			HttpConnectionParams.setConnectionTimeout(httpParams,
					connectionTimeOut);
			HttpConnectionParams.setSoTimeout(httpParams, soTimeOut);
			HttpConnectionParams.setSocketBufferSize(httpParams,
					socketBufferSize);
			HttpClientParams.setRedirecting(httpParams, true);

			// /**
			// * 创建一个 HttpClient 实例 //增加自动选择网络，自适应cmwap、CMNET、wifi或3G
			// */
			// li = (MyApplication) mContext.getApplicationContext();
			// String proxyStr = li.getHttpProxyStr();
			// if (proxyStr != null && proxyStr.trim().length() > 0) {
			// HttpHost proxy = new HttpHost(proxyStr, 80);
			// httpClient.getParams().setParameter(
			// ConnRouteParams.DEFAULT_PROXY, proxy);
			// }
			/** 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient **/
			httpClient = new DefaultHttpClient(httpParams);
			// httpClient.setHttpRequestRetryHandler(requestRetryHandler);
		}
		return httpClient;
	}

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试N次
			if (executionCount >= 3) {
				// 如果超过最大重试次数，那么就不要继续了
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// 如果服务器丢掉了连接，那么就重试
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// 不要重试SSL握手异常
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// 如果请求被认为是幂等的，那么就重试
				return true;
			}
			return false;
		}
	};

	/**
	 * 保存session
	 * 
	 * @param bcookieStore
	 */
	public void saveSession(CookieStore bcookieStore) {
		List<Cookie> cookies = bcookieStore.getCookies();
		for (Cookie cookie : cookies) {
			String sessionId = cookie.getValue();
			li.setSessionId(sessionId);
		}
	}

	/**
	 * 上传文件(需要httpmime.jar)
	 * 
	 * @param url
	 * @param params
	 * @param obj
	 * @return
	 */
	public String uploadFile(String url, HashMap<String, Object> params) {
		try {
			HttpPost httppost = new HttpPost(url);
			CustomMultipartEntity mpEntity = new CustomMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							int progress = (int) ((num / (float) totalSize) * 100);
							if (mFileUploadListener != null) {
								mFileUploadListener.onProgress(progress);
							}
						}
					});
			if (params != null) {
				for (String param : params.keySet()) {
					Object object = params.get(param);
					if (object instanceof String) {
						StringBody stringBody = new StringBody(
								(String) params.get(param));
						mpEntity.addPart((String) param, stringBody);
					} else if (object instanceof File) {
						ContentBody cbFile = new FileBody(
								(File) params.get(param));// 文件传输
						mpEntity.addPart((String) param, cbFile); // <input
																	// type="file"
																	// name="userfile"
																	// /> 对应的
					}
				}
			}

			totalSize = mpEntity.getContentLength();
			httppost.setEntity(mpEntity);

			/** 保持会话Session **/
			/** 设置Cookie **/
			PersistentCookieStore cookieStore = li.getPersistentCookieStore();
			httpClient.setCookieStore(cookieStore);

			HttpResponse response = httpClient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				PersistentCookieStore myCookieStore = li
						.getPersistentCookieStore();
				List<Cookie> cookies = httpClient.getCookieStore().getCookies();
				for (Cookie cookie : cookies) {
					myCookieStore.addCookie(cookie);
				}
				return EntityUtils.toString(response.getEntity());
			}
			if (resEntity != null) {
				resEntity.consumeContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mStrResult;
	}

}
