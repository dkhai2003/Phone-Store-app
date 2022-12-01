package com.example.duan1.views;

import static com.example.duan1.views.HomeScreenActivity.myRef;

import android.app.ProgressDialog;
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
import com.example.duan1.model.User;
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
    private ProgressDialog progressDialog;
    private TextView userPhoneNumber, userAddress, userName;

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
        btnConfirmAndPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomSheetDialog();
            }
        });

        getUserInformation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
////            getFragmentManager().popBackStack();
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void clickOpenBottomSheetDialog() {
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


    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        btnConfirmAndPay = findViewById(R.id.btnConfirmAndPay);
        tvTotalCheckOut = findViewById(R.id.tvTotalCheckOut);
        userPhoneNumber = (TextView) findViewById(R.id.userPhoneNumber);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userName = (TextView) findViewById(R.id.userName);
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

    private void updateBillToFireBase1() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Task<DataSnapshot> gia= myRef().child("Total").get();
        Log.d(TAG, "updateBillToFireBase: "+ gia);
        myRef().child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
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