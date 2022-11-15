package com.example.duan1.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.duan1.R;

public class FavoriteActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnStartOrdering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        unitUi();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Favorites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnStartOrdering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FavoriteActivity.this, "This is Button Start Ordering", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        btnStartOrdering = findViewById(R.id.btnStartOrdering);
    }
}