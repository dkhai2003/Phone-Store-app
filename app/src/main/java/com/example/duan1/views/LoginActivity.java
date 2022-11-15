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
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private TextView tvForgot, tvCreate;
    private Button btnLogin;
    private EditText edtEmail, edtPass;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("duan");
    private ProgressDialog progressDialog;

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
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Connecting to the server ... ");
                onClickSignInUser();
            }
        });
    }

    public void setTvCreate() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSignInUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        if (validate(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");

                                // <== Get UserId
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();

                                // ==>
                                progressDialog.dismiss();
                                startMainActivityMethod(userId);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            progressDialog.dismiss();
        }
    }

    TextInputLayout inpPass;

    public void unitUi() {
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvCreate = (TextView) findViewById(R.id.tvCreate);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        inpPass = findViewById(R.id.inpPass);
    }

    // <== Validate form
    private Boolean validate(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            inpPass.setPasswordVisibilityToggleEnabled(true);
            return true;
        }
        if (email.isEmpty()) {
            edtEmail.setError("Email is empty !");
        }
        if (password.isEmpty()) {
            inpPass.setPasswordVisibilityToggleEnabled(false);
            edtPass.setError("Password is empty !");
        } else {
            inpPass.setPasswordVisibilityToggleEnabled(true);
        }
        return false;
    } //==>

    // <== Start intent to LoginActivity.class
    private void startMainActivityMethod(String userId) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("userId", userId);
        startActivity(i);
        finish();
    }    // ==>

}