package com.example.exampleproject.data.util.net;


import com.example.exampleproject.data.util.net.okhttp.OkHttpUtil;
import com.example.exampleproject.data.util.net.okhttp.callback.BitmapCallback;
import com.example.exampleproject.data.util.net.okhttp.callback.FileCallback;
import com.example.exampleproject.data.util.net.okhttp.callback.StringCallback;
import com.example.exampleproject.data.util.net.okhttp.request.GetRequest;
import com.example.exampleproject.data.util.net.okhttp.request.PostRequest;

import java.io.File;
import java.util.Map;

/**
 * http请求工具类
 *
 * @author chang
 */
public class HttpUtil {

    private final static String TAG = HttpUtil.class.getSimpleName();
    private static HttpUtil instance = null;
    /**
     * 返回连接失败信息
     **/
    private String mStrResult = "{\"error_code\":1000,\"error_msg\":\"服务器无法连接，请稍后再试\"}";

    public HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (null == instance) {
            instance = new HttpUtil();
        }
        return instance;
    }

    public void cancelTag(Object tag) {
        OkHttpUtil.getInstance().cancelTag(tag);
    }

    public void cancelAll() {
        OkHttpUtil.getInstance().cancelAll();
    }

    public void post(String url, Map<String, String> headers, Map<String, Object> params, StringCallback callback) {
        getPostRequest(url, headers, params).execute(callback);
    }

    public void post(String url, String data, StringCallback callback) {
        OkHttpUtil.post(url)
                .tag(this)
                .upString(data)
                .execute(callback);
    }

//    public Observable<String> post(String url, Map<String, String> headers, Map<String, Object> params) {
//        return getPostRequest(url, headers, params).getCall(StringConvert.create(), RxAdapter.<String>create());
//    }

    public void get(String url, Map<String, String> headers, Map<String, Object> params, StringCallback callback) {
        getGetRequest(url, headers, params).execute(callback);
    }

//    public Observable<String> get(String url, Map<String, String> headers, Map<String, Object> params) {
//        return getGetRequest(url, headers, params).getCall(StringConvert.create(), RxAdapter.<String>create());
//    }

    public void getBitmap(String url, Map<String, String> headers, Map<String, Object> params, BitmapCallback callback) {
        getGetRequest(url, headers, params).execute(callback);
    }

    public void getBitmap(String url, BitmapCallback callback) {
        OkHttpUtil.get(url)
                .tag(this)
                .execute(callback);
    }

//    public Observable<Bitmap> getBitmap(String url) {
//        return OkHttpUtil.get(url)
//                .tag(this)
//                .getCall(BitmapConvert.create(), RxAdapter.<Bitmap>create());
//    }

    public void downloadFileByGet(String url, Map<String, String> headers, Map<String, Object> params, FileCallback callback) {
        getGetRequest(url, headers, params).execute(callback);
    }

    public void downloadFileByPost(String url, Map<String, String> headers, Map<String, Object> params, FileCallback callback) {
        getPostRequest(url, headers, params).execute(callback);
    }

//    public Observable<File> downloadFileByPost(String url, Map<String, String> headers, Map<String, Object> params) {
//        return getPostRequest(url, headers, params).getCall(new FileConvert(), RxAdapter.<File>create());
//    }

    public void postByJson(String url, Map<String, String> headers, String json, StringCallback callback) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String param = headers.get(key);
                postRequest.headers(key, param);
            }
        }
        if (json != null) {
            postRequest.upJson(json);
        }
        postRequest.execute(callback);
    }

    private PostRequest getPostRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String param = headers.get(key);
                postRequest.headers(key, param);
            }
        }
        if (params != null) {
            for (String param : params.keySet()) {
                Object object = params.get(param);
                if (object instanceof String) {
                    postRequest.params((String) param, (String) object);
                } else if (object instanceof File) {
                    postRequest.params((String) param, (File) object);
                }
            }
        }
        return postRequest;
    }

    private GetRequest getGetRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        GetRequest getRequest = OkHttpUtil.get(url);
        getRequest.tag(this);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String param = headers.get(key);
                getRequest.headers(key, param);
            }
        }
        if (params != null) {
            for (String param : params.keySet()) {
                Object object = params.get(param);
                if (object instanceof String) {
                    getRequest.params((String) param, (String) object);
                }
            }
        }
        return getRequest;
    }

}
