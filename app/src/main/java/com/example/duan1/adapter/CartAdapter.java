package com.example.duan1.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CartAdapter extends FirebaseRecyclerAdapter<Product,CartAdapter.myViewHolder> {

    private final IClickCart iClickCart;

    public interface IClickCart{
        void onClickDeleteCart(Product product);
    }

    public CartAdapter(@NonNull FirebaseRecyclerOptions<Product> options,IClickCart iClickCart) {
        super(options);
        this.iClickCart = iClickCart;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Product model) {
        holder.name.setText(model.getTenSP());
        holder.price.setText(model.getGiaSP() + "$");
        Glide.with(holder.img.getContext())
                .load(model.getHinhSP())
                .into(holder.img);

//        holder.cardViewCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iClickCart.onClickDeleteCart(model);
//            }
//        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iClickCart.onClickDeleteCart(model);
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
        ImageView img, imgDelete;
        TextView name, price;
        CardView cardViewCart;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgCart);
            name = itemView.findViewById(R.id.tvNameCart);
            price = itemView.findViewById(R.id.tvPriceCart);
            cardViewCart = itemView.findViewById(R.id.card_view_cart);
            imgDelete = itemView.findViewById(R.id.imgDeleteCart);

        }
    }

}
