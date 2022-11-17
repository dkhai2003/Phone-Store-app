package com.example.duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.CategoryProduct;

import java.util.ArrayList;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder> {
    ArrayList<CategoryProduct> categoryList;

    public CategoryProductAdapter(ArrayList<CategoryProduct> _categoryList) {
        this.categoryList = _categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_product_custom, parent, false);
        ImageView imgCategoryProduct = view.findViewById(R.id.imgCategoryProduct);
        TextView nameCategoryProduct = view.findViewById(R.id.nameCategoryProduct);
        CardView card_view_category_product = view.findViewById(R.id.card_view_category_product);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryProduct newItems = categoryList.get(position);
        holder._imgCategoryProduct.setImageResource(newItems.getPic());
        holder._nameCategoryProduct.setText(newItems.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView _imgCategoryProduct;
        TextView _nameCategoryProduct;
        LinearLayout linear_card_view_category_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _imgCategoryProduct = itemView.findViewById(R.id.imgCategoryProduct);
            _nameCategoryProduct = itemView.findViewById(R.id.nameCategoryProduct);
            linear_card_view_category_product = itemView.findViewById(R.id.linear_card_view_category_product);
        }
    }

}
