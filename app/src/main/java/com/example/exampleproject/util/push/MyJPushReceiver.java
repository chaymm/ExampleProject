package com.example.exampleproject.util.push;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by chang on 2016/12/27.
 */
public class MyJPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(!isNotificationEnabled(context)){
            return;
        }

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            showMsg(context,bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void processCustomMessage(Context context, Bundle bundle){
        NotificationManager manger=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = 1000;
        Intent clickIntent = new Intent(); //点击通知之后要发送的广播
        clickIntent.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
        clickIntent.putExtra(JPushInterface.EXTRA_EXTRA,bundle.getString(JPushInterface.EXTRA_EXTRA));
        clickIntent.putExtra(JPushInterface.EXTRA_MESSAGE,bundle.getString(JPushInterface.EXTRA_MESSAGE));
        PendingIntent contentIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Ticker是状态栏显示的提示
        builder.setTicker(bundle.getString(JPushInterface.EXTRA_TITLE));
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle("您有一条新的信息");
        //第二行内容 通常是通知正文
        builder.setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
//        builder.setSmallIcon(R.drawable.small_notification_icon);
//        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.large_notification_icon));
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
//        notification.sound = Uri.parse("android.resource://"
//                + context.getPackageName() + "/" + R.raw.notification);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;

        manger.notify(id,notification);
    }

    public boolean isNotificationEnabled(Context context) {
//        return (Boolean) SPUtil.get(context, PreferencesConstant.MOA,PreferencesConstant.SETTINGS_NOTIFICATION_ENABLED,true);
        return false;
    }

    private void showMsg(Context context, String message){
        //判断app进程是否存活
        if(isAppAlive(context, "com.teamwings.amobile2.meetingsystem")){
            Log.i(TAG, "the app process is alive");
//            Intent mainIntent = new Intent(context, WebViewActivity.class);
//            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
//            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
//            //如果Task栈不存在MainActivity实例，则在栈顶创建
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//            Intent detailIntent = new Intent(context, MessageActivity.class);
//            detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            detailIntent.putExtra("messageContent", message);
//
//            Intent[] intents = {mainIntent, detailIntent};
//            context.startActivities(intents);
        }else {
            Log.i(TAG, "the app process is dead");
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.teamwings.amobile2.meetingsystem");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Bundle args = new Bundle();
            args.putString("messageContent", message);
            launchIntent.putExtras(args);
            context.startActivity(launchIntent);
        }
    }

    public boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }
}
