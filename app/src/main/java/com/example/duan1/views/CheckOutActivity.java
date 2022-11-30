package com.example.duan1.views;

import static com.example.duan1.views.HomeScreenActivity.myRef;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.duan1.R;
import com.example.duan1.fragment.HomeFragment;
import com.example.duan1.model.HoaDon;
import com.example.duan1.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnConfirmAndPay;
    private TextView tvTotalCheckOut;
    private String TAG = "=====";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        unitUi();
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Check Out");

//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTotalCheckOut(tvTotalCheckOut);

    }



    @Override
    protected void onResume() {
        super.onResume();
        btnConfirmAndPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomSheetDialog();
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

    public void clickOpenBottomSheetDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        Button btnPayNow = viewDialog.findViewById(R.id.btnPayNow);
        TextView tvCost = viewDialog.findViewById(R.id.tvCost);
        setTotalCheckOut(tvCost);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckOutActivity.this, "This is Button PayNow", Toast.LENGTH_SHORT).show();
               // updateBillToFireBase();
                updateBillToFireBase();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(androidx.appcompat.R.id.home, new HomeFragment()).commit();

//                Intent intent = new Intent(CheckOutActivity.this, HomeFragment.class);
//                startActivityFromFragment(HomeFragment.newInstance(),intent,1);
//                finish();
//                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();
    }


    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        btnConfirmAndPay = findViewById(R.id.btnConfirmAndPay);
        tvTotalCheckOut = findViewById(R.id.tvTotalCheckOut);
    }

    public void setTotalCheckOut(TextView textView){
        myRef().child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double value = snapshot.getValue(Double.class);
                textView.setText("$"+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateBillToFireBase() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Task<DataSnapshot> gia= myRef().child("Total").get();
        Log.d(TAG, "updateBillToFireBase: "+ gia);
        myRef().child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int soLuong = (int) snapshot.getChildrenCount();

                Map<String,Product> map = new HashMap<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    Product value = dataSnapshot.getValue(Product.class);
                    map.put(key,value);
                }
                myRef().child("HoaDon").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int value = (int) snapshot.getChildrenCount();
                        if (soLuong>0){

                            String id = "hd000"+value;
                            HoaDon hoaDon = new HoaDon(formatter.format(date),id,tvTotalCheckOut.getText().toString(),soLuong);
                            Log.d(TAG, "onDataChange: "+value);
                            myRef().child("HoaDon").child(id).setValue(hoaDon);
                            myRef().child("HoaDon").child(id).child("Cart").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    myRef().child("Cart").removeValue();
                                    myRef().child("Total").setValue(0);
                                }
                            });

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



//    public DatabaseReference myRef(){
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userEmail = user.getEmail();
//        String[] subEmail = userEmail.split("@");
//        String pathUserId = "User" + subEmail[0];
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("duan/User").child(pathUserId);
//        return myRef;
//    }

}