package woopy.com.juanmckenzie.caymanall.utils;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.Categories;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.Objects.promotions;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.ChatsT;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.TUser;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.landing.SplashScreen;
import woopy.com.juanmckenzie.caymanall.wizard.WizardActivity;


public class Configs extends Application {


    private RequestQueue mRequestQueue;

    public int selectedadposition = 0;
    public List<Ads> adsList = new ArrayList<>();
    private static Configs mInstance;
    String SelectedPlaceID;
    public Boolean secondtabs = false;

    public List<Ads> getAdsList() {
        return adsList;
    }

    public void setAdsList(List<Ads> adsList) {
        this.adsList = adsList;
    }

    public int getSelectedadposition() {
        return selectedadposition;
    }

    public void setSelectedadposition(int selectedadposition) {
        this.selectedadposition = selectedadposition;
    }

    public Boolean getSecondtabs() {
        return secondtabs;
    }

    public void setSecondtabs(Boolean secondtabs) {
        this.secondtabs = secondtabs;
    }

    List<String> Images = new ArrayList<>();

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public String getSelectedPlaceID() {
        return SelectedPlaceID;
    }

    public void setSelectedPlaceID(String selectedPlaceID) {
        SelectedPlaceID = selectedPlaceID;
    }

    String SessionUserID;
    String SelectedChatID;
    String SelectedCategory;
    public FirebaseStorage storage;
    User CurrentUser;
    User ReportUser;
    List<Categories> categoriesList = new ArrayList<>();
    List<String> categoriesListforevents = new ArrayList<>();
    List<Banner> banners = new ArrayList<>();
    int selectedbanner = 0;
    Ads SelectedAd;
    Banner SelecteBanner;
    promotions SelectePromotions;
    ChatsT selectedChat;
    EventObj SelectedEvent;
    Formobject selectedform;
    TUser selectedUser;


    SharedPreferences.Editor editor;

    public List<String> getCategoriesListforevents() {
        return categoriesListforevents;
    }

    public void setCategoriesListforevents(List<String> categoriesListforevents) {
        this.categoriesListforevents = categoriesListforevents;
    }

    public promotions getSelectePromotions() {
        return SelectePromotions;
    }

