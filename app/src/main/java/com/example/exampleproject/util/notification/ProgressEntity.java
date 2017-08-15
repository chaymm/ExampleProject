package com.example.exampleproject.util.notification;

/**
 * 进度实体类
 * Created by chang on 2017/7/10.
 */

public class ProgressEntity {
    //极大值
    private int max;
    //进度
    private int progress;
    //是否具体显示
    private boolean interminate;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isInterminate() {
        return interminate;
    }

    public void setInterminate(boolean interminate) {
        this.interminate = interminate;
    }
}
