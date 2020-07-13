package woopy.com.juanmckenzie.caymanall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parse.ParseGeoPoint;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.common.CreateOptionsDialog;
import woopy.com.juanmckenzie.caymanall.common.MediaPickerDialog;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity1;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.Constants;
import woopy.com.juanmckenzie.caymanall.utils.FileUtils;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;
import woopy.com.juanmckenzie.caymanall.utils.PermissionsUtils;
import woopy.com.juanmckenzie.caymanall.utils.RealPathUtil;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;


public class Purchase extends BaseActivity implements LocationListener {


    public static final String EDIT_AD_OBJ_ID_EXTRA_KEY = "EDIT_AD_OBJ_ID_EXTRA_KEY";

    private static final int UPLOADING_IMAGE_SIZE = 800;

    private static final int SELECT_LOCATION_REQ_CODE = 11;
    private static final int SELECT_PAYMENT = 110;
    private static final int CATEGORY_REQ_CODE = 2;
    private static final int CATEGORY_REQ_CODE1 = 22;


    private static final int LOCATION_PERMISSION_REQ_CODE = 13;
    private static final int PHOTO_CAMERA_PERMISSION_REQ_CODE = 14;
    private static final int VIDEO_CAMERA_PERMISSION_REQ_CODE = 15;
    private static final int PHOTO_GALLERY_PERMISSION_REQ_CODE = 16;
    private static final int VIDEO_GALLERY_PERMISSION_REQ_CODE = 17;

    private static final int TAKE_IMAGE1_REQ_CODE = 18;

    private static final int PICK_IMAGE1_REQ_CODE = 21;
    private static final int VIDEO_REQ_CODE = 24;

