package com.example.duan1.views;

import static com.example.duan1.views.HomeScreenActivity.myRef;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.adapter.HistoryAdapter;
import com.example.duan1.model.Bill;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderHistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnStartOrdering;
    private RecyclerView recyclerViewListHistory;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_not_null);
        anhXa();
        setRecyclerViewListHistory();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Order History");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void anhXa() {
        toolbar = findViewById(R.id.toolbar);
//        btnStartOrdering = findViewById(R.id.btnStartOrdering);
        recyclerViewListHistory = findViewById(R.id.recyclerviewListHistory);
    }

    private void setRecyclerViewListHistory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewListHistory.setLayoutManager(linearLayoutManager);
        FirebaseRecyclerOptions<Bill> options =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(myRef().child("Bill"), Bill.class)
                        .build();
        historyAdapter = new HistoryAdapter(options, new HistoryAdapter.IClickHistory() {
            @Override
            public void onClickShowListProduct(Bill bill) {
                onClickGoToListProduct(bill);
            }
        });

        recyclerViewListHistory.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
        historyAdapter.startListening();
    }


    public void onClickGoToListProduct(Bill bill) {
//        Intent intent = new Intent(this, ListProductHistoryActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("Bill", bill);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        historyAdapter.stopListening();
    }

}