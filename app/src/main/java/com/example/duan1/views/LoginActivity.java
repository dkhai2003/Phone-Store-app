package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;
import com.example.duan1.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private TextView tvHello, tvLogin, tvForgot, tvCreate;
    private Button btnLogin;
    private EditText edtEmail, edtPass;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("duan");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unitUi();
        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTvCreate();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnLogin();
            }
        });
    }

    public void setTvCreate() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void setBtnLogin() {
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtPass.getText().toString().trim();
        ArrayList<User> list = new ArrayList<>();
        myRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    list.add(user);
                }
                Log.d("Tag", String.valueOf(list.size()));
                for (User u : list) {
                    if (u.getTenDangNhap().equals(email) && u.getMatKhau().equals(matKhau)) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Firebase Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unitUi() {
        tvHello = (TextView) findViewById(R.id.tvHello);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvCreate = (TextView) findViewById(R.id.tvCreate);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
    }

}