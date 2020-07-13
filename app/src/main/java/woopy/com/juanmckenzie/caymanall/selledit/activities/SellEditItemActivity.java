package woopy.com.juanmckenzie.caymanall.selledit.activities;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parse.ParseGeoPoint;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import net.igenius.customcheckbox.CustomCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.gujun.android.taggroup.TagGroup;
import woopy.com.juanmckenzie.caymanall.BuildConfig;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Purchase;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.MapActivity;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.MediaPickerDialog;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity1;
import woopy.com.juanmckenzie.caymanall.formsjobs.JobApplications;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.ExifUtil;
import woopy.com.juanmckenzie.caymanall.utils.FileUtils;
import woopy.com.juanmckenzie.caymanall.utils.PermissionsUtils;
import woopy.com.juanmckenzie.caymanall.utils.RealPathUtil;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;


public class SellEditItemActivity extends BaseActivity implements LocationListener {

    public static final String EDIT_AD_OBJ_ID_EXTRA_KEY = "EDIT_AD_OBJ_ID_EXTRA_KEY";

    private static final int UPLOADING_IMAGE_SIZE = 800;
    private static final int SELECT_PAYMENT = 110;
    private static final int SELECT_LOCATION_REQ_CODE = 11;

    private static final int SELECT_CATEGORY_REQ_CODE = 12;
    private static final int SELECT_CATEGORY_REQ_CODE1 = 232;

    private static final int LOCATION_PERMISSION_REQ_CODE = 13;
    private static final int PHOTO_CAMERA_PERMISSION_REQ_CODE = 14;
    private static final int VIDEO_CAMERA_PERMISSION_REQ_CODE = 15;
    private static final int PHOTO_GALLERY_PERMISSION_REQ_CODE = 16;
    private static final int VIDEO_GALLERY_PERMISSION_REQ_CODE = 17;

    private static final int TAKE_IMAGE1_REQ_CODE = 18;
    private static final int TAKE_IMAGE2_REQ_CODE = 19;
    private static final int TAKE_IMAGE3_REQ_CODE = 20;

    private static final int TAKE_IMAGE4_REQ_CODE = 181;
    private static final int TAKE_IMAGE5_REQ_CODE = 191;
    private static final int TAKE_IMAGE6_REQ_CODE = 201;

    private static final int PICK_IMAGE1_REQ_CODE = 21;
    private static final int PICK_IMAGE2_REQ_CODE = 22;
    private static final int PICK_IMAGE3_REQ_CODE = 23;

    private static final int PICK_IMAGE4_REQ_CODE = 211;
    private static final int PICK_IMAGE5_REQ_CODE = 221;
    private static final int PICK_IMAGE6_REQ_CODE = 231;
    private static final int VIDEO_REQ_CODE = 24;

    private String[] locationPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] cameraPermissions = {Manifest.permission.CAMERA};
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    /* Views */
    private TextView screenTitleTV;
    private TextView backIV;
    private TextView doneIV;
    private FrameLayout addImage1FL;
    private FrameLayout addImage2FL;
    private FrameLayout addImage3FL;
    private FrameLayout addImage4FL;
    private FrameLayout addImage5FL;
    private FrameLayout addImage6FL;
    private ImageView addImage1IV;
    private ImageView addImage2IV;
    private ImageView addImage3IV;
    private ImageView addImage4IV;
    private ImageView addImage5IV;
    private ImageView addImage6IV;
    private TextView addImage1TV;
    private TextView addImage2TV;
    private TextView addImage3TV;
    private TextView addImage4TV;
    private TextView addImage5TV;
    private TextView addImage6TV;
    private FrameLayout addVideoFL;
    private ImageView addVideoIV;
    private TextView addVideoTV;
    private TextView categoryTV;
    private EditText titleET;
    private EditText priceET;
    private RadioGroup conditionRG;
    private RadioButton newConditionRB;
    private RadioButton usedConditionRB;


    private RadioGroup StatusRG;
    private RadioButton newStatusRB;
    private RadioButton usedStatusRB;

    private EditText descriptionET;
    private LinearLayout locationLL;
    private TextView locationTV;
    private TextView deleteAdTV;

    private Bitmap image1Bmp;
    private Bitmap image2Bmp;
    private Bitmap image3Bmp;

    private Bitmap image4Bmp;
    private Bitmap image5Bmp;
    private Bitmap image6Bmp;
    private Bitmap videoThumbnail;

    private String videoPath = null;
    private Uri videoURI = null;
    private String imageTakenPath;

    /* Variables */
    private Ads adObj;
    private Location currentLocation;
    private LocationManager locationManager;
    private String selectedCategory = "";
    private String selectedCategorysub = "";
    private String condition = "";
    private String Status = "Pending";
    CustomCheckBox customCheckBox;
    private Boolean Update = false;
    private int lastAskedPermissionsReqCode = -1;

    Boolean ImageSelected = false;
    ScrollView mScrollView;

    EditText entertagshere;
    TagGroup mTagGroup;

    LinearLayout tagslayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_edit);


        tagslayout = findViewById(R.id.tagslayout);
        mScrollView = findViewById(R.id.mScrollView);
