package com.example.exampleproject.data.util.net;

import android.graphics.Bitmap;

import com.example.exampleproject.data.util.net.okhttp.OkHttpUtil;
import com.example.exampleproject.data.util.net.okhttp.callback.BitmapCallback;
import com.example.exampleproject.data.util.net.okhttp.callback.FileCallback;
import com.example.exampleproject.data.util.net.okhttp.callback.StringCallback;
import com.example.exampleproject.data.util.net.okhttp.convert.BitmapConvert;
import com.example.exampleproject.data.util.net.okhttp.convert.FileConvert;
import com.example.exampleproject.data.util.net.okhttp.convert.StringConvert;
import com.example.exampleproject.data.util.net.okhttp.okrx.RxAdapter;
import com.example.exampleproject.data.util.net.okhttp.request.GetRequest;
import com.example.exampleproject.data.util.net.okhttp.request.PostRequest;

import java.io.File;
import java.util.Map;

import rx.Observable;

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

    public void post(String url, Map<String, String> params, StringCallback callback) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
        if (params != null) {
            for (String key : params.keySet()) {
                String param = params.get(key);
                postRequest.params(key, param);
            }
        }
        postRequest.execute(callback);
    }

    public void post(String url, String data, StringCallback callback) {
        OkHttpUtil.post(url)
                .tag(this)
                .upString(data)
                .execute(callback);
    }

    public Observable<String> post(String url, Map<String, String> params) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
        if (params != null) {
            for (String key : params.keySet()) {
                String param = params.get(key);
                postRequest.params(key, param);
            }
        }
        return postRequest.getCall(StringConvert.create(), RxAdapter.<String>create());
    }

    public void get(String url, Map<String, String> params, StringCallback callback) {
        GetRequest getRequest = OkHttpUtil.get(url);
        getRequest.tag(this);
        if (params != null) {
            for (String key : params.keySet()) {
                String param = params.get(key);
                getRequest.params(key, param);
            }
        }
        getRequest.execute(callback);
    }

    public Observable<String> get(String url, Map<String, String> params) {
        GetRequest getRequest = OkHttpUtil.get(url);
        getRequest.tag(this);
        if (params != null) {
            for (String key : params.keySet()) {
                String param = params.get(key);
                getRequest.params(key, param);
            }
        }
        return getRequest.getCall(StringConvert.create(), RxAdapter.<String>create());
    }

    public void getBitmap(String url,Map<String, String> params, BitmapCallback callback) {
        GetRequest getRequest = OkHttpUtil.get(url);
        getRequest.tag(this);
        if (params != null) {
            for (String key : params.keySet()) {
                String param = params.get(key);
                getRequest.params(key, param);
            }
        }
        getRequest.execute(callback);
    }

    public void getBitmap(String url, BitmapCallback callback) {
        OkHttpUtil.get(url)
                .tag(this)
                .execute(callback);
    }

    public Observable<Bitmap> getBitmap(String url) {
        return OkHttpUtil.get(url)
                .tag(this)
                .getCall(BitmapConvert.create(), RxAdapter.<Bitmap>create());
    }

    public void downloadFile(String url, Map<String, String> params, FileCallback callback) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
        if (params != null) {
            for (String key : params.keySet()) {
                String param = params.get(key);
                postRequest.params(key, param);
            }
        }
        postRequest.execute(callback);
    }

    public Observable<File> downloadFile(String url, Map<String, String> params) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
        if (params != null) {
            for (String key : params.keySet()) {
                String param = params.get(key);
                postRequest.params(key, param);
            }
        }
        return postRequest.getCall(new FileConvert(), RxAdapter.<File>create());
    }

    /**
     * 上传文件
     */
    public void uploadFile(String url, Map<String, Object> params, StringCallback callback) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
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
        postRequest.execute(callback);
    }

    public Observable<String> uploadFile(String url, Map<String, Object> params) {
        PostRequest postRequest = OkHttpUtil.post(url);
        postRequest.tag(this);
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
        return postRequest.getCall(StringConvert.create(), RxAdapter.<String>create());
    }


}
