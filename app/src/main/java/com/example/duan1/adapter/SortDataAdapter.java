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

import java.util.List;

public class SortDataAdapter extends RecyclerView.Adapter<SortDataAdapter.myViewHolder>{

    private List<Product> listProduct;
    private final IClickProduct2 iClickProduct;

    public interface IClickProduct2 {
        void onClickDetailsScreen(Product product);
    }


    public SortDataAdapter(List<Product> listProduct, IClickProduct2 iClickProduct) {
        this.listProduct = listProduct;
        this.iClickProduct = iClickProduct;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_custom, parent, false);


        return new SortDataAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Product model =  listProduct.get(position);
        if(model ==  null){
            return;
        }

        holder.name.setText(model.getTenSP());
        holder.price.setText(model.getGiaSP() + "$");


        Glide.with(holder.img.getContext())
                .load(model.getHinhSP())
                .into(holder.img);

        holder.imgDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickProduct.onClickDetailsScreen(model);
            }
        });
        holder.card_view_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickProduct.onClickDetailsScreen(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listProduct != null){
            return listProduct.size();
        }

        return 0;
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img, imgDetails;
        TextView name, price;
        CardView card_view_product;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgSanPham);
            name = itemView.findViewById(R.id.tvTenSanPham);
            price = itemView.findViewById(R.id.tvGiaSanPham);
            imgDetails = itemView.findViewById(R.id.imgRemove);
            card_view_product = itemView.findViewById(R.id.card_view_product);
        }
    }
}
