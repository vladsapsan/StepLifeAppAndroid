package com.example.steplifeapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Bt_module extends AppCompatActivity {


    TextView CheckOutText;
    Button buttonConnectBt;
    int REQUEST_ENABLE_BT = 1;
    private int BTCheck = 0;

    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> adapter;

    BluetoothManager bluetoothManager;
    int REQUEST_CODE_PERMISSION_COARSE_LOCATION;
    BluetoothAdapter bluetoothAdapter;
    ListView ListViewBtMOdule;

    ArrayList <String> mItems = new ArrayList<>();

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Получение устройств найденных поблизости
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               try {
                   mItems.add(device.getAddress());
                   adapter.notifyDataSetChanged();
               }
               catch (Exception e){
                   Log.d("Device", String.valueOf(e));
               }

        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 777;
    CardView CardDevice;


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission. ACCESS_COARSE_LOCATION)
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
                        new String[]{Manifest.permission. ACCESS_COARSE_LOCATION},
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

        //Получениче Bluetooth адаптера
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothManager = getSystemService(BluetoothManager.class);
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        try {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
            ListViewBtMOdule = findViewById(R.id.ListViewBtMOdule);
            ListViewBtMOdule.setAdapter(adapter);
        }catch (Exception e){}



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




        int permissionStatus2 = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permissionStatus2 == PackageManager.PERMISSION_GRANTED) {
            //Доступ есть к устройства поблизости
        } else {
            //Запрос доступа
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.BLUETOOTH_SCAN}, REQUEST_CODE_PERMISSION_COARSE_LOCATION );
        }



        BtCheck(BTCheck);
        CheckoutBt();


    }



    private void GetDevice(){
        IntentFilter DeviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver,DeviceFilter);
    }

    //Проверка ответа пользователя на соглашение включения блютуз
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            //Включен
            if(resultCode== RESULT_OK)
            {
                buttonConnectBt.setVisibility(View.GONE);
                BTCheck = 3;
                BtCheck(BTCheck);
                CheckoutBt();
            }
            else {
                BTCheck = 2;
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        bluetoothAdapter.cancelDiscovery();
    }

    @SuppressLint("MissingPermission")
    int BtCheck(int BT){
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

    void CheckoutBt(){
        if(BTCheck == 0) {
            CheckOutText.setText("Что-то пошло не-так");
        } else if (BTCheck == 1){
            CheckOutText.setText("Ваше устройство не поддерживает Bluetooth");
        } else if (BTCheck == 2){
            CheckOutText.setText("Bluetooth выключен");
            buttonConnectBt.setVisibility(View.VISIBLE);
        } else {
            CheckOutText.setText("Поиск модуля...");

            try {
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
}