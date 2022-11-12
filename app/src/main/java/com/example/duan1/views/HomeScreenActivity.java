package com.example.duan1.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.duan1.R;
import com.example.duan1.adapter.NewItemsAdapter;
import com.example.duan1.adapter.SanPhamAdapter;
import com.example.duan1.model.NewItems;
import com.example.duan1.model.SanPham;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private  RecyclerView recyclerViewCategoryList,recyclerViewItemAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        recyclerViewCategory();
        recyclerViewItemAll1();

    }

    private void recyclerViewItemAll1(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerViewItemAll = findViewById(R.id.recyclerView2);
        recyclerViewItemAll.setLayoutManager(gridLayoutManager);
        ArrayList<SanPham> itemAlls = new ArrayList<>();
        itemAlls.add(new SanPham(R.drawable.iphone11,"Poliframe","$ 70.00"));
        itemAlls.add(new SanPham(R.drawable.iphone11,"Poliframe tuf...","$ 70.00"));
        itemAlls.add(new SanPham(R.drawable.iphone11,"Poliframe tuf...","$ 70.00"));
        itemAlls.add(new SanPham(R.drawable.iphone11,"Poliframe tuf...","$ 70.00"));
        itemAlls.add(new SanPham(R.drawable.iphone11,"Poliframe tuf...","$ 70.00"));
        itemAlls.add(new SanPham(R.drawable.iphone11,"Poliframe tuf...","$ 70.00"));
        itemAlls.add(new SanPham(R.drawable.iphone11,"Poliframe tuf...","$ 70.00"));


        adapter = new SanPhamAdapter(itemAlls);
        recyclerViewItemAll.setAdapter(adapter);
    }

    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerViewCategoryList = findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);
        ArrayList<NewItems> category = new ArrayList<>();
//        category.add(new CategoryDomain("cat_1"));
        category.add(new NewItems(R.drawable.iphone11));
        category.add(new NewItems(R.drawable.iphone11));
        category.add(new NewItems(R.drawable.iphone11));
        category.add(new NewItems(R.drawable.iphone11));
        category.add(new NewItems(R.drawable.iphone11));
        category.add(new NewItems(R.drawable.iphone11));
        category.add(new NewItems(R.drawable.iphone11));


//        category.add(new CategoryDomain("Burger","cat_1"));
//        category.add(new CategoryDomain("Burger","cat_2"));
//        category.add(new CategoryDomain("Burger","cat_3"));
//        category.add(new CategoryDomain("Burger","cat_4"));
//        category.add(new CategoryDomain("Burger","cat_5"));
        adapter = new NewItemsAdapter(category);
//        Log.d("TAG", "recyclerViewCategory: " + category.get(2));
        recyclerViewCategoryList.setAdapter(adapter);

    }
}