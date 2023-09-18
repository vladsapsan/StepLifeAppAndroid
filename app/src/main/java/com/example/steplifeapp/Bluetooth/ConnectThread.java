package com.example.steplifeapp.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread{
    private String UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothSocket mSocket = null;
    ConnectThread(BluetoothDevice device){
        try {
            //Создание канал передачи данных
            mSocket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        }catch (SecurityException e){} catch (IOException e) {

        }
    }

    @Override
    public void run() {
        super.run();
        try {
            if(mSocket!=null) {
                Log.d("BTConnect", "Connected...");
                //Запуск канал передачи данных
            mSocket.connect();
                Log.d("BTConnect", "Connect");
            }
        }catch (SecurityException e){
            Log.d("BTConnect", e.toString());
        } catch (IOException e) {
            Log.d("BTConnect", e.toString());
        }
    }

    //Закрытие канала передачи данных
    public void CloseConnect(){
        try {
            if(mSocket!=null) {
                //Закрытие канала передачи данных
                mSocket.close();
            }
        }catch (SecurityException e){} catch (IOException e) {
        }
    }


}
