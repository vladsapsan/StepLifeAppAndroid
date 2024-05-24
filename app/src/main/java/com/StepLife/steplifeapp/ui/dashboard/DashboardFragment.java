package com.StepLife.steplifeapp.ui.dashboard;

import static android.app.Activity.RESULT_OK;
import static android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED;
import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static android.bluetooth.BluetoothDevice.BOND_BONDING;
import static android.bluetooth.BluetoothDevice.BOND_NONE;
import static android.bluetooth.BluetoothDevice.EXTRA_BOND_STATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.StepLife.steplifeapp.Bluetooth.BluetoothController;
import com.StepLife.steplifeapp.Bluetooth.ModuleConnectionCommand;
import com.StepLife.steplifeapp.other.BtTelephoneModuleState;
import com.StepLife.steplifeapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;

public class DashboardFragment extends Fragment implements BluetoothController.Listner {
    BluetoothAdapter bluetoothAdapter;
    //Кнопки управления колленым модулем
    CardView ModuleCalibration,DisconnectModuleButton;
    BluetoothDevice BondDevice;
    int REQUEST_ENABLE_BT = 1;
    boolean PermissionCheck;
    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> adapter;
    BluetoothController bluetoothController;
    int REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT;
    BottomSheetDialog bottomSheetWaitDialog;
    ListView ListViewBtMOdule;
    List<BluetoothDevice> ListSetDevice = new ArrayList<>();
    ArrayList<String> mEditItems = new ArrayList<>();
    Set<String> mItemsSet = new HashSet<String>();
    CardView cardView;
    View bottomSheetWaitView,bottomSheetStartView;
    ArrayList<String> mItems = new ArrayList<>(mItemsSet);
    TextView DeviceText,TextViewFoot;
    TextView TextViewBatteryCharge,ModuleIsActiveText;
    Boolean DeviceCheck = false;
    View bottomSheetDeviceList;
    BtTelephoneModuleState btTelephoneState = BtTelephoneModuleState.BT_NoFound;

    