    public void setSelectePromotions(promotions selectePromotions) {
        SelectePromotions = selectePromotions;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public int getSelectedbanner() {
        return selectedbanner;
    }

    public void setSelectedbanner(int selectedbanner) {
        this.selectedbanner = selectedbanner;
    }

    // IMPORTANT: Replace the red strings below with your own App ID and Client Key of your app on back4app.com
    public static String PARSE_APP_ID = "jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk";
    public static String PARSE_CLIENT_KEY = "gOs0IUeM35o8aSSR5p4r4DJZeCsNRmvo13gmGT6L";


    // THIS IS THE RED MAIN COLOR OF THIS APP
    public static String MAIN_COLOR = "#f6b93b";


    // YOU CAN CHANGE THE CURRENCY SYMBOL AS YOU WISH
    public static String CURRENCY = "$";


    // DECLARE FONT FILES (the font files are located into "app/src/main/assets/font" folder)
    public static Typeface qsBold, qsLight, qsRegular, titBlack, titLight, titRegular, titSemibold;


    // THIS IS THE MAX DURATION OF A VIDEO RECORDING FOR AN AD (in seconds)
    public static int MAXIMUM_DURATION_VIDEO = 60;


    // YOU CAN CHANGE THE DEFAULT LOCATION COORDINATES FOR ADS AS YOU WISH BY EDITING THE FLOAT VALUES BELOW:
    public static LatLng DEFAULT_LOCATION = new LatLng(40.7143528, -74.0059731);


    // YOU CAN CHANGE THE AD REPORT OPTIONS BELOW AS YOU WISH
    public static String[] reportAdOptions = {
            "Prohibited item",
            "Conterfeit",
            "Wrong category",
            "Keyword spam",
            "Repeated post",
            "Nudity/pornography/mature content",
            "Hateful speech/blackmail",
    };


    // YOU CAN CHANGE THE USER REPORT OPTIONS BELOW AS YOU WISH
    public static String[] reportUserOptions = {
            "Selling counterfeit items",
            "Selling prohibited items",
            "Items wrongly categorized",
            "Nudity/pornography/mature content",
            "Keyword spammer",
            "Hateful speech/blackmail",
            "Suspected fraudster",
            "No-show on meetup",
            "Backed out of deal",
            "Touting",
            "Spamming",
    };


    public static String[] packageOptions = {
            "3 days $15",
            "7 days $30",
            "14 days $50",
            "21 days $69",
            "30 days $79",
            "45 days $89"
    };


    /***********  DO NOT EDIT THE CODE BELOW!! **********/

    public static String USER_CLASS_NAME = "_User";
    public static String USER_USERNAME = "username";
    public static String USER_EMAIL = "email";
    public static String USER_EMAIL_VERIFIED = "emailVerified";
    public static String USER_FULLNAME = "fullName";
    public static String USER_AVATAR = "avatar";
    public static String USER_LOCATION = "location";
    public static String USER_ABOUT_ME = "aboutMe";
    public static String USER_WEBSITE = "website";
    public static String USER_IS_REPORTED = "isReported";
    public static String USER_REPORT_MESSAGE = "reportMessage";
    public static String USER_HAS_BLOCKED = "hasBlocked";

    public static String CATEGORIES_CLASS_NAME = "Categories";
    public static String CATEGORIES_CATEGORY = "category";
    public static String CATEGORIES_IMAGE = "image";

    public static String ADS_CLASS_NAME = "Ads";
    public static String ADS_SELLER_POINTER = "sellerPointer";
    public static String ADS_LIKED_BY = "likedBy"; // Array
    public static String ADS_KEYWORDS = "keywords"; // Array
    public static String ADS_TITLE = "title";
    public static String ADS_PRICE = "price";
    public static String ADS_CURRENCY = "currency";
    public static String ADS_CATEGORY = "category";
    public static String ADS_LOCATION = "location";
    public static String ADS_IMAGE1 = "image1";
    public static String ADS_IMAGE2 = "image2";
    public static String ADS_IMAGE3 = "image3";
    public static String ADS_VIDEO = "video";
    public static String ADS_VIDEO_THUMBNAIL = "videoThumbnail";
    public static String ADS_DESCRIPTION = "description";
    public static String ADS_CONDITION = "condition";
    public static String ADS_LIKES = "likes";
    public static String ADS_COMMENTS = "comments";
    public static String ADS_IS_REPORTED = "isReported";
    public static String ADS_REPORT_MESSAGE = "reportMessage";
    public static String ADS_CREATED_AT = "createdAt";


    public static String LIKES_CLASS_NAME = "Likes";
    public static String LIKES_CURR_USER = "currUser";
    public static String LIKES_AD_LIKED = "adLiked";
    public static String LIKES_CREATED_AT = "createdAt";

    public static String COMMENTS_CLASS_NAME = "CommentsObj";
    public static String COMMENTS_USER_POINTER = "userPointer";
    public static String COMMENTS_AD_POINTER = "adPointer";
    public static String COMMENTS_COMMENT = "comment";
    public static String COMMENTS_CREATED_AT = "createdAt";

    public static String ACTIVITY_CLASS_NAME = "Activity";
    public static String ACTIVITY_CURRENT_USER = "currUser";
    public static String ACTIVITY_OTHER_USER = "otherUser";
    public static String ACTIVITY_TEXT = "text";
    public static String ACTIVITY_CREATED_AT = "createdAt";


    public static String INBOX_CLASS_NAME = "Inbox";
    public static String INBOX_AD_POINTER = "adPointer";
    public static String INBOX_SENDER = "sender";
    public static String INBOX_RECEIVER = "receiver";
    public static String INBOX_INBOX_ID = "inboxID";
    public static String INBOX_MESSAGE = "message";
    public static String INBOX_IMAGE = "image";
    public static String INBOX_CREATED_AT = "createdAt";

    public static String CHATS_CLASS_NAME = "Chats";
    public static String CHATS_LAST_MESSAGE = "lastMessage";
    public static String CHATS_USER_POINTER = "userPointer";
    public static String CHATS_OTHER_USER = "otherUser";
    public static String CHATS_ID = "chatID";
    public static String CHATS_AD_POINTER = "adPointer";
    public static String CHATS_CREATED_AT = "createdAt";

    public static String FEEDBACKS_CLASS_NAME = "Feedbacks";
    public static String FEEDBACKS_AD_TITLE = "adTitle";
    public static String FEEDBACKS_SELLER_POINTER = "sellerPointer";
    public static String FEEDBACKS_REVIEWER_POINTER = "reviewerPointer";
    public static String FEEDBACKS_STARS = "stars";
    public static String FEEDBACKS_TEXT = "text";


    /* Global Variables */
    public static float distanceInMiles = (float) 50;
    public static String IMAGE_FORMAT = ".jpg";
    // For query pagination
    public static final int DEFAULT_PAGE_SIZE = 200;
    public static final int DEFAULT_PAGE_THRESHOLD = 3;


    boolean isParseInitialized = false;

    public static Boolean iSConnected = false;
    private static Configs instance;

    public static Configs getInstance() {
        return instance;
    }

    public boolean shownotification;

    public boolean isShownotification() {
        return prefs.getBoolean("shownotification", true);
    }

    SharedPreferences prefs;


    public void setShownotification(boolean shownotification) {
        editor.putBoolean("shownotification", shownotification);
        editor.apply();

    }

    @Override
    public void onTerminate() {
        setShownotification(true);
        super.onTerminate();
    }

    public void onCreate() {
        super.onCreate();
        instance = this;


        editor = getSharedPreferences("Caymanll", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("Caymanll", MODE_PRIVATE);

        Tovuti.from(this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean Connected, boolean isFast) {
                iSConnected = Connected;
            }
        });

        CaocConfig.Builder.create()
                .restartActivity(SplashScreen.class) //default: null (your app's launch activity)
                .errorActivity(SplashScreen.class) //default: null (default error activity)
                .apply();
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();

        categoriesListforevents.add("Appreciation");
        categoriesListforevents.add("Brunch");
        categoriesListforevents.add("Ceremony");
        categoriesListforevents.add("Concert");
        categoriesListforevents.add("Conference");
        categoriesListforevents.add("Exhibit");
        categoriesListforevents.add("Fair");
        categoriesListforevents.add("Festival");
        categoriesListforevents.add("Film");
        categoriesListforevents.add("Fundraiser");
        categoriesListforevents.add("Grand Opening");
        categoriesListforevents.add("Lecture");
        categoriesListforevents.add("Meeting");
        categoriesListforevents.add("Party");
        categoriesListforevents.add("Performance");
        categoriesListforevents.add("Protest");
        categoriesListforevents.add("Reception");
        categoriesListforevents.add("Recreation");
        categoriesListforevents.add("Religious");
        categoriesListforevents.add("Reunion");
        categoriesListforevents.add("Seminar");
        categoriesListforevents.add("Social");
        categoriesListforevents.add("Sports");
        categoriesListforevents.add("Theatre");
        categoriesListforevents.add("Trade Shows");
        categoriesListforevents.add("Workshop");

        // Init fonts
        qsBold = Typeface.createFromAsset(getAssets(), "font/Quicksand-Bold.ttf");
        qsLight = Typeface.createFromAsset(getAssets(), "font/Quicksand-Light.ttf");
        qsRegular = Typeface.createFromAsset(getAssets(), "font/Quicksand-Regular.ttf");
        titBlack = Typeface.createFromAsset(getAssets(), "font/Titillium-Black.otf");
        titLight = Typeface.createFromAsset(getAssets(), "font/Titillium-Light.otf");
        titRegular = Typeface.createFromAsset(getAssets(), "font/Titillium-Regular.otf");
        titSemibold = Typeface.createFromAsset(getAssets(), "font/Titillium-Semibold.otf");


        FirebaseMessaging.getInstance().subscribeToTopic("CaymanAll");


    }// end onCreate()

