package com.example.steplifeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.steplifeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.rpc.Code;

import java.util.concurrent.TimeUnit;

public class OtpTelephoneCheckActiviti extends AppCompatActivity {

    final String OTPPIN_KEY = "OTPPIN";
    String VerificationID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_telephone_check_activiti);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        getWindow().setStatusBarColor(getResources().getColor(R.color.MainGray));
        getSupportActionBar().hide(); //Скрытие actionBar

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        mAuth = FirebaseAuth.getInstance();

        Bundle arguments = getIntent().getExtras();
        String Phone = arguments.get(OTPPIN_KEY).toString();


        final PinView pinView = findViewById(R.id.OTPPinView);



       Button RegistrationBtn = findViewById(R.id.OTPPINbuttonRegistration);
        RegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyPinCode(pinView.getText().toString());
            }
        });



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                final String PinAuth = credential.getSmsCode();
                if(PinAuth!= null)
                {
                    VerifyPinCode(pinView.getText().toString());
                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(OtpTelephoneCheckActiviti.this,e.toString(), Toast.LENGTH_LONG).show();
            }



            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token)
            {
               super.onCodeSent(verificationId,token);
                VerificationID = verificationId;
            }


        };

        SendOtpPin(Phone);

    }

    void SendOtpPin(String PhoneNumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+7"+PhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    void VerifyPinCode(String PinCode)
    {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationID, PinCode);
        SingUpbyCredential(credential);
    }

    private void SingUpbyCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(OtpTelephoneCheckActiviti.this, User_ProfileActiviti.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
    }


}