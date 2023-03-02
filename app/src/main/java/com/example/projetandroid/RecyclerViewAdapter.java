package com.example.projetandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    public final static String TYPE = "com.example.projetcoffee.TYPE";
    public final static String PRIX = "com.example.projetcoffee.PRIX";
    public final static String QUANTITE = "com.example.projetcoffee.QUANTITE";
    public final static String IMAGE = "com.example.projetcoffee.IMAGE";
    public final static String ID = "com.example.projetcoffee.ID";


    private ArrayList<Coffee> element_list = new ArrayList<>();
    private Context context;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;

    public RecyclerViewAdapter(Context context, ArrayList<Coffee> element_list) {
        this.element_list = element_list;
        this.context = context;
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        Log.d(TAG, " " + this.element_list.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        Log.v(TAG,"onCreateViewHolder : called !");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffeelist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int pos){
        Log.d(TAG, "onBindViewHolder: called !");
        Coffee coffee = element_list.get(pos);
        holder.boisson.setText(coffee.getType());
        holder.prix.setText(String.valueOf(coffee.getPrix())+" D.T");
        holder.img.setImageBitmap(coffee.getImage());
        holder.quantite.setText(String.valueOf(coffee.getQuantite()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("coffees").document(coffee.getDocID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    firebaseStorage.getReference("image/"+coffee.getType()+".jpg").delete();
                                    element_list.remove(element_list.get(pos));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "produit supprimer", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "erreur", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),UpdateCoffee.class);

               /* SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MyCoffee", view.getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TYPE,coffee.getType());
                editor.putFloat(PRIX, (float) coffee.getPrix());
                editor.putInt(QUANTITE,coffee.getQuantite());
                editor.commit();*/
                intent.putExtra(TYPE,coffee.getType());
                intent.putExtra(PRIX,coffee.getPrix());
                intent.putExtra(QUANTITE,coffee.getQuantite());
                intent.putExtra(ID,coffee.getDocID());
                context.startActivity(intent);


            }
        });



    }


    @Override
    public int getItemCount() {
        return element_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView boisson,prix,quantite;
        ImageView delete,update;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            boisson = itemView.findViewById(R.id.type);
            prix= itemView.findViewById(R.id.prixempty);
            quantite= itemView.findViewById(R.id.quantiteempty);
            img =itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
            update=itemView.findViewById(R.id.update);

        }


    }
}



