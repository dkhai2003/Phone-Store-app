package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.adapter.ProductAdapterAnother;
import com.example.duan1.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class DetailsScreenActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rcvListAnotherItem;
    private ProductAdapterAnother productAdapter;
    int a = 0;
    private Button btnAddToCart;
    private ImageView imgDetail, iv_fav, img1, img2, img3, img4, btnMinus, btnPlus;
    private TextView tvNameDetail, tvPriceDetail, tvSlMua, tvTotalDetail, tvMota;
    int soLuong = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);
        unitUi();
        checkTotal();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    private void checkTotal() {
        int Soluong = Integer.parseInt(tvSlMua.getText().toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];
        DatabaseReference myRef = database.getReference("duan/User/" + pathUserId);
        myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    myRef.child("Total").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            getFragmentManager().popBackStack();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void unitUi() {
        rcvListAnotherItem = findViewById(R.id.rcvListAnotherItem);
        toolbar = findViewById(R.id.toolbar);
        imgDetail = findViewById(R.id.imgDetail);
        tvNameDetail = findViewById(R.id.tvNameDetail);
        tvPriceDetail = findViewById(R.id.tvPriceDetail);
        tvMota = findViewById(R.id.tvDescribe);
        iv_fav = findViewById(R.id.iv_fav);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        btnAddToCart = findViewById(R.id.btnAddToCard);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        tvSlMua = findViewById(R.id.tvSlMua);
        tvTotalDetail = findViewById(R.id.tvTotalDetail);
    }

    private void setValue() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Product product = (Product) bundle.get("SanPham");


        getRecyclerViewListProductAnother();


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


        tvMota.setText(product.getMoTa());
        tvNameDetail.setText(product.getTenSP());
        tvPriceDetail.setText(product.getGiaSP() + "");

        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_fav.setImageResource(R.drawable.favorite_true);
                updateFavToFirebase(product);
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCartToFireBase(product);
            }
        });


        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuong > 1) {
                    soLuong -= 1;
                    tvSlMua.setText(soLuong + "");
                    tvTotalDetail.setText("Total: $" + soLuong * product.getGiaSP() + "");
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuong >= 1) {
                    soLuong += 1;
                    tvSlMua.setText(soLuong + "");
                    tvTotalDetail.setText("Total: $" + soLuong * product.getGiaSP() + "");
                }
            }
        });

        tvTotalDetail.setText("Total: $" + soLuong * product.getGiaSP() + "");

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userEmail = user.getEmail();
//        String[] subEmail = userEmail.split("@");
//        String pathUserId = "User" + subEmail[0];
//        DatabaseReference myRef1 = database.getReference("duan/User/" + pathUserId);
//        myRef1.child("Total").setValue(0);
    }

    private void updateFavToFirebase(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];
        DatabaseReference myRef = database.getReference("duan/User/" + pathUserId);
        myRef.child("favorites/" + product.getMaSP()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(DetailsScreenActivity.this, "Add Favorites Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailsScreenActivity.this, "Add Favorites Failure" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateCartToFireBase(Product product) {
        int Soluong = Integer.parseInt(tvSlMua.getText().toString());
        product.setSoLuong(Soluong);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];
        DatabaseReference myRef = database.getReference("duan/User/" + pathUserId);
        myRef.child("Cart").child(product.getMaSP()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailsScreenActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double value = snapshot.getValue(Double.class);
                value += soLuong * product.getGiaSP();
                myRef.child("Total").setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getRecyclerViewListProductAnother() {
        Random rd = new Random();
        int number1 = rd.nextInt(8);
        int b = number1 + 1;
        String lsp1 = "lsp" + b;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvListAnotherItem.setLayoutManager(linearLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp1).child("SanPham");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(myRef, Product.class)
                        .build();
        productAdapter = new ProductAdapterAnother(options, new ProductAdapterAnother.IClickProduct1() {
            @Override
            public void onClickDetailsScreen(Product product) {
                onClickGoToDetail(product);
            }
        });
        rcvListAnotherItem.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
        productAdapter.startListening();
    }

    public void onClickGoToDetail(Product product) {
        Intent intent = new Intent(this, DetailsScreenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SanPham", product);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

//        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                Toast.makeText(DetailsScreenActivity.this, task+"", Toast.LENGTH_SHORT).show();
//                Log.d("====>>>TAG", "onComplete: "+task);
//                Log.d("====>>>TAG", "onComplete: "+myRef);
//
//            }
//        });


//        myRef.child("SanPham/" + product.getMaSP()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(DetailsScreenActivity.this, "Add to cart success", Toast.LENGTH_SHORT).show();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String userEmail = user.getEmail();
//                String[] subEmail = userEmail.split("@");
//                String pathUserId = "User" + subEmail[0];
//                DatabaseReference myRef1 = database.getReference("duan/User/" + pathUserId).child("SanPham").child(product.getMaSP());
//                if (myRef1 == myRef){
//                    Toast.makeText(DetailsScreenActivity.this, "Add to cart fail", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(DetailsScreenActivity.this, "Add to cart thanh cong", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(DetailsScreenActivity.this, "Add to cart fail", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
