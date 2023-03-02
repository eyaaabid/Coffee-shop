package com.example.projetandroid.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.projetandroid.AllCoffeeListFragment;
import com.example.projetandroid.Model.CoffeeModel;
import com.example.projetandroid.R;

import java.util.List;

public class CoffeeAdapter  extends RecyclerView.Adapter<CoffeeAdapter.CoffeeListHolder> {

 List<CoffeeModel> coffeeModelList;
 GetOneCoffee interfacegetCoffee;

 public CoffeeAdapter(GetOneCoffee interfacegetCoffee) {
  this.interfacegetCoffee = interfacegetCoffee;
 }

 @Override
 public CoffeeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffeeliststyle, parent, false);
  return new CoffeeListHolder(view);
 }

 @Override
 public void onBindViewHolder(CoffeeListHolder holder, int position) {
  holder.coffeename.setText(coffeeModelList.get(position).getCoffeename());
  holder.description.setText(coffeeModelList.get(position).getDescription());

  Glide.with(holder.itemView.getContext()).load(coffeeModelList.get(position).getImageURL()).into(holder.iamgeView);
 }

 @Override
 public int getItemCount() {
  return coffeeModelList.size();
 }



 public void setCoffeeModelList(List<CoffeeModel> coffeeModelList){
  this.coffeeModelList = coffeeModelList;
  GetOneCoffee interfacegetCoffee;


 }
 class CoffeeListHolder extends ViewHolder implements View.OnClickListener{
  TextView coffeename, description;
  ImageView iamgeView;

  public CoffeeListHolder( View itemView) {
   super(itemView);
   coffeename = itemView.findViewById(R.id.coffeeName);
   description = itemView.findViewById(R.id.coffeedetail);
   iamgeView = itemView.findViewById(R.id.coffeeImage);

   coffeename.setOnClickListener(this);
   description.setOnClickListener(this);
   iamgeView.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
   interfacegetCoffee.clickedCoffee(getAdapterPosition(), coffeeModelList);
  }
 }

 public interface GetOneCoffee{
  void clickedCoffee(int position,List<CoffeeModel> coffeeModels);
 }
}
