package com.example.projetandroid.MVVM;



import com.example.projetandroid.Model.CoffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    List<CoffeeModel> coffeeModelList=new ArrayList<>();

    CoffeeList interfaceocfeelist;


    public Repository(CoffeeList interfaceocfeelist) {
        this.interfaceocfeelist = interfaceocfeelist;
    }

    public void getCoffee(){
        firebaseFirestore.collection("coffees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    coffeeModelList.clear();
                   for(DocumentSnapshot ds:task.getResult().getDocuments()){
                       CoffeeModel coffeeModel=ds.toObject(CoffeeModel.class);
                       coffeeModelList.add(coffeeModel);
                       interfaceocfeelist.coffeeLists(coffeeModelList);
                   }
                }

            }
        });

    }
    public interface CoffeeList{
       void coffeeLists(List<CoffeeModel>coffeeModels);
    }

}
