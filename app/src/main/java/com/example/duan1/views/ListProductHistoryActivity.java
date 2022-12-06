package com.example.duan1.views;

import static com.example.duan1.views.HomeScreenActivity.myRef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.duan1.R;
import com.example.duan1.adapter.ListHistoryProductAdapter;
import com.example.duan1.model.Bill;
import com.example.duan1.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ListProductHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListProduct;
    private ImageView imgBack;
    ListHistoryProductAdapter listHistoryProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_history);
        unitui();
        setListProduct();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void unitui() {
//        toolbar = findViewById(R.id.toolbar);
//        btnStartOrdering = findViewById(R.id.btnStartOrdering);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct1);
        imgBack = findViewById(R.id.imgBack1);

    }

    public void setListProduct(){
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Bill bill = (Bill) bundle.get("Bill");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewListProduct.setLayoutManager(gridLayoutManager);

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(myRef().child("Bill").child(bill.getMaHoaDon()).child("Cart"), Product.class)
                        .build();

        listHistoryProductAdapter = new ListHistoryProductAdapter(options);
        recyclerViewListProduct.setAdapter(listHistoryProductAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listHistoryProductAdapter.startListening();
    }
}