    // SHORTCUT TO GET AN IMAGE FROM PARSE
    public static void getParseImage(final ImageView imgView, ParseObject obj, String columnName) {
        ParseFile fileObject = obj.getParseFile(columnName);
        if (fileObject != null) {
            fileObject.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException error) {
                    if (error == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        if (bmp != null) {
                            imgView.setImageBitmap(bmp);
                        }
                    }
                }
            });
        }
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
        imageObj.setID(getSessionUserID());
        final StorageReference ref = storage.getReference().child("Files/" + getSessionUserID());
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
                if (task.isSuccessful()) {

                    Uri downloadUri = task.getResult();
                    imageObj.setUrl(downloadUri.toString());
                    FirebaseDatabase.getInstance().getReference().child("users/" + getCurrentUser().getFirebaseID() + "/avatar").setValue(imageObj);
                    Log.d("ImageUrlFirebase", downloadUri.toString());

                }
            }
        });

    }


    // MARK: - CUSTOM PROGRESS DIALOG -----------
    public static Dialog pd;

    public static void showPD(String mess, Context ctx) {


        pd = new Dialog(ctx, android.R.style.Theme);
        View view = LayoutInflater.from(ctx).inflate(R.layout.pd, null);
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.progressBar2);
        avi.show();
        TextView messTxt = view.findViewById(R.id.pdMessTxt);
        messTxt.setText(mess);

        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setContentView(view);
        pd.setCancelable(true);
        pd.show();

    }

    public static void hidePD() {
        pd.dismiss();
    }

    public static Dialog buildProgressLoadingDialog(Context ctx) {
        Dialog pd = new Dialog(ctx, android.R.style.Theme);
        View view = LayoutInflater.from(ctx).inflate(R.layout.dialog_progress, null);
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.progressBar2);
        TextView messTxt = view.findViewById(R.id.pdMessTxt);
        messTxt.setText(ctx.getString(R.string.please_wait_1));
        avi.show();
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCancelable(true);
        pd.setContentView(view);
        return pd;
    }

    // MARK: - SCALE BITMAP TO MAX SIZE ---------------------------------------
    public static Bitmap scaleBitmapToMaxSize(int maxSize, Bitmap bm) {
        int outWidth;
        int outHeight;
        int inWidth = bm.getWidth();
        int inHeight = bm.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        return Bitmap.createScaledBitmap(bm, outWidth, outHeight, false);
    }


    // MARK: - GET TIME AGO SINCE DATE ------------------------------------------------------------
    public static String timeAgoSinceDate(Date date, Context context) {
        String dateString = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            String sentStr = (df.format(date));
            Date past = df.parse(sentStr);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) {
                dateString = seconds + context.getString(R.string.seconds_ago);
            } else if (minutes < 60) {
                dateString = minutes + context.getString(R.string.minutes_ago);
            } else if (hours < 24) {
                dateString = hours + context.getString(R.string.hours_ago);
            } else {
                dateString = days + context.getString(R.string.days_ago);
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return dateString;
    }


    // ROUND THOUSANDS NUMBERS ------------------------------------------
    public static String roundThousandsIntoK(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }


    // MARK: - SIMPLE ALERT ------------------------------
    public static void simpleAlert(String mess, Context activity) {
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setMessage(mess)
                    .setTitle(R.string.app_name)
                    .setPositiveButton(activity.getString(R.string.ok), null)
                    .setIcon(R.drawable.logo);
            alert.create().show();
        } catch (Exception ex) {

        }
    }


    // MARK: - LOGIN ALERT ------------------------------
    public static void loginAlert(String mess, final Context activity) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(mess)
                .setTitle(R.string.app_name)
                .setPositiveButton(activity.getString(R.string.login), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(activity, WizardActivity.class);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), null)
                .setIcon(R.drawable.logo);
        alert.create().show();
    }


    // Method to get URI of a stored image
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
        return Uri.parse(path);
    }

    public static boolean isConnected() {
        return iSConnected;

    }


    public String getSessionUserID() {
        return SessionUserID;
    }

    public void setSessionUserID(String sessionUserID) {
        SessionUserID = sessionUserID;
    }

    public User getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }

    public Ads getSelectedAd() {
        return SelectedAd;
    }

    public EventObj getSelectedEvent() {
        return SelectedEvent;
    }

    public void setSelectedEvent(EventObj selectedEvent) {
        SelectedEvent = selectedEvent;
    }

    public String getSelectedChatID() {
        return SelectedChatID;
    }

    public void setSelectedChatID(String selectedChatID) {
        SelectedChatID = selectedChatID;
    }

    public List<Categories> getCategoriesList() {
        return categoriesList;
    }

    public String getSelectedCategory() {
        return SelectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        SelectedCategory = selectedCategory;
    }

    public void setCategoriesList(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public Formobject getSelectedform() {
        return selectedform;
    }

    public void setSelectedform(Formobject selectedform) {
        this.selectedform = selectedform;
    }

    public ChatsT getSelectedChat() {
        return selectedChat;
    }

    public void setSelectedChat(ChatsT selectedChat) {
        this.selectedChat = selectedChat;
    }

    public User getReportUser() {
        return ReportUser;
    }

    public Banner getSelecteBanner() {
        return SelecteBanner;
    }

    public void setSelecteBanner(Banner selecteBanner) {
        SelecteBanner = selecteBanner;
    }

    public void setReportUser(User reportUser) {
        ReportUser = reportUser;
    }

    public TUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(TUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setSelectedAd(Ads selectedAd) {
        SelectedAd = selectedAd;
    }


    //get the instance of request Queue
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "12" : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("12");
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}

