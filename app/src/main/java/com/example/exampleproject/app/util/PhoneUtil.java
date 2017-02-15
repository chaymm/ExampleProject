package com.example.exampleproject.app.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by chang on 2016/6/24.
 */
public class PhoneUtil {

	public static String TAG = PhoneUtil.class.getSimpleName();

	/**
	 * 挂断电话
	 * 
	 * @param context
	 */
	public static void endCall(Context context) {
		try {
			Object telephonyObject = getTelephonyObject(context);
			if (null != telephonyObject) {
				Class telephonyClass = telephonyObject.getClass();

				Method endCallMethod = telephonyClass.getMethod("endCall");
				endCallMethod.setAccessible(true);

				endCallMethod.invoke(telephonyObject);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接听电话
	 * 
	 * @param context
	 */
	public static void answerRingingCall(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) { // 2.3或2.3以上系统
			answerRingingCallWithBroadcast(context);
		} else {
			answerRingingCallWithReflect(context);
		}
	}

	/**
	 * 静音
	 * 
	 * @param context
	 */
	public static void silenceRinger(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	/**
	 * 直接发短信
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static void sendSMS(String phoneNumber, String message) {
		// 获取短信管理器
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		// 拆分短信内容（手机短信长度限制）
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phoneNumber, null, text, null, null);
		}
	}

	private static Object getTelephonyObject(Context context) {
		Object telephonyObject = null;
		try {
			// 初始化iTelephony
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// Will be used to invoke hidden methods with reflection
			// Get the current object implementing ITelephony interface
			Class telManager = telephonyManager.getClass();
			Method getITelephony = telManager
					.getDeclaredMethod("getITelephony");
			getITelephony.setAccessible(true);
			telephonyObject = getITelephony.invoke(telephonyManager);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return telephonyObject;
	}

	/**
	 * 通过反射调用的方法，接听电话，该方法只在android 2.3之前的系统上有效。
	 * 
	 * @param context
	 */
	private static void answerRingingCallWithReflect(Context context) {
		try {
			Object telephonyObject = getTelephonyObject(context);
			if (null != telephonyObject) {
				Class telephonyClass = telephonyObject.getClass();
				Method endCallMethod = telephonyClass
						.getMethod("answerRingingCall");
				endCallMethod.setAccessible(true);

				endCallMethod.invoke(telephonyObject);
				// ITelephony iTelephony = (ITelephony) telephonyObject;
				// iTelephony.answerRingingCall();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 伪造一个有线耳机插入，并按接听键的广播，让系统开始接听电话。
	 * 
	 * @param context
	 */
	private static void answerRingingCallWithBroadcast(Context context) {
		// 按下耳机按钮
		Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
				KeyEvent.KEYCODE_HEADSETHOOK);
		localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
		context.sendOrderedBroadcast(localIntent2,
				"android.permission.CALL_PRIVILEGED");
		// 放开耳机按钮
		Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
				KeyEvent.KEYCODE_HEADSETHOOK);
		localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
		context.sendOrderedBroadcast(localIntent3,
				"android.permission.CALL_PRIVILEGED");
	}

	/**
	 * 通过输入获取电话号码
	 * 
	 * @param c
	 * @param name
	 * @return
	 */
	public static String getNumberByName(Context c, String name) {
		String ret = null;
		// 使用ContentResolver查找联系人数据
		Cursor cursor = c.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		// 遍历查询结果，找到所需号码
		while (cursor.moveToNext()) {
			// 获取联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获取联系人的名字
			String contactName = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			if (name.equals(contactName)) {
				// 使用ContentResolver查找联系人的电话号码
				Cursor phone = c.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				if (phone.moveToNext()) {
					ret = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					break;
				}

			}
		}
		return ret;
	}

	/**
	 * 根据电话号码取得联系人姓名
	 * 
	 * @param c
	 * @param number
	 * @return
	 */
	public static String getContactNameByPhoneNumber(Context c, String number) {
		String ret = null;
		Cursor cursor = c.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (cursor.moveToNext()) {
			// 获取联系人ID
			String contactId = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			// 获取联系人的名字
			String phoneNumber = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			if (number != null && number.equals(phoneNumber)) {
				// 使用ContentResolver查找联系人的电话号码
				Cursor phone = c.getContentResolver().query(
						ContactsContract.Contacts.CONTENT_URI, null,
						ContactsContract.Contacts._ID + " = " + contactId,
						null, null);
				if (phone.moveToNext()) {
					ret = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * 获取所有联系人内容
	 * 
	 * @param context
	 * @return
	 */
	public static String getContacts(Context context) {
		StringBuilder sb = new StringBuilder();

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		if (cursor.moveToFirst()) {
			do {
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 第一条不用换行
				if (sb.length() == 0) {
					sb.append(name);
				} else {
					sb.append("\n" + name);
				}

				Cursor phones = cr.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// 添加Phone的信息
					sb.append("\t").append(phoneNumber);
				}
				phones.close();

			} while (cursor.moveToNext());
		}
		cursor.close();
		return sb.toString();
	}

	/**
	 * 获取通话记录
	 * 
	 * @param context
	 * @return
	 */
	public static List<RecordEntity> getCallLogs(Context context) {
		List<RecordEntity> mRecordList = new ArrayList<RecordEntity>();
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null,
					null, null, CallLog.Calls.DATE + " desc");
			if (cursor == null)
				return null;
			while (cursor.moveToNext()) {
				RecordEntity record = new RecordEntity();
				record.setName(cursor.getString(cursor
						.getColumnIndex(CallLog.Calls.CACHED_NAME)));
				record.setNumber(cursor.getString(cursor
						.getColumnIndex(CallLog.Calls.NUMBER)));
				record.setType(cursor.getInt(cursor
						.getColumnIndex(CallLog.Calls.TYPE)));
				record.setlDate(cursor.getLong(cursor
						.getColumnIndex(CallLog.Calls.DATE)));
				record.setDuration(cursor.getLong(cursor
						.getColumnIndex(CallLog.Calls.DURATION)));
				record.set_new(cursor.getInt(cursor
						.getColumnIndex(CallLog.Calls.NEW)));
				Log.e(TAG, record.toString());
				// int photoIdIndex = cursor.getColumnIndex(CACHED_PHOTO_ID);
				// if (photoIdIndex >= 0) {
				// record.cachePhotoId = cursor.getLong(photoIdIndex);
				// }

				mRecordList.add(record);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return mRecordList;
	}

	/**
	 * 添加通讯录
	 * 
	 * @param context
	 * @param given_name
	 * @param mobile_number
	 * @param work_email
	 * @return
	 */
	public static boolean insertContacts(Context context, String given_name,
			String mobile_number, String work_email) {
		try {
			ContentValues values = new ContentValues();

			// 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
			Uri rawContactUri = context.getContentResolver().insert(
					ContactsContract.RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);

			// 向data表插入姓名数据
			if (given_name != "") {
				values.clear();
				values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
						rawContactId);
				values.put(
						ContactsContract.Contacts.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
				values.put(
						ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
						given_name);
				context.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
			}

			// 向data表插入电话数据
			if (mobile_number != "") {
				values.clear();
				values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
						rawContactId);
				values.put(
						ContactsContract.Contacts.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
				values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,
						mobile_number);
				values.put(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
				context.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
			}

			// 向data表插入Email数据
			if (work_email != "") {
				values.clear();
				values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
						rawContactId);
				values.put(
						ContactsContract.Contacts.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
				values.put(ContactsContract.CommonDataKinds.Email.DATA,
						work_email);
				values.put(ContactsContract.CommonDataKinds.Email.TYPE,
						ContactsContract.CommonDataKinds.Email.TYPE_WORK);
				context.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 打电话
	 * 
	 * @param context
	 * @param number
	 */
	public static void call(Context context, String number) {
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ number));
		context.startActivity(intent);
	}

	/**
	 * 获取所有短信信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<SmsInfo> getSmsInfo(Context context) {
		List<SmsInfo> list = new ArrayList<SmsInfo>();
		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[] { "_id", "address", "person",
				"date", "body", "type" };// "_id", "address", "person",, "date",
											// "type
		Cursor cur = cr.query(Uri.parse("content://sms/"), projection, null,
				null, "date desc");
		if (cur != null) {
			while (cur.moveToNext()) {
				int id = cur.getInt(cur.getColumnIndex("_id"));
				String phone = cur.getString(cur.getColumnIndex("address"));// 手机号
				String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
				String body = cur.getString(cur.getColumnIndex("body"));
				long date = cur.getLong(cur.getColumnIndex("date"));
				String d = DateUtils.formatDate(new Date(date),
						"yyyy-MM-dd HH:mm:ss");
				int type = cur.getInt(cur.getColumnIndex("type"));
				list.add(new SmsInfo(id, name, phone, body, d, type));
			}
		}
		return list;
	}

	/**
	 * 获取最新短信信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<SmsInfo> getLastedSmsInfo(Context context) {
		List<SmsInfo> list = new ArrayList<SmsInfo>();
		List<String> phoneList = new ArrayList<String>();
		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[] { "_id", "address", "person",
				"date", "body", "type" };// "_id", "address", "person",, "date",
											// "type
		Cursor cur = cr.query(Uri.parse("content://sms/"), projection, null,
				null, "date desc");
		if (cur != null) {
			while (cur.moveToNext()) {
				String phone = cur.getString(cur.getColumnIndex("address"));// 手机号
				if (!phoneList.contains(phone)) {
					int id = cur.getInt(cur.getColumnIndex("_id"));
					String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
					String body = cur.getString(cur.getColumnIndex("body"));
					long date = cur.getLong(cur.getColumnIndex("date"));
					String d = DateUtils.formatDate(new Date(date),
							"yyyy-MM-dd HH:mm:ss");
					int type = cur.getInt(cur.getColumnIndex("type"));
					list.add(new SmsInfo(id, name, phone, body, d, type));
					phoneList.add(phone);
				}
			}
		}
		return list;
	}

	/**
	 * 获取指定电话的短信信息
	 * 
	 * @param context
	 * @param phone
	 * @return
	 */
	public static List<SmsInfo> getSmsInfoByPhone(Context context, String phone) {
		List<SmsInfo> list = new ArrayList<SmsInfo>();
		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[] { "_id", "address", "person",
				"date", "body", "type" };// "_id", "address", "person",, "date",
											// "type
		Cursor cur = cr.query(Uri.parse("content://sms/"), projection,
				" address = ?", new String[] { phone }, "date");
		if (cur != null) {
			while (cur.moveToNext()) {
				int id = cur.getInt(cur.getColumnIndex("_id"));
				String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
				String body = cur.getString(cur.getColumnIndex("body"));
				long date = cur.getLong(cur.getColumnIndex("date"));
				String d = DateUtils.formatDate(new Date(date),
						"yyyy-MM-dd HH:mm");
				int type = cur.getInt(cur.getColumnIndex("type"));
				list.add(new SmsInfo(id, name, phone, body, d, type));
			}
		}
		return list;
	}

	/**
	 * 删除信息（4.0以下）
	 * 
	 * @param context
	 * @param phone
	 * @return
	 */
	public static boolean deleteSMS(Context context, String phone) {
		try {
			// // 准备系统短信收信箱的uri地址
			// Uri uri = Uri.parse("content://sms/inbox");// 收信箱
			// // 查询收信箱里所有的短信
			// Cursor isRead =
			// context.getContentResolver().query(uri, null, "read=" + 0,
			// null, null);
			// while (isRead.moveToNext()) {
			// String address =
			// isRead.getString(isRead.getColumnIndex("address"));
			// if (address.equals(phone)) {
			context.getContentResolver().delete(Uri.parse("content://sms"),
					"address=" + phone, null);
			return true;
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 免提
	 * 
	 * @param context
	 */
	public static void toggleSpeaker(Context context) {
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		am.setMode(AudioManager.MODE_IN_CALL);
		am.setSpeakerphoneOn(true);
		am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
				am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
				AudioManager.STREAM_VOICE_CALL);
	}

}

class RecordEntity implements Serializable {
	private static final long serialVersionUID = 1738633342031768755L;
	private String name;
	private String number;
	private int type;
	private long lDate;
	private long duration;
	private int _new;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getlDate() {
		return lDate;
	}

	public void setlDate(long lDate) {
		this.lDate = lDate;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int get_new() {
		return _new;
	}

	public void set_new(int _new) {
		this._new = _new;
	}

}

class SmsInfo implements Serializable {
	private static final long serialVersionUID = 8706642702531822704L;
	private int id;
	private String sender;
	private String phone;
	private String content;
	private String time;
	private int type;

	public SmsInfo(int id, String sender, String phone, String content,
					 String time, int type) {
		this.id = id;
		this.sender = sender;
		this.phone = phone;
		this.content = content;
		this.time = time;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}