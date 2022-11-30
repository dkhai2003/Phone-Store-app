package com.example.duan1.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.duan1.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnConfirmAndPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        unitUi();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Check Out");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnConfirmAndPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomSheetDialog();
            }
        });
    }

    private void clickOpenBottomSheetDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        Button btnPayNow = viewDialog.findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckOutActivity.this, "This is Button PayNow", Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();
    }

    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        btnConfirmAndPay = findViewById(R.id.btnConfirmAndPay);
    }
}