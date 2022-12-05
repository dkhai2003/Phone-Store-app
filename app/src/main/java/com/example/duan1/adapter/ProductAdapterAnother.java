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

public class ProductAdapterAnother extends FirebaseRecyclerAdapter<Product, ProductAdapterAnother.myViewHolder> {


    private final IClickProduct1 iClickProduct;

    public interface IClickProduct1 {
        void onClickDetailsScreen(Product product);
    }


    public ProductAdapterAnother(@NonNull FirebaseRecyclerOptions<Product> options, IClickProduct1 iClickProduct) {
        super(options);
        this.iClickProduct = iClickProduct;
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Product model) {
        holder.name.setText(model.getTenSPSubString15());
        holder.price.setText(model.getGiaSP() + "$");


        Glide.with(holder.img.getContext())
                .load(model.getHinhSP())
                .into(holder.img);

//        holder.imgDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iClickProduct.onClickDetailsScreen(model);
//            }
//        });
        holder.card_view_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickProduct.onClickDetailsScreen(model);
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_custom_another, parent, false);


        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img, imgDetails;
        TextView name, price;
        CardView card_view_product;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgSanPham1);
            name = itemView.findViewById(R.id.tvTenSanPham1);
            price = itemView.findViewById(R.id.tvGiaSanPham1);
//            imgDetails = itemView.findViewById(R.id.imgRemove1);
            card_view_product = itemView.findViewById(R.id.card_view_product);
        }
    }
}
