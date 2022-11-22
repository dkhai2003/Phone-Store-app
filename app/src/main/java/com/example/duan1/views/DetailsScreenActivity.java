package com.example.duan1.views;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.Product;
import com.example.duan1.model.Product_Type;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DetailsScreenActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imgDetail, iv_fav,img1,img2,img3,img4,btnMinus,btnPlus;
    private TextView tvNameDetail, tvPriceDetail,tvSlMua;

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
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        Product product = (Product) bundle.get("SanPham");
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(img1.getContext())
                        .load(product.getSpct().getHinh1())
                        .into(imgDetail);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(img2.getContext())
                        .load(product.getSpct().getHinh2())
                        .into(imgDetail);

            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(img3.getContext())
                        .load(product.getSpct().getHinh3())
                        .into(imgDetail);

            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(img4.getContext())
                        .load(product.getSpct().getHinh4())
                        .into(imgDetail);

            }
        });

    }

    private void getStatusFav(int status) {
        if (status == 1) {
            iv_fav.setImageResource(R.drawable.favorite_true);
        } else {
            iv_fav.setImageResource(R.drawable.favorite_false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        imgDetail = findViewById(R.id.imgDetail);
        tvNameDetail = findViewById(R.id.tvNameDetail);
        tvPriceDetail = findViewById(R.id.tvPriceDetail);
        iv_fav = findViewById(R.id.iv_fav);
        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        img3=findViewById(R.id.img3);
        img4=findViewById(R.id.img4);
        btnMinus=findViewById(R.id.btnMinus);
        btnPlus=findViewById(R.id.btnPlus);
        tvSlMua=findViewById(R.id.tvSlMua);
        setNumber();



    }
    public void setNumber() {
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt((String) tvSlMua.getText());

                    if(number==1){
                        tvSlMua.setText("1");
                    }else {
                        number=number-1;
                        tvSlMua.setText(number + "");
                    }



            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt((String) tvSlMua.getText());

                    number=number+1;
                    tvSlMua.setText(number + "");



            }
        });
    }





    private void setValue() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        Product product = (Product) bundle.get("SanPham");

        String lsp = (String) bundle.get("lsp");
        Glide.with(imgDetail.getContext())
                .load(product.getHinhSP())
                .into(imgDetail);
        Glide.with(img1.getContext())
                .load(product.getSpct().getHinh1())
                .into(img1);
        Glide.with(img2.getContext())
                .load(product.getSpct().getHinh2())
                .into(img2);
        Glide.with(img3.getContext())
                .load(product.getSpct().getHinh3())
                .into(img3);
        Glide.with(img4.getContext())
                .load(product.getSpct().getHinh4())
                .into(img4);


        tvNameDetail.setText(product.getTenSP());
        tvPriceDetail.setText(product.getGiaSP() + "");
        getStatusFav(product.getFav());
        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getFav() != 0) {
                    product.setFav(0);
                    getStatusFav(product.getFav());
//                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp).child("SanPham");
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().getParent();
//                    myRef.setValue(product.getFav()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(DetailsScreenActivity.this, "ok", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    Log.d("MyREF", myRef + "");
                } else {
                    product.setFav(1);
                    getStatusFav(product.getFav());
                }
            }
        });
    }


    public void getSanPham(Product product, Product_Type type){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("duan/LoaiSanPham").child(type.getMaLoai()).child("SanPham");
    }

}
