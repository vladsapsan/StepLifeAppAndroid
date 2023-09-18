package com.example.steplifeapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.steplifeapp.R;
import com.example.steplifeapp.TelephoneSign.TelephoneSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;
    private FirebaseAuth mAuth;
    private EditText TextPhone,Password;
    private Button buttonAuth;

        TextView TextBtnRegistration;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);




    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextPhone = (EditText)view.findViewById(R.id.editTextEmail);
        Password = (EditText) view.findViewById(R.id.editTextTextPassword);

        //Аунтефикация
        mAuth = FirebaseAuth.getInstance();


        //Кнопка авторизации
        buttonAuth = (Button) view.findViewById(R.id.buttonAuth);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(TextPhone.getText().toString())&&!TextUtils.isEmpty(Password.getText().toString()))
                {
                    mAuth.signInWithEmailAndPassword(TextPhone.getText().toString(),Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user = mAuth.getCurrentUser();
                                boolean emailVerified = user.isEmailVerified();
                                if(emailVerified==false)
                                {
                                    Fragment fragment = new EmailVerificationFragment();
                                    FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                                    ft.setCustomAnimations(R.anim.slide_down, R.anim.slide_up,R.anim.slide_down, R.anim.slide_up);
                                    ft.addToBackStack("EmailVerification");
                                    ft.add(R.id.AuthorizationFrame,fragment,"EmailVerification").commit();

                                    BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
                                    bottomNavigationView.setVisibility(View.INVISIBLE);
                                }
                                else{

                                    Navigation.findNavController(v).navigate(R.id.userProfileFragment);
                                }


                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Неверны данные или ошибка входа",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



                }
            }
        });



       //кнопка регистрации на  экране авторизации
        TextBtnRegistration = (TextView) view.findViewById(R.id.RegistrationTextView);
        TextBtnRegistration.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


       // Fragment fragment = new RegistrationFragment();
       // FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
       // ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
       // ft.addToBackStack("ProfileFragment");
       // ft.add(R.id.AuthorizationFrame,fragment,"ProfileFragment").commit();

       // BottomNavigationView bottomNavigationView= getActivity().findViewById(R.id.nav_view);
       // bottomNavigationView.setVisibility(View.INVISIBLE);

      //  Fragment fragment = new PhoneSignFragment();
       // FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
       // ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_right,R.anim.slide_left, R.anim.slide_right);
      //  ft.addToBackStack("ProfileFragment");
        //  ft.add(R.id.AuthorizationFrame,fragment,"ProfileFragment").commit();

        Intent i = new Intent(getActivity(), TelephoneSignUp.class);
        startActivity(i);
        //переход на новый фрагмент


    }
}