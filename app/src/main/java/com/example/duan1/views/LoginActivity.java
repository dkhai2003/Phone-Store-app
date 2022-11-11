package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;

public class LoginActivity extends AppCompatActivity {
    private TextView tvHello, tvLogin, tvForgot, tvCreate;
    private Button btnLogin;
    private LinearLayout lnEmail, lnPass;

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void unitUi() {
        tvHello = (TextView) findViewById(R.id.tvHello);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvCreate = (TextView) findViewById(R.id.tvCreate);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        lnEmail = (LinearLayout) findViewById(R.id.lnEmail);
        lnPass = (LinearLayout) findViewById(R.id.lnPass);
    }
}