    // Фильтр не сопряженный устройств вокруг, возможных для подключения
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
                //Получение устройств найденных поблизости
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null && !mEditItems.equals(device.getName())) {
                    if (device.getName().equals("HC-06")) {
                            mItems.add(device.getName());
                            adapter.notifyDataSetChanged();
                            ListSetDevice.add(device);
                    }
                }

            //Проверка на соединение
                final int bondState = intent.getIntExtra(EXTRA_BOND_STATE, 0);
                switch (bondState) {
                    case BOND_BONDING:
                        // Старт
                        break;
                    case BOND_BONDED:
                        //Сопряжение успешно
                        // пытаемся соедениться с устройством в потоке
                        ConnectPairedModule(BondDevice);
                        ModuleIsConnected();
                        bottomSheetWaitDialog.dismiss();
                        break;
                    case BOND_NONE:
                        // Не удалось сопряжение
                        break;
                }
            }


    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    private BluetoothAdapter InitBluetoothAdapter(){
        return BluetoothAdapter.getDefaultAdapter();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBluetoothAdapter = InitBluetoothAdapter();

        //Количество шагов текстовое представление
        TextViewFoot = view.findViewById(R.id.TextViewFoot);

        //Кнопка отключения коленного модуля
        DisconnectModuleButton = view.findViewById(R.id.DisconnectModuleButton);
        DisconnectModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseConnectPairedModule();
            }
        });

        //Текст о подключении модуля
        ModuleIsActiveText  = view.findViewById(R.id.ModuleIsActiveText);

        //Текстовое представление о состоянии аккумулятора
        TextViewBatteryCharge = view.findViewById(R.id.TextViewBatteryCharge);

        //Кнопка калибровки
        ModuleCalibration = view.findViewById(R.id.ModuleCalibration);
        ModuleCalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mItems);
        bottomSheetWaitDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bottomSheetWaitDialog.setCanceledOnTouchOutside(false);
        bottomSheetWaitDialog.setDismissWithAnimation(true);
        //Диалог включения блютуз
        bottomSheetWaitView = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.bottomsheet_bluetooth_off,
                        view.findViewById(R.id.SheetDialogWaitBluetoothContainer)
                );
        bottomSheetWaitView.findViewById(R.id.BluetoothOnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Нажатие на кнопку включения Bluetooth
                if (btTelephoneState==BtTelephoneModuleState.BT_Disable) {
                    Intent enableBtIntent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });
        //диалог старта подключения коленного модуля
        bottomSheetStartView = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.bottomsheet_start_moduleconnection,
                        view.findViewById(R.id.SheetDialogStartBluetoothContainer)
                );
        //Кнопка начала подключения
        bottomSheetStartView.findViewById(R.id.StartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionCheck(PermissionCheck);
                BtCheck(mBluetoothAdapter);
                CheckoutBt();
            }
        });

        //диалог выбора модуля из списка блютуз устройств
        bottomSheetDeviceList = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.bottomsheet_bluetooth_devicelist,
                        view.findViewById(R.id.SheetDialogWaitBluetoothDeviceContainer)
                );
        ListViewBtMOdule = bottomSheetDeviceList.findViewById(R.id.ListViewBtMOdule);
        ListViewBtMOdule.setAdapter(adapter);
        ListViewBtMOdule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Пара создана
                if (ListSetDevice.get(i).createBond()) {
                    //инициализация контролера
                    GetBond();
                    bluetoothController = new BluetoothController(mBluetoothAdapter);
                    BondDevice = ListSetDevice.get(i);
                } else {
                    //Отмена создания?
                    Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT);

                }


            }
        });


        //Включение начального диалоговое окно
        bottomSheetWaitDialog.setContentView(bottomSheetStartView);
    }

    //Получение списка сопряженных устройств коленных модулей steplife (Фильтрация по модулям не известна)
    @SuppressLint("MissingPermission")
    protected BluetoothDevice getPairedDevice(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        //Проверяем среди сопряженных устройств, коленные модули steplife и выводим их отдельным списком
        for (BluetoothDevice device : pairedDevices) {
            if(device.getName()==null){
                return null;
            }
            if (device.getName().equals("HC-06")) {
                return device;
            }
        }
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();
        //Если устройство не подключено
        if (!DeviceCheck) {
            bottomSheetWaitDialog.show();
        } else {
            //Устройство подключено
        }
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(btTelephoneState==BtTelephoneModuleState.BT_Enable) {
            bluetoothAdapter.cancelDiscovery();
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    private void GetDevice() {
        IntentFilter DeviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(broadcastReceiver, DeviceFilter);
    }

    private void GetBond() {
        IntentFilter BondFilter = new IntentFilter(ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(broadcastReceiver, BondFilter);
    }

    //Проверка ответа пользователя на соглашение включения блютуз
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            //Включен
            if (resultCode == RESULT_OK) {
                bottomSheetWaitDialog.dismiss();
                BtCheck(mBluetoothAdapter);
                CheckoutBt();
            }
        }
    }

    private boolean PermissionCheck(boolean Check) {

        boolean BLUETOOTH_CONNECT = false, BLUETOOTH_SCAN = false, ACCESS_FINE_LOCATION = false;
        int permissionStatus1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT);
        int permissionStatus2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN);
        int permissionStatus3 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionStatus1 == PackageManager.PERMISSION_GRANTED) {
            //Доступ есть к устройства поблизости
            BLUETOOTH_CONNECT = true;
            if (permissionStatus2 == PackageManager.PERMISSION_GRANTED) {
                if (permissionStatus3 == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
                }
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
            }
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
        }

        if (ACCESS_FINE_LOCATION == true && BLUETOOTH_SCAN == true && BLUETOOTH_CONNECT == true) {
            Check = true;
        }
        return Check;
    }

    private void BtCheck(BluetoothAdapter mBluetoothAdapter) {
        if (mBluetoothAdapter == null) {
            //Устройство не поддерживает блютуз???
            btTelephoneState = BtTelephoneModuleState.BT_NoFound;
        } else if (!mBluetoothAdapter.isEnabled()) {
            btTelephoneState = BtTelephoneModuleState.BT_Disable;
        } else {
            //блютуз включен
            btTelephoneState = BtTelephoneModuleState.BT_Enable;
        }
    }


    protected void ConnectPairedModule(BluetoothDevice device){
        //Создание потока соединения с уже сопряженным устройством
        bluetoothController = new BluetoothController(mBluetoothAdapter);
        bluetoothController.Connect(device.getAddress(),DashboardFragment.this);
    }
    //Отключение текущего модуля
    protected void CloseConnectPairedModule(){
        //Создание потока соединения с уже сопряженным устройством
        if(bluetoothController!=null) {
            bluetoothController.connectThread.CloseConnect();
            DeviceCheck = false;
            ModuleIsActiveText.setVisibility(View.GONE);
            DisconnectModuleButton.setVisibility(View.GONE);
        }
    }
    protected void ModuleIsConnected(){
        DeviceCheck = true;
        ModuleIsActiveText.setVisibility(View.VISIBLE);
        DisconnectModuleButton.setVisibility(View.VISIBLE);
    }

    public final void RefreshTextBatteryCharge(){
        if(bluetoothController.connectThread!=null){
            if(bluetoothController.connectThread.BatteryCharge!=null) {
                TextViewBatteryCharge.setText(bluetoothController.connectThread.BatteryCharge);
            }
        }

    }

    //Проверка текущего состояния блютуз системы
    protected void CheckoutBt() {
        if (btTelephoneState == BtTelephoneModuleState.BT_Error) {

        } else if (btTelephoneState == BtTelephoneModuleState.BT_NoFound) {

        } else if (btTelephoneState == BtTelephoneModuleState.BT_Disable) {
            bottomSheetWaitDialog.setContentView(bottomSheetWaitView);
            bottomSheetWaitDialog.show();
        } else {
            //Пытамся соедениться с уже сопряженным устройством если оно есть
            if(getPairedDevice()!=null){
                ConnectPairedModule(getPairedDevice());
                ModuleIsConnected();
                bottomSheetWaitDialog.dismiss();
            } else {
                //Если с сопряженными устройством не удалось соедениться, пробуем найти устройства в округе
                bottomSheetWaitDialog.setContentView(bottomSheetDeviceList);
                bottomSheetWaitDialog.show();
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    bluetoothAdapter.startDiscovery();
                }catch (Exception e){}
                GetDevice();
            }
        }
    }

    //Важное примечание при получении данных с ардуино Данных которые вводятся через сериалпорт в строке имеют за собой продолжение в виде пробелов от которого нужно очистить строку для дальнешего преобразования
    @Override
    public void onReceive(byte[] Bytemessage) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("BTDashBoard", Arrays.toString(Bytemessage));
                handleReceivedData(Bytemessage);
            }
        });
    }
    private void handleReceivedData(byte[] byteData) {
        // Получаем код функции
        byte ModuleCode = byteData[0];
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
                ModuleIsConnected();
                Log.d("BTConnect",      String.valueOf("ModuleISConnectedAndChecked"));
                //Отправляем запрос на получение заряда аккумулятора
                bluetoothController.connectThread.SendCommand(ModuleConnectionCommand.CommandToK3MBatteryCharge);
                break;
            case 0x12:
                // Чтение слова ОЗУ
                if(DeviceCheck){

                }else {

                }
                break;
            case 0x13:
                // Запись слова ОЗУ
                if(DeviceCheck){

                }else {

                }
                break;
            case 0x14:
                // Чтение слова EEPROM
                if(DeviceCheck){

                }else {

                }
                break;
            case 0x15:
                // Запись слова EEPROM
                if(DeviceCheck){

                }else {

                }
                break;
            case 0x16:
                // Чтение показаний АЦП
                if(DeviceCheck){
                    ATPModuleRecived(ModuleCode);
                }else{
                }
                break;
            case 0x17:
                // Чтение показаний АЦП (диагностическое)
                if(DeviceCheck){

                }else {

                }
                break;
            case 0x18:
                // Чтение кодов версий АО и ПО
                if(DeviceCheck){

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


    //Обработка и возврат показаний АЦП
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
                for (int i = 4;i<11;i++)
                    BateryCharge = BateryCharge + Integer.parseInt(new String(String.valueOf(DataByte[i])));
                Log.d("BTConnect", String.valueOf(BateryCharge));
                TextViewBatteryCharge.setText(String.valueOf(BateryCharge));
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
}