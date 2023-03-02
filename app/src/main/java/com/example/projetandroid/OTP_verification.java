package com.example.projetandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP_verification extends AppCompatActivity {
EditText phonenumber ;
Button getotp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        phonenumber= findViewById(R.id.inputmobile);
        getotp=findViewById(R.id.Send_otp);
       getotp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (phonenumber.getText().toString().isEmpty())
               {
                   Toast.makeText(OTP_verification.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                   return;
               }
               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       "+216"+phonenumber.getText().toString(), 60, TimeUnit.SECONDS,
           OTP_verification.this,
           new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                           @Override
                           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                           }

                           @Override
                           public void onVerificationFailed(@NonNull FirebaseException e) {
                               Toast.makeText(OTP_verification.this, "FAILED", Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                               super.onCodeSent(verificationID, forceResendingToken);
                               Intent gotosendotp = new Intent(getApplicationContext(),Send_OTP.class);
                                gotosendotp.putExtra("verificationid " , verificationID );

                               startActivity(gotosendotp);

                           }
                       }
               );

           }
       });
    }
}