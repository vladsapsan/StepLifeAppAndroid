package com.StepLife.steplifeapp.ui;

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

import com.StepLife.steplifeapp.R;

public class DoneSingUp extends Fragment {

    private DoneSingUpViewModel mViewModel;
    ImageView BackBtn;

    public static DoneSingUp newInstance() {
        return new DoneSingUp();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_done_sing_up, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DoneSingUpViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Возвращение обратно
        BackBtn = view.findViewById(R.id.imagebackDoneRegistration);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate("DoneRegistration",POP_BACK_STACK_INCLUSIVE);

            }
        });
    }
}