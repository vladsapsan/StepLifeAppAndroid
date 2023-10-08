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
import androidx.lifecycle.ViewModelProvider;

import com.StepLife.steplifeapp.Bluetooth.BluetoothController;
import com.StepLife.steplifeapp.Bluetooth.Bt_module;
import com.StepLife.steplifeapp.ProthesisModule.ProthesisModuleSettings;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.databinding.FragmentDashboardBinding;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.other.ProgressBarAnimation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardFragment extends Fragment implements BluetoothController.Listner {

    private DashboardViewModel dashboardViewModel;
    Button buttonConnect, CalibationModuleButton;
    BluetoothDevice device;
    BluetoothAdapter bluetoothAdapter;
    CardView TypeProthesisModuleCardButton;
    TextView CheckOutText;
    BluetoothDevice BondDevice;
    Button buttonConnectBt;
    int REQUEST_ENABLE_BT = 1;
    private int BTCheck = 0;
    boolean PermissionCheck;
    NetworkChangeListner networkChangeListner;

    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> adapter;

    BluetoothManager bluetoothManager;
    BluetoothController bluetoothController;
    int REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT;
    int REQUEST_CODE_PERMISSION_BLUETOOTH_SCAN;
    int REQUEST_CODE_PERMISSION_FINE_LOCATION;

    BottomSheetDialog bottomSheetWaitDialog;
    ListView ListViewBtMOdule;

    List<BluetoothDevice> ListSetDevice = new ArrayList<>();
    ArrayList<String> mEditItems = new ArrayList<>();
    Set<String> mItemsSet = new HashSet<String>();
    View bottomSheetWaitView,bottomSheetStartView;
    ArrayList<String> mItems = new ArrayList<>(mItemsSet);
    TextView DeviceText,TextViewFoot;
    CircularProgressIndicator ProgressBarBatteryCharge;
    TextView TextViewBatteryCharge, TextBattery;
    Intent intent1;
    Boolean DeviceCheck = false;
    View bottomSheetDeviceList;

    CardView SettingModuleButton;

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
                        mItems.add(device.getName());
                        adapter.notifyDataSetChanged();
                        ListSetDevice.add(device);
                    }
                } catch (Exception e) {
                    Log.d("Device", String.valueOf(e));
                }



            //Проверка на соединение
                final int bondState = intent.getIntExtra(EXTRA_BOND_STATE, check);
                switch (bondState) {
                    case BOND_BONDING:
                        // Bonding started

                        break;
                    case BOND_BONDED:
                        // Bonding succeeded
                        bluetoothController.Connect(BondDevice.getAddress(),DashboardFragment.this);
                        TextViewFoot.setText("671");
                        DeviceCheck = true;
                        bottomSheetWaitDialog.dismiss();
                        break;
                    case BOND_NONE:
                        // Oh oh


                        break;
                }
            }


    };


    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent(getActivity(), ProthesisModuleSettings.class);
        intent1 = new Intent(getActivity(), Bt_module.class);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        CalibationModuleButton = view.findViewById(R.id.CalibationModuleButton);

        //Количество шагов текст
        TextViewFoot = view.findViewById(R.id.TextViewFoot);
        //Прогрессбар о состоянии аккумулятора
        ProgressBarBatteryCharge = view.findViewById(R.id.ProgressBarBatteryCharge);

        //Текстовое представление о состоянии аккумулятора
        TextViewBatteryCharge = view.findViewById(R.id.TextViewBatteryCharge);


        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mItems);
        //  ListViewBtMOdule = view.findViewById(R.id.ListViewBtMOdule);
        // ListViewBtMOdule.setAdapter(adapter);


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




        TypeProthesisModuleCardButton = view.findViewById(R.id.TypeProthesisModuleCardButton);
        TypeProthesisModuleCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothController.SendMessage("VLadsapsan");
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser==true){

        }
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

    void CheckoutBt() {
        if (BTCheck == 0) {
          //  CheckOutText.setText("Что-то пошло не-так");
        } else if (BTCheck == 1) {
         //   CheckOutText.setText("Ваше устройство не поддерживает Bluetooth");
        } else if (BTCheck == 2) {
         //   CheckOutText.setText("Bluetooth выключен");
         //   buttonConnectBt.setVisibility(View.VISIBLE);
            bottomSheetWaitDialog.setContentView(bottomSheetWaitView);
            bottomSheetWaitDialog.show();
        } else {
          //  CheckOutText.setText("Выберите модуль из списка...");
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
    @Override
    public void onReceive(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Получили данные что блютуз подключен
                if(message == BluetoothController.BLUETOOTH_CONNECTED){
                    CalibationModuleButton.setVisibility(View.VISIBLE);
                    Log.d("BTConnect", message);
                }
                //Прием данных из модуля
                    try {
                     //   ProgressBarBatteryCharge.setProgress(Integer.parseInt(message));
                        ProgressBarAnimation anim = new ProgressBarAnimation(ProgressBarBatteryCharge, ProgressBarBatteryCharge.getProgress(),Integer.parseInt(message));
                        anim.setDuration(500);
                        ProgressBarBatteryCharge.startAnimation(anim);
                        TextViewBatteryCharge.setText(Integer.parseInt(message)+"%");
                        Log.d("BTConnect", message);
                    }catch (Exception e){}

                //Ошибка подключения или что то еще
                if(message == BluetoothController.BLUETOOTH_NO_CONNECTED){

                }
            }
        });
    }
}