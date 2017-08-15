package com.example.exampleproject.util.notification;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * 通知实体类
 * Created by chang on 2017/7/6.
 */

public class NotificationEntity {

    //通知默认模式
    private int defaults = Notification.DEFAULT_ALL;
    //标题(required)
    private String title;
    //消息内容(required)
    private String text;
    //通知的状态栏提示文本(required)
    private String ticker;
    //小图标(required)
    private int smallIcon;
    //大图标
    private Bitmap largeIcon;
    //用于叠加式通知单，通知栏个数
    private int number = 1;
    //点击事件行为
    private Intent intent;
    //通知样式
    private NotificationCompat.Style style;
    //自定义视图（可选）
    private RemoteViews views;
    //设置是否自动取消
    private boolean autoCancel = true;
    //进度
    private ProgressEntity progressEntity;

    public int getDefaults() {
        return defaults;
    }

    public void setDefaults(int defaults) {
        this.defaults = defaults;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(Bitmap largeIcon) {
        this.largeIcon = largeIcon;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public NotificationCompat.Style getStyle() {
        return style;
    }

    public void setStyle(NotificationCompat.Style style) {
        this.style = style;
    }

    public RemoteViews getViews() {
        return views;
    }

    public void setViews(RemoteViews views) {
        this.views = views;
    }

    public boolean isAutoCancel() {
        return autoCancel;
    }

    public void setAutoCancel(boolean autoCancel) {
        this.autoCancel = autoCancel;
    }

    public ProgressEntity getProgressEntity() {
        return progressEntity;
    }

    public void setProgressEntity(ProgressEntity progressEntity) {
        this.progressEntity = progressEntity;
    }
}
