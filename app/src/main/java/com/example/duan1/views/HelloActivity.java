package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;

public class HelloActivity extends AppCompatActivity {

    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        unitUi();
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStart();
            }
        });
    }

    private void getStart() {
        Intent intent = new Intent(HelloActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void unitUi() {
        btnGetStarted = findViewById(R.id.btnGetStarted);
    }
}   