    private String[] locationPermissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] cameraPermissions = {android.Manifest.permission.CAMERA};
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};


    private String selectedCategory = "", selectedCategorysub = "";

    /* Views */

    private TextView backIV;
    private TextView doneIV;
    private FrameLayout addImage1FL;
    private ImageView addImage1IV;
    private TextView addImage1TV;
    private TextView Pricepackage, Category;
    private EditText titleET, url;

    private Bitmap image1Bmp;
    private String imageTakenPath;

    /* Variables */
    private Banner adObj;
    private Location currentLocation;
    private LocationManager locationManager;
    private String condition = "";

    RadioButton Cash, Paypal;
    Boolean ImageSelected = false;
    private TextView locationTV;

    private Boolean CheckOnline = false;

    private int lastAskedPermissionsReqCode = -1;
    GPSTracker gpsTracker;

    TextView screenTitleTV, ase_delete_ad_tv;
    Boolean Update = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);


        storage = FirebaseStorage.getInstance();
        myapplication = (Configs) getApplicationContext();
        Cash = findViewById(R.id.Cash);
        Paypal = findViewById(R.id.Paypal);
        Paypal.setChecked(true);
        url = findViewById(R.id.url);

        gpsTracker = new GPSTracker(this);
        currentLocation = new Location("dummyprovider");
        currentLocation.setLatitude(gpsTracker.getLatitude());
        currentLocation.setLongitude(gpsTracker.getLongitude());

        screenTitleTV = findViewById(R.id.screenTitleTV);
        ase_delete_ad_tv = findViewById(R.id.ase_delete_ad_tv);

        ase_delete_ad_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(Purchase.this);
                alert.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_banner))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.delete_banner), new DialogInterface.OnClickListener() {
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


        String objectID = getIntent().getStringExtra(EDIT_AD_OBJ_ID_EXTRA_KEY);
        Log.i("log-", "OBJECT ID: " + objectID);
        initViews();

        // YOU'RE EDITING AN ITEM ------------------
        if (objectID != null) {
            Update = true;
            adObj = myapplication.getSelecteBanner();
            screenTitleTV.setText(getString(R.string.edit_banner));
            ase_delete_ad_tv.setVisibility(View.VISIBLE);
            Cash.setVisibility(View.GONE);
            Paypal.setVisibility(View.GONE);
            findViewById(R.id.paymenttring).setVisibility(View.GONE);
            // Call query
            ShoeDetails();
            // YOU'RE SELLING A NEW ITEM ---------------
        } else {
            adObj = new Banner();
            screenTitleTV.setText(getString(R.string.buying_banner));
            ase_delete_ad_tv.setVisibility(View.GONE);


            findViewById(R.id.Pricepackage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CreateOptionsDialog dialog = new CreateOptionsDialog(Purchase.this, Configs.packageOptions);
                    dialog.setOnOptionSelectedListener(new CreateOptionsDialog.OnOptionSelectedListener() {
                        @Override
                        public void onOptionSelected(String index) {


                            Pricepackage.setText(index);

                            if (index.equals("3 days $15")) {
                                adObj.setDays("3");
                            }
                            if (index.equals("7 days $30")) {
                                adObj.setDays("7");
                            }
                            if (index.equals("14 days $50")) {
                                adObj.setDays("14");
                            }
                            if (index.equals("21 days $69")) {
                                adObj.setDays("21");
                            }
                            if (index.equals("30 days $79")) {
                                adObj.setDays("30");
                            }
                            if (index.equals("45 days $89")) {
                                adObj.setDays("45");
                            }


                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

        }


        setUpViews();

        // Set default variables
        condition = "New";
        if (PermissionsUtils.hasPermissions(this, locationPermissions)) {
            loadCurrentLocation();
        } else {
            lastAskedPermissionsReqCode = LOCATION_PERMISSION_REQ_CODE;
            ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_PERMISSION_REQ_CODE);
        }
    }

    public void ShoeDetails() {
        url.setText(adObj.getUrl());
        titleET.setText(adObj.getTitle());

        if (adObj.getDays().equals("3")) {
            Pricepackage.setText("3 days $15");
        }
        if (adObj.getDays().equals("7")) {
            Pricepackage.setText("7 days $30");
        }
        if (adObj.getDays().equals("14")) {
            adObj.setDays("14");
            Pricepackage.setText("14 days $50");
        }
        if (adObj.getDays().equals("21")) {
            Pricepackage.setText("21 days $69");
        }
        if (adObj.getDays().equals("30")) {
            Pricepackage.setText("30 days $79");
        }
        if (adObj.getDays().equals("45")) {
            Pricepackage.setText("45 days $89");
        }


        if (adObj.getImage1().getUrl() != null) {

            Glide.with(this)
                    .asBitmap()
                    .load(adObj.getImage1().getImage1024())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage1IV, addImage1TV, (Bitmap) resource);
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


    }

    byte bytes[];
    File file;

    // IMAGE/VIDEO PICKED DELEGATE ------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BufferedInputStream bis = null;

        if (resultCode == Activity.RESULT_OK) {
            hideLoading();
            switch (requestCode) {
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

                    file = new File(imageTakenPath);
                    bytes = new byte[(int) file.length()];

                    bis = null;
                    try {
                        bis = new BufferedInputStream(new FileInputStream(file));
                        DataInputStream dis = new DataInputStream(bis);
                        dis.readFully(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    showAdImage(addImage1IV, addImage1TV, image1Bmp);
                    break;

                case PICK_IMAGE1_REQ_CODE:
                    ImageSelected = true;
                    bytes = convertImageToByte(data.getData());

                    image1Bmp = onSelectFromGalleryResult(data);
                    showAdImage(addImage1IV, addImage1TV, image1Bmp);
                    break;
                case CATEGORY_REQ_CODE:
                    selectedCategory = data.getStringExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY);
                    myapplication.setSelectedCategory(selectedCategory);
                    Category.setText(selectedCategory);
                    if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME)) {
                        myapplication.setSelectedCategory(selectedCategory);
                        Intent categoriesIntent = new Intent(Purchase.this, CategoriesActivity1.class);
                        categoriesIntent.putExtra(CategoriesActivity1.SELECTED_CATEGORY_EXTRA_KEY, selectedCategorysub);
                        startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE1);
                    }
                    break;
                case CATEGORY_REQ_CODE1:
                    selectedCategorysub = data.getStringExtra(CategoriesActivity1.SELECTED_CATEGORY_EXTRA_KEY);
                    Category.setText(selectedCategorysub);
                    Category.setText(selectedCategory + " > " + selectedCategorysub);
                    break;
                case SELECT_PAYMENT:
                    PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (confirm != null) {
                        SaveDatabase(true);
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
        hideLoading();
    }

    public byte[] convertImageToByte(Uri uri) {
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
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

    private void initViews() {
        Pricepackage = findViewById(R.id.Pricepackage);
        Category = findViewById(R.id.Category);
        backIV = findViewById(R.id.ase_back_iv);
        doneIV = findViewById(R.id.ase_done_iv);
        addImage1FL = findViewById(R.id.ase_add_image1_fl);
        addImage1IV = findViewById(R.id.ase_add_image1_iv);
        addImage1TV = findViewById(R.id.ase_add_image1_tv);
        locationTV = findViewById(R.id.ase_location_tv);
        conditionRG = findViewById(R.id.ase_condition_rg);
        titleET = findViewById(R.id.ase_item_title_et);
        url = findViewById(R.id.url);

    }

    private RadioGroup conditionRG;

    final Calendar myCalendar = Calendar.getInstance();

    private void setUpViews() {


        conditionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Cash:
                        setConditionRB(Cash, true);
                        setConditionRB(Paypal, false);
                        condition = "Cash";
                        Paypal.setChecked(false);
                        break;
                    case R.id.Paypal:
                        setConditionRB(Cash, false);
                        setConditionRB(Paypal, true);
                        condition = "Paypal";
                        Paypal.setChecked(true);
                        break;
                }
            }
        });
        loadCityCountryNames();

        // MARK: - BACK BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // MARK: - UPLOAD IMAGE 1 ----------------------------------------------------------------
        addImage1FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog(TAKE_IMAGE1_REQ_CODE, PICK_IMAGE1_REQ_CODE);
            }
        });


        findViewById(R.id.Category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(Purchase.this, CategoriesActivity.class);
                categoriesIntent.putExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY, selectedCategory);
                startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE);
            }
        });

        // MARK: - SUBMIT AD BUTTON -----------------------------------------------------------------
        doneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.hideKeyboard(Purchase.this);


                if (!Update) {
                    // You haven't filled all required the fields
                    if (!ImageSelected || TextUtils.isEmpty(titleET.getText()) ||
                            adObj.getDays() == null) {
                        Configs.simpleAlert(getString(R.string.you_must_make_sure) +
                                getString(R.string.title_package_image_category), Purchase.this);
                        // You can submit your Ad!
                    } else {
                        showLoading();
                        submitAd();
                    }
                } else {
                    showLoading();
                    submitAd();
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

    int files = 0;
    Configs myapplication;


    public void SaveDatabase(Boolean checkpaypal) {


        showLoading();
        ParseGeoPoint userGP = loadUserLocationGeoPoint();
        myapplication = (Configs) getApplicationContext();
        adObj.setSellerPointer(myapplication.getCurrentUser());
        adObj.setTitle(titleET.getText().toString());
        adObj.setCategory(selectedCategory);
        adObj.setSubcategory(selectedCategorysub);
        adObj.setLocation(userGP);

        // In case this is a new Ad
        if (adObj.getID() == null) {
            adObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
            adObj.setID(FirebaseDatabase.getInstance().getReference().child("Banners").push().getKey());

        }

        if (!Update) {
            if (Paypal.isChecked()) {
                adObj.setStatus(true);
            } else {
                adObj.setStatus(false);
            }
        }
        adObj.setUrl(url.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Banners").child(adObj.getID()).setValue(adObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Save image1
                    if (image1Bmp != null) {
                        files++;
                        uplaodfile(bytes, "image1", checkpaypal);
                    }


                    if (files == 0) {
                        if (!Update) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(Purchase.this);
                            String message = "";
                            if (!checkpaypal)
                                message = getString(R.string.your_banner_request_message);
                            else message = "Your Banners has been successfully Created";
                            alert.setMessage(message)
                                    .setTitle(R.string.app_name)
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            sendEmail();
                                            sendemailtouser(Purchase.this, adObj.getSellerPointer().getFullName(), adObj.getSellerPointer().getEmail(), adObj.getTitle());
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(R.drawable.logo);
                            alert.create().show();
                        } else {


                            AlertDialog.Builder alert = new AlertDialog.Builder(Purchase.this);
                            alert.setMessage(getString(R.string.banner_request_updated))
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
                        }

                    }

                } else {
                    hideLoading();
                    ToastUtils.showMessage(task.getException().getMessage());
                }
            }
        });


    }

    private void submitAd() {
        if (Update) {
            SaveDatabase(false);
            return;
        }
        if (Paypal.isChecked()) {
            PayPalConfiguration config = new PayPalConfiguration()

                    // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                    // or live (ENVIRONMENT_PRODUCTION)
                    .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

                    .clientId("<YOUR_CLIENT_ID>");


            PayPalPayment payment = new PayPalPayment(new BigDecimal("3.75"), "USD", "Buying Banner",
                    PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(Purchase.this, PaymentActivity.class);
            // send the same configuration for restart resiliency
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, SELECT_PAYMENT);
        } else {
            SaveDatabase(false);
        }


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
        dialog.setCameraOptionTitle(getString(R.string.take_picture));
        dialog.setOnOptionSelectedListener(new MediaPickerDialog.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int index) {
                if (index == MediaPickerDialog.CAMERA_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(Purchase.this, cameraPermissions)) {
                        openCamera(cameraReqCode);
                    } else {
                        lastAskedPermissionsReqCode = cameraReqCode;
                        ActivityCompat.requestPermissions(Purchase.this,
                                cameraPermissions, PHOTO_CAMERA_PERMISSION_REQ_CODE);
                    }
                } else if (index == MediaPickerDialog.GALLERY_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(Purchase.this, cameraPermissions)) {
                        openGallery(galleryReqCode);
                    } else {
                        lastAskedPermissionsReqCode = galleryReqCode;
                        ActivityCompat.requestPermissions(Purchase.this,
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
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), requestCode);
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

    private void loadCityCountryNames() {
        try {
            Geocoder geocoder = new Geocoder(Purchase.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (Geocoder.isPresent() && addresses != null && !addresses.isEmpty()) {
                Address returnAddress = addresses.get(0);
                String city = returnAddress.getLocality();
                String adminArea = returnAddress.getAdminArea();
                String locality = returnAddress.getLocality();
                String country = returnAddress.getCountryName();

                if (city == null) {
                    city = "";
                }

                if (locality == null) {
                    city = "";
                }
                if (adminArea == null) {
                    city = "";
                }
                // Show City/Country
//                locationTV.setText(city + ", " + country);
                String Location = city + ", " + country + " , " + locality + " , " + adminArea;
                if (Location.toLowerCase().contains("cayman islands") || Location.toLowerCase().contains("cayman")) {
                    Cash.setVisibility(View.VISIBLE);
                    conditionRG.check(R.id.Paypal);
                    setConditionRB(Cash, true);
                    setConditionRB(Paypal, false);
                    Paypal.setChecked(false);
                } else {
                    conditionRG.check(R.id.Cash);
                    setConditionRB(Cash, false);
                    setConditionRB(Paypal, true);
                    Cash.setVisibility(View.GONE);
                    Paypal.setChecked(true);

                }
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
        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideLoading();
                if (task.isSuccessful()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Purchase.this);
                    alert.setMessage(getString(R.string.banner_delete_message))
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
                    Configs.simpleAlert(task.getException().getMessage(), Purchase.this);
                }
            }
        });


    }

    FirebaseStorage storage;

    public void uplaodfile(byte[] byteArray, final String Check, Boolean checkpaypal) {
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
                    adObj.setImage1(imageObj);
                    if (Check.equals("image1")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/image1").setValue(imageObj);
                    }
                    if (Check.equals("image2")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/image2").setValue(imageObj);
                    }
                    if (Check.equals("image3")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/image3").setValue(imageObj);
                    }
                    if (Check.equals("image4")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/image4").setValue(imageObj);
                    }
                    if (Check.equals("image5")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/image5").setValue(imageObj);
                    }
                    if (Check.equals("image6")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/image6").setValue(imageObj);
                    }
                    if (Check.equals("thumb")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/videoThumbnail").setValue(imageObj);
                    }
                    if (Check.equals("video")) {
                        FirebaseDatabase.getInstance().getReference().child("Banners/" + adObj.getID() + "/video").setValue(downloadUri.toString());
                    }


                    files--;
                    if (files == 0) {
                        hideLoading();
                        AlertDialog.Builder alert = new AlertDialog.Builder(Purchase.this);

                        String message = "";
                        if (!checkpaypal)
                            message = getString(R.string.your_banner_request_message);
                        else message = "Your Banners has been successfully Created";

                        alert.setMessage(message)
                                .setTitle(R.string.app_name)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                        sendemailtouser(Purchase.this, adObj.getSellerPointer().getFullName(), adObj.getSellerPointer().getEmail(), adObj.getTitle());
                                        sendEmail();
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

    public void sendEmail() {
        if (Paypal.isChecked()) {
            return;
        }
        BackgroundMail.newBuilder(Purchase.this)
                .withUsername("juanmckenzie82@gmail.com")
                .withPassword("mifamiliayyo")
                .withType(BackgroundMail.TYPE_HTML)
                .withProcessVisibility(false)
                .withSubject("New Purchase in CaymanAll")
                .withBody("Username: " + adObj.getSellerPointer().getUsername() + "\n" +
                        "Email: " + adObj.getSellerPointer().getEmail() + "\n" +
                        "Email Verified: " + adObj.getSellerPointer().getEmailVerified() + "\n" +
                        "Price: " + Pricepackage.getText().toString() + "\n")
                .withMailto("caymanall345@gmail.com")
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }


    public static void sendemailtouser(final Context activity, final String name, final String email, String bannernme) {


        final String url = "https://caymanall.appspot.com/sendConformationemail?name=" + name + "&email=" + email + "&message=Your Purchase has been completed successfully \\n Banner(" + bannernme + ") is live now.";
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

        };

        Volley.newRequestQueue(activity).add(myReq);
    }

}



