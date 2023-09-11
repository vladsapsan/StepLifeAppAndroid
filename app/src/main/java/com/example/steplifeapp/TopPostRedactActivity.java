package com.example.steplifeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TopPostRedactActivity extends AppCompatActivity {

    Button SaveTopPostRedactButton;
    ImageView imagebackEditTopPost;

    private String TopPost_Key ="TopPostArticle";
    private String Library_Key ="Lib";

    private DatabaseReference mDataBase;
    ProgressBar progressBar;

    TextView LastEditText;
    EditText NameTopPost,SecNameTopPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_post_redact);

        //Дефолт стиль
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        NameTopPost = findViewById(R.id.NameTopPost);
        SecNameTopPost = findViewById(R.id.SecNameTopPost);
        LastEditText = findViewById(R.id.LastEditText);
        progressBar = findViewById(R.id.progressBar);


        //закрытие окна
        imagebackEditTopPost = findViewById(R.id.imagebackEditTopPost);
        imagebackEditTopPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Сохранение изменений
        SaveTopPostRedactButton = findViewById(R.id.SaveTopPostRedactButton);
        SaveTopPostRedactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                imagebackEditTopPost.setVisibility(View.GONE);
                //Аунтефикация
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser cUser = mAuth.getCurrentUser();
                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                String Simpledate = df.format(Calendar.getInstance().getTime());
                mDataBase.child("Name").setValue(NameTopPost.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mDataBase.child("SecName").setValue(SecNameTopPost.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                mDataBase.child("LastEdit").setValue(Simpledate+" "+cUser.getPhoneNumber().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        imagebackEditTopPost.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }





    @Override
    public void onStart() {
        super.onStart();
        mDataBase = FirebaseDatabase.getInstance().getReference(Library_Key).child(TopPost_Key);
        //Получение данных из базы
        mDataBase.child("Name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        NameTopPost.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){
                        Log.e("Profile",e.toString());
                    }

                }
            }
        });

        //Получение данных из базы
        mDataBase.child("SecName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        SecNameTopPost.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){
                        Log.e("Profile",e.toString());
                    }

                }
            }
        });
        mDataBase.child("LastEdit").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Ошибка получения данных
                }
                else {
                    try {
                        //Данные получены
                        LastEditText.setText((String)task.getResult().getValue());
                    }
                    catch (Exception e){
                        Log.e("Profile",e.toString());
                    }

                }
            }
        });
    }
}