//        mScrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return Scroll;
//            }
//        });
        storage = FirebaseStorage.getInstance();
        myapplication = (Configs) getApplicationContext();
        initViews();
        setUpViews();
        customCheckBox = findViewById(R.id.ase_feature_tv);
        // Get objectID for adObj
        String objectID = getIntent().getStringExtra(EDIT_AD_OBJ_ID_EXTRA_KEY);
        Log.i("log-", "OBJECT ID: " + objectID);

        // YOU'RE EDITING AN ITEM ------------------
        if (objectID != null) {
            Update = true;
            adObj = myapplication.getSelectedAd();
            screenTitleTV.setText(getString(R.string.edit_item));
            deleteAdTV.setVisibility(View.VISIBLE);
            // Call query
            showAdDetails();

            if (adObj.getPaymentdone()) {
                customCheckBox.setChecked(true);
                return;
            }
            // YOU'RE SELLING A NEW ITEM ---------------
        } else {

            adObj = new Ads();
            adObj.setFeatured(false);
            adObj.setPaymentdone(false);
            screenTitleTV.setText(getString(R.string.sell_an_item));
            deleteAdTV.setVisibility(View.GONE);

            // Set default variables
            condition = "New";
            Status = "Pending";
        }


        customCheckBox.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {

                if (adObj.getPaymentdone()) {
                    customCheckBox.setChecked(true);
                    return;
                }
                adObj.setFeatured(isChecked);
            }
        });
        findViewById(R.id.ase_feature_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adObj.getPaymentdone()) {
                    customCheckBox.setChecked(true);
                    return;
                }

                customCheckBox.setChecked(!customCheckBox.isChecked());
                adObj.setFeatured(customCheckBox.isChecked());
            }
        });


        if (PermissionsUtils.hasPermissions(this, locationPermissions)) {
            loadCurrentLocation();
        } else {
            lastAskedPermissionsReqCode = LOCATION_PERMISSION_REQ_CODE;
            ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_PERMISSION_REQ_CODE);
        }
    }

    // IMAGE/VIDEO PICKED DELEGATE ------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_CATEGORY_REQ_CODE:
                    selectedCategory = data.getStringExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY);
                    categoryTV.setText(selectedCategory);
                    myapplication.setSelectedCategory(selectedCategory);
                    Intent categoriesIntent = new Intent(SellEditItemActivity.this, CategoriesActivity1.class);
                    categoriesIntent.putExtra(CategoriesActivity1.SELECTED_CATEGORY_EXTRA_KEY, selectedCategory);
                    categoriesIntent.putExtra(CategoriesActivity1.INCLUDE_ALL_CATEGORY_EXTRA_KEY, false);
                    startActivityForResult(categoriesIntent, SELECT_CATEGORY_REQ_CODE1);

//                    if (selectedCategory.equals("Jobs")) {
//                        findViewById(R.id.addnewFeild).setVisibility(View.VISIBLE);
//                    } else {
//                        findViewById(R.id.addnewFeild).setVisibility(View.GONE);
//                    }

                    break;
                case SELECT_CATEGORY_REQ_CODE1:
                    selectedCategorysub = data.getStringExtra(CategoriesActivity1.SELECTED_CATEGORY_EXTRA_KEY);
                    categoryTV.setText(selectedCategory + " > " + selectedCategorysub);

