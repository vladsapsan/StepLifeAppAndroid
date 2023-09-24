package com.StepLife.steplifeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity {


    Button ConnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectButton = (Button) findViewById(R.id.ConnectModuleButton);

        setContentView(R.layout.activity_start_screen);
    }


    public void clickConnectModule(View view) {
        Intent i;
        i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}