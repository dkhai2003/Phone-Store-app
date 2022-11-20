package com.example.duan1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {
    private SearchView edSearch;

    private View mView;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewListProduct, recyclerViewListProduct_type;
    private TextView tvNameHome;
    private ImageView ivAvatarHome;
    ProductAdapter productAdapter;
    Product_TypeAdapter product_typeAdapter;
    String lsp = "lsp1";

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
                txtSreach(query,lsp);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSreach(query,lsp);
                return false;
            }
        });

        return mView;
    }



    private void unitUi() {
        ivAvatarHome = (ImageView) mView.findViewById(R.id.ivAvatarHome);
        tvNameHome = (TextView) mView.findViewById(R.id.tvNameHome);
        edSearch = mView.findViewById(R.id.edSreach);


    }

    // ham update sảech
    private void txtSreach(String str,String lsp){
            FirebaseRecyclerOptions<Product> options =
                    new FirebaseRecyclerOptions.Builder<Product>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp).child("SanPham").orderByChild("tenSP").startAt(str).endAt(str+"~"), Product.class)
                            .build();

            productAdapter = new ProductAdapter(options);
            productAdapter.startListening();
            recyclerViewListProduct.setAdapter(productAdapter);
        }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            tvNameHome.setText(user.getDisplayName());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(ivAvatarHome);
        }
    }
    // asddas


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRecyclerViewListProduct_type();
        getRecyclerViewListProduct(lsp);
    }

    private void getRecyclerViewListProduct(String lsp) {

        recyclerViewListProduct = mView.findViewById(R.id.recyclerViewListProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
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
        recyclerViewListProduct_type = mView.findViewById(R.id.recyclerViewListProduct_type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerViewListProduct_type.setLayoutManager(linearLayoutManager);
        FirebaseRecyclerOptions<Product_Type> options =
                new FirebaseRecyclerOptions.Builder<Product_Type>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham"), Product_Type.class)
                        .build();


        product_typeAdapter = new Product_TypeAdapter(options, new Product_TypeAdapter.IclickListener() {
            @Override
            public void onClickGetMaLoai(Product_Type type) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("duan/LoaiSanPham");

                myRef.child(type.getMaLoai()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("=>>>>>>>>>>>>firebase", "Error getting data", task.getException());
//                            Log.d("firebase", String.valueOf(task.getResult().getValue()));



                        }
                        else {
                            Toast.makeText(getContext(), type.getMaLoai(), Toast.LENGTH_SHORT).show();
                            lsp = type.getMaLoai();
                            getRecyclerViewListProduct(lsp);
                            productAdapter.notifyDataSetChanged();
                            productAdapter.startListening();


                        }
                    }
                });



            }
        });
        recyclerViewListProduct_type.setAdapter(product_typeAdapter);


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