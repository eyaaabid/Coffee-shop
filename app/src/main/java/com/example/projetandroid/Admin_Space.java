package com.example.projetandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Admin_Space extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<Coffee> element_list ;
    RecyclerView recyclerView;
    FirebaseFirestore db ;
    RecyclerViewAdapter recyclerViewAdapter;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_space);
        Log.d(TAG, "on create : started");
        FloatingActionButton ajt = findViewById(R.id.ajouterbtn);
        ajt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddCoff.class);
                startActivity(intent);

            }
        });
        initList();
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        element_list = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(this,element_list);
        recyclerView.setAdapter(recyclerViewAdapter);





    }

    private void initList() {
        db = FirebaseFirestore.getInstance();
        db.collection("coffees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                String type = (String) document.getData().get("type");
                                Double prix = (Double) document.getData().get("prix");
                                Long quantite = (Long) document.getData().get("quantity");
                                Coffee coffee = new Coffee(type,prix, Math.toIntExact(quantite));
                                storageReference = FirebaseStorage.getInstance().getReference("image/"+type+".jpg");
                                try {
                                    File localfile= File.createTempFile("tempfile",".jpg");
                                    storageReference.getFile(localfile)
                                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                    coffee.setImage(bitmap);
                                                    element_list.add(coffee);
                                                    recyclerViewAdapter.notifyDataSetChanged();
                                                }
                                            });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                coffee.setDocID(document.getId());




                            }
                        }else {
                            Log.v(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });



    }

}