package com.example.exampleproject.util.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * 蓝牙监听器
 * Created by chang on 2017/6/19.
 */

public interface OnBluetoothListener {

    /**
     * 发现设备
     *
     * @param device 蓝牙设备
     */
    void onFound(BluetoothDevice device);

    /**
     * 访问控制列表连接
     *
     * @param device 蓝牙设备
     */
    void onACLConnected(BluetoothDevice device);

    /**
     * 访问控制列表断开连接
     *
     * @param device 蓝牙设备
     */
    void onACLDisconnected(BluetoothDevice device);

    /**
     * 正在配对
     *
     * @param device 蓝牙设备
     */
    void onBondBonding(BluetoothDevice device);

    /**
     * 完成配对
     *
     * @param device 蓝牙设备
     */
    void onBondBonded(BluetoothDevice device);

    /**
     * 取消配对
     *
     * @param device 蓝牙设备
     */
    void onBondNone(BluetoothDevice device);

    /**
     * 搜索完成
     */
    void onDiscoveryFinished();

    /**
     * 开始搜索
     */
    void onDiscoveryStarted();

    /**
     * 状态发生改变
     *
     * @param blueState 状态
     */
    void onStateChanged(int blueState);
}
