package com.StepLife.steplifeapp.TelephoneSign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.StepLife.steplifeapp.R;
import com.StepLife.steplifeapp.UserProfile.User_ProfileActiviti;
import com.StepLife.steplifeapp.other.NetworkChangeListner;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class OtpTelephoneCheckActiviti extends AppCompatActivity {

    final String OTPPIN_KEY = "OTPPIN";
    String VerificationID;
    String Phone;
    private DatabaseReference mDataBase;
    NetworkChangeListner networkChangeListner;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_telephone_check_activiti);



        mAuth = FirebaseAuth.getInstance();

        Bundle arguments = getIntent().getExtras();
        Phone = arguments.get(OTPPIN_KEY).toString();


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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}