package com.example.steplifeapp.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class BluetoothController {
    BluetoothAdapter adapter;
    BluetoothDevice device;
    ConnectThread connectThread = null;
    BluetoothController(BluetoothAdapter adapter){
        this.adapter = adapter;
    }
    public void Connect(String MacAdress){
        if(adapter.isEnabled()&&!MacAdress.isEmpty()) {
            device = adapter.getRemoteDevice(MacAdress);
            connectThread = new ConnectThread(device);
            connectThread.start();
        }
    }
}
