package com.example.duan1.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.User;
import com.example.duan1.views.EditProfileActivity;
import com.example.duan1.views.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserFragment extends Fragment {
    private Toolbar toolbar;
    private TextView userName, userEmail, userAddress, userPhoneNumber;
    private ImageView userAvatar;
    private Button btnLogout;
    private Button btnEditProfile;
    private FrameLayout frameUser;
    private View mView;
    private ProgressDialog progressDialog;
    public static final String TAG = UserFragment.class.getName();

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

    private void createDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Connecting to the server ... ");
        progressDialog.setIcon(R.drawable.none_avatar);
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
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void onClickLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void getUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(userAvatar);
            // Check if user's email is verified
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String uEmail = user.getEmail();
            String userDisplayName = user.getDisplayName();
            String[] subEmail = uEmail.split("@");
            String pathUserId = "User" + subEmail[0];
            DatabaseReference myRef = database.getReference("duan/User/");
            if (user == null) {
                return;
            } else {
                createDialog();
                progressDialog.show();
                myRef.child(pathUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User CurrentUser = snapshot.getValue(User.class);
                        if (CurrentUser.getUserName() == null) {
                            userName.setText(userDisplayName);
                        } else {
                            userName.setText(CurrentUser.getUserName());
                        }
                        if (CurrentUser.getEmail() == null) {
                            userEmail.setText(uEmail);
                        } else {
                            userEmail.setText(CurrentUser.getEmail());
                        }
                        userAddress.setText(CurrentUser.getAddress());
                        userPhoneNumber.setText(CurrentUser.getPhoneNumber());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "getInformationUserFromFirebase:error");
                        progressDialog.dismiss();
                    }
                });
            }
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
        } else {
            Toast.makeText(getContext(), "No User", Toast.LENGTH_SHORT).show();
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
        userPhoneNumber = (TextView) mView.findViewById(R.id.tvPhoneNumber);
        userAddress = (TextView) mView.findViewById(R.id.tvAddress);
    }
}