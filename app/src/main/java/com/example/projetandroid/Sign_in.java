package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Sign_in extends AppCompatActivity {
    private EditText pass,email;
    private Button btn_signin;
    private TextView forget , signup  ;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ArrayList<Clients> clients = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn_signin=findViewById(R.id.sign_in);
        forget = findViewById(R.id.forgetpassword);
        signup=findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tosignup = new Intent(getApplicationContext(),Sign_up.class);
                startActivity(tosignup);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toforget = new Intent(getApplicationContext(),Forget_password.class);
                startActivity(toforget);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        pass=findViewById(R.id.editextpassword);
        email=findViewById(R.id.editextemail) ;
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final String emailfirebase = email.getText().toString();
            final String passwordfirebase = pass.getText().toString();
            if (passwordfirebase.isEmpty()|| emailfirebase.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Verify your personal data", Toast.LENGTH_SHORT).show();
            }

                mAuth.signInWithEmailAndPassword(emailfirebase,passwordfirebase).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        CheckUserAccesLevel(authResult.getUser().getUid());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),"Please verify your email or your password",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
        private void CheckUserAccesLevel(String uid) {
        DocumentReference df= firestore.collection("Clients").document(uid);
        //extract the data from the documennt
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //the document snapshot contains t he current data of the current user
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","OnSuccess : " +documentSnapshot.getData());
                //identity of the user acess
                if(documentSnapshot.getString("IsSimpleUser") != null){
                    //simple user
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                if(documentSnapshot.getString("IsAdmin") != null) {
                    //go to admin space
                    startActivity(new Intent(getApplicationContext(),Admin_Space.class));
                    finish();
                }

            }
        });
    }
}