package com.example.duan1.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.adapter.FavoritesItemAdapter;
import com.example.duan1.model.Product;
import com.example.duan1.views.DetailsScreenActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoritesFragment extends Fragment {
    private RecyclerView recyclerViewFav;
    private View mView;
    private FavoritesItemAdapter favoritesItemAdapter;
    private TextView count_Fav;
    private ProgressDialog progressDialog;
    public static final String TAG = FavoritesFragment.class.getName();

    public static FavoritesFragment newInstance() {
        Bundle args = new Bundle();
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorites_null, container, false);

        return mView;
    }

    private void setViewLayout(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(id, null);
        count_Fav = mView.findViewById(R.id.count_fav);
        recyclerViewFav = mView.findViewById(R.id.recyclerViewFav);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListItemProduct();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog();
        progressDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListItemProduct();
    }

    private void getListItemProduct() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            String uEmail = user.getEmail();
            String[] subEmail = uEmail.split("@");
            String pathUserId = "User" + subEmail[0];
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("duan").child("User").child(pathUserId).child("favorites");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        setViewLayout(R.layout.fragment_favorites_not_null);
                        getRecyclerViewListProduct();
                        favoritesItemAdapter.startListening();
                        progressDialog.dismiss();
                    }else{
                        setViewLayout(R.layout.fragment_favorites_null);
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("DatabaseError", databaseError.getDetails());
                }
            });
        }
    }

    private void getRecyclerViewListProduct() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            String uEmail = user.getEmail();
            String[] subEmail = uEmail.split("@");
            String pathUserId = "User" + subEmail[0];
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewFav.setLayoutManager(linearLayoutManager);
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("duan").child("User").child(pathUserId).child("favorites");
            FirebaseRecyclerOptions<Product> options =
                    new FirebaseRecyclerOptions.Builder<Product>()
                            .setQuery(myRef, Product.class)
                            .build();
            favoritesItemAdapter = new FavoritesItemAdapter(options, new FavoritesItemAdapter.IClickProduct() {
                @Override
                public void onClickDetailsScreen(Product product) {
                    Toast.makeText(getContext(), "Card" + product.getMaSP(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), DetailsScreenActivity.class);
                    intent.putExtra("SanPham", product);
                    startActivity(intent);
                }
//
                @Override
                public void onClickRemoveProduct(Product product) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    alertBuilder.setTitle("Notifications");
                    alertBuilder.setMessage("Do you want to delete?");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.show();
                            Toast.makeText(getContext(), "Remove" + product.getMaSP(), Toast.LENGTH_SHORT).show();
                            DatabaseReference removeValue = myRef.child(product.getMaSP());
                            removeValue.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Remove Success", Toast.LENGTH_SHORT).show();
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            count_Fav.setText(dataSnapshot.getChildrenCount() + " items");
                                            getListItemProduct();
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.d("DatabaseError", databaseError.getDetails());
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }).setNegativeButton("No", null);
                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                }
            });

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count_Fav.setText(dataSnapshot.getChildrenCount() + " items");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("DatabaseError", databaseError.getDetails());
                }
            });
            recyclerViewFav.setAdapter(favoritesItemAdapter);
            favoritesItemAdapter.notifyDataSetChanged();
        }
    }

    private void createDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Connecting to the server ... ");
        progressDialog.setIcon(R.drawable.none_avatar);
    }
}