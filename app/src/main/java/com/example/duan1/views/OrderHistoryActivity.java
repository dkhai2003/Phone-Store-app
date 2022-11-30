package com.example.duan1.views;

import static com.example.duan1.views.HomeScreenActivity.myRef;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.adapter.HistoryAdapter;
import com.example.duan1.model.HoaDon;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderHistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnStartOrdering;
    private RecyclerView recyclerViewListHistory;
    HistoryAdapter historyAdapter;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_not_null);
        anhXa();
        setRecyclerViewListHistory();
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Order History");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        btnStartOrdering.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(OrderHistoryActivity.this, "This is Button Start Ordering", Toast.LENGTH_SHORT).show();
//            }
//        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

        @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            getFragmentManager().popBackStack();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void anhXa() {
//        toolbar = findViewById(R.id.toolbar);
//        btnStartOrdering = findViewById(R.id.btnStartOrdering);
        recyclerViewListHistory = findViewById(R.id.recyclerviewListHistory);
        imgBack = findViewById(R.id.imgBack1);

    }

    private void setRecyclerViewListHistory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewListHistory.setLayoutManager(linearLayoutManager);


        FirebaseRecyclerOptions<HoaDon> options =
                new FirebaseRecyclerOptions.Builder<HoaDon>()
                        .setQuery(myRef().child("HoaDon"), HoaDon.class)
                        .build();
        historyAdapter = new HistoryAdapter(options, new HistoryAdapter.IClickHistory() {
            @Override
            public void onClickShowListProduct(HoaDon hoaDon) {
                onClickGoToListProduct(hoaDon);
            }
        });
        recyclerViewListHistory.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
        historyAdapter.startListening();
    }

    public void onClickGoToListProduct(HoaDon hoaDon) {
        Intent intent = new Intent(this, ListProductHistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("HoaDon", hoaDon);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        historyAdapter.stopListening();
    }
}