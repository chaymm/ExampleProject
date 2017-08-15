package com.example.exampleproject.data.remote;


import com.example.exampleproject.data.util.net.HttpUtil;

/**
 * Created by chang on 2017/3/3.
 */

public class BaseRemoteDataSource {

    protected HttpUtil mHttpUtil;

    public BaseRemoteDataSource(){
        mHttpUtil = HttpUtil.getInstance();
    }

    public void cancelTag(Object tag) {
        mHttpUtil.cancelTag(tag);
    }

    public void cancelAll() {
        mHttpUtil.cancelAll();
    }
}
