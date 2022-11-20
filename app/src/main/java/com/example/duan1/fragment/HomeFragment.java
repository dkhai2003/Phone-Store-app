package com.example.duan1.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.adapter.ProductAdapter;
import com.example.duan1.adapter.Product_TypeAdapter;
import com.example.duan1.model.Product;
import com.example.duan1.model.Product_Type;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {
    private View mView;
    private SearchView edSearch;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewListProduct, recyclerViewListProduct_type;
    private TextView tvNameHome;
    private ImageView ivAvatarHome;

    private ProductAdapter productAdapter;
    private Product_TypeAdapter product_typeAdapter;
    private String loaiSanPham = "lsp1";
    private ProgressDialog progressDialog;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        unitUi();
        setUserInformation();
        edSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSreach(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSreach(query);
                return false;
            }
        });
        return mView;
    }


    private void unitUi() {
        ivAvatarHome = (ImageView) mView.findViewById(R.id.ivAvatarHome);
        tvNameHome = (TextView) mView.findViewById(R.id.tvNameHome);
        edSearch = mView.findViewById(R.id.edSreach);
        recyclerViewListProduct = (RecyclerView) mView.findViewById(R.id.recyclerViewListProduct);
        recyclerViewListProduct_type = (RecyclerView) mView.findViewById(R.id.recyclerViewListProduct_type);

    }

    
    private void txtSreach(String str) {
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child("lsp1").child("SanPham").orderByChild("tenSP").startAt(str).endAt(str + "~"), Product.class)
                        .build();

        productAdapter = new ProductAdapter(options);
        productAdapter.startListening();
        recyclerViewListProduct.setAdapter(productAdapter);
    }

    private void setUserInformation() {
        createDialog();
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            progressDialog.dismiss();
            tvNameHome.setText(user.getDisplayName());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(ivAvatarHome);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecyclerViewListProduct();
    }

    private void getRecyclerViewListProduct() {
        getRecyclerViewListProduct_type();
        getRecyclerViewListProduct(loaiSanPham);
    }

    private void getRecyclerViewListProduct(String lsp) {
        recyclerViewListProduct = mView.findViewById(R.id.recyclerViewListProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewListProduct.setLayoutManager(gridLayoutManager);

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp).child("SanPham"), Product.class)
                        .build();

        productAdapter = new ProductAdapter(options);
        recyclerViewListProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    private void getRecyclerViewListProduct_type() {
        createDialog();

        recyclerViewListProduct_type = mView.findViewById(R.id.recyclerViewListProduct_type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewListProduct_type.setLayoutManager(linearLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("duan/LoaiSanPham");
        FirebaseRecyclerOptions<Product_Type> options =
                new FirebaseRecyclerOptions.Builder<Product_Type>()
                        .setQuery(myRef, Product_Type.class)
                        .build();


        product_typeAdapter = new Product_TypeAdapter(options, new Product_TypeAdapter.IclickListener() {
            @Override
            public void onClickGetMaLoai(Product_Type type) {
                progressDialog.show();
                myRef.child(type.getMaLoai()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("=>>>>>>>>>>>>firebase", "Error getting data", task.getException());
                            loaiSanPham = type.getMaLoai();
                            progressDialog.dismiss();
                        } else {
                            loaiSanPham = type.getMaLoai();
                            getRecyclerViewListProduct(loaiSanPham);
                            productAdapter.notifyDataSetChanged();
                            productAdapter.startListening();
                            progressDialog.dismiss();
                        }
                    }
                });

            }
        });
        recyclerViewListProduct_type.setAdapter(product_typeAdapter);
    }

    private void createDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Connecting to the server ... ");
    }

    @Override
    public void onStart() {
        super.onStart();
        productAdapter.startListening();
        product_typeAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        productAdapter.stopListening();
        product_typeAdapter.stopListening();
    }
}