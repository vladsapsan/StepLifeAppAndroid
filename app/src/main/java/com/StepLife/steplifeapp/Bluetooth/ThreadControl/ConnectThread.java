package com.StepLife.steplifeapp.Bluetooth.ThreadControl;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class ConnectThread extends Thread{
    private String UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public String BatteryCharge = null;
    BluetoothController.Listner listner;
    private BluetoothSocket mSocket = null;
    public static Boolean ModuleIsConnected =false;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

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

                //Определяем потоки входящий и выходящий
                mInputStream = mSocket.getInputStream();
                mOutputStream = mSocket.getOutputStream();
                ModuleIsConnected=true;

                SendCommand(ModuleConnectionCommand.CommandToCheckConnection);
                ReadCommands();
            }
        }catch (SecurityException e){
            Log.d("BTConnect", e.toString());
        } catch (IOException e) {
            Log.d("BTConnect", e.toString());
        }
    }

    public void ReadCommands(){
        byte[] buffer = new byte[512];
        //Цикл до перрывания потока
            while (ModuleIsConnected) {
                if(mSocket.isConnected()) {
                    try {
                        Log.d("Bt", Arrays.toString(buffer));
                        mInputStream.read(buffer);
                        processReceivedData(buffer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    }
    private void processReceivedData(byte[] buffer) {
        int StartofCommand=0;
        int EndofCommand=0;
        byte[] CommandByte = new byte[128];
                // Ищем символ начала кадра
                for (int i=0;i<buffer.length;i++){
                    if(buffer[i]== ModuleConnectionCommand.START_BYTE){
                        StartofCommand = i;
                        break;
                    }
                }
                // Ищем символ начала кадра
                for (int i=StartofCommand;i<buffer.length;i++){
                    if(buffer[i]==ModuleConnectionCommand.END_BYTE){
                        EndofCommand = i-1;
                        break;  
                    }
                }
                if (EndofCommand>0){
                    for (int i = 0;i<EndofCommand;i++)
                    {
                        CommandByte[i]=buffer[i+StartofCommand+1];
                    }
                    Log.d("BTConnect", Arrays.toString(CommandByte));
                    // Получаем подстроку после символа начала кадра
                    // Получаем данные между символами начала и конца кадра
                    // Проверяем контроль целостности кадра
                    if (isValidFrame(CommandByte)){
                        // отправляем в dashboardfragment и Обрабатываем полученные данные
                        listner.onReceive(CommandByte);
                    }else {
                        //контроль целонстности не пройден??? Сумма не равна?
                    }
                }else {
                    //Ошибка подсчетов
                }
        }
    //Контроль целостности суммы
    private boolean isValidFrame(byte[] byteData) {
        Log.d("BTConnect", Arrays.toString(byteData));
        // Вычисляем сумму всех информационных байтов
        int sum = 0;
        for (int i = 0; i < byteData.length; i++) {
            if(sum<256) {
                if (byteData[i] < 0) {
                    sum += 256 + byteData[i];
                } else {
                    sum += byteData[i];
                }
            }else {
                    sum += byteData[i];
            }
        }
        Log.d("BTConnect", String.valueOf(sum));
        // Проверяем, что сумма по модулю 256 равна 0
        return (sum % 256 == 0);
    }
    //Отправка сообщения
    public void SendCommand(byte[] message){
        try {
            mSocket.getOutputStream().write(message);
        }catch (IOException e){
            Log.d("BTConnect", e.toString());
        }
    }
    //Отправка команд обратно на модуль
    public void sendResponse(byte[] responseData) {
        try {
            mSocket.getOutputStream().write(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Закрытие канала передачи данных
    public void CloseConnect(){
        try {
            if(mSocket!=null) {
                //Закрытие канала передачи данных
                ModuleIsConnected=false;
                mSocket.close();
            }
        }catch (SecurityException e){} catch (IOException e) {
            Log.e("BtConnection", "Ошибка при закрытии BluetoothSocket:" + e.getMessage());
        }
    }}
