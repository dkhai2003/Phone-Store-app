package com.example.duan1.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duan1.R;

public class LoginActivity extends AppCompatActivity {
    TextView tvHello,tvLogin,tvForgot,tvCreate;
    Button btnLogin;
    LinearLayout lnEmail,lnPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();


    }

    public void setTvCreate(View view){
        Intent intent= new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    public void setBtnLogin(View view){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void anhXa(){
        tvHello=(TextView) findViewById(R.id.tvHello);
        tvLogin=(TextView) findViewById(R.id.tvLogin);
        tvForgot=(TextView) findViewById(R.id.tvForgot);
        tvCreate=(TextView) findViewById(R.id.tvCreate);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        lnEmail=(LinearLayout) findViewById(R.id.lnEmail);
        lnPass=(LinearLayout) findViewById(R.id.lnPass);
    }
}