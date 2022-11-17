package com.example.duan1.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.adapter.CategoryProductAdapter;
import com.example.duan1.adapter.ProductAdapterRCV;
import com.example.duan1.fragment.UserFragment;
import com.example.duan1.model.CategoryProduct;
import com.example.duan1.model.Product;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView rcvCategoryProduct, rvcMainProduct;
    private Button btnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        btnUser = findViewById(R.id.btnUser);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, UserFragment.newInstance()).commit();
            }
        });
        recyclerViewCategory();
        recyclerViewItemAll1();
    }


    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvCategoryProduct = findViewById(R.id.rcvCategoryProduct);
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvcMainProduct = findViewById(R.id.rvcMainProduct);
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