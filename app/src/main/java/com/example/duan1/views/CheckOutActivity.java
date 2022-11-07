package com.example.duan1.views;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.duan1.R;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        anhXa();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Check Out");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleMarginStart(160);
    }

    private void anhXa() {
        toolbar = findViewById(R.id.toolbar);
    }
}