package com.StepLife.steplifeapp.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.StepLife.steplifeapp.Helper;
import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.StafFunction.AddNewArticle;
import com.StepLife.steplifeapp.StafFunction.ArticleOnTopSettingsActivity;
import com.StepLife.steplifeapp.StafFunction.EditArticlesActiviti;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.StepLife.steplifeapp.ui.AddArticleFragment;
import com.StepLife.steplifeapp.ui.SettingsFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class User_ProfileActiviti extends AppCompatActivity {

    ImageView BackBtn;
    Button LogOutButton,buttonHelp;
    List<String> EditorsPhoneList;
    final String EDITORS = "AllEditors";
    Fragment Addarticleragment ;
    private DatabaseReference mDataBase;
    ImageView ImageProfile;
    NetworkChangeListner networkChangeListner;
    int USER_ROLE = 0;
    Fragment SettingsFragment;
    CardView ProfileUserCard;
    FrameLayout AddArticleFrameButton,RedactArticleFrameButton,SettingsFrameButton,ArticleChooseFrameButton;
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



        Intent EditArticlesActivitiintent = new Intent(getApplicationContext(), EditArticlesActiviti.class);
        Intent AddNewArticleiintent = new Intent(getApplicationContext(), AddNewArticle.class);

        //Инициализация фрагментов
        Addarticleragment = new AddArticleFragment();
        SettingsFragment = new SettingsFragment();

        ImageProfile = findViewById(R.id.ImageProfile);

        //Карточка профиля
        ProfileUserCard = findViewById(R.id.ProfileUserCard);
        ProfileUserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSetting = new Intent(User_ProfileActiviti.this, ProfileRedactActivity.class);
                startActivity(intentSetting);
            }
        });

        //Кнопка получения помощи
        buttonHelp = findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_ProfileActiviti.this, Helper.class));
            }
        });

        //Кнопка добавления статьи
        AddArticleFrameButton = findViewById(R.id.AddArticleFrameButton);
        AddArticleFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddNewArticleiintent);
            }
        });
        //Кнопка перехода в настройки
        SettingsFrameButton = findViewById(R.id.SettingsFrameButton);

        SettingsFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSetting = new Intent(User_ProfileActiviti.this, MainSettingsActivity.class);
                startActivity(intentSetting);
            }
        });

        //Редактирование статей
        RedactArticleFrameButton = findViewById(R.id.RedactArticleFrameButton);
        RedactArticleFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(EditArticlesActivitiintent);
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




        //Настройка первой полосы статей
        ArticleChooseFrameButton = findViewById(R.id.ArticleChooseFrameButton);
        ArticleChooseFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_ProfileActiviti.this, ArticleOnTopSettingsActivity.class);
                startActivity(intent);
            }
        });
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
        //Данные аутентификации
        if(cUser!=null)
        {
            String name = cUser.getDisplayName();
            String phoneNumber = cUser.getPhoneNumber();

            if(cUser.getPhotoUrl()!=null) {
                //   Загрузка фото
                //Picasso.get().load(cUser.getPhotoUrl()).into(ImageProfile);
                Glide
                        .with(this)
                        .load(cUser.getPhotoUrl())
                        .into(ImageProfile);
            }
            if(name!=null)
            {
                Phone.setText(name);
            }
            else {
                Phone.setText(phoneNumber);
            }

        }

        //Сверка данных сотрудников из базы с текущим пользователем для предоставления доступа к созданию статей
        mDataBase = FirebaseDatabase.getInstance().getReference(EDITORS);
        mDataBase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        EditorsPhoneList = (List<String>) task.getResult().getValue();
                        String phoneNumber = cUser.getPhoneNumber();
                        Log.e("Profile",phoneNumber);
                        for (String phone : EditorsPhoneList) {
                            if (phoneNumber.equals(phone)) {
                                USER_ROLE = 1;
                                ArticleChooseFrameButton.setVisibility(View.VISIBLE);
                                AddArticleFrameButton.setVisibility(View.VISIBLE);
                                RedactArticleFrameButton.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                    catch (Exception e){
                        Log.e("Profile",e.toString());
                    }

                }
            }
        });

    }



    @Override
    protected void onStop() {
        super.onStop();

    }
}