package com.example.exampleproject.app.util;

import android.content.Context;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 访问WebService的工具类,
 * 
 */
public class WebServiceUtil {

	private Context mContext;
	private final static Object syncObj = new Object();
	private static WebServiceUtil instance;

	public WebServiceUtil(Context context) {
		mContext = context;
	}

	public static WebServiceUtil getInstance(Context context) {
		if (null == instance) {
			synchronized (syncObj) {
				instance = new WebServiceUtil(context);
			}
			return instance;
		}
		return instance;
	}

	/**
	 * 
	 * @param url
	 *            WebService服务器地址
	 * @param namespace
	 *            命名空间
	 * @param methodName
	 *            WebService的调用方法名
	 * @param properties
	 *            WebService的参数
	 */
	public Object callWebService(String url, String namespace,
			final String methodName, HashMap<String, String> properties) {
		// 创建HttpTransportSE对象，传递WebService服务器地址
		final HttpTransportSE httpTransportSE = new HttpTransportSE(url);
		// 创建SoapObject对象
		SoapObject soapObject = new SoapObject(namespace, methodName);

		// SoapObject添加参数
		if (properties != null) {
			for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				soapObject.addProperty(entry.getKey(), entry.getValue());
			}
		}

		// 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
		final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.setOutputSoapObject(soapObject);
		soapEnvelope.bodyOut = soapObject;
		// 设置是否调用的是dotNet开发的WebService
		soapEnvelope.dotNet = true;
		try {
			httpTransportSE.call(namespace + "/" + methodName, soapEnvelope);
			if (soapEnvelope.getResponse() != null) {
				// 获取服务器响应返回的SoapObject
				return soapEnvelope.bodyIn;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

}
