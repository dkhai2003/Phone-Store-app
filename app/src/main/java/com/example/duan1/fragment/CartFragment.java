package com.example.duan1.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.adapter.CartAdapter;
import com.example.duan1.model.Product;
import com.example.duan1.views.CheckOutActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class CartFragment extends Fragment {

    public static final String TAG = CartFragment.class.getName();
    private TextView tvCountCart, tvTotalCart;
    private View mView;
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private Button btnCheckOut;
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
        setTotalCart();
        return mView;
    }

    private void uniUi() {
        recyclerViewCart = mView.findViewById(R.id.recyclerviewListCart);
        tvCountCart = mView.findViewById(R.id.tvCountCart);
        tvTotalCart = mView.findViewById(R.id.tvTotalCart);
        btnCheckOut = mView.findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), CheckOutActivity.class);
                startActivity(i);
            }
        });
    }

    public void getRecyclerViewCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];


        recyclerViewCart = mView.findViewById(R.id.recyclerviewListCart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId);
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(myRef.child("Cart"), Product.class)
                        .build();
        ObservableSnapshotArray<Product> t = options.getSnapshots();

        Log.d(">>>>>>>>>", "getRecyclerViewCart: " + t);
        cartAdapter = new CartAdapter(options, new CartAdapter.IClickCart() {
            @Override
            public void onClickDeleteCart(Product product) {
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
                                myRef.child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int soLuongSanPham = (int) snapshot.getChildrenCount();
                                        tvCountCart.setText((soLuongSanPham) + " items");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        double value = snapshot.getValue(Double.class);
                                        if (value != 0) {
                                            value -= product.getSoLuong() * product.getGiaSP();
                                            myRef.child("Total").setValue(value);
                                            tvTotalCart.setText("Total: $" + value);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                    }
                }).setNegativeButton("no", null);
                Dialog dialog = alerBuider.create();
                dialog.show();

            }


            @Override
            public void onClickMinus(Product product) {
                Map<String, Object> map = new HashMap<>();
                map.put("soLuong", tru(product));
                FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId).child("Cart").child(product.getMaSP()).updateChildren(map);
                myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double value = snapshot.getValue(Double.class);
                        if (product.getSoLuong() != 1) {
                            value -= product.getGiaSP();
                            myRef.child("Total").setValue(value);
                            tvTotalCart.setText("Total: $" + value);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onClickPlus(Product product) {
                Map<String, Object> map = new HashMap<>();
                map.put("soLuong", cong(product));
                FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId).child("Cart").child(product.getMaSP()).updateChildren(map);

                myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double value = snapshot.getValue(Double.class);
                        if (value >= 0) {
                            value += product.getGiaSP();
                            myRef.child("Total").setValue(value);
                            tvTotalCart.setText("Total: $" + value);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        myRef.child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int soLuongSanPham = (int) snapshot.getChildrenCount();
                tvCountCart.setText((soLuongSanPham) + " items");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerViewCart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
//        itemTouchHelper.attachToRecyclerView(recyclerViewCart);


    }


    public int tru(Product product) {
        int mTru = product.getSoLuong();
        if (mTru > 1) {
            mTru = product.getSoLuong() - 1;
        }
        return mTru;
    }

    public int cong(Product product) {
        int mcong = product.getSoLuong();
        if (mcong >= 1) {
            mcong = product.getSoLuong() + 1;
        } else {
            mcong = 1;
        }
        return mcong;
    }


    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }

    public void setTotalCart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];
        DatabaseReference myRef = database.getReference("duan/User/" + pathUserId);


        myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    double value = snapshot.getValue(Double.class);
                    tvTotalCart.setText("Total: $" + value);
                } else {
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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