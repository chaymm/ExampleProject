package com.example.exampleproject.util;

import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

/**
 * 通知栏工具类
 * 
 * @author chang
 * 
 */
@SuppressLint("NewApi")
public class NotificationHelper {
	public final static int TYPE_MESSAGE = 0;//即时通讯类型
	public final static int TYPE_MEETING_MSG = 1;//会议信息类型
	private static final Random random = new Random(System.currentTimeMillis());
	private final static int NOTICATION_CODE_MESSAGE = 0X100;
	private final static Object syncObj = new Object();
	private static NotificationHelper instance;
	private Context mContext;
	private NotificationManager mNotificationManager;

	public NotificationHelper(Context context) {
		super();
		this.mContext = context;
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public static NotificationHelper getInstance(Context context)
	{
		if(null == instance)
		{
			synchronized(syncObj) {
				instance = new NotificationHelper(context);
			}
			return instance;
		}
		return instance;
	}
	
	public void showNofication(int notificationType,Map<String, Object> params,
			String content) {
		PendingIntent pendingIntent = null;
		int smallIcon = (Integer) params.get("small_icon");
		int icon = (Integer) params.get("icon");
		String tickerText = (String) params.get("tickerText");
		Intent intent = (Intent) params.get("intent");
		String title = (String) params.get("title");
		String subTitle = (String) params.get("subTitle");

		// 点击时的跳转
		intent.putExtra("content", content);
		
		switch (notificationType) {
		case TYPE_MESSAGE:
			pendingIntent = PendingIntent.getActivity(
					mContext.getApplicationContext(), NOTICATION_CODE_MESSAGE, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case TYPE_MEETING_MSG:
			pendingIntent = PendingIntent.getActivity(
					mContext.getApplicationContext(), random.nextInt(), intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		}
		
		//3.0后不使用
//		Notification notification = new Notification();
//		notification.icon = icon;
//		notification.tickerText = tickerText;
//		notification.defaults = Notification.DEFAULT_ALL;
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//		notification.setLatestEventInfo(context.getApplicationContext(), title,
//				subTitle, pendingIntent);
		
		//3.0后使用
		Notification.Builder builder = new Notification.Builder(mContext)
		.setContentTitle(title)
		.setContentText(subTitle)
		.setSmallIcon(smallIcon)
		.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
		.setTicker(tickerText)
		.setContentIntent(pendingIntent);
		//4.4.2
//		Notification notification = builder.build();
		//4.0.3
		Notification notification = builder.getNotification();
		notification.defaults = Notification.DEFAULT_ALL;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		switch (notificationType) {
		case TYPE_MESSAGE:
			mNotificationManager.notify(NOTICATION_CODE_MESSAGE, notification);
			break;
		case TYPE_MEETING_MSG:
			mNotificationManager.notify(random.nextInt(), notification);
			break;
		}
	}

}
