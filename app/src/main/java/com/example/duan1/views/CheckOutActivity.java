package com.example.duan1.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.duan1.R;
import com.example.duan1.fragment.BottomSheetDialog;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnConfirmAndPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        anhXa();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Check Out");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnConfirmAndPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.newInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
            }
        });
    }

    private void anhXa() {
        toolbar = findViewById(R.id.toolbar);
        btnConfirmAndPay = findViewById(R.id.btnConfirmAndPay);
    }
}