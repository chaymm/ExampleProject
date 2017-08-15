package com.example.exampleproject.base.adapter;

import android.os.Bundle;

/**
 * Created by chang on 2017/8/2.
 */

public class BasePagerInfo {
    private String title;
    private Class<?> clx;
    private Bundle args;

    public BasePagerInfo(String title, Class<?> clx, Bundle args) {
        this.title = title;
        this.clx = clx;
        this.args = args;
    }

    public BasePagerInfo(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getClx() {
        return clx;
    }

    public void setClx(Class<?> clx) {
        this.clx = clx;
    }

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

}
