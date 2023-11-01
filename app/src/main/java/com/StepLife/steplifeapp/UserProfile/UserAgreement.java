package com.StepLife.steplifeapp.UserProfile;

import android.os.Bundle;
import android.view.View;
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