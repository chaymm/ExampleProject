package com.example.exampleproject.util.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 蓝牙Socket客户端工具类
 * Created by chang on 2017/6/19.
 */

public class BluetoothClientUtil {

    private final static String TAG = BluetoothClientUtil.class.getSimpleName();
    //默认UUID
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    /**
     * 根据UUID获取BluetoothSocket对象
     *
     * @param device 蓝牙设备对象
     * @return
     */
    private BluetoothSocket getSocketByUUID(BluetoothDevice device) {
        try {
            Log.i(TAG, "getSocketByUUID");
            return device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据反射获取BluetoothSocket对象
     *
     * @param device 蓝牙设备对象
     * @return
     */
    private BluetoothSocket getSocketByReflect(BluetoothDevice device) {
        try {
            Log.i(TAG, "getSocketByReflect");
            Method m = device.getClass().getMethod("createRfcommSocket",
                    new Class[]{int.class});
            return (BluetoothSocket) m.invoke(device, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 连接蓝牙设备
     *
     * @param device 蓝牙设备对象
     * @throws IOException
     */
    public boolean connect(BluetoothDevice device) throws IOException {
        mSocket = getSocketByUUID(device);
        if (null == mSocket)
            mSocket = getSocketByReflect(device);

        if (mSocket == null) {
            return false;
        }
        try {
            mSocket.connect();
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IOException();
    }

    /**
     * 判断BluetoothSocket是否关闭
     *
     * @return
     */
    public boolean isClosed() {
        return (null == mSocket);
    }

    /**
     * 关闭BluetoothSocket
     */
    public void close() {
        if (mSocket != null) {
            try {
                if (mInputStream != null) {
                    mInputStream.close();
                    mInputStream = null;
                }
                if (mOutputStream != null){
                    mOutputStream.close();
                    mOutputStream = null;
                }
                mSocket.close();
                mSocket = null;
                Log.i("mBluetoothClientUtil","close======");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送蓝牙数据
     *
     * @param data 蓝牙数据
     * @throws IOException
     */
    public void send(byte[] data) throws IOException {
        send(data, 0, data.length);
    }

    /**
     * 发送蓝牙数据
     *
     * @param data   蓝牙数据
     * @param offset 数据偏移值
     * @param length 数据长度
     * @throws IOException
     */
    public void send(byte[] data, int offset, int length) throws IOException {
        if (null == mSocket)
            return;
        if (mOutputStream != null) {
            try {
                mOutputStream.write(data, offset, length);
                mOutputStream.flush();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 接收蓝牙数据
     *
     * @param buffer 缓冲区数据
     * @param offset 偏移值
     * @param length 缓冲区数据长度
     * @return
     * @throws IOException
     */
    public int receive(byte[] buffer, int offset, int length)
            throws IOException {
        Log.i("mBluetoothClientUtil","receive=====");
        if (mSocket != null && mInputStream != null) {
            try {
                return mInputStream.read(buffer, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("","receive error--------");
            }
        }
        throw new IOException();
    }
}
