package com.example.duan1.views;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView userName, userEmail;
    private ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        unitUi();
        getUserInformation();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
            Glide.with(this).load(photoUrl).error(R.drawable.avatar).into(userAvatar);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userAvatar = findViewById(R.id.userAvatar);
    }
}
