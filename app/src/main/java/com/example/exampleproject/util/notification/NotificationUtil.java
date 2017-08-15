package com.example.exampleproject.util.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.exampleproject.R;


/**
 * 通知工具类
 * Created by chang on 2017/7/6.
 */

public class NotificationUtil {

    private static final String NOTIFICATION_TAG = "NotificationUtil";
    private Context mContext;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private static NotificationUtil instance;

    public NotificationUtil(Context context) {
        mContext = context;
        mBuilder = new NotificationCompat.Builder(context);
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NotificationUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (NotificationUtil.class) {
                if (instance == null) {
                    instance = new NotificationUtil(context);
                }
            }
        }
        return instance;
    }

    private void example(Context context) {
        NotificationEntity notificationEntity = new NotificationEntity();
        //多文本
        notificationEntity.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("提供较长的文本以模板的大形式代替内容文本显示。")
                .setBigContentTitle("覆盖contenttitle在模板的大形式")
                .setSummaryText("在模板的大表单后面的细节部分设置第一行文本"));
        //图片
        notificationEntity.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setBigContentTitle("")
                .setSummaryText("Dummy summary text"));
        //列表
        notificationEntity.setStyle(new NotificationCompat.InboxStyle()
                .addLine("将一行添加到收件箱通知的摘要部分")
                .addLine("")
                .setBigContentTitle("")
                .setSummaryText("Dummy summary text"));
    }

    /**
     * 创建通知
     *
     * @param requestCode        请求代码
     * @param notificationEntity 通知对象
     */
    public void notify(int requestCode,
                       NotificationEntity notificationEntity) {
        if (notificationEntity == null) {
            return;
        }

        mBuilder.setDefaults(notificationEntity.getDefaults())
                .setSmallIcon(notificationEntity.getSmallIcon())
                .setContentTitle(notificationEntity.getTitle())
                .setContentText(notificationEntity.getText())
                .setAutoCancel(notificationEntity.isAutoCancel())
                .setNumber(notificationEntity.getNumber())
                .setTicker(notificationEntity.getTicker());

        if (notificationEntity.getProgressEntity() != null) {
            mBuilder.setProgress(notificationEntity.getProgressEntity().getMax(),
                    notificationEntity.getProgressEntity().getProgress(),
                    notificationEntity.getProgressEntity().isInterminate());
        }

        if (notificationEntity.getLargeIcon() != null) {
            mBuilder.setLargeIcon(notificationEntity.getLargeIcon());
        }

        if (notificationEntity.getIntent() != null) {
            mBuilder.setContentIntent(
                    PendingIntent.getActivity(
                            mContext,
                            requestCode,
                            notificationEntity.getIntent(),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }

        if (notificationEntity.getStyle() != null) {
            mBuilder.setStyle(notificationEntity.getStyle());
        }

        if (notificationEntity.getViews() != null) {
            mBuilder.setContent(notificationEntity.getViews());
        }

        notify(requestCode, mBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void notify(int requestCode, final Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            mNotificationManager.notify(NOTIFICATION_TAG, requestCode, notification);
        } else {
            mNotificationManager.notify(requestCode, notification);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void cancel(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            mNotificationManager.cancel(NOTIFICATION_TAG, requestCode);
        } else {
            mNotificationManager.cancel(requestCode);
        }
    }


}

