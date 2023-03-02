package com.example.projetandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class UpdateCoffee extends AppCompatActivity {

    private static final String TAG = "updateActivity";
    ImageView img;
    Uri imageUri;
    StorageReference storageRef ;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String fileName="";
    String id;
    FirebaseFirestore db;
    Intent intent =getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      /*  getSupportActionBar().setTitle("Mettre a jour Un Café");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brown)));*/
        setContentView(R.layout.activity_update_coffee);
        db= FirebaseFirestore.getInstance();
        Intent intent =getIntent();
        String id = intent.getStringExtra(RecyclerViewAdapter.ID);
        String type = intent.getStringExtra(RecyclerViewAdapter.TYPE);
        double prix =intent.getDoubleExtra(RecyclerViewAdapter.PRIX,0);
        int quantite = intent.getIntExtra(RecyclerViewAdapter.QUANTITE,0);
        ImageView img = findViewById(R.id.image);
        storageReference = FirebaseStorage.getInstance().getReference("image/"+type+".jpg");
        try {
            File localFile = File.createTempFile("images","jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    ImageView img = findViewById(R.id.image);
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    img.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"id = "+id);
        fileName = type;
        EditText E_type = findViewById(R.id.type);
        EditText E_prix = findViewById(R.id.prix);
        EditText E_quantite = findViewById(R.id.quantite);
        Button ajt_img = findViewById(R.id.changeImage);
        Button update = findViewById(R.id.updateCoffee);
        E_type.setText(type);
        E_prix.setText(Double.toString(prix));
        E_quantite.setText(Integer.toString(quantite));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference= db.collection("coffees").document(id);
        Log.v(TAG,"doc ="+ documentReference.getId().toString());
        Log.v(TAG,"fileName = "+fileName);
        storageRef = FirebaseStorage.getInstance().getReference("image/"+fileName+".jpg");
        storageRef.delete();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();

            }
        });
        ajt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "upadate : " + id);
                storageReference = FirebaseStorage.getInstance().getReference("image/" + E_type.getText().toString() + ".jpg");
                storageReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                img.setImageURI(null);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = db.collection("coffees").document(id);
                                documentReference.update("type", E_type.getText().toString(),
                                                "prix", Double.parseDouble(E_prix.getText().toString()),
                                                "quantity", Integer.parseInt(E_quantite.getText().toString()),
                                                "img", "ddd")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(UpdateCoffee.this, "Café a jour !", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(UpdateCoffee.this,Admin_Space.class);
                                                startActivity(intent);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UpdateCoffee.this, "erreur !", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        });
            }
        });

    }

    private void updateCoffee() {

        img=findViewById(R.id.image);
        EditText  E_type = (EditText) findViewById(R.id.type);
        EditText E_prix = (EditText) findViewById(R.id.prix);
        EditText E_quantite =(EditText) findViewById(R.id.quantite);

        Log.v(TAG,"type ="+E_type.getText().toString());
        Log.v(TAG,"prix ="+E_prix.getText().toString());
        Log.v(TAG,"quantite ="+ E_quantite.getText().toString());

        this.id =intent.getStringExtra(RecyclerViewAdapter.ID);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Téléchargement du photo .....");
        progressDialog.show();

     /*   storageReference = FirebaseStorage.getInstance().getReference("image/"+E_type.getText().toString()+".jpg");
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {*/
        Log.v(TAG,"LOL");
        //         img.setImageURI(null);
        Coffee coffee = new Coffee(E_type.getText().toString(),Double.parseDouble(E_prix.getText().toString()),Integer.parseInt(E_quantite.getText().toString()));
        //   coffee.setImage(imageView.);
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference=   db.collection("coffees").document(this.id);
        documentReference.update("type",E_type.getText().toString(),
                        "prix",Double.parseDouble(E_prix.getText().toString()),
                        "quantity",Integer.parseInt(E_quantite.getText().toString()),
                        "img","ddd")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateCoffee.this, "Café a jour !", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateCoffee.this, "erreur !", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
        //         }
        //       });



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