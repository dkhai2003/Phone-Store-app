package com.example.duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.NewItems;

import java.util.ArrayList;

public class NewItemsAdapter extends RecyclerView.Adapter<NewItemsAdapter.ViewHolder>{
    ArrayList<NewItems> newItemsList;

    public NewItemsAdapter(ArrayList<NewItems> newItemsList) {
        this.newItemsList = newItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category  , parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewItems newItems = newItemsList.get(position);
        String pickUrl = "";

        holder.categoryPic.setImageResource(newItems.getPic());
    }

    @Override
    public int getItemCount() {
        return newItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryPic;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryPic = itemView.findViewById(R.id.imgCat);

        }
    }

}
