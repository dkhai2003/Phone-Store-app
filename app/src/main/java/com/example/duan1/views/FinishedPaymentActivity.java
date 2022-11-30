package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;

public class FinishedPaymentActivity extends AppCompatActivity {
    private Button btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_payment);
        btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishedPaymentActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}