package com.example.exampleproject.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

/**
 * https请求工具类
 * 
 * @author chang
 * 
 */
public class HttpsClientUtil {

	private final static String TAG = HttpsClientUtil.class
			.getSimpleName();
	/** 返回连接失败信息 **/
	private String strResult = "{\"code\":0,\"message\":\"服务器无法连接，请稍后再试\"}";
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

	private final static Object syncObj = new Object();
	private static HttpsClientUtil instance;

	public HttpsClientUtil(Context context) {
		mContext = context;
		getHttpClient();
	}

	public static HttpsClientUtil getInstance(Context context) {
		if (null == instance) {
			synchronized (syncObj) {
				instance = new HttpsClientUtil(context);
			}
			return instance;
		}
		return instance;
	}

	/**
	 * https Post webservice请求
	 * 
	 * @param url
	 *            请求地址
	 * @param headerParams
	 *            请求头参数
	 * @param params
	 *            请求参数
	 * @param soapRequestData
	 *            soap请求参数
	 * @return
	 */
	public String httpsPost(String url, HashMap<String, String> headerParams,
			HashMap<String, String> params, String soapRequestData) {
		try {
			HttpPost httpRequest = new HttpPost(url);
			if (headerParams != null) {
				for (String headerParam : headerParams.keySet()) {
					httpRequest.setHeader(headerParam,
							headerParams.get(headerParam));
				}
			}
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
			if (soapRequestData != null) {
				HttpEntity httpEntity = new StringEntity(soapRequestData,
						HTTP.UTF_8);
				httpRequest.setEntity(httpEntity);
			}

			/** 保持会话Session end **/
			HttpResponse response = httpClient.execute(httpRequest);
			HttpEntity entity = response.getEntity();//
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				String result = EntityUtils.toString(response.getEntity());
				return result;
			}
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
		return null;
	}

	/**
	 * 得到 apache http HttpClient对象(Https)
	 * 
	 * @return
	 */
	public DefaultHttpClient getHttpClient() {
		if (null == httpClient) {
			// 初始化工作
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许所有主机的验证

				HttpParams params = new BasicHttpParams();

				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params,
						HTTP.DEFAULT_CONTENT_CHARSET);
				HttpProtocolParams.setUseExpectContinue(params, true);

				// 设置连接管理器的超时
				ConnManagerParams.setTimeout(params, timeOut);
				// 设置连接超时
				HttpConnectionParams.setConnectionTimeout(params,
						connectionTimeOut);
				// 设置socket超时
				HttpConnectionParams.setSoTimeout(params, soTimeOut);

				// 设置http https支持
				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				schReg.register(new Scheme("https", sf, 443));

				ClientConnectionManager conManager = new ThreadSafeClientConnManager(
						params, schReg);

				httpClient = new DefaultHttpClient(conManager, params);
			} catch (Exception e) {
				e.printStackTrace();
				return new DefaultHttpClient();
			}
		}
		return httpClient;
	}

}

class SSLSocketFactoryEx extends SSLSocketFactory {

	SSLContext sslContext = SSLContext.getInstance("TLS");

	public SSLSocketFactoryEx(KeyStore truststore)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		super(truststore);

		TrustManager tm = new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {

			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {

			}
		};

		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}
}
