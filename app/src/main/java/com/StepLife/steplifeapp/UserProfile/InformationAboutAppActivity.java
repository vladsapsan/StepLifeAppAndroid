package com.StepLife.steplifeapp.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.StepLife.steplifeapp.R;

public class InformationAboutAppActivity extends AppCompatActivity {



    CardView UserAgreementButton;
    ImageView BackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_about_app);




        //Кнопка пользовательского соглашения
        UserAgreementButton = findViewById(R.id.UserAgreementButton);
        UserAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationAboutAppActivity.this, UserAgreement.class);
                startActivity(intent);
            }
        });

        BackBtn = findViewById(R.id.BacktoSettings);
        //Кнопка возвращения
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}