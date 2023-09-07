package com.example.steplifeapp.ui;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static androidx.navigation.fragment.FragmentKt.findNavController;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.steplifeapp.R;
import com.example.steplifeapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {

    private RegistrationViewModel mViewModel;
    private DatabaseReference  DataBase;
    private String User_Key = "Users";
    Button RegistrationBtn;
    ImageView BackBtn;
    EditText Phone;
    EditText Password,RepitePassword;
    private FirebaseAuth mAuth;
    CheckBox UsersRules;


    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        // TODO: Use the ViewModel
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    public final static boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }


    public static boolean IsSucsesReg(String pass,String passrepite,CharSequence email,CheckBox UsersRules){
        if(isValidPassword(pass))
        {
            if(isValidEmail(email))
            {
                if (UsersRules.isChecked())
                {
                    if (Objects.equals(pass, passrepite))
                    {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        BackBtn = view.findViewById(R.id.BacktoProfileAuthorization);

        UsersRules = view.findViewById(R.id.checkBoxUsersRules);
        RepitePassword  = view.findViewById(R.id.editTextTextRepitePassword);
        RegistrationBtn = view.findViewById(R.id.buttonRegistration);
        Phone = view.findViewById(R.id.editTextPhone);
        Password = view.findViewById(R.id.editTextTextPassword);



        //Переход
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("ProfileFragment",POP_BACK_STACK_INCLUSIVE);
                BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });



        //Кнопка регистрации
        RegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(Phone.getText().toString())&&!TextUtils.isEmpty(Password.getText().toString())) {

                    if (IsSucsesReg(Password.getText().toString(), RepitePassword.getText().toString(), Phone.getText(),UsersRules)) {

                    mAuth.createUserWithEmailAndPassword(Phone.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Navigation.findNavController(v).navigate(R.id.navigation_profile);
                                                    Fragment fragment = new DoneSingUp();
                                                    FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                                                    ft.setCustomAnimations(R.anim.slide_down, R.anim.slide_up, R.anim.slide_down, R.anim.slide_up);
                                                    ft.addToBackStack("DoneRegistration");
                                                    ft.add(R.id.AuthorizationFrame, fragment, "DoneRegistration").commit();

                                                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                                                    bottomNavigationView.setVisibility(View.VISIBLE);

                                                }
                                            }
                                        });

                            } else {
                                Toast.makeText(getActivity(), "ошибка регистрации", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                    else
                    {
                        Toast.makeText(getActivity(), "Неверные данные", Toast.LENGTH_SHORT).show();
                    }
                }











            }
        });

    }
}