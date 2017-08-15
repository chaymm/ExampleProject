package com.example.exampleproject.util.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 蓝牙接收器
 * Created by chang on 2017/4/21.
 */

public class BluetoothReceiver extends BroadcastReceiver {

    private final static String TAG = BluetoothReceiver.class.getSimpleName();
    /**
     * 蓝牙监听器
     */
    private OnBluetoothListener mListener;

    public BluetoothReceiver() {
    }

    public void setBluetoothListener(OnBluetoothListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent
                .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.d(TAG, action);
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Log.d(TAG, "ACTION_FOUND");
            if (mListener != null)
                mListener.onFound(device);
        } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            Log.d(TAG, "ACTION_ACL_CONNECTED");
            if (mListener != null)
                mListener.onACLConnected(device);
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            Log.d(TAG, "ACTION_ACL_DISCONNECTED");
            if (mListener != null)
                mListener.onACLDisconnected(device);
        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            String str = device.getName() + "\n" + device.getAddress();
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING:
                    Log.d(TAG, "正在配对......");
                    if (mListener != null)
                        mListener.onBondBonding(device);
                    break;
                case BluetoothDevice.BOND_BONDED:
                    Log.d(TAG, "完成配对");
                    if (mListener != null)
                        mListener.onBondBonded(device);
                    break;
                case BluetoothDevice.BOND_NONE:
                    Log.d(TAG, "取消配对：" + device.getAddress());
                    if (mListener != null)
                        mListener.onBondNone(device);
                    break;
            }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Log.d(TAG, "搜索完成");
            if (mListener != null)
                mListener.onDiscoveryFinished();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Log.d(TAG, "正在搜索...");
            if (mListener != null)
                mListener.onDiscoveryStarted();
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            Log.d(TAG, "ACTION_STATE_CHANGED:"+blueState);
            if (mListener != null)
                mListener.onStateChanged(blueState);
        }
    }
}
