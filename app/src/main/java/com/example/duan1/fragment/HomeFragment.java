package com.example.duan1.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1.R;
import com.example.duan1.adapter.PhotoSlideAdapter;
import com.example.duan1.adapter.ProductAdapter;
import com.example.duan1.adapter.Product_TypeAdapter;
import com.example.duan1.model.PhotoSlide;
import com.example.duan1.model.Product;
import com.example.duan1.model.Product_Type;
import com.example.duan1.views.DetailsScreenActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class HomeFragment extends Fragment {
    private View mView;
    private SearchView edSearch;
    private RecyclerView recyclerViewListProduct, recyclerViewListProduct_type;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private ImageView btnSortListProduct;

    private ProductAdapter productAdapter;
    private Product_TypeAdapter product_typeAdapter;
    private String loaiSanPham = "lsp1";
    private ProgressDialog progressDialog;
    private List<PhotoSlide> mListPhoto;
    private final Handler mHandler = new Handler(Looper.myLooper());

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
        setSlideShow();
        edSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSreach(query, loaiSanPham);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSreach(query, loaiSanPham);
                return false;
            }
        });

        btnSortListProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });


        return mView;
    }

    private void setSlideShow() {
        mListPhoto = new ArrayList<>();
        mListPhoto.add(new PhotoSlide(R.drawable.slide_amd));
        mListPhoto.add(new PhotoSlide(R.drawable.slide_flipz));
        mListPhoto.add(new PhotoSlide(R.drawable.slide_ipad));
        mListPhoto.add(new PhotoSlide(R.drawable.slide_tv));
        mListPhoto.add(new PhotoSlide(R.drawable.slide_max));
        mListPhoto.add(new PhotoSlide(R.drawable.slide_oppo));
        mListPhoto.add(new PhotoSlide(R.drawable.slide_vivo));
        PhotoSlideAdapter photoSlideAdapter = new PhotoSlideAdapter(mListPhoto);
        mViewPager2.setAdapter(photoSlideAdapter);
        mCircleIndicator3.setViewPager(mViewPager2);
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRun);
                mHandler.postDelayed(mRun, 3000);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRun);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRun, 3000);
    }

    private final Runnable mRun = new Runnable() {
        @Override
        public void run() {
            int currentPos = mViewPager2.getCurrentItem();
            if (currentPos == mListPhoto.size() - 1) {
                mViewPager2.setCurrentItem(0);
            } else {
                mViewPager2.setCurrentItem(currentPos + 1);
            }
        }
    };

    private void showdialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        b.setTitle("Sort by");
        String[] types = {"Price low to high", "Price high to low"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    case 0:
                        sortLowToHigh(loaiSanPham);
                        productAdapter.startListening();
                        break;
                    case 1:
                        sortHighToLow(loaiSanPham);
                        productAdapter.startListening();
                        break;
                }
            }

        });

        b.show();
    }

    private void unitUi() {
        edSearch = mView.findViewById(R.id.edSreach);
        recyclerViewListProduct = (RecyclerView) mView.findViewById(R.id.recyclerViewListProduct);
        recyclerViewListProduct_type = (RecyclerView) mView.findViewById(R.id.recyclerViewListProduct_type);
        mViewPager2 = (ViewPager2) mView.findViewById(R.id.mViewPager2);
        mCircleIndicator3 = (CircleIndicator3) mView.findViewById(R.id.mCircleIndicator3);
        btnSortListProduct = mView.findViewById(R.id.btnSortListProduct);
    }

    private void txtSreach(String str, String lsp) {
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp).child("SanPham").orderByChild("tenSP").startAt(str).endAt(str + "~"), Product.class)
                        .build();

        productAdapter = new ProductAdapter(options, new ProductAdapter.IClickProduct() {
            @Override
            public void onClickDetailsScreen(Product product) {
                onClickGoToDetail(product);
            }
        });
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
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecyclerViewListProduct();
    }

    private void getRecyclerViewListProduct() {
        getRecyclerViewListProduct_type();
        getRecyclerViewListProduct("lsp1");
    }

    private void getRecyclerViewListProduct(String lsp) {
        recyclerViewListProduct = mView.findViewById(R.id.recyclerViewListProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewListProduct.setLayoutManager(gridLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp).child("SanPham");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(myRef, Product.class)
                        .build();
        productAdapter = new ProductAdapter(options, new ProductAdapter.IClickProduct() {
            @Override
            public void onClickDetailsScreen(Product product) {
                onClickGoToDetail(product);
            }
        });
        recyclerViewListProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    private void getRecyclerViewListProduct_type() {
        createDialog();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.dismiss();
            }
        };
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
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 1000);
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
        progressDialog.setIcon(R.drawable.none_avatar);
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

    private void sortLowToHigh(String lsp) {
        recyclerViewListProduct = mView.findViewById(R.id.recyclerViewListProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewListProduct.setLayoutManager(gridLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp).child("SanPham");
        Query query = myRef.orderByChild("giaSP");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query, Product.class)
                        .build();

        productAdapter = new ProductAdapter(options, new ProductAdapter.IClickProduct() {
            @Override
            public void onClickDetailsScreen(Product product) {

            }
        });
        recyclerViewListProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    private void sortHighToLow(String lsp) {
        recyclerViewListProduct = mView.findViewById(R.id.recyclerViewListProduct);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewListProduct.setLayoutManager(gridLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("duan").child("LoaiSanPham").child(lsp).child("SanPham");
        Query query = myRef.orderByChild("giaSP");


        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query, Product.class)
                        .build();

        productAdapter = new ProductAdapter(options, new ProductAdapter.IClickProduct() {
            @Override
            public void onClickDetailsScreen(Product product) {
                onClickGoToDetail(product);
            }
        });
        recyclerViewListProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    public void onClickGoToDetail(Product product) {
        Intent intent = new Intent(getContext(), DetailsScreenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SanPham", product);
        bundle.putString("lsp", loaiSanPham);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}