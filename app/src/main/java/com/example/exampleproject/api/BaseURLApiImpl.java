package com.example.exampleproject.api;

import com.example.exampleproject.api.net.HttpEngine;

/**
 * Created by chang on 2017/2/15.
 */

public class BaseURLApiImpl implements BaseURLApi {

    public HttpEngine httpEngine;

    public BaseURLApiImpl(){
        httpEngine = HttpEngine.getInstance();
    }

    @Override
    public void cancelTag(Object tag) {

    }

    @Override
    public void cancelAll() {

    }
}
