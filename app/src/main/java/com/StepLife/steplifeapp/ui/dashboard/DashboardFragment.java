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
import android.bluetooth.BluetoothManager;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.StepLife.steplifeapp.Bluetooth.BluetoothController;
import com.StepLife.steplifeapp.Bluetooth.Bt_module;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.other.ProgressBarAnimation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.transition.MaterialFadeThrough;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardFragment extends Fragment implements BluetoothController.Listner {
    BluetoothAdapter bluetoothAdapter;
    //Кнопки управления колленым модулем
    CardView ModuleCalibration,DisconnectModuleButton;
    BluetoothDevice BondDevice;
    int REQUEST_ENABLE_BT = 1;
    private int BTCheck = 0;
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
    View bottomSheetWaitView,bottomSheetStartView;
    ArrayList<String> mItems = new ArrayList<>(mItemsSet);
    TextView DeviceText,TextViewFoot;
    TextView TextViewBatteryCharge;
    Intent intent1;
    Boolean DeviceCheck = false;
    View bottomSheetDeviceList;

    // Нахождение устройств вокруг
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            final int check = 0;

                //Получение устройств найденных поблизости
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    if (device.getName() != null && !mEditItems.equals(device.getName())) {
                        if (device.getName().equals("HC-06")) {
                            mItems.add(device.getName());
                            adapter.notifyDataSetChanged();
                            ListSetDevice.add(device);
                        }

                    }
                } catch (Exception e) {
                    Log.d("Device", String.valueOf(e));
                }
            //Проверка на соединение
                final int bondState = intent.getIntExtra(EXTRA_BOND_STATE, check);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //анимация
        setExitTransition(new MaterialFadeThrough());
        setEnterTransition(new MaterialFadeThrough());

        intent1 = new Intent(getActivity(), Bt_module.class);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Количество шагов текстовое представление
        TextViewFoot = view.findViewById(R.id.TextViewFoot);

        //Кнопка отключения коленного модуля
        DisconnectModuleButton = view.findViewById(R.id.DisconnectModuleButton);
        DisconnectModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisconnectModuleButton.setVisibility(View.GONE);
            }
        });

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
                if (!mBluetoothAdapter.isEnabled()) {
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
        //Кнопка начала подключения на данный момент отключена
        bottomSheetStartView.findViewById(R.id.StartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionCheck(PermissionCheck);
                BtCheck(BTCheck);
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



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){

        }
    }

    //Получение списка сопряженных устройств коленных модулей steplife (Фильтрация по модулям не известна)
    @SuppressLint("MissingPermission")
    protected BluetoothDevice getPairedDevice(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        BluetoothDevice BluetoothDevice = null;

        //Проверяем среди сопряженных устройств, коленные модули steplife и выводим их отдельным списком
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName() != null && !mEditItems.equals(device.getName())) {
                if (device.getName().equals("HC-06")) {
                    return device;
                }
            }
        }
        return BluetoothDevice;
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

        if(BTCheck == 3) {
            bluetoothAdapter.cancelDiscovery();
         //   getActivity().unregisterReceiver(broadcastReceiver);
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
                BTCheck = 3;
                BtCheck(BTCheck);
                CheckoutBt();
            } else {
                BTCheck = 2;
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

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
                }
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
        }


        if (ACCESS_FINE_LOCATION == true && BLUETOOTH_SCAN == true && BLUETOOTH_CONNECT == true) {
            Check = true;
        }
        return Check;
    }

    int BtCheck(int BT) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //Устройство не поддерживает блютуз
            BTCheck = 1;
        } else if (!mBluetoothAdapter.isEnabled()) {
            BTCheck = 2;
        } else {
            //блютуз включен
            BTCheck = 3;
        }
        return BTCheck;
    }


    protected void ConnectPairedModule(BluetoothDevice device){
        //Создание потока соединения с уже сопряженным устройством
        bluetoothController = new BluetoothController(mBluetoothAdapter);
        bluetoothController.Connect(device.getAddress(),DashboardFragment.this);
    }
    protected void ModuleIsConnected(){
        DeviceCheck = true;
        DisconnectModuleButton.setVisibility(View.VISIBLE);
    }

    protected boolean ModuleisConnectedCheck(){
        if(DeviceCheck==true){
            return true;
        }else {
            return false;
        }
    }
    protected void ModuleisDisconnected(){
        DeviceCheck = false;
        DisconnectModuleButton.setVisibility(View.GONE);
    }

    //Проверка текущего состояния блютуз системы
    protected void CheckoutBt() {
        if (BTCheck == 0) {
          //  CheckOutText.setText("Что-то пошло не-так");
        } else if (BTCheck == 1) {
         //   CheckOutText.setText("Ваше устройство не поддерживает Bluetooth");
        } else if (BTCheck == 2) {
         //   CheckOutText.setText("Bluetooth выключен");
            bottomSheetWaitDialog.setContentView(bottomSheetWaitView);
            bottomSheetWaitDialog.show();
        } else {
          //  CheckOutText.setText("Выберите модуль из списка...");
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
    public void onReceive(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Получили данные что блютуз подключен
                if(message == BluetoothController.BLUETOOTH_CONNECTED){

                    Log.d("BTConnect", message);
                }

                //Получение данных не связанных с информацией о самом модуле блютуз
                if(message.replaceAll("\\s","").matches(".*\\d.*")){
                    TextViewBatteryCharge.setText(Integer.parseInt(message.replaceAll("\\s",""))+"%");
                }



                //Ошибка подключения или что то еще
                if(message == BluetoothController.BLUETOOTH_NO_CONNECTED){

                }
                Log.d("BTConnect", message);
            }
        });
    }
}