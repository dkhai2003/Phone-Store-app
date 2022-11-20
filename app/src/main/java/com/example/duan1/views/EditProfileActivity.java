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
import com.example.duan1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private Toolbar toolbar;
    private EditText edUsername, edEmail, edPhoneNumber, edSex, edAddress, edJob, edAge;
    private ImageView ivAvatar, updateAvatar;
    private Button btnUpdate, btnVeri, btnNewPass;
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
        getInformationUserFromFirebase();
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
                onClickUpdateProfile();
            }
        });
        btnVeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendVeri();
            }
        });
        btnNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdatePassword();
            }
        });
    }

    private void onClickUpdatePassword() {
        View viewDialogUpdate = getLayoutInflater().inflate(R.layout.bottom_sheet_change_pass, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditProfileActivity.this);
        EditText tvOldPass = viewDialogUpdate.findViewById(R.id.tvOldPass);
        EditText tvNewPass = viewDialogUpdate.findViewById(R.id.tvNewPass);
        EditText tvReNewPass = viewDialogUpdate.findViewById(R.id.tvReNewPass);
        Button btnCancelUpdate = viewDialogUpdate.findViewById(R.id.btnCancelUpdate);
        Button btnSaveUpdate = viewDialogUpdate.findViewById(R.id.btnSaveUpdate);
        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProgressDialog();
                progressDialog.show();
                String oldPassword = tvOldPass.getText().toString();
                String newPassword = tvNewPass.getText().toString().trim();
                String reNewPassword = tvReNewPass.getText().toString().trim();
                if (oldPassword.length() < 6) {
                    Toast.makeText(EditProfileActivity.this, "Current Password Error !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
                    if (checkLength(newPassword)) {
                        if (newPassword.equals(reNewPassword)) {
                            user.reauthenticate(authCredential)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            user.updatePassword(newPassword)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Re-entered password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Length must be more than 6", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        });
        bottomSheetDialog.setContentView(viewDialogUpdate);
        bottomSheetDialog.show();
    }

    private Boolean checkLength(String newPassword) {
        if (newPassword.length() >= 6) {
            return true;
        } else {
            return false;
        }
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
        edEmail = (EditText) findViewById(R.id.edEmail);
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
        btnNewPass = (Button) findViewById(R.id.btnNewPass);
    }

    private void getInformationUserFromFirebase() {
        createProgressDialog();
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String userDisplayName = user.getDisplayName();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];
        DatabaseReference myRef = database.getReference("duan/User/");
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.none_avatar).into(ivAvatar);
        if (user == null) {
            return;
        } else {
            myRef.child(pathUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User CurrentUser = snapshot.getValue(User.class);
                    if (CurrentUser.getUserName() == null) {
                        edUsername.setText(userDisplayName);
                    } else {
                        edUsername.setText(CurrentUser.getUserName());
                    }
                    if (CurrentUser.getEmail() == null) {
                        edEmail.setText(userEmail);
                    } else {
                        edEmail.setText(CurrentUser.getEmail());
                    }
                    edPhoneNumber.setText(CurrentUser.getPhoneNumber());
                    edSex.setText(CurrentUser.getSex());
                    edAge.setText(CurrentUser.getAge());
                    edAddress.setText(CurrentUser.getAddress());
                    edJob.setText(CurrentUser.getJob());
                    checkVerifiedEmail();
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", "getInformationUserFromFirebase:error");
                    progressDialog.dismiss();
                }
            });

        }
    }

    private void onClickUpdateProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String[] subEmail = userEmail.split("@");
        String pathUserId = "User" + subEmail[0];
        DatabaseReference myRef = database.getReference("duan/User/");
        createProgressDialog();
        progressDialog.show();
        if (user == null) {
            return;
        } else {
            if (uriImage == null) {
                String updateName = edUsername.getText().toString().trim();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(updateName)
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    UserFragment a = new UserFragment();
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
                                    Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            String updateEmail = edEmail.getText().toString().trim();
            String updateName = edUsername.getText().toString().trim();
            String updatePhoneNumber = edPhoneNumber.getText().toString().trim();
            String updateAddress = edAddress.getText().toString().trim();
            String updateJob = edJob.getText().toString().trim();
            String updateSex = edSex.getText().toString().trim();
            String updateAge = edAge.getText().toString().trim();
            Boolean verifyEmail = checkVerifiedEmail();
            checkVerifiedEmail();
            User updateUser = new User(updateEmail, updateName, updatePhoneNumber, updateAddress, updateSex, updateJob, updateAge, verifyEmail);
            myRef.child(pathUserId)
                    .setValue(updateUser, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Log.d("SaveUidToRealtime", "saveIdU");
                        }
                    });

            progressDialog.dismiss();
        }
    }

    private Boolean checkVerifiedEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
            tvStatus.setText("Status: Yes");
            return true;
        } else {
            tvStatus.setText("Status: No");
            return false;
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

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Connecting to the server ... ");
    }
}