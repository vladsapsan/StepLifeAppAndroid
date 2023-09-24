package com.StepLife.steplifeapp.UserProfile;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.StepLife.steplifeapp.R;

public class UserAgreement extends AppCompatActivity {

    WebView UserAgreementWebView;
    final  static String UserAgreeementPdf = "https://drive.google.com/file/d/1174mfrMajd7NFw2Hg0_WzAH9n-MIq4Rk/view?usp=sharing";
    ImageView Backto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);


        //Дефолт стиль
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Пользовательское соглашение
        UserAgreementWebView = findViewById(R.id.UserAgreementWebView);
        UserAgreementWebView.getSettings().setJavaScriptEnabled(true);
        UserAgreementWebView.loadUrl(UserAgreeementPdf);


        //Кнопка зыкрытия
        Backto = findViewById(R.id.Backto);
        Backto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}