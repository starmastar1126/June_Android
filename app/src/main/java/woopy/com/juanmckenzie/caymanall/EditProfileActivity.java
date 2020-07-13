package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.common.MediaPickerDialog;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.landing.TermsOfUse;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.FileUtils;
import woopy.com.juanmckenzie.caymanall.utils.PermissionsUtils;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

public class EditProfileActivity extends BaseActivity {

    private static final int CAMERA_PERMISSION_REQ_CODE = 11;
    private static final int GALLERY_PERMISSION_REQ_CODE = 12;

    private static final int CAMERA_REQ_CODE = 0;
    private static final int GALLERY_REQ_CODE = 1;

    private static final int IMAGE_SIZE = 800;

    private EditText usernameET;
    private EditText fullnameET;
    private EditText websiteET;
    private EditText aboutET;
    private ImageView avatarIV;
    private TextView changeAvatarTV;
    private TextView resetPasswordTV;
    private TextView termsTV;
    private ImageView backIV;
    private ImageView saveIV;

    private String currentPhotoPath;

    Configs myapplication;

    private RadioButton usedConditionRB;
    private RadioGroup conditionRG;
    private RadioButton newConditionRB;
    String gander = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        myapplication = (Configs) getApplicationContext();


        initViews();
        setUpViews();

        // Call query
        showMyDetails();


