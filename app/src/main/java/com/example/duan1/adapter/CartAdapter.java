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

public class CartAdapter extends FirebaseRecyclerAdapter<Product, CartAdapter.myViewHolder> {

    private final IClickCart iClickCart;

    public interface IClickCart {
        void onClickDeleteCart(Product product);

        void onClickMinus(Product product);

        void onClickPlus(Product product);
    }

    public CartAdapter(@NonNull FirebaseRecyclerOptions<Product> options, IClickCart iClickCart) {
        super(options);
        this.iClickCart = iClickCart;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Product model) {
        holder.name.setText(model.getTenSPSubString17());
        holder.price.setText(model.getGiaSP() * model.getSoLuong() + "$");
        holder.count.setText(model.getSoLuong() + "");
        holder.tvCount.setText("Số lượng: " + model.getSoLuong() + "");
        Glide.with(holder.img.getContext())
                .load(model.getHinhSP())
                .into(holder.img);
        if (model.getSoLuong() == 1) {
            holder.imgMinus.setEnabled(false);
            holder.imgMinus.setImageResource(R.drawable.minus_false);
        } else {
            holder.imgMinus.setEnabled(true);
            holder.imgMinus.setImageResource(R.drawable.minus);
        }
        holder.maSP.setText("Mã sản phẩm: " + model.getMaSP());


        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickCart.onClickDeleteCart(model);
            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickCart.onClickMinus(model);
            }
        });

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickCart.onClickPlus(model);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img, imgDelete, imgMinus, imgPlus;
        TextView name, price, count, tvCount, maSP;
        CardView cardViewCart;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.tvSoLuong);
            img = itemView.findViewById(R.id.imgCart);
            name = itemView.findViewById(R.id.tvNameCart);
            price = itemView.findViewById(R.id.tvPriceCart);
            cardViewCart = itemView.findViewById(R.id.card_view_cart);
            imgDelete = itemView.findViewById(R.id.imgDeleteCart);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            tvCount = itemView.findViewById(R.id.tvAmountCart);
            maSP = itemView.findViewById(R.id.tvMaSP);
        }
    }
}


//                AlertDialog.Builder alerBuider = new AlertDialog.Builder(v.getContext());
//                alerBuider.setTitle("Notifications");
//                alerBuider.setMessage("Do you want to delete?");
//                alerBuider.setCancelable(false);
//                alerBuider.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        String userEmail = user.getEmail();
//                        String[] subEmail = userEmail.split("@");
//                        String pathUserId = "User" + subEmail[0];
//                        FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId).child("Cart").child(model.getMaSP()).removeValue();
//                    }
//                }).setNegativeButton("no",null);
//                Dialog dialog = alerBuider.create();
//                dialog.show();