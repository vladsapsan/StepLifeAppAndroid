package com.StepLife.steplifeapp.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.media.tv.TvContract;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firestore.v1.Value;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ConnectThread extends Thread{
    private String UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final byte START_BYTE = 0x3A;
    public static final byte[] STARTCHECK_BYTE = {0x11};
    public static final byte[] STARTKZM_BYTE = {0x16};
    public static final byte[] CommandToK3M = {0x3A,0x16};
    public static final byte[] CommandToCheckConnection = {0x3A,0x10,0x11, (byte) 0xDF,0x0A,0x0D};

    private static final byte END_BYTE = 0x0A;
    private static final byte ERROR_BYTE = (byte) 0xFF;
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

                SendCommand(STARTCHECK_BYTE);
                ReadCommands();
            }
        }catch (SecurityException e){
            Log.d("BTConnect", e.toString());
        } catch (IOException e) {
            Log.d("BTConnect", e.toString());
        }
    }

    public void ReadCommands(){
        byte[] buffer = new byte[1024];
        //Цикл до перрывания потока
        while (!Thread.currentThread().isInterrupted()){
               try {
                   int numBytes = mInputStream.read(buffer);
                   processReceivedData(buffer, numBytes);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
       }
    }



    private void processReceivedData(byte[] buffer, int numBytes) {
        Log.d("BTConnect", Arrays.toString(buffer));
        int StartofCommand=0;
        int EndofCommand=0;
        byte[] CommandByte = new byte[128];
                // Ищем символ начала кадра
                for (int i=0;i<buffer.length;i++){
                    if(buffer[i]==START_BYTE){
                        StartofCommand = i;
                        break;
                    }

                }
                // Ищем символ начала кадра
                for (int i=StartofCommand;i<buffer.length;i++){
                    if(buffer[i]==END_BYTE){
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
                    if (isValidFrame(CommandByte)) {
                        // Обрабатываем полученные данные
                        handleReceivedData(CommandByte);
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



    private void handleReceivedData(byte[] byteData) {
        // Получаем код функции
        byte ModuleCode = byteData[0];
        Log.d("BTConnect", String.valueOf(ModuleCode));
        // Обрабатываем в зависимости от кода функции
        switch (ModuleCode) {
            case 0x10   :
                // КЗМ
                KZMModuleFunction(byteData);
                break;
            case 0x20:
                //Опрная трубка
            case 0x30:
                // Гидромодуль
                break;
            case 0x40:
                //Алгоритмический процессор
                break;
            case 0x50:
                // Датчик MPU-6050
                break;
            default:
                // Неизвестный код функции
                break;
        }
    }


    //Обращение к модуля функци кзм
    private void KZMModuleFunction(byte[] ModuleCode){
        switch (ModuleCode[1]) {
            case 0x00:
                // Несуществующая функция
                break;
            case 0x11:
                // Проверка связи
                ModuleIsConnected=true;
                Log.d("BTConnect", String.valueOf("ModuleISConnectedAndChecked"));
                //Отправляем запрос на получение заряда аккумулятора
                SendCommand(STARTKZM_BYTE);
                break;
            case 0x12:
                // Чтение слова ОЗУ
                if(ModuleIsConnected){

                }else {

                }
                break;
            case 0x13:
                // Запись слова ОЗУ
                if(ModuleIsConnected){

                }else {

                }
                break;
            case 0x14:
                // Чтение слова EEPROM
                if(ModuleIsConnected){

                }else {

                }
                break;
            case 0x15:
                // Запись слова EEPROM
                if(ModuleIsConnected){

                }else {

                }
                break;
            case 0x16:
                // Чтение показаний АЦП
                if(ModuleIsConnected){
                      ATPModuleRecived(ModuleCode);
                }else{
                }
                break;
            case 0x17:
                // Чтение показаний АЦП (диагностическое)
                if(ModuleIsConnected){

                }else {

                }
                break;
            case 0x18:
                // Чтение кодов версий АО и ПО
                if(ModuleIsConnected){

                }else {

                }
                break;
            case 0x1F:
                // Сообщение об ошибке
                break;
            default:
                // Неизвестный код функции
                break;
        }
    }


    //Обработка показаний АЦП
    private void ATPModuleRecived(byte[] DataByte){
        switch (DataByte[2]){
            case 0x01:
                //Битовые флаги состояния КМ
                break;
            case 0x03:
                //Рассчитанный угол сгибания коленки
                break;
            case 0x04:
                //напряжение аккумулятора
                int BateryCharge = 0;
                //Получаем данные о напряжении аккумулятора коленного модуля
                for (int i = 4;i<11;i++){
                    BateryCharge = BateryCharge + Integer.valueOf(DataByte[i]);
                }
                Log.d("BTConnect", String.valueOf(BateryCharge));
                break;
            case 0x05:
                //Температура кристалла контроллера

                break;
            case 0x08:
                //Вес TWT
                break;
            default:
                break;
        }
    };
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
                mSocket.close();
            }
        }catch (SecurityException e){} catch (IOException e) {
        }
    }


}
