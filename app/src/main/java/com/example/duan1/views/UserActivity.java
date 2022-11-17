package com.example.duan1.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.fragment.FrofileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView userName, userEmail;
    private ImageView userAvatar;
    private Button btnLogout;
    private Button btnEditProfile;
    private FrameLayout frameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        unitUi();
        getUserInformation();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEditProfile();
            }
        });
    }

    private void onClickEditProfile() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameUser, FrofileFragment.newInstance()).commit();
    }

    private void onClickLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        Toast.makeText(this, "You are log out !", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    private void getUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(userAvatar);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    private void unitUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userAvatar = (ImageView) findViewById(R.id.userAvatar);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        frameUser = (FrameLayout) findViewById(R.id.frameUser);
    }
}
