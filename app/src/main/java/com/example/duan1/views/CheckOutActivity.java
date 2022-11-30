package com.example.duan1.views;

import android.app.ProgressDialog;
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

import com.example.duan1.R;
import com.example.duan1.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnConfirmAndPay;
    private TextView userPhoneNumber, userAddress, userName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        unitUi();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Check Out");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnConfirmAndPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomSheetDialog();
            }
        });
        getUserInformation();
    }

    private void clickOpenBottomSheetDialog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        Button btnPayNow = viewDialog.findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckOutActivity.this, "This is Button PayNow", Toast.LENGTH_SHORT).show();

               // updateBillToFireBase();
//                updateBillToFireBase();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(androidx.appcompat.R.id.home, new HomeFragment()).commit();

//                Intent intent = new Intent(CheckOutActivity.this, HomeFragment.class);
//                startActivityFromFragment(HomeFragment.newInstance(),intent,1);
                finish();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getFragmentManager().popBackStack();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {
        progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Connecting to the server ... ");
        progressDialog.setIcon(R.drawable.none_avatar);
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
        toolbar = findViewById(R.id.toolbar);
        btnConfirmAndPay = findViewById(R.id.btnConfirmAndPay);
        userPhoneNumber = (TextView) findViewById(R.id.userPhoneNumber);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userName = (TextView) findViewById(R.id.userName);
    }
}