package com.example.duan1.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.fragment.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private Toolbar toolbar;
    private EditText edUsername, edPhoneNumber, edSex, edAddress, edJob, edAge;
    private ImageView ivAvatar, updateAvatar;
    private Button btnUpdate, btnVeri;
    private Uri uriImage;
    private TextView tvStatus;
    private ProgressDialog progressDialog;
    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) {
                            Toast.makeText(EditProfileActivity.this, "Image Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            Uri uri = intent.getData();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            setUriImage(uri);
                            setBitMapImageViewAvatar(bitmap);
                        } catch (IOException e) {
                            Log.d("IntentResult", e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Image Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private void setUriImage(Uri uri) {
        uriImage = uri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        unitUi();
        unitListener();
        setUserInformation();
        checkVerifiedEmail();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Profile");
    }

    private void unitListener() {
        updateAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermisstion();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(EditProfileActivity.this);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Connecting to the server ... ");
                onClickUpdateProfile();
            }
        });
        btnVeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendVeri();
            }
        });
    }

    private void onClickSendVeri() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "Email sent.");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "CC", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    //<== Code request permission
    private void onClickRequestPermisstion() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permisstions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(permisstions, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "You are DENIED permisstions", Toast.LENGTH_SHORT).show();

                String[] permisstions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                this.requestPermissions(permisstions, MY_REQUEST_CODE);
            }
        }
    }

    //==>
    //<== code Open Gallery
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void setBitMapImageViewAvatar(Bitmap bitMapImageViewAvatar) {
        ivAvatar.setImageBitmap(bitMapImageViewAvatar);
    }

    //==>
    private void unitUi() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edUsername = (EditText) findViewById(R.id.edUsername);
        edPhoneNumber = (EditText) findViewById(R.id.edPhoneNumber);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        updateAvatar = (ImageView) findViewById(R.id.updateAvatar);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnVeri = (Button) findViewById(R.id.btnVeri);
        edSex = (EditText) findViewById(R.id.edSex);
        edJob = (EditText) findViewById(R.id.edJob);
        edAddress = (EditText) findViewById(R.id.edAddress);
        edAge = (EditText) findViewById(R.id.edAge);
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else {
            edUsername.setText(user.getDisplayName());
            edPhoneNumber.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(ivAvatar);
        }
    }

    private void onClickUpdateProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        DatabaseReference myRef = database.getReference("duan/User/User" + subEmail[0]);
        progressDialog.show();
        if (user == null) {
            return;
        } else {
            if (uriImage == null) {
                String updateName = edUsername.getText().toString().trim();
                String updatePhoneNumber = edPhoneNumber.getText().toString().trim();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(updateName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    UserFragment a = new UserFragment();
                                    a.reload(2);
                                    Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            } else {
                String updateName = edUsername.getText().toString().trim();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(updateName)
                        .setPhotoUri(uriImage)
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    UserFragment a = new UserFragment();
                                    a.reload(2);
                                    Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            String updatePhoneNumber = edPhoneNumber.getText().toString().trim();
            String updateAddress = edAddress.getText().toString().trim();
            String updateJob = edJob.getText().toString().trim();
            String updateSex = edSex.getText().toString().trim();
            String updateAge = edAge.getText().toString().trim();
            myRef.child("Age")
                    .setValue(updateAge, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Log.d("SaveUidToRealtime", "saveIdU");
                        }
                    });
            checkVerifiedEmail();
            progressDialog.dismiss();
        }
    }

    private void checkVerifiedEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
            tvStatus.setText("Status: Yes");
        } else {
            tvStatus.setText("Status: No");
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}