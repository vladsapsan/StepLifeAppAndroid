package com.StepLife.steplifeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Helper extends AppCompatActivity {


    ImageView BacktoSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        // Установка стиля безрамочного
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainYellow));

        //Кнопка выхода
        BacktoSettings = findViewById(R.id.BacktoSettings);
        BacktoSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}