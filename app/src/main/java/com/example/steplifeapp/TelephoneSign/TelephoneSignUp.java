package com.example.steplifeapp.TelephoneSign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.steplifeapp.R;
import com.example.steplifeapp.UserProfile.UserAgreement;
import com.example.steplifeapp.other.NetworkChangeListner;
import com.example.steplifeapp.ui.RegistrationViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;
import java.util.regex.Pattern;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class TelephoneSignUp extends AppCompatActivity {

    private RegistrationViewModel mViewModel;
    private DatabaseReference DataBase;
    private String User_Key = "Users";
    final String OTPPIN_KEY = "OTPPIN";
    TextView UserAgreementButton;
    NetworkChangeListner networkChangeListner;
    Button RegistrationBtn;
    ImageView BackBtn;
    EditText Phone;
    EditText Password,RepitePassword;
    private FirebaseAuth mAuth;
    CheckBox UsersRules;
    private MaskedEditText Telephone_input = null;
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


    public static boolean IsSucsesReg(String pass, String passrepite, CharSequence email, CheckBox UsersRules){
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        setContentView(R.layout.activity_telephone_sign_up);

        mAuth = FirebaseAuth.getInstance();

        BackBtn = findViewById(R.id.BacktoProfileAuthorization);



        RegistrationBtn = findViewById(R.id.buttonRegistration);

        Telephone_input = findViewById(R.id.phone_input);




        //Пользовательское соглашение
        UserAgreementButton = findViewById(R.id.UserAgreementButton);
        UserAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelephoneSignUp.this, UserAgreement.class);
                startActivity(intent);
            }
        });


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        //Переход в новое окно с вводом пин-кода для аутентификации
        RegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Telephone_input.getRawText().length()==10)
                {
                    Intent intent = new Intent(TelephoneSignUp.this, OtpTelephoneCheckActiviti.class);
                    intent.putExtra(OTPPIN_KEY, Telephone_input.getRawText());
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(TelephoneSignUp.this,"Номер введен не верно", Toast.LENGTH_SHORT).show();
               }


                }


        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}









