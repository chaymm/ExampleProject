package com.example.exampleproject.api.net;

import com.example.exampleproject.api.net.okhttp.OkHttpUtil;
import com.example.exampleproject.api.net.okhttp.callback.BitmapCallback;
import com.example.exampleproject.api.net.okhttp.callback.FileCallback;
import com.example.exampleproject.api.net.okhttp.callback.StringCallback;
import com.example.exampleproject.api.net.okhttp.request.PostRequest;

import java.io.File;
import java.util.Map;

/**
 * http请求工具类
 *
 * @author chang
 */
public class HttpEngine {

    private final static String TAG = HttpEngine.class.getSimpleName();
    private static HttpEngine instance = null;
    /**
     * 返回连接失败信息
     **/
    private String mStrResult = "{\"error_code\":1000,\"error_msg\":\"服务器无法连接，请稍后再试\"}";

    public HttpEngine() {
    }

    public static HttpEngine getInstance() {
        if (null == instance) {
            instance = new HttpEngine();
        }
        return instance;
    }

    public void cancelTag(Object tag){
        OkHttpUtil.getInstance().cancelTag(tag);
    }

    public void cancelAll(){
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

    public void  post(String url, String data, StringCallback callback) {
        OkHttpUtil.post(url)
                .tag(this)
                .upString(data)
                .execute(callback);
    }

    public void getBitmap(String url, BitmapCallback callback) {
        OkHttpUtil.get(url)
                .tag(this)
                .execute(callback);
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

}
