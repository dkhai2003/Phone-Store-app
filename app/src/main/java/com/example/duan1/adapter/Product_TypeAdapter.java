package com.example.duan1.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.example.duan1.model.Product_Type;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Product_TypeAdapter extends FirebaseRecyclerAdapter<Product_Type, Product_TypeAdapter.myViewHolder> {
    //
    private IclickListener iclickListener;
    private int pos = 0;

    public interface IclickListener {
        void onClickGetMaLoai(Product_Type type);
    }


    public Product_TypeAdapter(@NonNull FirebaseRecyclerOptions<Product_Type> options, IclickListener iclickListener) {
        super(options);
        this.iclickListener = iclickListener;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Product_Type model) {
        holder.name.setText(model.getTenLoai());
        Glide.with(holder.img.getContext())
                .load(model.getHinhLoai())
                .into(holder.img);
//        holder.itemView.setBackgroundColor(pos == position ? Color.GREEN : Color.WHITE);
        holder.itemView.setBackgroundResource(pos == position ? R.drawable.bottom_border_product_type : R.drawable.button_custom_white);
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickListener.onClickGetMaLoai(model);
                if (holder.getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION) return;

                // Updating old as well as new positions
                notifyItemChanged(pos);
                pos = holder.getAbsoluteAdapterPosition();
                notifyItemChanged(pos);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_type, parent, false);
        return new Product_TypeAdapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        CardView item_layout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgLoaiHinh);
            name = itemView.findViewById(R.id.tvTenLoai);
            item_layout = itemView.findViewById(R.id.item_layout);

        }

    }
}



