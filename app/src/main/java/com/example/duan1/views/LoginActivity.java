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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private TextView tvForgot, tvCreate;
    private Button btnLogin;
    private LoginButton btnLoginFacebook;
    private SignInButton btnGoogleSignIn;
    private EditText edtEmail, edtPass;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ProgressDialog progressDialogLogin, progressDialogLoginGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private Integer reCode = 0;

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
                progressDialogLogin = new ProgressDialog(LoginActivity.this);
                progressDialogLogin.setTitle("Please Wait..");
                progressDialogLogin.setMessage("Connecting to the server ... ");
                onClickSignInUser();
            }
        });
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickForgotPassword();
            }
        });
        //<== Login Google Authenticate
        createRequest();
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialogLoginGoogle = new ProgressDialog(LoginActivity.this);
                progressDialogLoginGoogle.setTitle("Please Wait..");
                progressDialogLoginGoogle.setMessage("Connecting to Google ... ");
                onClickSignInGoogle();
            }
        });// ==>
        //<== Login Facebook
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reCode = 1;
                callbackManager = CallbackManager.Factory.create();
                btnLoginFacebook.setReadPermissions("email", "public_profile");
                btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("TAG", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG", "facebook:onError", error);
                    }
                });
            }
        });

        //==>
    }

    private void onClickForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d("TAG", "handleFacebookAccessToken:" + accessToken);
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userEmail = user.getEmail();
                            Log.d("TAG1", userEmail);
                            String[] subEmail = userEmail.split("@");
                            DatabaseReference myRef = database.getReference("duan/User");
                            myRef.child("User" + subEmail[0]).child("UserId")
                                    .setValue(mAuth.getUid(), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Log.d("SaveUidToRealtime", "saveIdU");

                                        }
                                    });
                            Log.d("User Google", user.getEmail());
                            startMainActivityMethod(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onClickSignInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void createRequest() {
        // <==Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startMainActivityMethod(user.getUid());
        } else {
            mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
            mGoogleSignInClient.revokeAccess();
        }
        // ==>

        // <== Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        // ==>
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
        if (reCode == 1) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialogLoginGoogle.show();
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userEmail = user.getEmail();
                            String[] subEmail = userEmail.split("@");
                            DatabaseReference myRef = database.getReference("duan/User");
                            myRef.child("User" + subEmail[0]).child("UserId")
                                    .setValue(mAuth.getUid(), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Log.d("SaveUidToRealtime", "saveIdU");

                                        }
                                    });
                            Log.d("User Google", user.getEmail());
                            progressDialogLoginGoogle.dismiss();
                            startMainActivityMethod(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
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
        progressDialogLogin.show();
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
                                progressDialogLogin.dismiss();
                                startMainActivityMethod(userId);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialogLogin.dismiss();
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            progressDialogLogin.dismiss();
        }
    }

    TextInputLayout inpPass;

    public void unitUi() {
        btnLoginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvCreate = (TextView) findViewById(R.id.tvCreate);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPassword);
        inpPass = (TextInputLayout) findViewById(R.id.inpPass);
        btnGoogleSignIn = (SignInButton) findViewById(R.id.btnGoogleSignIn);
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
        Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
        startActivity(i);
        finish();
    }    // ==>

}