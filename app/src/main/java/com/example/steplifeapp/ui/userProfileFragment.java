package com.example.steplifeapp.ui;

import static android.app.Activity.RESULT_OK;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.steplifeapp.R;
import com.example.steplifeapp.TelephoneSign.TelephoneSignUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class userProfileFragment extends Fragment {

    private static final int SELECT_PICTURE = 1;
    private UserProfileViewModel mViewModel;
    private FirebaseAuth mAuth;
    private TextView Email,ChoosePhoto;

    Button StartEditorFragmentBtn;


    private ImageView ImageProfile;

    public static userProfileFragment newInstance() {
        return new userProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
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
    //загрузка фото
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                ImageProfile.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();

        ImageProfile = view.findViewById(R.id.ImageProfile);





        //Плашка с sheetDialog
        ImageView UserSettingsSheet = view.findViewById(R.id.ButtonSettingProfile);
        UserSettingsSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialog);
                bottomSheetDialog.setDismissWithAnimation(true);
                View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(
                                R.layout.sheetprofilesettings,
                                (FrameLayout) view.findViewById(R.id.SheetDialogSettingsContainer)
                                );
                //Выход из профиля
                bottomSheetView.findViewById(R.id.ExitFrameButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        mAuth.signOut();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });

                //кнопка создания новой статьи в учебнике
                bottomSheetView.findViewById(R.id.AddArticleFrameButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Fragment fragment = new AddArticleFragment();
                        FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.anim.slide_down, R.anim.slide_up,R.anim.slide_down, R.anim.slide_up);
                        ft.addToBackStack("AddArticle");

                        ft.add(R.id.UserProfileFrame,fragment,"AddArticle").commit();
                        BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
                        bottomNavigationView.setVisibility(View.INVISIBLE);
                    }
                });
                //Переход в настройки
                bottomSheetView.findViewById(R.id.SettingsFrameBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Fragment fragment = new SettingsFragment();
                        FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                        ft.addToBackStack("SettingsFragment");
                        ft.add(R.id.UserProfileFrame,fragment,"SettingsFragment").commitAllowingStateLoss();
                        BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
                        bottomNavigationView.setVisibility(View.INVISIBLE);

                    }
                });
                //Переход к редактированию статей в учебнике
                bottomSheetView.findViewById(R.id.RedactArticleFrameButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.show();

            }
        });
        Email = (TextView) view.findViewById(R.id.textViewEmailProfile);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser!=null)
        {
            String name = cUser.getDisplayName();
            String email = cUser.getPhoneNumber();

           // boolean emailVerified = cUser.isEmailVerified();
            if(email!=null) {
                Email.setText(email);
            }

        }
        else
        {
            Intent intent = new Intent(getActivity(), TelephoneSignUp.class);
            startActivity(intent);

        }
    }
}