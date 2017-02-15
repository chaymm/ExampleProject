package com.example.exampleproject.api.net.okhttp.okserver.download.db;


import com.example.exampleproject.api.net.okhttp.cache.CacheMode;
import com.example.exampleproject.api.net.okhttp.model.HttpHeaders;
import com.example.exampleproject.api.net.okhttp.model.HttpParams;
import com.example.exampleproject.api.net.okhttp.request.BaseRequest;
import com.example.exampleproject.api.net.okhttp.request.DeleteRequest;
import com.example.exampleproject.api.net.okhttp.request.GetRequest;
import com.example.exampleproject.api.net.okhttp.request.HeadRequest;
import com.example.exampleproject.api.net.okhttp.request.OptionsRequest;
import com.example.exampleproject.api.net.okhttp.request.PostRequest;
import com.example.exampleproject.api.net.okhttp.request.PutRequest;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/8/8
 * 描    述：与BaseRequest对应,主要是为了序列化
 * 修订历史：
 * ================================================
 */
public class DownloadRequest implements Serializable {

    private static final long serialVersionUID = -6883956320373276785L;

    public String method;
    public String url;
    public CacheMode cacheMode;
    public String cacheKey;
    public long cacheTime;
    public HttpParams params;
    public HttpHeaders headers;

    public static String getMethod(BaseRequest request) {
        if (request instanceof GetRequest) return "get";
        if (request instanceof PostRequest) return "post";
        if (request instanceof PutRequest) return "put";
        if (request instanceof DeleteRequest) return "delete";
        if (request instanceof OptionsRequest) return "options";
        if (request instanceof HeadRequest) return "head";
        return "";
    }

    public static BaseRequest createRequest(String url, String method) {
        if (method.equals("get")) return new GetRequest(url);
        if (method.equals("post")) return new PostRequest(url);
        if (method.equals("put")) return new PutRequest(url);
        if (method.equals("delete")) return new DeleteRequest(url);
        if (method.equals("options")) return new OptionsRequest(url);
        if (method.equals("head")) return new HeadRequest(url);
        return null;
    }
}
