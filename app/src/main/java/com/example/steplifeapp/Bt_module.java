package com.example.steplifeapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Bt_module extends AppCompatActivity {


    TextView CheckOutText;
    Button buttonConnectBt;
    int REQUEST_ENABLE_BT = 1;
    private int BTCheck = 0;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothManager bluetoothManager;
    RecyclerView BtRecycle;
    CardView CardDevice;

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





        BtRecycle = findViewById(R.id.BtRecycle);



        //Кнопка включения bluetooth
        buttonConnectBt = findViewById(R.id.buttonConnectBt);
        buttonConnectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });

        BtCheck(BTCheck);
        CheckoutBt();

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
                CheckOutText.setText("Поиск модуля");
            }
            else {
                BTCheck = 2;
            }
        }
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
            BtRecycle.setVisibility(View.VISIBLE);

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