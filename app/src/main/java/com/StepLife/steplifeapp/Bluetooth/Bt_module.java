package com.StepLife.steplifeapp.Bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.other.ItemViewModel;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bt_module extends AppCompatActivity {


    TextView CheckOutText;
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
    BluetoothAdapter bluetoothAdapter;
    ListView ListViewBtMOdule;

    List<BluetoothDevice> ListSetDevice = new ArrayList<>();
    ArrayList<String> mEditItems = new ArrayList<>();
    Set<String> mItemsSet = new HashSet<String>();
    ArrayList<String> mItems = new ArrayList<>(mItemsSet);


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
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

        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 777;
    CardView CardDevice;


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //показываем диалог
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton("Предоставить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Юзер одобрил
                                ActivityCompat.requestPermissions(Bt_module.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();    
                    
            } else {
                //запрашиваем пермишен, уже не показывая диалогов с пояснениями
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_bt_module);

        CheckOutText = findViewById(R.id.CheckOutText);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        //Диалог ожидания загрузки
        bottomSheetWaitDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetWaitDialog.setDismissWithAnimation(true);
        View bottomSheetWaitView = LayoutInflater.from(this.getApplicationContext())
                .inflate(
                        R.layout.sheetconnectbtmodulewaiting,
                        findViewById(R.id.SheetDialogWaitConnectBtContainer)
                );
        bottomSheetWaitDialog.setContentView(bottomSheetWaitView);


        //Получениче Bluetooth адаптера
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothManager = getSystemService(BluetoothManager.class);
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        ListViewBtMOdule = findViewById(R.id.ListViewBtMOdule);
        ListViewBtMOdule.setAdapter(adapter);
        //Нажатие на устройство из списка




        ListViewBtMOdule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bottomSheetWaitDialog.show();
                //Пара создана
                if(ListSetDevice.get(i).createBond()){
                    //инициализация контролера=
                     //  bluetoothController = new BluetoothController(mBluetoothAdapter);
                    BluetoothDevice device = ListSetDevice.get(i);
                    Bundle result = new Bundle();
                    result.putString("Device", device.getAddress());
                    result.putBoolean("DeviceCheck", true);
                    getSupportFragmentManager().setFragmentResult("DeviceKey",result);

                    //  bluetoothController.Connect(device.getAddress(),this);

                }else {
                    //Отмена создания?
                    Toast.makeText(getApplicationContext(),"Что-то пошло не так",Toast.LENGTH_SHORT);

                }


            }
        });


        //Кнопка включения bluetooth
        buttonConnectBt = findViewById(R.id.buttonConnectBt);
        buttonConnectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });


        PermissionCheck(PermissionCheck);
        BtCheck(BTCheck);
        CheckoutBt();


    }


    private void GetDevice() {
        IntentFilter DeviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, DeviceFilter);
    }

    //Проверка ответа пользователя на соглашение включения блютуз
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            //Включен
            if (resultCode == RESULT_OK) {
                buttonConnectBt.setVisibility(View.GONE);
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
        int permissionStatus1 = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT);
        int permissionStatus2 = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        int permissionStatus3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionStatus1 == PackageManager.PERMISSION_GRANTED) {
            //Доступ есть к устройства поблизости
            BLUETOOTH_CONNECT = true;
            if (permissionStatus2 == PackageManager.PERMISSION_GRANTED) {
                if (permissionStatus3 == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_BLUETOOTH_CONNECT);
        }


        if (ACCESS_FINE_LOCATION == true && BLUETOOTH_SCAN == true && BLUETOOTH_CONNECT == true) {
            Check = true;
        }
        return Check;
    }

    @Override
    public void finish() {
        super.finish();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(broadcastReceiver);
    }

    @SuppressLint("MissingPermission")
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
            CheckOutText.setText("Что-то пошло не-так");
        } else if (BTCheck == 1) {
            CheckOutText.setText("Ваше устройство не поддерживает Bluetooth");
        } else if (BTCheck == 2) {
            CheckOutText.setText("Bluetooth выключен");
            buttonConnectBt.setVisibility(View.VISIBLE);
        } else {
            CheckOutText.setText("Выберите модуль из списка...");

            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                bluetoothAdapter.startDiscovery();
            }catch (Exception e){}
            GetDevice();
        }
    }



    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //Found, add to a device list
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }


}