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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.User;
import com.example.duan1.views.AboutActivity;
import com.example.duan1.views.EditProfileActivity;
import com.example.duan1.views.LoginActivity;
import com.example.duan1.views.MapsActivity;
import com.example.duan1.views.OrderHistoryActivity;
import com.facebook.login.LoginManager;
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
    private Button btnEditProfile, btnOrderHistory, btnAddress, btnLogout,btnAboutUs;
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

        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHistory();
            }
        });
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMap();
            }
        });

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onClickMap() {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        startActivity(intent);
    }

    private void onClickEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void onClickHistory() {
        Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
        startActivity(intent);
    }

    private void onClickLogout() {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void getUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            createDialog();
            progressDialog.show();
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
                myRef.child(pathUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() == 0) {
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                        }
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "getInformationUserFromFirebase:error");
                        progressDialog.dismiss();
                    }
                });

            }
            boolean emailVerified = user.isEmailVerified();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        } else {
            progressDialog.dismiss();
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
        btnOrderHistory = (Button) mView.findViewById(R.id.btnOrderHistory);
        btnAddress = (Button) mView.findViewById(R.id.btnAddress);
        btnAboutUs = (Button) mView.findViewById(R.id.btnAboutUs);
    }

    private void createDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Connecting to the server ... ");
        progressDialog.setIcon(R.drawable.none_avatar);
    }

}