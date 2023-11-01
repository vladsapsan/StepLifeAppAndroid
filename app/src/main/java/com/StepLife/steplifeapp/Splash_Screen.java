package com.StepLife.steplifeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Установка стиля безрамочного
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainYellow));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.MainYellow));

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Splash_Screen.this.startActivity(new Intent(Splash_Screen.this,MainActivity.class));
                Splash_Screen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}