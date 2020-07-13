package woopy.com.juanmckenzie.caymanall.landing;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import woopy.com.juanmckenzie.caymanall.BuildConfig;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.MarshMallowPermission;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;

public class SignUp extends AppCompatActivity {

    /* Views */
    ImageView avatarImg;
    EditText usernameTxt, passwordTxt, fullnameTxt, emailTxt;


    /* Variables */
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    boolean isAccepted = false;
    FirebaseStorage storage;


    User user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Configs myApplication;
    Dialog progress;
    RadioButton Female, Male;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Female = findViewById(R.id.Female);
        Male = findViewById(R.id.Male);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        myApplication = (Configs) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        progress = Configs.buildProgressLoadingDialog(this);

        // Hide Status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Init views
        avatarImg = findViewById(R.id.cadAvatarImg);
        usernameTxt = findViewById(R.id.usernameTxt2);
        passwordTxt = findViewById(R.id.passwordTxt2);
        fullnameTxt = findViewById(R.id.fullnameTxt);
        emailTxt = findViewById(R.id.emailTxt2);
        usernameTxt.setTypeface(Configs.titSemibold);
        passwordTxt.setTypeface(Configs.titSemibold);
        fullnameTxt.setTypeface(Configs.titSemibold);
        emailTxt.setTypeface(Configs.titSemibold);


        // MARK: - CAMERA BUTTON ----------------------------------------
        Button camButt = findViewById(R.id.signupCamButt);
        camButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForCamera()) {
                    mmp.requestPermissionForCamera();
                } else {
                    openCamera();
                }
            }
        });


        // MARK: - GALLERY BUTTON ----------------------------------------
        Button galButt = findViewById(R.id.signupGalleryButt);
        galButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForReadExternalStorage()) {
                    mmp.requestPermissionForReadExternalStorage();
                } else {
                    openGallery();
                }
            }
        });


        // MARK: - CHECKBOX BUTTON ------------------------------------
        final Button checkboxButt = findViewById(R.id.supCheckBoxButt);
        checkboxButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAccepted = true;
                checkboxButt.setBackgroundResource(R.drawable.checkbox_on);
            }
        });


        // SIGN UP BUTTON ------------------------------------------------------------------------
        Button signupButt = findViewById(R.id.signUpButt2);
        signupButt.setTypeface(Configs.titBlack);
        signupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // USER HAS ACCEPTED THE TERMS OF SERVICE
                if (isAccepted) {

                    if (usernameTxt.getText().toString().matches("") || passwordTxt.getText().toString().matches("") ||
                            emailTxt.getText().toString().matches("") || fullnameTxt.getText().toString().matches("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setMessage(getString(R.string.you_must_fill_all_fields))
                                .setTitle(R.string.app_name)
                                .setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.setIcon(R.drawable.logo);
                        dialog.show();


                    } else {
                        progress.show();
                        dismisskeyboard();

                        user = new User();
                        user.setUsername(usernameTxt.getText().toString());
                        user.setEmail(emailTxt.getText().toString());
                        user.setFullName(fullnameTxt.getText().toString());
                        user.setReported(false);
                        user.setHasBlocked(new ArrayList<String>());
                        user.setEmail(emailTxt.getText().toString());


                        mAuth.createUserWithEmailAndPassword(user.getEmail().trim(), passwordTxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Now save avatar
                                    myApplication.setSessionUserID(mAuth.getCurrentUser().getUid());
                                    user.setFirebaseID(mAuth.getCurrentUser().getUid());
                                    user.setEmailVerified(false);

                                    if (Female.isChecked()) {
                                        user.setGander("Female");
                                    }
                                    if (Male.isChecked()) {
                                        user.setGander("Male");
                                    }

                                    user.setJoindate(Calendar.getInstance().getTimeInMillis() + "");
                                    ImageObj imageObj = new ImageObj();
                                    imageObj.setUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Avatars%2F" + user.getFirebaseID());
                                    imageObj.setID(user.getFirebaseID());
                                    user.setAvatar(imageObj);
                                    user.setJoindate(Calendar.getInstance().getTime().getTime() + "");
                                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    myApplication.saveParseImage(avatarImg);
                                                    progress.dismiss();
                                                    startActivity(new Intent(SignUp.this, HomeActivity.class));
                                                }
                                            });

                                        }
                                    });


                                } else {
                                    progress.dismiss();
                                    Configs.simpleAlert(task.getException().getMessage(), SignUp.this);
                                }
                            }
                        });

                    }


                    // USER HAS NOT ACCEPTED THE TERMS OF SERVICE
                } else {
                    Configs.simpleAlert(getString(R.string.terms_and_conditions_message), SignUp.this);
                }

            }
        });


        // MARK: - TERMS OF SERVICE BUTTON ----------------------------------------------------------
        Button tosButt = findViewById(R.id.touButt);
        tosButt.setTypeface(Configs.titSemibold);
        tosButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, TermsOfUse.class));
            }
        });


        // MARK: - DISMISS BUTTON ---------------------------------------------------------------
        Button dismissButt = findViewById(R.id.signupDismissButt);
        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }// end onCreate()


    // IMAGE HANDLING METHODS ------------------------------------------------------------------------
    int CAMERA = 0;
    int GALLERY = 1;
    String imageURI;
    File file;

    private File createEmptyFile() {
        File storageDir = getCacheDir();
        File myDir = null;
        try {
            myDir = File.createTempFile("image", ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("log-", "Image file creation failure: " + e.getMessage());
            e.printStackTrace();
        }
        return myDir;
    }


    // OPEN CAMERA
    public void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageTakenFile = createEmptyFile();
        imageURI = imageTakenFile.getAbsolutePath();
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                ".provider", imageTakenFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA);
    }


    private Bitmap onCaptureImageResult(String photoPath) {
        Bitmap bitmap = getbitmap(photoPath);

        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return null;
        }
        return bitmap;
    }


    public Bitmap getbitmap(String mCurrentPhotoPath) {
        int rotate = 0;
        try {
            File imageFile = new File(mCurrentPhotoPath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        int targetW = 640;
        int targetH = 640;

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        /* Associate the Bitmap to the ImageView */
        return bitmap;
    }


    // OPEN GALLERY
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), GALLERY);
    }


    // IMAGE PICKED DELEGATE -----------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Bitmap bm = null;

            // Image from Camera
            if (requestCode == CAMERA) {

                bm = onCaptureImageResult(imageURI);

                // Image from Gallery
            } else if (requestCode == GALLERY) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            // Set image
//            Bitmap scaledBm = Configs.scaleBitmapToMaxSize(bm.getWidth(), bm);
            avatarImg.setImageBitmap(bm);
        }
    }


    //---------------------------------------------------------------------------------------------


    // DISMISS KEYBOARD
    public void dismisskeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(fullnameTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailTxt.getWindowToken(), 0);
    }


}//@end