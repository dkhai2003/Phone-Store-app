package com.example.duan1.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.adapter.CartAdapter;
import com.example.duan1.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CartFragment extends Fragment  {

    public static final String TAG = CartFragment.class.getName();
    private TextView tvCountCart;
    private View mView;
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    int mcount_cart = 0;


    public static CartFragment newInstance() {
        Bundle args = new Bundle();
        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_cart_not_null, container, false);
        //anh xa
        uniUi();
        getRecyclerViewCart();

        return mView;
    }

    private void uniUi(){
        recyclerViewCart = mView.findViewById(R.id.recyclerviewListCart);
         tvCountCart = mView.findViewById(R.id.tvCountCart);
    }

    public String getUserID(String pathUserId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        pathUserId = "User" + subEmail[0];
        return pathUserId;
    }


    public void getRecyclerViewCart() {
//        String userid ="";
//        getUserID(userid);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];


        recyclerViewCart = mView.findViewById(R.id.recyclerviewListCart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId).child("Cart");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(myRef, Product.class)
                        .build();
//        cartAdapter = new CartAdapter(options);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//
//
//            }
//
//        });
        cartAdapter = new CartAdapter(options, new CartAdapter.IClickCart() {
            @Override
            public void onClickDeleteCart(Product product) {
//                FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId).child("SanPham").child(product.getMaSP()).removeValue();
                AlertDialog.Builder alerBuider = new AlertDialog.Builder(getContext());
                alerBuider.setTitle("Notifications");
                alerBuider.setMessage("Do you want to delete?");
                alerBuider.setCancelable(false);
                alerBuider.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userEmail = user.getEmail();
                        String[] subEmail = userEmail.split("@");
                        String pathUserId = "User" + subEmail[0];
                        FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId).child("Cart").child(product.getMaSP()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Remove Success", Toast.LENGTH_SHORT).show();
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        mcount_cart = (int) snapshot.getChildrenCount();
                                        tvCountCart.setText(mcount_cart+" items");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                    }
                }).setNegativeButton("no",null);
                Dialog dialog = alerBuider.create();
                dialog.show();
                
            }
        });
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mcount_cart = (int) snapshot.getChildrenCount();
                tvCountCart.setText(mcount_cart+" items");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        

        recyclerViewCart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
//        itemTouchHelper.attachToRecyclerView(recyclerViewCart);





    }






    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }



//    @NonNull
//    @Override
//    public CreationExtras getDefaultViewModelCreationExtras() {
//        return super.getDefaultViewModelCreationExtras();
//    }
//
//    @Override
//    public void onClickDeleteCart(Product product) {
//        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return true;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String userEmail = user.getEmail();
//                String[] subEmail = userEmail.split("@");
//                String pathUserId = "User" + subEmail[0];
//                FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId).child("SanPham").child(product.getMaSP()).removeValue();
//            }
//        });







    ///cc
}