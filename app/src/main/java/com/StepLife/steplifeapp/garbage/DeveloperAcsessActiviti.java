package com.StepLife.steplifeapp.garbage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.StepLife.steplifeapp.MainActivity.MainActivity;
import com.StepLife.steplifeapp.R;
import com.chaos.view.PinView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeveloperAcsessActiviti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_acsess_activiti);
        if(verifyInstallerId(getApplicationContext()))
        {
            Intent intent = new Intent(DeveloperAcsessActiviti.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        final PinView pinView = findViewById(R.id.DeveloperPinView);
        //Проверка кода разработчика
        Button CheckBtn = findViewById(R.id.DeveloperPinButton);
        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pinView.getText().toString().equals("128500")){
                    Intent intent = new Intent(DeveloperAcsessActiviti.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),pinView.getText().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //Проверка источника с которого установлено приложение
    boolean verifyInstallerId(Context context) {

        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        return installer != null && validInstallers.contains(installer);
    }
}