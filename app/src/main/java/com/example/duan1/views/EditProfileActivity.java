package com.example.duan1.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
    private ImageView ivAvatar, updateAvatar, btnVeri, btnNewPass;
    private Button btnSaveProfile;
    private Uri uriImage;
    private TextView edName, edPhone, edAddress, edGender, edBirthDay, edEmail, tvEmail, tvName, name, phone, address, gender, birthday, email;
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
                            setBitMapImageViewAvatar(bitmap);
                            setUriImage(uri);
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

    //    ==>
    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        edName = findViewById(R.id.edName);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        edEmail = findViewById(R.id.edEmail);
        edPhone = findViewById(R.id.edPhone);
        ivAvatar = findViewById(R.id.ivAvatar);
        updateAvatar = findViewById(R.id.updateAvatar);
        edGender = findViewById(R.id.edGender);
        edAddress = findViewById(R.id.edAddress);
        edBirthDay = findViewById(R.id.edBirthDay);
        btnVeri = findViewById(R.id.btnVeri);
        btnNewPass = findViewById(R.id.btnNewPass);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.gender);
        birthday = findViewById(R.id.birthday);
        email = findViewById(R.id.email);
    }

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
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
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
        edName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetContext(edName, name);
            }
        });
        edPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetContext(edPhone, phone);
            }
        });
        edAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetContext(edAddress, address);
            }
        });
        edGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetContext(edGender, gender);
            }
        });
        edBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetContext(edBirthDay, birthday);
            }
        });
        edEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetContext(edEmail, email);
            }
        });
    }

    private void onClickSetContext(TextView context, TextView title_context) {
        View viewDialogContext = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_profile, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditProfileActivity.this);
        TextView dialog_title = viewDialogContext.findViewById(R.id.dialog_title);
        dialog_title.setText(title_context.getText().toString());
        EditText edContext = viewDialogContext.findViewById(R.id.edContext);
        edContext.setHint("Enter your change ...");
        Button btnSaveUpdate = viewDialogContext.findViewById(R.id.btnSaveUpdate);
        Button btnCancelUpdate = viewDialogContext.findViewById(R.id.btnCancelUpdate);
        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setText(edContext.getText().toString().trim());
                bottomSheetDialog.dismiss();
            }
        });
        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(viewDialogContext);
        bottomSheetDialog.show();
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
                progressDialog.dismiss();
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
                                                            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                                                            FirebaseAuth.getInstance().signOut();
                                                            startActivity(intent);
                                                            finishAffinity();
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
                                Toast.makeText(EditProfileActivity.this, "Đã gửi email xác thực về tài khoản gmail của bạn vui lòng kiểm tra!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "email xác thực đã được gửi vui lòng kiểm tra lại ở mục thư rác!!", Toast.LENGTH_SHORT).show();
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
                return;
//                String[] permisstions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                this.requestPermissions(permisstions, MY_REQUEST_CODE);
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
                        edName.setText(userDisplayName);
                        tvName.setText(userDisplayName);
                    } else {
                        edName.setText(CurrentUser.getUserName());
                        tvName.setText(CurrentUser.getUserName());
                    }
                    if (CurrentUser.getEmail() == null) {
                        edEmail.setText(userEmail);
                        tvEmail.setText(userEmail);
                    } else {
                        edEmail.setText(CurrentUser.getEmail());
                        tvEmail.setText(CurrentUser.getEmail());
                    }
                    edPhone.setText(CurrentUser.getPhoneNumber());
                    edGender.setText(CurrentUser.getGender());
                    edBirthDay.setText(CurrentUser.getBirthday());
                    edAddress.setText(CurrentUser.getAddress());
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
                String updateName = edName.getText().toString().trim();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(updateName)
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            } else {
                progressDialog.show();
                String updateName = edName.getText().toString().trim();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(updateName)
                        .setPhotoUri(uriImage)
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "Update Successful 1", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            progressDialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            };
            String updateEmail = edEmail.getText().toString().trim();
            String updateName = edName.getText().toString().trim();
            String updatePhoneNumber = edPhone.getText().toString().trim();
            String updateAddress = edAddress.getText().toString().trim();
            String updateGenDer = edGender.getText().toString().trim();
            String updateBirthDay = edBirthDay.getText().toString().trim();
            Boolean verifyEmail = checkVerifiedEmail();
            checkVerifiedEmail();
            User updateUser = new User(updateEmail, updateName, updatePhoneNumber, updateAddress, updateGenDer, updateBirthDay, verifyEmail);
            myRef.child(pathUserId)
                    .updateChildren(updateUser.toMap(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Log.d("SaveUidToRealtime", "saveIdU");
                            if (uriImage != null) {
                                Glide.with(getApplicationContext()).load(uriImage).error(R.drawable.none_avatar).into(ivAvatar);
                            }
                            progressDialog.dismiss();
                        }
                    });
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1000);
        }
    }

    private Boolean checkVerifiedEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getFragmentManager().popBackStack();
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