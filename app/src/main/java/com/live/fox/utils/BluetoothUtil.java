package com.live.fox.utils;

import android.bluetooth.BluetoothAdapter;

/**
 * 蓝牙辅助类
 */
public class BluetoothUtil {

    /**
     * 判断蓝牙是否开启
     *
     * @return true 表示开启
     */
    public static final boolean isOPen() {
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        //支持蓝牙模块
        if (blueadapter != null) {
            if (blueadapter.isEnabled()) {
                return true;
            } else {
                return false;
            }
        } else {//不支持蓝牙模块
            ToastUtils.showShort("该设备不支持蓝牙或没有蓝牙模块");
        }
        return false;
    }

    /**
     * 强制帮用户打开蓝牙
     *
     */
    public static final boolean openBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();

        if (bluetoothAdapter != null) {
            return bluetoothAdapter.enable();
        }

        return false;
    }
}