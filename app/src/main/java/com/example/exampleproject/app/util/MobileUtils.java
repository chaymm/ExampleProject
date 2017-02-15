package com.example.exampleproject.app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * 手机工具类
 * 
 * @author chang
 * 
 */
public class MobileUtils {

	/**
	 * 获取设备厂商
	 * 
	 * @return
	 */
	public static String getManufacturer(){
		return Build.MANUFACTURER;
	}
	
	/**
	 * 获取设备型号
	 * 
	 * @return
	 */
	public static String getModel() {
		return Build.MODEL;
	}

	/**
	 * 获取设备版本号
	 * 
	 * @return
	 */
	public static String getVersionRelease() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取设备SDK版本号
	 * 
	 * @return
	 */
	public static int getVersionSDK() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 获取手机机器串号
	 */
	public static String getIMEI(Context ctx) {
		String retVal = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			retVal = telephonyManager.getDeviceId();
			if (retVal == null)
				retVal = "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 获取手机SIM卡的序列号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getSIM(Context ctx) {
		String retVal = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			retVal = telephonyManager.getSimSerialNumber();
			if (retVal == null)
				retVal = "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 获取设备唯一标识
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		// 1 compute IMEI(仅仅只对Android手机有效)
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String m_szImei = TelephonyMgr.getDeviceId(); // Requires
														// READ_PHONE_STATE

		// 2 compute DEVICE ID(这个在任何Android手机中都有效,ID不是唯一的)
		String m_szDevIDShort = "35"
				+ // we make this look like a valid IMEI
				Build.BOARD.length() % 10 + Build.BRAND.length() % 10
				+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
				+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
				+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
				+ Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
				+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10
				+ Build.USER.length() % 10; // 13 digits
		// 3 android ID - unreliable(通常被认为不可信，因为它有时为null)
		String m_szAndroidID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		// 4 wifi manager, read MAC address - requires
		// android.permission.ACCESS_WIFI_STATE or comes as null
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

		// 5 Bluetooth MAC address android.permission.BLUETOOTH required
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		String m_szBTMAC = "";
		try {
			m_szBTMAC = m_BluetoothAdapter.getAddress();
		} catch (NullPointerException e) {
		}

		// 6 SUM THE IDs
		String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID
				+ m_szWLANMAC + m_szBTMAC;
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
		byte p_md5Data[] = m.digest();

		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {
			int b = (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper
			// padding)
			if (b <= 0xF)
				m_szUniqueID += "0";
			// add number to string
			m_szUniqueID += Integer.toHexString(b);
		}
		m_szUniqueID = m_szUniqueID.toUpperCase();
		return m_szUniqueID;
	}

}
