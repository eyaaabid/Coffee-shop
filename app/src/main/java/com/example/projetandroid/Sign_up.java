package com.example.projetandroid;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Sign_up extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText email,password,username;
    private TextView tvsignin ;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.emailuser);
        password=findViewById(R.id.password);
        tvsignin=findViewById(R.id.tvSignIn);
        username=findViewById(R.id.username);
        signup=findViewById(R.id.bSignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerclient();
            }
        });
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tosignin = new Intent(getApplicationContext(),Sign_in.class);
                startActivity(tosignin);
            }
        });
    }

    private void registerclient() {
        String email_firebase=email.getText().toString();
        String password_firebase=password.getText().toString();
        String username_firebase=username.getText().toString();
        // checking inputs entered by users such as emails , password ..
        if(!Patterns.EMAIL_ADDRESS.matcher(email_firebase).matches()){
            email.setError("Please verify your email");
            email.requestFocus();
            return;
        }
        if(password_firebase.length()<6){
            password.setError("Min password length should be 6 caracters ");
            password.requestFocus();
            return;
        }
        if(username_firebase.isEmpty()){
            username.setError("Email is required");
            username.requestFocus();
            return;
        }
        //create a map_obj to store user's info into firebase firestore :)
        Map<String, Object> User_info = new HashMap<>();
        User_info.put("Email", email_firebase);
        User_info.put("Password", password_firebase);
        User_info.put("User_name", username_firebase);
        User_info.put("IsSimpleUser","1");
        mAuth.createUserWithEmailAndPassword(email_firebase,password_firebase).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();
                    firestore.collection("Clients")
                            .document(uid)
                            .set(User_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(),Sign_in.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "smth went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }}
        });
    }
    }
