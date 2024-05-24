package com.StepLife.steplifeapp.MainEnterenceActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.StepLife.steplifeapp.Model.Message;
import com.StepLife.steplifeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Helper extends AppCompatActivity {
    ImageView BacktoSettings;
    EditText MessegetextField,TelephonetextField,NametextField;
    public static final String AllMeseges_Key = "AllMeseges";
    CardView buttonStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        // Установка стиля безрамочного
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));

        NametextField = findViewById(R.id.NametextField);
        TelephonetextField = findViewById(R.id.TelephonetextField);
        MessegetextField = findViewById(R.id.MessegetextField);

        //Кнопка отправки сообщения
        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NametextField.getText().length()!=0){
                    if(TelephonetextField.getText().length()!=0){

                        if(MessegetextField.getText().length()!=0){
                            Message message = new Message(NametextField.getText().toString(),TelephonetextField.getText().toString(),MessegetextField.getText().toString(), Calendar.getInstance().getTime().toString());
                            FirebaseDatabase.getInstance().getReference().child(AllMeseges_Key).push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Сообщение отправлено",Toast.LENGTH_SHORT).show();

                                        Helper.this.finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Ошибка отправки",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Поле Сообщение не может быть пустым",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Поле Телефон не может быть пустым",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Поле Имя не может быть пустым",Toast.LENGTH_SHORT).show();
                }
            }
        });

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