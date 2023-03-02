package com.example.projetandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetandroid.databinding.ActivitySendOtpBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Send_OTP extends AppCompatActivity {
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static String TAG="MAIN_TAG";
    private ActivitySendOtpBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySendOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.phoneLl.setVisibility(View.VISIBLE);
        binding.codeLll.setVisibility(View.GONE);
        firebaseAuth=FirebaseAuth.getInstance();
        pb=new ProgressDialog(this);
        pb.setTitle("Please wait...");
        pb.setCanceledOnTouchOutside(false);



        mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pb.dismiss();
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                super.onCodeSent(verificationId, forceResendingToken);

                mVerificationId=verificationId;
                forceResendingToken = token;
                pb.dismiss();
                binding.phoneLl.setVisibility(View.GONE);
                binding.codeLll.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"OTP code sent " ,Toast.LENGTH_SHORT).show();


            }
        };

        binding.BtngetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone= "+216" + binding.inputmobile.getText().toString().trim();
                    startPhoneNumberVerification(phone);
                }

        });
        binding.Receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone="+216" + binding.inputmobile.getText().toString().trim();
                if(TextUtils.isEmpty(phone))
                {
                    binding.inputmobile.setError("REQUIRED");
                    binding.inputmobile.requestFocus();
                }
                else{
                    resendVerificationCode(phone ,forceResendingToken);
                }
            }
        });
        binding.codesubmut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code =binding.codeEt.getText().toString().trim();
                if(TextUtils.isEmpty(code))
                {
                    binding.codeEt.setError("REQUIRED");
                    binding.codeEt.requestFocus();
                }
                else{
                    verifyPhoneNumberWithCode(mVerificationId,code);
                }


            }
        });
    }




    private void startPhoneNumberVerification(String phone) {
        pb.setMessage("Verifying Phone Number");
        pb.show();
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void resendVerificationCode(String phone,PhoneAuthProvider.ForceResendingToken token) {
        pb.setMessage("Resending Code");
        pb.show();
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }
    //verify the code entered by the ADMIN
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        pb.setMessage("Verifying Code");
        pb.show();

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pb.setMessage("Identity verified");
        pb.show();
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pb.dismiss();
                        String phone= firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(getApplicationContext(),"Logged in as"+phone,Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),Admin_Space.class);
                        startActivity(intent);
                    }
                });
    }
}