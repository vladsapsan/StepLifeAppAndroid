package com.example.steplifeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.steplifeapp.ui.AddArticleFragment;
import com.example.steplifeapp.ui.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class User_ProfileActiviti extends AppCompatActivity {

    ImageView BackBtn;
    Button LogOutButton;
    Fragment Addarticleragment ;
    Fragment SettingsFragment;
    FrameLayout AddArticleFrameButton,RedactArticleFrameButton,SettingsFrameButton;
    ScrollView UserProfilescrollView;
    private FirebaseAuth mAuth;
    TextView Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_activiti);


        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();

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


        //Инициализация фрагментов
        Addarticleragment = new AddArticleFragment();
        SettingsFragment = new SettingsFragment();



        //Кнопка добавления статьи
        AddArticleFrameButton = findViewById(R.id.AddArticleFrameButton);
        AddArticleFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = (getSupportFragmentManager().beginTransaction());
                ft.setCustomAnimations(R.anim.slide_down, R.anim.slide_up,R.anim.slide_down, R.anim.slide_up);
                ft.addToBackStack("AddArticle");
                ft.add(R.id.UserProfileActivitiFrame,Addarticleragment,"AddArticle").commit();
            }
        });
        //Кнопка перехода в настройки
        SettingsFrameButton = findViewById(R.id.SettingsFrameButton);
        SettingsFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = (getSupportFragmentManager().beginTransaction());
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                ft.addToBackStack(null);
                ft.add(R.id.UserProfileActivitiFrame,SettingsFragment,"SettingsFragment").commit();
            }
        });

        //Редактирование статей
        RedactArticleFrameButton = findViewById(R.id.RedactArticleFrameButton);
        RedactArticleFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_ProfileActiviti.this,EditArticlesActiviti.class);
                startActivity(intent);
            }
        });

        //Телефон текст
        Phone = findViewById(R.id.textViewEmailProfile);

        //Кнопка выхода из профиля
        LogOutButton = findViewById(R.id.ExitProfileButton);
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });


        //Scrollview
        UserProfilescrollView = findViewById(R.id.UserprofileScrollView);
        OverScrollDecoratorHelper.setUpOverScroll(UserProfilescrollView);

        //Кнопка возвращения
        BackBtn = (ImageView) findViewById(R.id.CLosetoProfile);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                Phone.setText(name);
            }
            else {
                Phone.setText(phoneNumber);
            }

        }
    }
}