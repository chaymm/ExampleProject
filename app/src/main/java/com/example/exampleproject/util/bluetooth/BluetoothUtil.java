package com.example.exampleproject.util.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.reflect.Method;
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
     * 与设备配对
     * 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     *
     * @param btClass
     * @param btDevice
     * @return
     * @throws Exception
     */
    public boolean createBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * 与设备解除配对
     * 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     *
     * @param btClass
     * @param btDevice
     * @return
     * @throws Exception
     */
    public boolean removeBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * 设置密码
     *
     * @param btClass
     * @param btDevice
     * @param str
     * @return
     * @throws Exception
     */
    public boolean setPin(Class btClass, BluetoothDevice btDevice,
                          String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin",
                    new Class[]
                            {byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]
                            {str.getBytes()});
            Log.e("returnValue", "" + returnValue);
        } catch (SecurityException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    /**
     * 取消用户输入
     *
     * @param btClass
     * @param device
     * @return
     * @throws Exception
     */
    public boolean cancelPairingUserInput(Class btClass,
                                          BluetoothDevice device)

            throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    /**
     * 取消配对
     *
     * @param btClass
     * @param device
     * @return
     * @throws Exception
     */
    public boolean cancelBondProcess(Class btClass,
                                     BluetoothDevice device)

            throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    /**
     * 匹配设备
     * @param strAddress
     * @param strPsw
     * @return
     */
    public boolean pairBluetooth(String strAddress, String strPsw) {
        boolean result = false;

        if (!BluetoothAdapter.checkBluetoothAddress(strAddress)) { // 检查蓝牙地址是否有效
            Log.d("mylog", "devAdd un effient!");
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(strAddress);

        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
                Log.d("mylog", "NOT BOND_BONDED");
                setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                createBond(device.getClass(), device);
                result = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
            }
        } else {
            Log.d("mylog", "HAS BOND_BONDED");
            try {
                removeBond(device.getClass(), device);
                setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                createBond(device.getClass(), device);
                result = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 释放蓝牙
     */
    public void release() {
        instance = null;
    }

}
