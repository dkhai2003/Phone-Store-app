package com.example.duan1.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.duan1.R;

public class SignUpActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword,edtConfirm_pass;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        anhXa();

    }

    public void setBtnRegister(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    public void setTvLogin(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }

    //Ánh Xạ
    public void anhXa(){
        btnRegister=(Button) findViewById(R.id.btnRegister);
        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtPassword=(EditText) findViewById(R.id.edtPassword);
        edtConfirm_pass=(EditText) findViewById(R.id.edtConfirm_pass);
    }
}