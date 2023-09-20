package com.example.steplifeapp.UserProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.steplifeapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ProfileRedactActivity extends AppCompatActivity {


    private static final int SELECT_PICTURE = 1;
    ImageView BackBtn,ImageProfile;
    TextView ChoosePhotoText,CurrentPhoneNumber;
    StorageReference storageRef;
    ProgressBar progressBar;
    CardView TextViewSaveUserinformation;
    FirebaseStorage storage;
    EditText EditTextUserName;
    Uri UserProfilePhotoUri ;

    private FirebaseAuth mAuth;

    //загрузка фото
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                ImageProfile.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_redact);

        
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



        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("ImageProfiles");

        EditTextUserName = findViewById(R.id.editTextUserName);

        //Нажатие на фото и выбор фото
        ImageProfile = findViewById(R.id.ImageProfile);
        ImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImage();
            }
        });

        CurrentPhoneNumber = findViewById(R.id.CurrentPhoneNumber);
        progressBar = findViewById(R.id.progressBar);

        //Сохранение изменений кнопка
        TextViewSaveUserinformation = findViewById(R.id.TextViewSaveUserinformation);
        TextViewSaveUserinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserData();
            }
        });

        //Выбор фотографии по кнопке
        ChoosePhotoText  = findViewById(R.id.ChoosePhotoText);
        ChoosePhotoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImage();
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

        FireBaseSittingAuth();

    }


    //Загрузка информации профиля для редактирования
    private void FireBaseSittingAuth()
    {

        FirebaseUser cUser = mAuth.getCurrentUser();

        if(cUser!=null)
        {
            String name = cUser.getDisplayName();
            if(name!=null)
            {
                EditTextUserName.setText(name);
            }
            if(cUser.getPhoneNumber()!=null){
                CurrentPhoneNumber.setText(cUser.getPhoneNumber());
            }
            // Picasso.get().load(cUser.getPhotoUrl()).into(ImageProfile);
            Glide
                    .with(this)
                    .load(cUser.getPhotoUrl())
                    .into(ImageProfile);
        }
        else {
            Toast.makeText(getApplicationContext(), "Ошибка аутентификации пользователя",Toast.LENGTH_SHORT).show();

        }
    }

    //Функция обновления данных пользователя
    private void UpdateUserData()
    {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser!=null)
        {


            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(EditTextUserName.getText().toString())
                    .build();





            //Загрузка фото в базу
            if(ImageProfile.getDrawable()!=null) {
                Drawable photo = ImageProfile.getDrawable();
                Bitmap PhotoPreviewBitMap = ((BitmapDrawable)photo).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                //Оптимизация размера
                PhotoPreviewBitMap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] PhotoPreviewByteArray = baos.toByteArray();
                StorageReference MainPhotoRef = storageRef.child(cUser.getPhoneNumber() + "UserImage");


                //Передача фото
                UploadTask uploadTaskPhoto = MainPhotoRef.putBytes(PhotoPreviewByteArray);
                uploadTaskPhoto.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });

                Task<Uri> taskPhoto = uploadTaskPhoto.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return MainPhotoRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        UserProfilePhotoUri = task.getResult();
                        UserProfileChangeRequest PhotoProfileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(UserProfilePhotoUri)
                                .build();
                        //Фактическое Обновление данных в базе
                        cUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });

                        cUser.updateProfile(PhotoProfileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
            else {
                cUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Данные успешно обновлены",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }







        }
    }

    private void GetImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
}