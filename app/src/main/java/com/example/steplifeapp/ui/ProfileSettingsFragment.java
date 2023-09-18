package com.example.steplifeapp.ui;

import static android.app.Activity.RESULT_OK;
import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steplifeapp.MainActivity;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ProfileSettingsFragment extends Fragment {

    private ProfileSettingsViewModel mViewModel;
    private static final int SELECT_PICTURE = 1;
    ImageView BackBtn,ImageProfile;
    TextView TextViewSaveUserinformation,ChoosePhotoText;
    StorageReference storageRef;
    FirebaseStorage storage;
    EditText EditTextUserName;
    Uri UserProfilePhotoUri ;

    private FirebaseAuth mAuth;

    public static ProfileSettingsFragment newInstance() {
        return new ProfileSettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileSettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("ImageProfiles");

        EditTextUserName = view.findViewById(R.id.editTextUserName);
        ImageProfile = view.findViewById(R.id.ImageProfile);

        //Сохранение изменений кнопка
        TextViewSaveUserinformation = view.findViewById(R.id.TextViewSaveUserinformation);
        TextViewSaveUserinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserData();
            }
        });

        //Выбор фотографии
        ChoosePhotoText  = view.findViewById(R.id.ChoosePhotoText);
        ChoosePhotoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImage();
            }
        });

        BackBtn = view.findViewById(R.id.BacktoSettings);
        //Кнопка возвращения
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("ProfileSettings",POP_BACK_STACK_INCLUSIVE);
            }
        });


        FireBaseSittingAuth();
    }

    //Загрузка информации профиля для редактирования
    private void FireBaseSittingAuth()
    {
        FirebaseUser cUser = mAuth.getCurrentUser();
        Uri Photo = Uri.parse("https://firebasestorage.googleapis.com/v0/b/steplife1.appspot.com/o/ImageProfiles%2F%2B79151434484UserImage?alt=media&token=de789951-29a2-442f-ab22-db9571207fb4");
        if(cUser!=null)
        {
            String name = cUser.getDisplayName();
            Uri UriPhoto = cUser.getPhotoUrl();

            if(name!=null)
            {
                EditTextUserName.setText(name);
            }

                Picasso.get()
                        .load(cUser.getPhotoUrl())
                        .into(ImageProfile, new Callback() {
                            @Override
                            public void onSuccess() {

                            }
                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getActivity(), "Ошибка загрузки фото профиля",Toast.LENGTH_SHORT).show();
                            }
                        });


        }
        else {
            Toast.makeText(getActivity(), "Ошибка аутентификации пользователя",Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStackImmediate("ProfileSettings",POP_BACK_STACK_INCLUSIVE);
        }
    }

    //Функция обновления данных пользователя
    private void UpdateUserData()
    {
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser!=null)
        {


            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(EditTextUserName.getText().toString())
                    .build();


            UserProfileChangeRequest PhotoProfileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(UserProfilePhotoUri)
                    .build();


            //Загрузка фото в базу
            if(ImageProfile.getDrawable()!=null) {
                Drawable photo = ImageProfile.getDrawable();
                Bitmap PhotoPreviewBitMap = ((BitmapDrawable)photo).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PhotoPreviewBitMap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] PhotoPreviewByteArray = baos.toByteArray();
                StorageReference MainPhotoRef = storageRef.child(cUser.getPhoneNumber() + "UserImage");

                UploadTask uploadTaskPhoto = MainPhotoRef.putBytes(PhotoPreviewByteArray);
                uploadTaskPhoto.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
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
                        //Фактическое Обновление данных в базе
                        cUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Данные успешно обновлены",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        cUser.updateProfile(PhotoProfileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Данные успешно обновлены",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.toString(),Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getActivity(), "Данные успешно обновлены",Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        ImageProfile.setImageURI(selectedImageUri);

                    }
            }
        }
    }



}