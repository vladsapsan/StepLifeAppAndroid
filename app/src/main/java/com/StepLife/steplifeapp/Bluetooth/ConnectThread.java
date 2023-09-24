package com.StepLife.steplifeapp.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

public class ConnectThread extends Thread{
    private String UUID = "00001101-0000-1000-8000-00805F9B34FB";
    BluetoothController.Listner listner;
    private BluetoothSocket mSocket = null;

    ConnectThread(BluetoothDevice device, BluetoothController.Listner listner){
        try {
            //Создание канал передачи данных
            mSocket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        }catch (SecurityException e){} catch (IOException e) {

        }
        this.listner = listner;
    }

    @Override
    public void run() {
        super.run();
        try {
            if(mSocket!=null) {
                Log.d("BTConnect", "Connected...");
                //Запуск канал передачи данных
            mSocket.connect();
                listner.onReceive(BluetoothController.BLUETOOTH_CONNECTED);
                SendMessage(String.valueOf("Vlad"));
                ReadMessage();

            }

            
        }catch (SecurityException e){
            Log.d("BTConnect", e.toString());
        } catch (IOException e) {
            listner.onReceive(BluetoothController.BLUETOOTH_NO_CONNECTED);
            Log.d("BTConnect", e.toString());
        }
    }

    private void ReadMessage(){
        byte[] buffer = new byte[1024];
       while (true){
           try {
            int lenthbuffer = mSocket.getInputStream().read(buffer);
            String message = new String(buffer,0,lenthbuffer);
            listner.onReceive(message);
           }catch (IOException e){
               Log.d("BTConnect", e.toString());
               break;
           }
       }
    }

    public void SendMessage(String message){
        try {
            mSocket.getOutputStream().write(message.getBytes());
        }catch (IOException e){
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
