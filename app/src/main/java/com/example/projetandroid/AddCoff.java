package com.example.projetandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetandroid.databinding.ActivityAddCoffBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddCoff extends AppCompatActivity {


    ActivityAddCoffBinding binding;
    ImageView img;
    EditText type;
    EditText prix;
    EditText quantite;
    Uri imageUri;
    StorageReference storageRef ;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_coff);

        Intent intent =getIntent();
        img= findViewById(R.id.image);
        binding = ActivityAddCoffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        binding.addCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();

            }
        });

    }

    private void uploadImage() {

        img=findViewById(R.id.image);
        type = (EditText) findViewById(R.id.type);
        prix = (EditText) findViewById(R.id.prix);
        quantite =(EditText) findViewById(R.id.quantite);
        String fileName = type.getText().toString();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Téléchargement du photo .....");
        progressDialog.show();
        storageRef = FirebaseStorage.getInstance().getReference("image/"+fileName+".jpg");
        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        img.setImageURI(null);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String,Object> coffee = new HashMap<>();
                        coffee.put("type",fileName);
                        coffee.put("quantity",Integer.parseInt(quantite.getText().toString()));
                        coffee.put("img",FirebaseStorage.getInstance().getReference("image/"+fileName+".jpg").toString());
                        coffee.put("prix",Double.parseDouble(prix.getText().toString()));
                        db.collection("coffees").add(coffee);
                        Toast.makeText(AddCoff.this, "nouvelle caffée ajouter avec succée", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Intent intent=new Intent(AddCoff.this,Admin_Space.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(AddCoff.this, "erreur !!!", Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void selectImage() {

        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && data != null && data.getData() != null){
            img=findViewById(R.id.image);
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }
}