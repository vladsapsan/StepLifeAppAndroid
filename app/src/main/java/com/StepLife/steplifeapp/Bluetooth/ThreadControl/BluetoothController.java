package com.StepLife.steplifeapp.Bluetooth.ThreadControl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class BluetoothController {
    BluetoothAdapter adapter;
    BluetoothDevice device;
    public ConnectThread connectThread = null;
    public BluetoothController(BluetoothAdapter adapter){
        this.adapter = adapter;
    }
    public void Connect(String MacAdress,Listner listner){
        if(adapter.isEnabled()&&!MacAdress.isEmpty()) {
            device = adapter.getRemoteDevice(MacAdress);
            connectThread = new ConnectThread(device,listner);
            connectThread.start();
        }
    }
    public void SendCommand(byte[] message){
        connectThread.SendCommand(message);
    }
    public void CloseConnection(){
        connectThread.CloseConnect();
    }
    public interface Listner{
        void onReceive(byte[] message);
    }
}
