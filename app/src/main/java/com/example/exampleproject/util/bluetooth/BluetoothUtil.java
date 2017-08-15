package com.example.exampleproject.util.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import java.util.Set;

/**
 * 蓝牙工具类
 * Created by chang on 2017/6/19.
 */

public class BluetoothUtil {

    private static BluetoothUtil instance;
    private BluetoothReceiver mBluetoothReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private OnBluetoothListener mBluetoothListener;

    public BluetoothUtil() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothUtil getInstance() {
        if (instance == null) {
            synchronized (BluetoothUtil.class) {
                if (instance == null) {
                    instance = new BluetoothUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 注册蓝牙接收器
     *
     * @param context  上下文
     * @param listener 蓝牙监听器
     */
    public void registerReceiver(Context context, OnBluetoothListener listener) {
        this.mBluetoothListener = listener;
        if (mBluetoothReceiver == null)
            mBluetoothReceiver = new BluetoothReceiver();
        mBluetoothReceiver.setBluetoothListener(listener);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        context.registerReceiver(mBluetoothReceiver, filter);
    }

    /**
     * 取消注册蓝牙接收器
     *
     * @param context 上下文
     */
    public void unrRegisterReceiver(Context context) {
        if (mBluetoothReceiver != null) {
            context.unregisterReceiver(mBluetoothReceiver);
            mBluetoothReceiver = null;
        }
    }

    /**
     * 当前 Android 设备的 bluetooth 是否已经开启
     *
     * @return true：Bluetooth 已经开启 false：Bluetooth 未开启
     */
    public boolean isBluetoothEnabled() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        }
        return false;
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     *
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    public boolean turnOnBluetooth() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.enable();
        }
        return false;
    }

    /**
     * 强制关闭当前 Android 设备的 Bluetooth
     *
     * @return true：强制关闭 Bluetooth　成功　false：强制关闭 Bluetooth 失败
     */
    public boolean turnOffBluetooth() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.disable();
        }
        return false;
    }

    /**
     * 开始搜索蓝牙设备
     */
    public void startDiscovery() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.startDiscovery();
        }
    }

    /**
     * 取消搜索蓝牙设备
     */
    public void cancelDiscovery() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    /**
     * 获取已绑定设备列表
     *
     * @return
     */
    public Set<BluetoothDevice> getBondedDevices() {
        return mBluetoothAdapter != null ? mBluetoothAdapter.getBondedDevices() : null;
    }

    /**
     * 释放蓝牙
     */
    public void release(){
        instance = null;
    }

}
