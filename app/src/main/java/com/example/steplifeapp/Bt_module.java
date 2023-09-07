package com.example.steplifeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Bt_module extends AppCompatActivity {


    TextView CheckOutText;
    Button buttonConnectBt;
    private int BTCheck = 0;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothManager bluetoothManager;

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
        mBluetoothAdapter = bluetoothManager.getAdapter();



        //Кнопка включения bluetooth
        buttonConnectBt = findViewById(R.id.buttonConnectBt);
        buttonConnectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                int REQUEST_ENABLE_BT = 1;
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                BtCheck(BTCheck);
                CheckoutBt();
            }
        });

        BtCheck(BTCheck);
        CheckoutBt();

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
            CheckOutText.setText("Поиск модуля");
        }
    }
}