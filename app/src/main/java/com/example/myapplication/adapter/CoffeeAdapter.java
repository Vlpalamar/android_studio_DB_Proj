package com.example.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Coffee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder> {

    int CoffeeType;
    Context context;
    List<Coffee> coffeeList;
    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View coffeeItems= LayoutInflater.from(context).inflate(R.layout.coffee_item, parent, false);
        return new CoffeeViewHolder(coffeeItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {
        holder.coffeeName.setText(coffeeList.get(position).getName());
        holder.coffeeDescription.setText(coffeeList.get(position).getDescription());
        holder.coffeePrice.setText(String.valueOf(coffeeList.get(position).getCost()));

        Picasso.get().load(coffeeList.get(position).getImgPath()).into( holder.coffeeImg);

    }

    public CoffeeAdapter(Context context, List<Coffee> coffeeList) {
        this.context = context;
        this.coffeeList = coffeeList;
    }

    @Override
    public int getItemCount() {
        return coffeeList.size();
    }

    public static final   class CoffeeViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton coffeeImg;
        TextView coffeeName;
        TextView coffeeDescription;
        TextView coffeePrice;


        public CoffeeViewHolder(@NonNull View itemView) {
            super(itemView);

            coffeeImg= itemView.findViewById(R.id.ExampleСoffeePhoto);
            coffeeName= itemView.findViewById(R.id.ExampleСoffeeName);
            coffeeDescription= itemView.findViewById(R.id.ExampleCoffeeDescription);
            coffeePrice= itemView.findViewById(R.id.ExampleCoffeePrice);
        }



    }



}
