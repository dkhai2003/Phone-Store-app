package com.example.duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HistoryItemAdapter extends FirebaseRecyclerAdapter<Product, HistoryItemAdapter.myViewHolder> {

    private final IClickProduct iClickProduct;

    public interface IClickProduct {
        void onClickDetailsScreen(Product product);
    }

    public HistoryItemAdapter(@NonNull FirebaseRecyclerOptions<Product> options, IClickProduct iClickProduct) {
        super(options);
        this.iClickProduct = iClickProduct;
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Product model) {
        Glide.with(holder.img.getContext())
                .load(model.getHinhSP())
                .into(holder.img);
        holder.tvAmountCart.setText("Số lượng: " + model.getSoLuong());
        holder.tvNameCart.setText("" + model.getTenSPSubString15());
        holder.tvMaSP.setText("Mã sản phẩm: " + model.getMaSP());
        holder.tvPriceCart.setText("Giá sản phẩm: " + model.getGiaSP() + "$");
        holder.card_view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickProduct.onClickDetailsScreen(model);
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_piece, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvNameCart, tvMaSP, tvAmountCart, tvPriceCart;
        CardView card_view_cart;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgSanPham);
            tvNameCart = itemView.findViewById(R.id.tvNameCart);
            tvMaSP = itemView.findViewById(R.id.tvMaSP);
            tvAmountCart = itemView.findViewById(R.id.tvAmountCart);
            tvPriceCart = itemView.findViewById(R.id.tvPriceCart);
            card_view_cart = itemView.findViewById(R.id.card_view_cart);
        }
    }
}
