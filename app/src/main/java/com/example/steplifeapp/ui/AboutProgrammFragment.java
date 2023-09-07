package com.example.steplifeapp.ui;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.steplifeapp.EditArticlesActiviti;
import com.example.steplifeapp.MainActivity;
import com.example.steplifeapp.R;
import com.example.steplifeapp.UserAgreement;
import com.example.steplifeapp.User_ProfileActiviti;

public class AboutProgrammFragment extends Fragment {

    private AboutProgrammViewModel mViewModel;
    CardView UserAgreementButton;
    ImageView BackBtn;

    public static AboutProgrammFragment newInstance() {
        return new AboutProgrammFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_programm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Кнопка пользовательского соглашения
        UserAgreementButton = view.findViewById(R.id.UserAgreementButton);
        UserAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserAgreement.class);
                startActivity(intent);
            }
        });

        BackBtn = view.findViewById(R.id.BacktoSettings);
        //Кнопка возвращения
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("AboutProgramm",POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutProgrammViewModel.class);
        // TODO: Use the ViewModel
    }

}