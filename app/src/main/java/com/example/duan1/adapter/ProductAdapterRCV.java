package com.example.duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.Product;

import java.util.ArrayList;

public class ProductAdapterRCV extends RecyclerView.Adapter<ProductAdapterRCV.ViewHolder>{
    ArrayList<Product> listItemAll;

    public ProductAdapterRCV(ArrayList<Product> listItemAll) {
        this.listItemAll = listItemAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_custom, parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product itemAll = listItemAll.get(position);
        holder.imgAllItems.setImageResource(itemAll.getImgAllItem());
        holder.nameAllItems.setText(itemAll.getNameItems());
        holder.priceAllItems.setText(itemAll.getPriceItems());
    }

    @Override
    public int getItemCount() {
        return listItemAll.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAllItems;
        TextView nameAllItems;
        TextView priceAllItems;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAllItems = itemView.findViewById(R.id.imgSanPham);
            nameAllItems = itemView.findViewById(R.id.tvTenSanPham);
            priceAllItems = itemView.findViewById(R.id.tvGiaSanPham);
        }
    }
}
