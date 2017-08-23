package com.example.exampleproject.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * UDP客户端工具类
 * Created by chang on 2017/5/24.
 */

public class UDPClientUtil {

    public interface OnUDPListener{
        void onReceive(byte[] data);
        void onError();
    }

    private static String TAG = UDPClientUtil.class.getSimpleName();
    private DatagramSocket mSocket;
    private InetAddress mInetAddress;
    private int mPort;
    private OnUDPListener mListener;

    public UDPClientUtil(String host, int port) {
        mPort = port;
        try {
            mInetAddress = InetAddress.getByName(host);
            mSocket = new DatagramSocket();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            error();
        } catch (SocketException e) {
            e.printStackTrace();
            error();
        }
    }

    public UDPClientUtil(int port) {
        try {
            mSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            error();
        }
    }

    public void setListener(OnUDPListener listener){
        mListener = listener;
    }

    /**
     * 发送数据
     *
     * @param buffer
     */
    public boolean send(byte[] buffer) {
        if (isClosing())
            return false;
        DatagramPacket dataGramPacket = new DatagramPacket(buffer, buffer.length, mInetAddress, mPort);
        try {
            mSocket.send(dataGramPacket);
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            error();
        } catch (IOException e) {
            e.printStackTrace();
            error();
        }
        return false;
    }

    /**
     * 接收数据
     */
    public void receive() {
        if (isClosing()) {
            error();
            return;
        }
        while(!isClosing()){
            if(mSocket == null){
                return;
            }
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket backPacket = new DatagramPacket(buffer, buffer.length);
                mSocket.receive(backPacket);
                byte[] data = Arrays.copyOf(backPacket.getData(), backPacket.getLength());
                if(mListener != null && data != null)
                    mListener.onReceive(data);
            } catch (IOException e) {
                e.printStackTrace();
                error();
                close();
            }
        }
    }

    /**
     * 关闭socket连接
     */
    public void close() {
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
    }

    /**
     * 获取连接状态
     * @return
     */
    private boolean isClosing() {
        return mSocket != null ? mSocket.isClosed() : false;
    }

    /**
     * 错误回调
     */
    private void error(){
        if(mListener != null)
            mListener.onError();
    }

}