        findViewById(R.id.deleteaccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditProfileActivity.this);
                alert.setMessage(getString(R.string.are_you_sure_you_want_to_delete_your_account))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showLoading();
                                final String firebaseid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            FirebaseDatabase.getInstance().getReference().child("users").child(firebaseid).removeValue();
                                            FirebaseDatabase.getInstance().getReference().child("ads").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        Ads ads = dataSnapshot1.getValue(Ads.class);
                                                        if (ads.getSellerPointer().getFirebaseID().equals(firebaseid)) {
                                                            FirebaseDatabase.getInstance().getReference().child("ads").child(ads.getID()).removeValue();
                                                        }
                                                    }

                                                    FirebaseDatabase.getInstance().getReference().child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                EventObj ads = dataSnapshot1.getValue(EventObj.class);
                                                                if (ads.getSellerPointer().getFirebaseID().equals(firebaseid)) {
                                                                    FirebaseDatabase.getInstance().getReference().child("Events").child(ads.getID()).removeValue();
                                                                }
                                                            }

                                                            FirebaseDatabase.getInstance().getReference().child("News").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        EventObj ads = dataSnapshot1.getValue(EventObj.class);
                                                                        if (ads.getSellerPointer().getFirebaseID().equals(firebaseid)) {
                                                                            FirebaseDatabase.getInstance().getReference().child("News").child(ads.getID()).removeValue();
                                                                        }
                                                                    }

                                                                    FirebaseDatabase.getInstance().getReference().child("chatstindertype").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                if (dataSnapshot1.getKey().contains(firebaseid)) {
                                                                                    FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(dataSnapshot1.getKey()).removeValue();
                                                                                }
                                                                            }
                                                                            FirebaseDatabase.getInstance().getReference().child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                                        if (dataSnapshot1.getKey().contains(firebaseid)) {
                                                                                            FirebaseDatabase.getInstance().getReference().child("chats").child(dataSnapshot1.getKey()).removeValue();
                                                                                        }
                                                                                    }
                                                                                    hideLoading();
                                                                                    Intent homeIntent = new Intent(EditProfileActivity.this, HomeActivity.class);
                                                                                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                    startActivity(homeIntent);
                                                                                }


                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                        }


                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }


                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });


                                                        }


                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        } else {
                                            Toast.makeText(EditProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            hideLoading();
                                        }


                                    }
                                });


                            }
                        })

                        .setNegativeButton(getString(R.string.cancel), null)
                        .setIcon(R.drawable.logo);
                alert.create().show();
            }
        });

    }

    // IMAGE PICKED DELEGATE -----------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQ_CODE:
                    onSelectFromGalleryResult(data);
                    break;
                case CAMERA_REQ_CODE:
                    onCaptureImageResult(currentPhotoPath);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case CAMERA_PERMISSION_REQ_CODE:
                    openCamera();
                    break;
                case GALLERY_PERMISSION_REQ_CODE:
                    openGallery();
                    break;
            }
        }
    }

    private void initViews() {
        usernameET = findViewById(R.id.aep_username_et);
        fullnameET = findViewById(R.id.aep_fullname_et);
        websiteET = findViewById(R.id.aep_website_et);
        aboutET = findViewById(R.id.aep_about_et);
        avatarIV = findViewById(R.id.aep_avatar_iv);
        changeAvatarTV = findViewById(R.id.aep_change_avatar_tv);
        resetPasswordTV = findViewById(R.id.aep_reset_password_tv);
        termsTV = findViewById(R.id.aep_terms_tv);
        backIV = findViewById(R.id.aep_back_iv);
        saveIV = findViewById(R.id.aep_save_iv);
    }

    private void setUpViews() {
        usernameET.setTypeface(Configs.titRegular);
        fullnameET.setTypeface(Configs.titRegular);
        websiteET.setTypeface(Configs.titRegular);
        aboutET.setTypeface(Configs.titRegular);
        resetPasswordTV.setTypeface(Configs.titSemibold);
        termsTV.setTypeface(Configs.titSemibold);

        // MARK: - CHANGE AVATAR IMAGE ----------------------------------------
        View.OnClickListener changeAvatarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeAvatarDialog();
            }
        };
        avatarIV.setOnClickListener(changeAvatarClickListener);
        changeAvatarTV.setOnClickListener(changeAvatarClickListener);

        // MARK: - SAVE PROFILE BUTTON ------------------------------------
        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Configs.isConnected()) {
                    Configs.loginAlert(getString(R.string.please_check_your_internat_connection), EditProfileActivity.this);
                    return;
                }

                User user = myapplication.getCurrentUser();

                if (usernameET.getText().toString().matches("") || fullnameET.getText().toString().matches("")) {
                    Configs.simpleAlert(getString(R.string.must_insert_at_least_username),
                            EditProfileActivity.this);

                } else {
                    UIUtils.hideKeyboard(EditProfileActivity.this);

                    user.setUsername(usernameET.getText().toString());
                    user.setFullName(fullnameET.getText().toString());
                    if (!websiteET.getText().toString().matches("")) {
                        user.setWebsite(websiteET.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("users/" + user.getFirebaseID()).child("website").setValue(user.getWebsite());
                    }
                    if (!aboutET.getText().toString().matches("")) {
                        user.setAboutMe(aboutET.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("users/" + user.getFirebaseID()).child("aboutMe").setValue(user.getAboutMe());
                    }

                    FirebaseDatabase.getInstance().getReference().child("users/" + user.getFirebaseID()).child("username").setValue(user.getUsername());
                    FirebaseDatabase.getInstance().getReference().child("users/" + user.getFirebaseID()).child("fullName").setValue(user.getFullName());
                    myapplication.setCurrentUser(user);
                    // Save Avatar
                    showLoading();
                    saveParseImage(avatarIV);

                }
            }
        });

        // MARK: - RESET PASSWORD BUTTON ------------------------------------
        resetPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle(R.string.app_name)
                        .setIcon(R.drawable.logo)
                        .setMessage(getString(R.string.type_valid_email_address));

                // Add an EditTxt
                final EditText editTxt = new EditText(EditProfileActivity.this);
                editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                alert.setView(editTxt)
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.reset_password), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                FirebaseAuth.getInstance().sendPasswordResetEmail(editTxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                            builder.setMessage(getString(R.string.reset_email_sent))
                                                    .setTitle(R.string.app_name)
                                                    .setPositiveButton(getString(R.string.ok), null);
                                            AlertDialog dialog = builder.create();
                                            dialog.setIcon(R.drawable.logo);
                                            dialog.show();
                                        } else {
                                            Configs.simpleAlert(task.getException().getMessage(), EditProfileActivity.this);

                                        }
                                    }
                                });
                            }
                        });
                alert.show();
            }
        });

        // MARK: - TERMS OF SERVICE BUTTON ----------------------------------------------------------
        termsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, TermsOfUse.class));
            }
        });

        // MARK: - BACK BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showChangeAvatarDialog() {
        MediaPickerDialog dialog = new MediaPickerDialog(this);
        dialog.setOnOptionSelectedListener(new MediaPickerDialog.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int index) {
                if (index == MediaPickerDialog.CAMERA_OPTION_INDEX) {
                    String[] permissions = {Manifest.permission.CAMERA};
                    if (PermissionsUtils.hasPermissions(EditProfileActivity.this, permissions)) {
                        openCamera();
                    } else {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, permissions,
                                CAMERA_PERMISSION_REQ_CODE);
                    }
                } else if (index == MediaPickerDialog.GALLERY_OPTION_INDEX) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    if (PermissionsUtils.hasPermissions(EditProfileActivity.this, permissions)) {
                        openGallery();
                    } else {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, permissions,
                                GALLERY_PERMISSION_REQ_CODE);
                    }
                }
            }
        });
        dialog.show();
    }

    // MARK: - SHOW MY DETAILS ----------------------------------------------------------------
    void showMyDetails() {


        conditionRG = findViewById(R.id.ase_Status_rg);
        newConditionRB = findViewById(R.id.ase_new_Status_rb);
        usedConditionRB = findViewById(R.id.ase_used_Status_rb);

        usernameET.setText(myapplication.getCurrentUser().getUsername());
        fullnameET.setText(myapplication.getCurrentUser().getFullName());

        if (myapplication.getCurrentUser().getWebsite() != null) {
            websiteET.setText(myapplication.getCurrentUser().getWebsite());
        }
        if (myapplication.getCurrentUser().getAboutMe() != null) {
            aboutET.setText(myapplication.getCurrentUser().getAboutMe());
        }
        if (myapplication.getCurrentUser().getGander().equals("Male")) {
            setConditionRB(newConditionRB, true);
            setConditionRB(usedConditionRB, false);
        } else {
            setConditionRB(newConditionRB, false);
            setConditionRB(usedConditionRB, true);
        }


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);

        Glide.with(this).setDefaultRequestOptions(requestOptions)
                .load(myapplication.getCurrentUser().getAvatar().getImage1024()).into(avatarIV);


        conditionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ase_new_Status_rb:
                        setConditionRB(newConditionRB, true);
                        setConditionRB(usedConditionRB, false);
                        gander = "Male";

                        showLoading();
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gander").setValue(gander).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideLoading();
                            }
                        });
                        break;
                    case R.id.ase_used_Status_rb:
                        setConditionRB(newConditionRB, false);
                        setConditionRB(usedConditionRB, true);
                        gander = "Female";
                        showLoading();
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gander").setValue(gander).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideLoading();
                            }
                        });
                        break;
                }
            }
        });

    }

    private void setConditionRB(RadioButton radioButton, boolean isChecked) {
        if (isChecked) {
            radioButton.setTextColor(UIUtils.getColor(R.color.white));
            radioButton.setBackgroundColor(UIUtils.getColor(R.color.main_color));
        } else {
            radioButton.setTextColor(UIUtils.getColor(R.color.black));
            radioButton.setBackgroundResource(R.drawable.edit_text_shape_bg);
        }
    }


    // OPEN CAMERA_REQ_CODE
    public void openCamera() {
        if (Camera.getNumberOfCameras() == 0) {
            ToastUtils.showMessage("There is no available camera.");
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = FileUtils.createEmptyFile("image.jpg", Configs.IMAGE_FORMAT);
        currentPhotoPath = photoFile.getAbsolutePath();

        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                ".provider", photoFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(takePictureIntent, CAMERA_REQ_CODE);
    }

    // OPEN GALLERY_REQ_CODE
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), GALLERY_REQ_CODE);
    }

    private void onCaptureImageResult(String photoPath) {
        Bitmap bitmap = FileUtils.getPictureFromPath(photoPath, IMAGE_SIZE);

        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return;
        }
        displayAvatar(photoPath, bitmap);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = FileUtils.decodeIntentData(data.getData(), IMAGE_SIZE);

        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return;
        }
        displayAvatar(data.getDataString(), bitmap);
    }

    private void displayAvatar(String path, Bitmap bitmap) {
        bitmap = FileUtils.processExif(path, bitmap);
        avatarIV.setImageBitmap(bitmap);
    }


    // MARK: - SHORTCUT TO SAVE A PARSE IMAGE
    public void saveParseImage(ImageView imgView) {
        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        UplaodImage(byteArray);
    }


    public void UplaodImage(byte[] byteArray) {

        ImageObj imageObj = new ImageObj();
        imageObj.setID(FirebaseAuth.getInstance().getCurrentUser().getUid() + "");
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Files/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        UploadTask uploadTask = ref.putBytes(byteArray);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                hideLoading();
                ToastUtils.showMessage(getString(R.string.your_profile_has_been_updated));
                if (task.isSuccessful()) {

                    Uri downloadUri = task.getResult();
                    imageObj.setUrl(downloadUri.toString());
                    FirebaseDatabase.getInstance().getReference().child("users/" + myapplication.getCurrentUser().getFirebaseID() + "/avatar").setValue(imageObj);
                    Log.d("ImageUrlFirebase", downloadUri.toString());


                } else {
                }


            }
        });

    }

}
