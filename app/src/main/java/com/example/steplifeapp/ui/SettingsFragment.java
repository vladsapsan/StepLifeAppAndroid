package com.example.steplifeapp.ui;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steplifeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private FirebaseAuth mAuth;
    Fragment AboutProgrammFragment;
    Fragment  ProfileSettingsFragment;
    ImageView BackBtn;
    CardView  ProfileBtn;
    private FrameLayout AboutProgrammBtn;
    private TextView Email;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();

        ProfileBtn = view.findViewById(R.id.ProfileSettingsEdit);
        AboutProgrammBtn = view.findViewById(R.id.SettingAboutProgramm);
        BackBtn = view.findViewById(R.id.BacktoProfile);
        Email = (TextView) view.findViewById(R.id.textViewEmailProfile);

                ProfileSettingsFragment = new ProfileSettingsFragment();
        //Кнопка возвращения в профиль
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("SettingsFragment",POP_BACK_STACK_INCLUSIVE);

            }
        });

        //Кнопка перехода в фрагмент о программе
        AboutProgrammBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                ft.addToBackStack("AboutProgramm");
                ft.add(R.id.UserProfileActivitiFrame,AboutProgrammFragment,"AboutProgramm").commit();
            }
        });
        //Редактор информации профиля
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
                ft.addToBackStack("ProfileSettings");
                ft.add(R.id.UserProfileActivitiFrame,ProfileSettingsFragment,"ProfileSettings").commit();
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
            Toast.makeText(getActivity(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
        }
    }

}