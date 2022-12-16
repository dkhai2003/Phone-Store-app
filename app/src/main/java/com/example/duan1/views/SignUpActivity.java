package com.example.duan1.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirm_pass;
    private Button btnRegister;
    private TextView tvLogin;
    private long countUsers;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("duan/User");
    private ProgressDialog progressDialog;
    private TextInputLayout inpPass, inpRePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        unitUi();
        getCountUser();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Connecting to the server ... ");
                onClickSignUpUser();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivityMethod();
            }
        });
    }

    // <== Get count user from database
    private void getCountUser() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    countUsers = (snapshot.getChildrenCount());
                    Log.d("countUsers:get", countUsers + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }  // ==>


    // <== SignUp user to Authentication and save UserId to database
    public void onClickSignUpUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String repassword = edtConfirm_pass.getText().toString().trim();
        if (validate(email, password, repassword)) {
            if (password.equals(repassword)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");

                                    // <== Save user to database
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userEmail = user.getEmail();
                                    String[] subEmail = userEmail.split("@");
                                    myRef.child("User" + subEmail[0]).child("UserId")
                                            .setValue(mAuth.getUid(), new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    Log.d("SaveUidToRealtime", "saveIdU");
                                                    Toast.makeText(SignUpActivity.this, "Create Account Successful", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    myRef.child("User" + subEmail[0]).child("Total").setValue(0);
                                    Log.d("UserInformation", user.getUid());
                                    // ==>
                                    progressDialog.dismiss();
                                    startLoginActivityMethod();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if(password.length()<6){
                                        Toast.makeText(SignUpActivity.this, "vui lòng nhập mật khẩu trên 6 ký tự",
                                            Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(SignUpActivity.this, "vui lòng nhập đúng định dạng email",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
//                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog.dismiss();
        }
    }    // ==>


    // <== Validate form
    private Boolean validate(String email, String password, String repassword) {
        if (!email.isEmpty() && !password.isEmpty() && !repassword.isEmpty()) {
            inpPass.setPasswordVisibilityToggleEnabled(true);
            inpRePass.setPasswordVisibilityToggleEnabled(true);
            return true;
        }
        if (email.isEmpty()) {
            edtEmail.setError("Email is empty !");
        }
        if (password.isEmpty()) {
            inpPass.setPasswordVisibilityToggleEnabled(false);
            edtPassword.setError("Password is empty !");

        } else {
            inpPass.setPasswordVisibilityToggleEnabled(true);
        }
        if (repassword.isEmpty()) {
            inpRePass.setPasswordVisibilityToggleEnabled(false);
            edtConfirm_pass.setError("Re-password is empty !");

        } else {
            inpRePass.setPasswordVisibilityToggleEnabled(true);
        }
        return false;
    } //==>

    // <== Mapping
    public void unitUi() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirm_pass = (EditText) findViewById(R.id.edtConfirm_pass);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        inpPass = (TextInputLayout) findViewById(R.id.inpPass);
        inpRePass = (TextInputLayout) findViewById(R.id.inpRePass);
    }    // ==>


    // <== Start intent to LoginActivity.class
    private void startLoginActivityMethod() {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}