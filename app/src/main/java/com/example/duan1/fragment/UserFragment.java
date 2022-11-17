package com.example.duan1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserFragment extends Fragment {
    private Toolbar toolbar;
    private TextView userName, userEmail;
    private ImageView userAvatar;
    private Button btnLogout;
    private Button btnEditProfile;
    private FrameLayout frameUser;
    private View mView;

    public static UserFragment newInstance() {
        Bundle args = new Bundle();
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameUser, FrofileFragment.newInstance()).commit();
    }

    private void onClickLogout() {
        FirebaseAuth.getInstance().signOut();
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
        toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        userName = (TextView) mView.findViewById(R.id.userName);
        userEmail = (TextView) mView.findViewById(R.id.userEmail);
        userAvatar = (ImageView) mView.findViewById(R.id.userAvatar);
        btnLogout = (Button) mView.findViewById(R.id.btnLogout);
        btnEditProfile = (Button) mView.findViewById(R.id.btnEditProfile);
        frameUser = (FrameLayout) mView.findViewById(R.id.frameUser);
    }
}