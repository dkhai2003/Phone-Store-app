package com.example.duan1.views;

import static com.example.duan1.views.HomeScreenActivity.myRef;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.duan1.model.HoaDon;
import com.example.duan1.model.Product;
import com.example.duan1.model.User;
import com.example.duan1.zaloaccess.CreateOrder;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnConfirmAndPay, btnZaloPay;
    private TextView tvTotalCheckOut, userName, userAddress, userPhoneNumber;
    private String TAG = "=====";
    private ProgressDialog progressDialog;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        setTotalCheckOut(tvTotalCheckOut);
        btnConfirmAndPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomSheetDialog(view);
            }
        });
        //zalo
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX);
        btnZaloPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPayWithZaloPay();
            }
        });

        getUserInformation();
    }

    private void clickPayWithZaloPay() {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(tvTotalCheckOut.getText().toString());
            Log.d("Amount", tvTotalCheckOut.getText().toString());
            String code = data.getString("returncode");
            Log.d("code", code + "");
            if (code.equals("1")) {
                String token = data.getString("zptranstoken");
                ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(CheckOutActivity.this)
                                        .setTitle("Payment Success")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", null).show();
                            }

                        });
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void clickOpenBottomSheetDialog(View view) {
        View viewDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        Button btnPayNow = viewDialog.findViewById(R.id.btnPayNow);
        TextView tvCost = viewDialog.findViewById(R.id.tvCost);
        setTotalCheckOut(tvCost);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(CheckOutActivity.this, "This is Button PayNow", Toast.LENGTH_SHORT).show();


                updateBillToFireBase1();
                Intent intent = new Intent(CheckOutActivity.this, FinishedPaymentActivity.class);
                startActivity(intent);
                finish();
//                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            getFragmentManager().popBackStack();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            // Check if user's email is verified
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String uEmail = user.getEmail();
            String userDisplayName = user.getDisplayName();
            String[] subEmail = uEmail.split("@");
            String pathUserId = "User" + subEmail[0];
            DatabaseReference myRef = database.getReference("duan/User/");
            if (user == null) {
                return;
            } else {
                createDialog();
                progressDialog.show();
                myRef.child(pathUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User CurrentUser = snapshot.getValue(User.class);
                        if (CurrentUser.getUserName() == null) {
                            userName.setText(userDisplayName);
                        } else {
                            userName.setText(CurrentUser.getUserName());
                        }
                        userAddress.setText(CurrentUser.getAddress());
                        userPhoneNumber.setText(CurrentUser.getPhoneNumber());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "getInformationUserFromFirebase:error");
                        progressDialog.dismiss();
                    }
                });
            }
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
        } else {
            Toast.makeText(this, "No User", Toast.LENGTH_SHORT).show();
        }
    }


    private void unitUi() {
        btnZaloPay = findViewById(R.id.btnZaloPay);
        userName = findViewById(R.id.userName);
        userAddress = findViewById(R.id.userAddress);
        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        toolbar = findViewById(R.id.toolbar);
        btnConfirmAndPay = findViewById(R.id.btnConfirmAndPay);
        tvTotalCheckOut = findViewById(R.id.tvTotalCheckOut);
        userPhoneNumber = (TextView) findViewById(R.id.userPhoneNumber);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userName = (TextView) findViewById(R.id.userName);
    }

    public void setTotalCheckOut(TextView textView) {
        myRef().child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double value = snapshot.getValue(Double.class);
                textView.setText("$" + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateBillToFireBase1() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Task<DataSnapshot> gia = myRef().child("Total").get();
        Log.d(TAG, "updateBillToFireBase: " + gia);
        myRef().child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int soLuong = (int) snapshot.getChildrenCount();

                Map<String, Product> map = new HashMap<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    Product value = dataSnapshot.getValue(Product.class);
                    map.put(key, value);
                }
                myRef().child("HoaDon").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int value = (int) snapshot.getChildrenCount();
                        if (soLuong > 0) {

                            String id = "hd000" + value;
                            HoaDon hoaDon = new HoaDon(formatter.format(date), id, tvTotalCheckOut.getText().toString(), soLuong);
                            Log.d(TAG, "onDataChange: " + value);
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

//    public void getUserInformation() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            // Check if user's email is verified
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            String uEmail = user.getEmail();
//            String userDisplayName = user.getDisplayName();
//            String[] subEmail = uEmail.split("@");
//            String pathUserId = "User" + subEmail[0];
//            DatabaseReference myRef = database.getReference("duan/User/");
//            if (user == null) {
//                return;
//            } else {
//                createDialog();
//                progressDialog.show();
//                myRef.child(pathUserId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        User CurrentUser = snapshot.getValue(User.class);
//                        if (CurrentUser.getUserName() == null) {
//                            userName.setText(userDisplayName);
//                        } else {
//                            userName.setText(CurrentUser.getUserName());
//                        }
//                        userAddress.setText(CurrentUser.getAddress());
//                        userPhoneNumber.setText(CurrentUser.getPhoneNumber());
//                        progressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d("TAG", "getInformationUserFromFirebase:error");
//                        progressDialog.dismiss();
//                    }
//                });
//            }
//            boolean emailVerified = user.isEmailVerified();
//            String uid = user.getUid();
//        } else {
//            Toast.makeText(this, "No User", Toast.LENGTH_SHORT).show();
//        }
//    }


    private void createDialog() {
        progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Connecting to the server ... ");
        progressDialog.setIcon(R.drawable.none_avatar);
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