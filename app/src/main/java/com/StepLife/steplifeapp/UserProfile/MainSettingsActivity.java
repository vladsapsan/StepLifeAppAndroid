package com.StepLife.steplifeapp.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.ui.ProfileSettingsFragment;
import com.StepLife.steplifeapp.ui.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainSettingsActivity extends AppCompatActivity {


    private SettingsViewModel mViewModel;
    private FirebaseAuth mAuth;
    Fragment AboutProgrammFragment;
    Fragment  ProfileSettingsFragment;
    ImageView BackBtn;
    CardView ProfileBtn;
    private FrameLayout AboutProgrammBtn;
    private TextView Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);


        // Установка стиля безрамочного
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();

        ProfileBtn = findViewById(R.id.ProfileSettingsEdit);
        AboutProgrammBtn = findViewById(R.id.SettingAboutProgramm);
        BackBtn = findViewById(R.id.BacktoProfile);
        Email = (TextView) findViewById(R.id.textViewEmailProfile);


        ProfileSettingsFragment = new ProfileSettingsFragment();
        //Кнопка возвращения в профиль
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Кнопка перехода в окно о приложении
        AboutProgrammBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainSettingsActivity.this, InformationAboutAppActivity.class);
                startActivity(intent);
            }
        });
        //Редактор информации профиля
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainSettingsActivity.this, ProfileRedactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser!=null)
        {
            String name = cUser.getDisplayName();
            String phoneNumber = cUser.getPhoneNumber();
            if(name!=null)
            {
                Email.setText(name);
            }
            else {
                Email.setText(phoneNumber);
            }
        }
        else
        {
            Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
        }
    }
}