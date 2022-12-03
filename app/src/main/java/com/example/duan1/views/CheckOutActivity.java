package com.example.duan1.views;

import static com.example.duan1.views.HomeScreenActivity.myRef;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.example.duan1.R;
import com.example.duan1.model.CreateOrder;
import com.example.duan1.model.HoaDon;
import com.example.duan1.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnConfirmAndPay,btnZaLoPay;
    private TextView tvTotalCheckOut;
    private String TAG = "=====";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        unitUi();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Check Out");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTotalCheckOut(tvTotalCheckOut);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        btnZaLoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Amount","abc");
                CreateOrder orderApi = new CreateOrder();

                try {
                    JSONObject data = orderApi.createOrder("100000");
                    String code = data.getString("return_code");

                    if (code.equals("1")) {
                        Log.d("Amount","abc");
                        String token =  data.getString("zp_trans_token");
                        ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {

                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {

                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



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
        btnZaLoPay=findViewById(R.id.btnZaloPay);
    }




    public void setTotalCheckOut(TextView textView){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];
        DatabaseReference myRef = database.getReference("duan/User/" + pathUserId);
        myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
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
        double gia =0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        myRef().child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double value = snapshot.getValue(Double.class);
                Log.d(TAG, "onDataChange: "+value);
                value = gia;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef().child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
//                        if (value>0){

                            String id = "hd000"+value;
                            HoaDon hoaDon = new HoaDon(formatter.format(date),id,value,gia);
                            Log.d(TAG, "onDataChange: "+value);
                            myRef().child("HoaDon").child(id).setValue(hoaDon);
                            myRef().child("HoaDon").child(id).child("Cart").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
//                                    myRef().child("Cart").removeValue();
//                                    myRef().child("Total").setValue(0);
                                }
                            });

//                        }

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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
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