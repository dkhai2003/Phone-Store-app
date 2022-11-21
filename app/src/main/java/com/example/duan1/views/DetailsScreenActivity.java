package com.example.duan1.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.Product;


public class DetailsScreenActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imgDetail;
    private TextView tvNameDetail,tvPriceDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);
        unitUi();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setValue();

    }

    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        imgDetail = findViewById(R.id.imgDetail);
        tvNameDetail = findViewById(R.id.tvNameDetail);
        tvPriceDetail = findViewById(R.id.tvPriceDetail);

    }

    private void setValue(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }

        Product product = (Product) bundle.get("SanPham");
        Glide.with(imgDetail.getContext())
                .load(product.getHinhSP())
                .into(imgDetail);

        tvNameDetail.setText(product.getTenSP());
        tvPriceDetail.setText(product.getGiaSP()+"");
    }






}
