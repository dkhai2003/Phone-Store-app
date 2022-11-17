package com.example.duan1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.views.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FrofileFragment extends Fragment {
    private View mView;
    private EditText edUsername, edEmail;
    private ImageView ivAvatar;
    private ImageButton ibBack;

    public static FrofileFragment newInstance() {
        Bundle args = new Bundle();
        FrofileFragment fragmentFrofile = new FrofileFragment();
        fragmentFrofile.setArguments(args);
        return fragmentFrofile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frofile_edit, container, false);
        unitUi();
        setUserInformation();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                Intent intent = new Intent(getContext(), UserActivity.class);
                startActivity(intent);

            }
        });
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            edUsername.setText(user.getDisplayName());
            edEmail.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(ivAvatar);
        }
    }

    private void unitUi() {
        edUsername = (EditText) mView.findViewById(R.id.edUsername);
        edEmail = (EditText) mView.findViewById(R.id.edEmail);
        ivAvatar = (ImageView) mView.findViewById(R.id.ivAvatar);
        ibBack = (ImageButton) mView.findViewById(R.id.ibBack);
    }
}