//                    if (selectedCategory.toLowerCase().equals("housing")) {
//                        tagslayout.setVisibility(View.VISIBLE);
//                        mTagGroup.setVisibility(View.VISIBLE);
//                        tags = new ArrayList<>();
//                    } else {
//                        tags = new ArrayList<>();
//                        tagslayout.setVisibility(View.GONE);
//                        mTagGroup.setVisibility(View.GONE);
//                    }

                    break;
                case SELECT_LOCATION_REQ_CODE:
                    Location chosenLocation = data.getParcelableExtra(MapActivity.CHOSEN_LOCATION_EXTRA_KEY);
                    if (chosenLocation != null) {
                        currentLocation = chosenLocation;
                    }
                    loadCityCountryNames();
                    break;
                case TAKE_IMAGE1_REQ_CODE:
                    ImageSelected = true;
                    image1Bmp = onCaptureImageResult(imageTakenPath);
                    showAdImage(addImage1IV, addImage1TV, image1Bmp);
                    break;
                case TAKE_IMAGE2_REQ_CODE:
                    ImageSelected = true;
                    image2Bmp = onCaptureImageResult(imageTakenPath);
                    showAdImage(addImage2IV, addImage2TV, image2Bmp);
                    break;
                case TAKE_IMAGE3_REQ_CODE:
                    ImageSelected = true;
                    image3Bmp = onCaptureImageResult(imageTakenPath);
                    showAdImage(addImage3IV, addImage3TV, image3Bmp);
                    break;
                case TAKE_IMAGE4_REQ_CODE:
                    ImageSelected = true;
                    image4Bmp = onCaptureImageResult(imageTakenPath);
                    showAdImage(addImage4IV, addImage4TV, image4Bmp);
                    break;
                case TAKE_IMAGE5_REQ_CODE:
                    ImageSelected = true;
                    image5Bmp = onCaptureImageResult(imageTakenPath);
                    showAdImage(addImage5IV, addImage5TV, image5Bmp);
                    break;
                case TAKE_IMAGE6_REQ_CODE:
                    ImageSelected = true;
                    image6Bmp = onCaptureImageResult(imageTakenPath);
                    showAdImage(addImage6IV, addImage6TV, image6Bmp);
                    break;
                case PICK_IMAGE1_REQ_CODE:
                    ImageSelected = true;
                    image1Bmp = onSelectFromGalleryResult(data);
                    showAdImage(addImage1IV, addImage1TV, image1Bmp);
                    break;
                case PICK_IMAGE2_REQ_CODE:
                    ImageSelected = true;
                    image2Bmp = onSelectFromGalleryResult(data);
                    showAdImage(addImage2IV, addImage2TV, image2Bmp);
                    break;
                case PICK_IMAGE3_REQ_CODE:
                    ImageSelected = true;
                    image3Bmp = onSelectFromGalleryResult(data);
                    showAdImage(addImage3IV, addImage3TV, image3Bmp);
                    break;
                case PICK_IMAGE4_REQ_CODE:
                    ImageSelected = true;
                    image4Bmp = onSelectFromGalleryResult(data);
                    showAdImage(addImage4IV, addImage4TV, image4Bmp);
                    break;
                case PICK_IMAGE5_REQ_CODE:
                    ImageSelected = true;
                    image5Bmp = onSelectFromGalleryResult(data);
                    showAdImage(addImage5IV, addImage5TV, image5Bmp);
                    break;
                case PICK_IMAGE6_REQ_CODE:
                    ImageSelected = true;
                    image6Bmp = onSelectFromGalleryResult(data);
                    showAdImage(addImage6IV, addImage6TV, image6Bmp);
                    break;
                case VIDEO_REQ_CODE:
                    try {
                        videoURI = data.getData();
                        videoPath = getRealPathFromURI(videoURI);
                        Log.i("log-", "VIDEO PATH: " + videoPath);
                        Log.i("log-", "VIDEO URI: " + videoURI);

                        // Check video duration
                        MediaPlayer mp = MediaPlayer.create(this, videoURI);
                        int videoDuration = mp.getDuration();
                        mp.release();
                        Log.i("log-", "VIDEO DURATION: " + videoDuration);

                        if (videoPath == null) {
                            Configs.simpleAlert(getString(R.string.video_error_message), this);
                            break;
                        }
                        if (videoDuration < Configs.MAXIMUM_DURATION_VIDEO * 1100) {
//                        Video duration is within the allowed seconds
//                        Set video thumbnail
                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                            videoThumbnail = thumbnail;
                            if (thumbnail == null) {
                                addVideoIV.setVisibility(View.GONE);
                                addVideoTV.setVisibility(View.VISIBLE);
                                String filename = getFilenameFromPath(videoPath);
                                addVideoTV.setText(filename + "\n" + formatDuration(videoDuration));
                            } else {
                                addVideoTV.setText("+\nVIDEO");
                                showAdImage(addVideoIV, addVideoTV, thumbnail);
                            }
                        } else {
//                        Video exceeds the maximum allowed duration
                            Configs.simpleAlert(getString(R.string.your_video_no_longer) +
                                    String.valueOf(Configs.MAXIMUM_DURATION_VIDEO) +
                                    getString(R.string.seconds_please_choose), this);
//                        Reset variables and image
                            videoPath = null;
                            videoURI = null;
                            showAdImagePlaceholder(addVideoIV, addVideoTV);
                        }
                    } catch (Exception E) {
                        Configs.simpleAlert(getString(R.string.video_is_longer_then) +
                                String.valueOf(Configs.MAXIMUM_DURATION_VIDEO) +
                                getString(R.string.seconds_please_chose), this);
                    }
                    break;
                case SELECT_PAYMENT:
                    PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (confirm != null) {
                        adObj.setPaymentdone(true);
                        showLoading();
                        submitAd();
                        try {
                            Log.i("paymentExample", confirm.toJSONObject().toString(4));

                            // TODO: send 'confirm' to your server for verification.
                            // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                            // for more details.

                        } catch (JSONException e) {
                            Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case LOCATION_PERMISSION_REQ_CODE:
                    loadCurrentLocation();
                    break;
                case PHOTO_CAMERA_PERMISSION_REQ_CODE:
                    if (lastAskedPermissionsReqCode != -1) {
                        openCamera(lastAskedPermissionsReqCode);
                    }
                    break;
                case VIDEO_CAMERA_PERMISSION_REQ_CODE:
                    openVideoCamera();
                    break;
                case PHOTO_GALLERY_PERMISSION_REQ_CODE:
                    if (lastAskedPermissionsReqCode != -1) {
                        openGallery(lastAskedPermissionsReqCode);
                    }
                    break;
                case VIDEO_GALLERY_PERMISSION_REQ_CODE:
                    if (lastAskedPermissionsReqCode != -1) {
                        openGallery(lastAskedPermissionsReqCode);
                    }
                    break;
            }
            lastAskedPermissionsReqCode = -1;
        }
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


    List<String> tags = new ArrayList<>();

    private void initViews() {
        screenTitleTV = findViewById(R.id.ase_screen_title_tv);
        backIV = findViewById(R.id.ase_back_iv);
        doneIV = findViewById(R.id.ase_done_iv);
        addImage1FL = findViewById(R.id.ase_add_image1_fl);
        addImage2FL = findViewById(R.id.ase_add_image2_fl);
        addImage3FL = findViewById(R.id.ase_add_image3_fl);
        addImage1IV = findViewById(R.id.ase_add_image1_iv);
        addImage2IV = findViewById(R.id.ase_add_image2_iv);
        addImage3IV = findViewById(R.id.ase_add_image3_iv);
        addImage1TV = findViewById(R.id.ase_add_image1_tv);
        addImage2TV = findViewById(R.id.ase_add_image2_tv);
        addImage3TV = findViewById(R.id.ase_add_image3_tv);
        addVideoFL = findViewById(R.id.ase_add_video_fl);
        addVideoIV = findViewById(R.id.ase_add_video_iv);
        addVideoTV = findViewById(R.id.ase_add_video_tv);


        entertagshere = findViewById(R.id.entertagshere);
        mTagGroup = (TagGroup) findViewById(R.id.tag_group);

        tagslayout.setVisibility(View.VISIBLE);
        mTagGroup.setVisibility(View.VISIBLE);

        findViewById(R.id.addtags).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!entertagshere.getText().toString().equals("")) {
                    tags.add("  " + entertagshere.getText().toString() + "  ");
                    mTagGroup.setTags(tags);
                    entertagshere.setText("");
                }

            }
        });


        addImage4FL = findViewById(R.id.ase_add_image4_fl);
        addImage5FL = findViewById(R.id.ase_add_image5_fl);
        addImage6FL = findViewById(R.id.ase_add_image6_fl);
        addImage4IV = findViewById(R.id.ase_add_image4_iv);
        addImage5IV = findViewById(R.id.ase_add_image5_iv);
        addImage6IV = findViewById(R.id.ase_add_image6_iv);
        addImage4TV = findViewById(R.id.ase_add_image4_tv);
        addImage5TV = findViewById(R.id.ase_add_image5_tv);
        addImage6TV = findViewById(R.id.ase_add_image6_tv);


        categoryTV = findViewById(R.id.ase_category_tv);
        titleET = findViewById(R.id.ase_item_title_et);
        priceET = findViewById(R.id.ase_item_price_et);
        conditionRG = findViewById(R.id.ase_condition_rg);
        newConditionRB = findViewById(R.id.ase_new_condition_rb);
        usedConditionRB = findViewById(R.id.ase_used_condition_rb);


        StatusRG = findViewById(R.id.ase_Status_rg);
        newStatusRB = findViewById(R.id.ase_new_Status_rb);
        usedStatusRB = findViewById(R.id.ase_used_Status_rb);

        descriptionET = findViewById(R.id.ase_item_description_et);
        descriptionET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Scroll = hasFocus;
            }
        });
        locationLL = findViewById(R.id.ase_location_ll);
        locationTV = findViewById(R.id.ase_location_tv);
        deleteAdTV = findViewById(R.id.ase_delete_ad_tv);


        findViewById(R.id.addnewFeild).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FormsActivity.class));

            }
        });
    }

    Boolean Scroll = false;

    private void setUpViews() {
        // MARK: - BACK BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        conditionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ase_new_condition_rb:
                        setConditionRB(newConditionRB, true);
                        setConditionRB(usedConditionRB, false);
                        condition = "New";
                        break;
                    case R.id.ase_used_condition_rb:
                        setConditionRB(newConditionRB, false);
                        setConditionRB(usedConditionRB, true);
                        condition = "Used";
                        break;
                }
            }
        });


        StatusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ase_new_Status_rb:
                        setConditionRB(newStatusRB, true);
                        setConditionRB(usedStatusRB, false);
                        Status = "Pending";
                        break;
                    case R.id.ase_used_Status_rb:
                        setConditionRB(newStatusRB, false);
                        setConditionRB(usedStatusRB, true);
                        Status = "Sold";
                        break;
                }
            }
        });

        // MARK: - UPLOAD IMAGE 1 ----------------------------------------------------------------
        addImage1FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog(TAKE_IMAGE1_REQ_CODE, PICK_IMAGE1_REQ_CODE);
            }
        });

        // MARK: - UPLOAD IMAGE 2 ----------------------------------------------------------------
        addImage2FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog(TAKE_IMAGE2_REQ_CODE, PICK_IMAGE2_REQ_CODE);
            }
        });

        // MARK: - UPLOAD IMAGE 3 ---------------------------------------------l-------------------
        addImage3FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog(TAKE_IMAGE3_REQ_CODE, PICK_IMAGE3_REQ_CODE);
            }
        });


        // MARK: - UPLOAD IMAGE 1 ----------------------------------------------------------------
        addImage4FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog(TAKE_IMAGE4_REQ_CODE, PICK_IMAGE4_REQ_CODE);
            }
        });

        // MARK: - UPLOAD IMAGE 2 ----------------------------------------------------------------
        addImage5FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog(TAKE_IMAGE5_REQ_CODE, PICK_IMAGE5_REQ_CODE);
            }
        });

        // MARK: - UPLOAD IMAGE 3 ----------------------------------------------------------------
        addImage6FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog(TAKE_IMAGE6_REQ_CODE, PICK_IMAGE6_REQ_CODE);
            }
        });

        // MARK: - UPLOAD VIDEO ----------------------------------------------------------------
        addVideoFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVideoPickerDialog();
            }
        });

        categoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(SellEditItemActivity.this, CategoriesActivity.class);
                categoriesIntent.putExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY, selectedCategory);
                categoriesIntent.putExtra(CategoriesActivity.INCLUDE_ALL_CATEGORY_EXTRA_KEY, false);
                startActivityForResult(categoriesIntent, SELECT_CATEGORY_REQ_CODE);
            }
        });

        // MARK: - CHOOSE LOCATION BUTTON ------------------------------------
        locationTV.setTypeface(Configs.titSemibold);
        locationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent = new Intent(SellEditItemActivity.this, MapActivity.class);
                startActivityForResult(locationIntent, SELECT_LOCATION_REQ_CODE);
            }
        });

        // MARK: - SUBMIT AD BUTTON -----------------------------------------------------------------
        doneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.hideKeyboard(SellEditItemActivity.this);
                Boolean check = false;
                String Message = "";
