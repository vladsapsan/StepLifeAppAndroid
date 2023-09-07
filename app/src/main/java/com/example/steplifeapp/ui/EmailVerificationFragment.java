package com.example.steplifeapp.ui;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.steplifeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmailVerificationFragment extends Fragment {

    private EmailVerificationViewModel mViewModel;
    ImageView BackBtn;

    public static EmailVerificationFragment newInstance() {
        return new EmailVerificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Возвращение обратно
        BackBtn = view.findViewById(R.id.imagebackEmailVerif);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("EmailVerification",POP_BACK_STACK_INCLUSIVE);
                BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EmailVerificationViewModel.class);

    }

}