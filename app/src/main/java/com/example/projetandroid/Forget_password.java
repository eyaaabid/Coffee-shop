package com.example.projetandroid;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_password extends AppCompatActivity {
    Button reset_btn,back_btn;
    FirebaseAuth auth;
    private TextView text_provide;
    private EditText email_reset ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        text_provide=findViewById(R.id.provide_text);
        auth = FirebaseAuth.getInstance();
        email_reset=findViewById(R.id.email_reset);
        reset_btn=findViewById(R.id.reset_btn);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    public void resetPassword(){
        String email = email_reset.getText().toString().trim();
        if(email.isEmpty()){
            email_reset.setError("Email required");
            email_reset.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_reset.setError("Please verify your email");
            email_reset.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Check your gmail to reset your password ",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Try again! Something wrong happened ! ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}