//                if (!ImageSelected) {
//                    check = true;
//                    Message = Message + "First image\n";
//                }

                if (TextUtils.isEmpty(titleET.getText())) {
                    check = true;
                    Message = Message + "Ad Title\n";
                }

                if (TextUtils.isEmpty(condition)) {
                    check = true;
                    Message = Message + "Condition of the item\n";
                }

                if (TextUtils.isEmpty(descriptionET.getText())) {
                    check = true;
                    Message = Message + "Description\n";
                }

                if (TextUtils.isEmpty(selectedCategory)) {
                    check = true;
                    Message = Message + "Category\n";
                }

                // You haven't filled all required the fields
                if (check) {
                    Configs.simpleAlert(getString(R.string.ads_create_complete_form) + Message, SellEditItemActivity.this);
                    // You can submit your Ad!
                } else {
                    if (!adObj.getFeatured()) {
                        showLoading();
                        submitAd();
                    } else {
                        if (adObj.getPaymentdone()) {
                            showLoading();
                            submitAd();
                            return;
                        }
                        PayPalConfiguration config = new PayPalConfiguration()
                                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                                // or live (ENVIRONMENT_PRODUCTION)
                                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

                                .clientId("<YOUR_CLIENT_ID>");


                        PayPalPayment payment = new PayPalPayment(new BigDecimal("35"), "USD", "Featuring ads",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent intent = new Intent(SellEditItemActivity.this, PaymentActivity.class);
                        // send the same configuration for restart resiliency
                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                        startActivityForResult(intent, SELECT_PAYMENT);
                    }

                }
            }
        });

        // MARK: - DELETE AD BUTTON
        deleteAdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SellEditItemActivity.this);
                alert.setMessage(getString(R.string.delete_message))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.delete_item), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteAdInOtherClasses();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setIcon(R.drawable.logo);
                alert.create().show();

            }
        });

        findViewById(R.id.viewapplications).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JobApplications.class));
            }
        });
    }

    private void setConditionRB(RadioButton radioButton, boolean isChecked) {
        if (isChecked) {
            radioButton.setTextColor(UIUtils.getColor(R.color.white));
            radioButton.setBackgroundResource(R.drawable.shapestart);
        } else {
            radioButton.setTextColor(UIUtils.getColor(R.color.black));
            radioButton.setBackgroundResource(R.drawable.shapeend);
        }
    }

    int files = 0;
    Configs myapplication;

    private void submitAd() {


        ParseGeoPoint userGP = loadUserLocationGeoPoint();

        myapplication = (Configs) getApplicationContext();
        adObj.setSellerPointer(myapplication.getCurrentUser());
        adObj.setTitle(titleET.getText().toString());
        adObj.setCategory(selectedCategory);
        adObj.setSubcategory(selectedCategorysub);
        adObj.setCondition(condition);
        adObj.setTags(tags);
        adObj.setADStatus(Status);
        adObj.setDescription(descriptionET.getText().toString());
        adObj.setLocation(userGP);
        if (priceET.getText().toString().equals("")) {
            adObj.setPrice(Double.parseDouble("0"));
        } else {
            adObj.setPrice(Double.parseDouble(priceET.getText().toString()));
        }
        adObj.setCurrency(Configs.CURRENCY);
        List<String> empty = new ArrayList<>();
        adObj.setLikedBy(empty);

        // Add keywords
        List<String> keywords = new ArrayList<>();
        String[] a = titleET.getText().toString().toLowerCase().split(" ");
        String[] b = descriptionET.getText().toString().toLowerCase().split(" ");
        keywords.addAll(Arrays.asList(a));
        keywords.addAll(Arrays.asList(b));
        keywords.add(condition.toLowerCase());
        keywords.add("@" + myapplication.getCurrentUser().getUsername().toLowerCase());
        keywords.addAll(tags);

        adObj.setKeywords(keywords);
        // In case this is a new Ad
        if (adObj.getID() == null) {
            adObj.setReported(false);
            adObj.setLikes(0);
            adObj.setComments(0);
            adObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
            adObj.setID(FirebaseDatabase.getInstance().getReference().child("ads").push().getKey());

        }

        FirebaseDatabase.getInstance().getReference().child("ads").child(adObj.getID()).setValue(adObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    // Save video
                    if (videoURI != null) {
                        files++;
                        uplaodvideo(videoURI, "video");

                        // Save thumbnail
                        if (videoThumbnail != null) {
                            files++;
                            ByteArrayOutputStream st = new ByteArrayOutputStream();
                            videoThumbnail.compress(Bitmap.CompressFormat.JPEG, 70, st);
                            byte[] byteArr = st.toByteArray();
                            uplaodfile(byteArr, "thumb");
                        }
                    }

                    // Save image1
                    if (image1Bmp != null) {
                        files++;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image1Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] byteArray = stream.toByteArray();
                        uplaodfile(byteArray, "image1");
                    }

                    // Save image2
                    if (image2Bmp != null) {
                        files++;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image2Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] byteArray = stream.toByteArray();
                        uplaodfile(byteArray, "image2");
                    }

                    // Save image3
                    if (image3Bmp != null) {
                        files++;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image3Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] byteArray = stream.toByteArray();
                        uplaodfile(byteArray, "image3");
                    }


                    // Save image1
                    if (image4Bmp != null) {
                        files++;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image4Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] byteArray = stream.toByteArray();
                        uplaodfile(byteArray, "image4");
                    }

                    // Save image2
                    if (image5Bmp != null) {
                        files++;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image5Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] byteArray = stream.toByteArray();
                        uplaodfile(byteArray, "image5");
                    }

                    // Save image3
                    if (image6Bmp != null) {
                        files++;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image6Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] byteArray = stream.toByteArray();
                        uplaodfile(byteArray, "image6");
                    }

                    if (files == 0) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/allDone").setValue(true);
                        AlertDialog.Builder alert = new AlertDialog.Builder(SellEditItemActivity.this);
                        String message = "";
                        if (Update) {
                            message = getString(R.string.add_updated);
                        } else {
                            message = getString(R.string.add_Created);
                        }
                        alert.setMessage(message)
                                .setTitle(R.string.app_name)
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Update) {
                                            hideLoading();
                                            return;
                                        }
                                        showLoading();
                                        HashMap<String, String> rawParameters = new HashMap<>();
                                        rawParameters.put("body", getString(R.string.new_add_found) + adObj.getCategory() + " > " + adObj.getSubcategory());
                                        rawParameters.put("title", getString(R.string.app_name));
                                        rawParameters.put("page", "openadd");
                                        rawParameters.put("addid", adObj.getID());
                                        NotificationSednToallusers(getApplicationContext(), rawParameters, adObj.getCategory() + " > " + adObj.getSubcategory());

                                    }
                                })
                                .setCancelable(false)
                                .setIcon(R.drawable.logo);
                        alert.create().show();
                    }


                } else {
                    hideLoading();
                    ToastUtils.showMessage(task.getException().getMessage());
                }
            }
        });


    }

    private ParseGeoPoint loadUserLocationGeoPoint() {
        ParseGeoPoint userGP;

        // Current location is detected
        if (currentLocation != null) {
            userGP = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            // No current location detected
        } else {
            currentLocation = new Location("provider");
            currentLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            currentLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);
            userGP = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        Log.i("log-", "USER GEOPOINT: " + userGP.getLatitude() + " -- " + userGP.getLongitude());
        return userGP;
    }

    private void showVideoPickerDialog() {
        MediaPickerDialog dialog = new MediaPickerDialog(this);
        dialog.setCameraOptionTitle("Take a Video");
        dialog.setOnOptionSelectedListener(new MediaPickerDialog.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int index) {
                if (index == MediaPickerDialog.CAMERA_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(SellEditItemActivity.this, cameraPermissions)) {
                        openVideoCamera();
                    } else {
                        ActivityCompat.requestPermissions(SellEditItemActivity.this,
                                cameraPermissions, VIDEO_CAMERA_PERMISSION_REQ_CODE);
                        lastAskedPermissionsReqCode = VIDEO_CAMERA_PERMISSION_REQ_CODE;
                    }
                } else if (index == MediaPickerDialog.GALLERY_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(SellEditItemActivity.this, galleryPermissions)) {
                        openVideoGallery();
                    } else {
                        ActivityCompat.requestPermissions(SellEditItemActivity.this,
                                galleryPermissions, VIDEO_GALLERY_PERMISSION_REQ_CODE);
                        lastAskedPermissionsReqCode = VIDEO_GALLERY_PERMISSION_REQ_CODE;
                    }
                }
            }
        });
        dialog.show();
    }

    // MARK: - SHOW AD's DETAILS ------------------------------------------------------------------
    void showAdDetails() {
//
//
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        if (adObj.getCategory().equals("Jobs")) {
            findViewById(R.id.viewapplications).setVisibility(View.VISIBLE);
        }

        if (adObj.getImage1() != null) {

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(adObj.getImage1())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage1IV, addImage1TV, resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage1IV, addImage1TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        if (adObj.getImage2() != null) {

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(adObj.getImage2())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage2IV, addImage2TV, (Bitmap) resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage2IV, addImage2TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        if (adObj.getImage3() != null) {
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(adObj.getImage3())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage3IV, addImage3TV, (Bitmap) resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage3IV, addImage3TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }


        if (adObj.getImage4() != null) {
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(adObj.getImage4())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage4IV, addImage4TV, (Bitmap) resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage4IV, addImage4TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        if (adObj.getImage5() != null) {
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(adObj.getImage5())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage5IV, addImage5TV, (Bitmap) resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage5IV, addImage5TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        if (adObj.getImage6() != null) {
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(adObj.getImage6())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage6IV, addImage6TV, (Bitmap) resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage6IV, addImage6TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }


        // Get video thumbnail
        if (adObj.getVideo() != null) {

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(adObj.getVideoThumbnail())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addVideoIV, addVideoTV, (Bitmap) resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addVideoIV, addVideoTV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


        }

        // Get category
        selectedCategory = adObj.getCategory();
        categoryTV.setText(selectedCategory);

        // Get Title
        titleET.setText(adObj.getTitle());

        // Get Price
        priceET.setText(String.valueOf(adObj.getPrice()));

        // Get condition
        condition = adObj.getCondition();
        if (condition.equals("New")) {
            conditionRG.check(R.id.ase_new_condition_rb);
        } else {
            conditionRG.check(R.id.ase_used_condition_rb);
        }


        Status = adObj.getADStatus();
        if (Status.equals("Pending")) {
            StatusRG.check(R.id.ase_new_Status_rb);
        } else {
            StatusRG.check(R.id.ase_used_Status_rb);
        }

        // Get description
        descriptionET.setText(adObj.getDescription());

        // Get location
        ParseGeoPoint adGeoPoint = adObj.getLocation();
        if (adGeoPoint != null) {
            currentLocation = new Location("provider");
            currentLocation.setLatitude(adGeoPoint.getLatitude());
            currentLocation.setLongitude(adGeoPoint.getLongitude());
            loadCityCountryNames();
        }
    }

    private void showAdImage(ImageView imageIV, TextView buttonTV, Bitmap bitmap) {
        imageIV.setVisibility(View.VISIBLE);
        buttonTV.setVisibility(View.GONE);
        imageIV.setImageBitmap(bitmap);
    }

    private void showAdImagePlaceholder(ImageView imageIV, TextView buttonTV) {
        imageIV.setVisibility(View.GONE);
        buttonTV.setVisibility(View.VISIBLE);
    }

    // MARK: - SHOW ALERT FOR UPLOADING IMAGES -----------------------------------------------------
    void showImagePickerDialog(final int cameraReqCode, final int galleryReqCode) {
        MediaPickerDialog dialog = new MediaPickerDialog(this);
        dialog.setCameraOptionTitle(getString(R.string.take_a_picture1));
        dialog.setOnOptionSelectedListener(new MediaPickerDialog.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int index) {
                if (index == MediaPickerDialog.CAMERA_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(SellEditItemActivity.this, cameraPermissions)) {
                        openCamera(cameraReqCode);
                    } else {
                        lastAskedPermissionsReqCode = cameraReqCode;
                        ActivityCompat.requestPermissions(SellEditItemActivity.this,
                                cameraPermissions, PHOTO_CAMERA_PERMISSION_REQ_CODE);
                    }
                } else if (index == MediaPickerDialog.GALLERY_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(SellEditItemActivity.this, cameraPermissions)) {
                        openGallery(galleryReqCode);
                    } else {
                        lastAskedPermissionsReqCode = galleryReqCode;
                        ActivityCompat.requestPermissions(SellEditItemActivity.this,
                                galleryPermissions, PHOTO_GALLERY_PERMISSION_REQ_CODE);
                    }
                }
            }
        });
        dialog.show();
    }

    // IMAGE/VIDEO HANDLING METHODS ------------------------------------------------------------------------

    // OPEN CAMERA
    public void openCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageTakenFile = createEmptyFile();
        imageTakenPath = imageTakenFile.getAbsolutePath();

        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                ".provider", imageTakenFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, requestCode);
    }

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

    // OPEN GALLERY
    public void openGallery(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), requestCode);
    }

    // OPEN VIDEO CAMERA
    public void openVideoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Configs.MAXIMUM_DURATION_VIDEO);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, VIDEO_REQ_CODE);
    }

    // OPEN VIDEO GALLERY
    public void openVideoGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Configs.MAXIMUM_DURATION_VIDEO);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_video)), VIDEO_REQ_CODE);
    }

    // GET VIDEO PATH AS A STRING -------------------------------------
    public String getRealPathFromURI(Uri contentUri) {
        return RealPathUtil.getRealPathFromUri(this, contentUri);
    }

    public String getFilenameFromPath(String path) {
        if (path == null) {
            return "";
        }
        int lastIndexOfSlash = path.lastIndexOf("/");
        if (lastIndexOfSlash == -1 || lastIndexOfSlash > path.length() - 1) {
            return "";
        }

        return path.substring(lastIndexOfSlash + 1);
    }

    public static String formatDuration(int durationMillis) {
        long seconds = durationMillis / 1000;
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    // CONVERT VIDEO TO BYTES -----------------------------------
    private byte[] convertVideoToBytes(Uri uri) {
        byte[] videoBytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(getRealPathFromURI(uri)));

            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);

            videoBytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoBytes;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!PermissionsUtils.hasPermissions(this, locationPermissions)) {
            return;
        }

        //remove location callback:
        locationManager.removeUpdates(this);
        currentLocation = location;

        if (currentLocation != null) {
            Log.i("log-", "CURRENT LOCATION FOUND! " + currentLocation.getLatitude());
            // NO GPS location found!
        } else {
            Configs.simpleAlert(getString(R.string.location_message5), this);
            // Set the default currentLocation
            currentLocation = new Location("dummyprovider");
            currentLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            currentLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private Bitmap onCaptureImageResult(String photoPath) {
        Bitmap bitmap = getbitmap(photoPath);

        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return null;
        }
        return bitmap;
    }

    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = FileUtils.decodeIntentData(data.getData(), UPLOADING_IMAGE_SIZE);
        bitmap = ExifUtil.rotateBitmap(getRealPathFromURI(data.getData()), bitmap);


        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return null;
        }
        return bitmap;
    }

    // MARK: - GET CURRENT LOCATION ------------------------------------------------------
    protected void loadCurrentLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        String provider = locationManager.getBestProvider(criteria, true);

        if (!PermissionsUtils.hasPermissions(this, locationPermissions)) {
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);

        if (currentLocation != null) {
            Log.i("log-", "CURRENT LOCATION FOUND! " + currentLocation.getLatitude());
        } else {
            // Try to get current Location one more time
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
    }

    private void loadCityCountryNames() {
        try {
            Geocoder geocoder = new Geocoder(SellEditItemActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (Geocoder.isPresent() && addresses != null && !addresses.isEmpty()) {
                Address returnAddress = addresses.get(0);
                String city = returnAddress.getLocality();
                String country = returnAddress.getCountryName();

                if (city == null) {
                    city = "";
                }
                // Show City/Country
                locationTV.setText(city + ", " + country);
            } else {
                Toast.makeText(getApplicationContext(), "Location could not be retrieved!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            ToastUtils.showMessage(e.getMessage());
        }
    }

    // MARK: - DELETE AD IN OTHER CLASSES ------------------------------------------------------
    private void deleteAdInOtherClasses() {

        showLoading();
        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideLoading();
                if (task.isSuccessful()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SellEditItemActivity.this);
                    alert.setMessage(getString(R.string.ads_delete_message))
                            .setTitle(R.string.app_name)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(R.drawable.logo);
                    alert.create().show();
                } else {
                    Configs.simpleAlert(task.getException().getMessage(), SellEditItemActivity.this);
                }
            }
        });


    }

    FirebaseStorage storage;

    public void uplaodfile(byte[] byteArray, final String Check) {
        String ID = Calendar.getInstance().getTimeInMillis() + "";
        ImageObj imageObj = new ImageObj();
        imageObj.setID(ID);
        final StorageReference ref = storage.getReference().child("Files/" + ID);
        UploadTask uploadTask = ref.putBytes(byteArray);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageObj.setUrl(downloadUri.toString());
                    if (Check.equals("image1")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image1").setValue(imageObj);
                    }
                    if (Check.equals("image2")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image2").setValue(imageObj);
                    }
                    if (Check.equals("image3")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image3").setValue(imageObj);
                    }
                    if (Check.equals("image4")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image4").setValue(imageObj);
                    }
                    if (Check.equals("image5")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image5").setValue(imageObj);
                    }
                    if (Check.equals("image6")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image6").setValue(imageObj);
                    }
                    if (Check.equals("thumb")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/videoThumbnail").setValue(imageObj);
                    }
                    if (Check.equals("video")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/video").setValue(downloadUri.toString());
                    }


                    files--;
                    if (files == 0) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/allDone").setValue(true);
                        AlertDialog.Builder alert = new AlertDialog.Builder(SellEditItemActivity.this);
                        alert.setMessage(getString(R.string.ads_created_message))
                                .setTitle(R.string.app_name)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        showLoading();
                                        HashMap<String, String> rawParameters = new HashMap<>();
                                        rawParameters.put("body", getString(R.string.new_add_found) + adObj.getCategory() + " > " + adObj.getSubcategory());
                                        rawParameters.put("title", getString(R.string.app_name));

                                        rawParameters.put("page", "openadd");
                                        rawParameters.put("addid", adObj.getID());
                                        NotificationSednToallusers(getApplicationContext(), rawParameters, adObj.getCategory() + " > " + adObj.getSubcategory());

                                    }
                                })
                                .setCancelable(false)
                                .setIcon(R.drawable.logo);
                        alert.create().show();
                    }

                }
            }
        });

    }

    public void uplaodvideo(Uri byteArray, final String Check) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(getRealPathFromURI(byteArray)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final StorageReference ref = storage.getReference().child("Files/" + Calendar.getInstance().getTimeInMillis());
        UploadTask uploadTask = ref.putStream(stream);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (Check.equals("image1")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image1").setValue(downloadUri.toString());
                    }
                    if (Check.equals("image2")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image2").setValue(downloadUri.toString());
                    }
                    if (Check.equals("image3")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image3").setValue(downloadUri.toString());
                    }
                    if (Check.equals("image4")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image4").setValue(downloadUri.toString());
                    }
                    if (Check.equals("image5")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image5").setValue(downloadUri.toString());
                    }
                    if (Check.equals("image6")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/image6").setValue(downloadUri.toString());
                    }
                    if (Check.equals("thumb")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/videoThumbnail").setValue(downloadUri.toString());
                    }
                    if (Check.equals("video")) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/video").setValue(downloadUri.toString());
                    }


                    files--;
                    if (files == 0) {
                        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID() + "/allDone").setValue(true);
                        AlertDialog.Builder alert = new AlertDialog.Builder(SellEditItemActivity.this);
                        String message = "";
                        if (Update) {
                            message = getString(R.string.add_updated);
                        } else {
                            message = getString(R.string.add_created_message);
                        }
                        alert.setMessage(message)
                                .setTitle(R.string.app_name)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Update) {
                                            hideLoading();
                                            return;
                                        }
                                        showLoading();
                                        HashMap<String, String> rawParameters = new HashMap<>();
                                        rawParameters.put("body", getString(R.string.new_add_found) + adObj.getCategory() + " > " + adObj.getSubcategory());
                                        rawParameters.put("title", getString(R.string.app_name));

                                        rawParameters.put("page", "openadd");
                                        rawParameters.put("addid", adObj.getID());
                                        NotificationSednToallusers(getApplicationContext(), rawParameters, adObj.getCategory() + " > " + adObj.getSubcategory());

                                    }
                                })
                                .setCancelable(false)
                                .setIcon(R.drawable.logo);
                        alert.create().show();
                    }

                }
            }
        });

    }


    public void NotificationSednToallusers(final Context activity, final HashMap dataValue, String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/) {

        instanceIdToken = instanceIdToken.replaceAll("[^A-Za-z0-9]", "");
        instanceIdToken = "/topics/" + instanceIdToken.replaceAll("[^A-Za-z0-9]", "");


        final String url = "https://fcm.googleapis.com/fcm/send";
        final String finalInstanceIdToken = instanceIdToken;
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        hideLoading();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        finish();
                    }
                }) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Map<String, Object> rawParameters = new Hashtable<>();
                rawParameters.put("data", new JSONObject(dataValue));
                rawParameters.put("to", finalInstanceIdToken);
                return new JSONObject(rawParameters).toString().getBytes();
            }

            ;

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAA-cK_PpE:APA91bHdEBTxLv8k9f-Ylr0QKbHaa5CMe3DTyjnEc1ZSWvNw6od3JuHV-nwcrCmJ_aNPTL1Y9zX6BJGjcTyZInJUlzjB_XSjo5kbmE3HMf8wZe8xvq2Ix9JMrQm4zTdNuH1yfQ_RdBNB");
                return headers;
            }

        };

        Volley.newRequestQueue(activity).add(myReq);
    }

}
