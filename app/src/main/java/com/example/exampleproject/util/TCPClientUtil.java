package com.example.exampleproject.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * TCP客户端工具类
 * Created by chang on 2017/5/25.
 */

public class TCPClientUtil {

    private static TCPClientUtil utils = null;
    /**
     * 超时时间
     */
    private final static int SOCKET_TIME_OUT = 30000;
    /**
     * 数据输出流
     */
    private DataOutputStream mOutputStream = null;
    /**
     * 数据输入流
     */
    private DataInputStream mInputStream = null;
    private Socket mSocket = null;

    public static TCPClientUtil getInstance() {
        if (utils == null) {
            synchronized (TCPClientUtil.class) {
                if (utils == null) {
                    utils = new TCPClientUtil();
                }
            }
        }
        return utils;
    }

    /**
     * 创建连接
     *
     * @param host
     * @param port
     */
    public boolean connect(String host, int port) {
        if (mSocket != null)
            return false;
        try {
            mSocket = new Socket(host, port);
            mSocket.setSoTimeout(SOCKET_TIME_OUT);
            mInputStream = new DataInputStream(mSocket.getInputStream());
            mOutputStream = new DataOutputStream(mSocket.getOutputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭连接.
     */
    public void close() {
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e1) {
            }
            mInputStream = null;
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e1) {
            }
            mOutputStream = null;
        }
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e1) {
            }
            mSocket = null;
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public DataOutputStream getDataOutputStream() {
        return mOutputStream;
    }

    public DataInputStream getDataInputStream() {
        return mInputStream;
    }

    public boolean isClosed() {
        return mSocket == null ? true : mSocket.isClosed();
    }
}
