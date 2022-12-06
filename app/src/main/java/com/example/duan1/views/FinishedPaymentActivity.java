package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;

public class FinishedPaymentActivity extends AppCompatActivity {
    private Button btnGOBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_payment);
        btnGOBackToHome = findViewById(R.id.btnGoBackToHome);
        btnGOBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishedPaymentActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}