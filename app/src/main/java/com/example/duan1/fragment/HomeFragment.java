package com.example.duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.adapter.CategoryProductAdapter;
import com.example.duan1.adapter.ProductAdapterRCV;
import com.example.duan1.model.CategoryProduct;
import com.example.duan1.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private View mView;
    private RecyclerView.Adapter adapter;
    private RecyclerView rcvCategoryProduct, rvcMainProduct;
    private TextView tvNameHome;
    private ImageView ivAvatarHome;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        unitUi();
        setUserInformation();
        return mView;
    }

    private void unitUi() {
        ivAvatarHome = (ImageView) mView.findViewById(R.id.ivAvatarHome);
        tvNameHome = (TextView) mView.findViewById(R.id.tvNameHome);
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            tvNameHome.setText(user.getDisplayName());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(ivAvatarHome);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewCategory();
        recyclerViewItemAll1();
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvCategoryProduct = mView.findViewById(R.id.rcvCategoryProduct);
        rcvCategoryProduct.setLayoutManager(linearLayoutManager);
        ArrayList<CategoryProduct> category = new ArrayList<>();
        category.add(new CategoryProduct(R.drawable.category_icons_all, "All"));
        category.add(new CategoryProduct(R.drawable.category_icons_phone, "Phone"));
        category.add(new CategoryProduct(R.drawable.category_icons_all, "All 3"));
        category.add(new CategoryProduct(R.drawable.category_icons_all, "All 4"));
        category.add(new CategoryProduct(R.drawable.category_icons_all, "All 5"));
        category.add(new CategoryProduct(R.drawable.category_icons_all, "All 6"));
        category.add(new CategoryProduct(R.drawable.category_icons_all, "All 7"));
        category.add(new CategoryProduct(R.drawable.category_icons_all, "All 8"));
        adapter = new CategoryProductAdapter(category);
        rcvCategoryProduct.setAdapter(adapter);

    }

    private void recyclerViewItemAll1() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvcMainProduct = mView.findViewById(R.id.rvcMainProduct);
        rvcMainProduct.setLayoutManager(gridLayoutManager);
        ArrayList<Product> itemAlls = new ArrayList<>();
        itemAlls.add(new Product(R.drawable.item_simple_images, "Iphone 16", "200$"));
        itemAlls.add(new Product(R.drawable.item_simple_images, "Iphone 16", "200$"));
        itemAlls.add(new Product(R.drawable.item_simple_images, "Iphone 16", "200$"));
        itemAlls.add(new Product(R.drawable.item_simple_images, "Iphone 16", "200$"));
        itemAlls.add(new Product(R.drawable.item_simple_images, "Iphone 16", "200$"));
        itemAlls.add(new Product(R.drawable.item_simple_images, "Iphone 16", "200$"));
        itemAlls.add(new Product(R.drawable.item_simple_images, "Iphone 16", "200$"));
        adapter = new ProductAdapterRCV(itemAlls);
        rvcMainProduct.setAdapter(adapter);
    }
}