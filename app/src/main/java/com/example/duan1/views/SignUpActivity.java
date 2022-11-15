package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;
import com.example.duan1.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirm_pass;
    private Button btnRegister;
    private long maxid;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("duan/User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        unitUi();
        getIdUser();
    }

    private void getIdUser() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid = (snapshot.getChildrenCount());
//                    Toast.makeText(SignUpActivity.this, "" + maxid, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickSignUpUser(View view) {
        String maUser = "user" + maxid;
        String tenDangNhap = edtEmail.getText().toString().trim();
        String matKhau = edtPassword.getText().toString().trim();
        String reMatKhau = edtConfirm_pass.getText().toString().trim();
        if (checkValidate(tenDangNhap, matKhau, reMatKhau)) {
            if (matKhau.equals(reMatKhau)) {
                User user = new User(maUser, matKhau, tenDangNhap);
                myRef.child("u" + maxid).setValue(user.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.putExtra("maUser", maUser);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                Toast.makeText(this, "Nhập lại mật khẩu sai", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    //Ánh Xạ
    public void unitUi() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirm_pass = (EditText) findViewById(R.id.edtConfirm_pass);
        TextInputLayout input1 = findViewById(R.id.input1);
        input1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                if (pass.isEmpty()) {
//                    edtPassword.setError("Password is empty !");
//                }
//                if (rePass.isEmpty()) {
//                    edtConfirm_pass.setError("Re Password is empty !");
//                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    input1.setError("Email is empty !");
                    input1.setErrorEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public Boolean checkValidate(String tenDangNhap, String pass, String rePass) {
        if (!tenDangNhap.isEmpty() || !pass.isEmpty() || !rePass.isEmpty()) {
            return true;
        }
        return false;
    }

    public void setTvLogin(View view) {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}