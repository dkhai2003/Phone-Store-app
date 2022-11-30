package com.example.duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ListHistoryProductAdapter extends FirebaseRecyclerAdapter<Product, ListHistoryProductAdapter.myViewHolder> {



    public ListHistoryProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Product model) {
        holder.name.setText(model.getTenSP());
        holder.price.setText(model.getGiaSP() + "$");


        Glide.with(holder.img.getContext())
                .load(model.getHinhSP())
                .into(holder.img);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_history, parent, false);


        return new ListHistoryProductAdapter.myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgSanPham1);
            name = itemView.findViewById(R.id.tvTenSanPham1);
            price = itemView.findViewById(R.id.tvGiaSanPham1);

        }
    }
}
