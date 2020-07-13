package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.Objects.ReportUser;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.FilterAdapter;
import woopy.com.juanmckenzie.caymanall.filters.models.ReportType;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;

public class ReportAdOrUserActivity extends BaseActivity {

    public static final String AD_OBJECT_ID_EXTRA_KEY = "AD_OBJECT_ID_EXTRA_KEY";
    public static final String USER_OBJECT_ID_EXTRA_KEY = "USER_OBJECT_ID_EXTRA_KEY";
    public static final String REPORT_TYPE_EXTRA_KEY = "REPORT_TYPE_EXTRA_KEY";

    /* Views */
    private TextView titleTV;
    private ImageView backIV;
    private RecyclerView reportRV;

    /* Variables */
    private Ads adObj;
    private EventObj eventObj;
    private EventObj newsObj;
    private User userObj;
    private String reportType = "";
    private List<String> reportArray = new ArrayList<>();
    Configs myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_ad_or_user);

        // Get extras from previous .java
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String adObjectID = extras.getString(AD_OBJECT_ID_EXTRA_KEY);
        String userObjectID = extras.getString(USER_OBJECT_ID_EXTRA_KEY);
        reportType = extras.getString(REPORT_TYPE_EXTRA_KEY);

        myapplication = (Configs) getApplicationContext();
        initViews();

        if (reportType != null && reportType.equals(ReportType.USER.getValue())) {
            userObj = myapplication.getSelectedAd().getSellerPointer();
            reportArray = new ArrayList<>(Arrays.asList(Configs.reportUserOptions));
        } else if (reportType.equals(ReportType.AD.getValue())) {
            adObj = myapplication.getSelectedAd();
            reportArray = new ArrayList<>(Arrays.asList(Configs.reportAdOptions));
        } else if (reportType.equals(ReportType.NEWS.getValue())) {
            newsObj = myapplication.getSelectedEvent();
            reportArray = new ArrayList<>(Arrays.asList(Configs.reportAdOptions));
        } else if (reportType.equals(ReportType.EVENTS.getValue())) {
            eventObj = myapplication.getSelectedEvent();
            reportArray = new ArrayList<>(Arrays.asList(Configs.reportAdOptions));
        }

        setUpViews();
    }

    private void initViews() {
        backIV = findViewById(R.id.araou_back_iv);
        titleTV = findViewById(R.id.araou_title_tv);
        reportRV = findViewById(R.id.araou_report_rv);
    }

    private void setUpViews() {
        titleTV = findViewById(R.id.araou_title_tv);
        titleTV.setTypeface(Configs.titSemibold);
        titleTV.setText(getString(R.string.report) + reportType);

        reportRV.setLayoutManager(new LinearLayoutManager(this));
        reportRV.setAdapter(new FilterAdapter(reportArray, "", new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onFilterSelected(String filter) {
                showReportDialog(filter);
            }
        }));

        // MARK: - BACK BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showReportDialog(final String reportMessage) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ReportAdOrUserActivity.this);
        alert.setMessage(getString(R.string.are_you_sure) + reportType + getString(R.string.following_reason) + reportMessage + "?")
                .setTitle(R.string.app_name)
                .setPositiveButton(getString(R.string.yes_i_am_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showLoading();

                        if (reportType.equals(ReportType.AD.getValue())) {
                            reportAd(reportMessage);
                        } else if (reportType.equals(ReportType.USER.getValue())) {
                            reportUser(reportMessage);
                        } else if (reportType.equals(ReportType.EVENTS.getValue())) {
                            reportEvent(reportMessage);
                        } else if (reportType.equals(ReportType.NEWS.getValue())) {
                            reportNews(reportMessage);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.mo), null)
                .setIcon(R.drawable.logo);
        alert.create().show();
    }

    private void reportAd(String message) {

        ReportUser reportUser = new ReportUser();
        reportUser.setMessage(message);
        reportUser.setId(FirebaseDatabase.getInstance().getReference().child("ReportsAds").push().getKey());
        reportUser.setUser(myapplication.getCurrentUser());
        reportUser.setAds(adObj);
        FirebaseDatabase.getInstance().getReference().child("ReportsAds").child(reportUser.getId()).setValue(reportUser);

        FirebaseDatabase.getInstance().getReference().child("ads").child(adObj.getID()).child("reportMessage").setValue(message);
        FirebaseDatabase.getInstance().getReference().child("ads").child(adObj.getID()).child("reported").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideLoading();

                    AlertDialog.Builder alert = new AlertDialog.Builder(ReportAdOrUserActivity.this);
                    alert.setMessage(getString(R.string.thanks_for_reporting_add))
                            .setTitle(R.string.app_name)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Go back to BrowseFragment
                                    startActivity(new Intent(ReportAdOrUserActivity.this, HomeActivity.class));
                                }
                            })
                            .setIcon(R.drawable.logo);
                    alert.create().show();
                } else {
                    hideLoading();
                    ToastUtils.showMessage(task.getException().getMessage());
                }
            }
        });

    }

    private void reportEvent(String message) {

        ReportUser reportUser = new ReportUser();
        reportUser.setMessage(message);
        reportUser.setId(FirebaseDatabase.getInstance().getReference().child("ReportsEvents").push().getKey());
        reportUser.setUser(myapplication.getCurrentUser());
        reportUser.setEventObj(eventObj);
        FirebaseDatabase.getInstance().getReference().child("ReportsEvents").child(reportUser.getId()).setValue(reportUser);
        FirebaseDatabase.getInstance().getReference().child("Events").child(eventObj.getID()).child("reportMessage").setValue(message);
        FirebaseDatabase.getInstance().getReference().child("Events").child(eventObj.getID()).child("reported").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideLoading();

                    AlertDialog.Builder alert = new AlertDialog.Builder(ReportAdOrUserActivity.this);
                    alert.setMessage("Thanks for reporting this Event. We\\'ll review it within 24h")
                            .setTitle(R.string.app_name)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Go back to BrowseFragment
                                    startActivity(new Intent(ReportAdOrUserActivity.this, HomeActivity.class));
                                }
                            })
                            .setIcon(R.drawable.logo);
                    alert.create().show();
                } else {
                    hideLoading();
                    ToastUtils.showMessage(task.getException().getMessage());
                }
            }
        });

    }

    private void reportNews(String message) {

        ReportUser reportUser = new ReportUser();
        reportUser.setMessage(message);
        reportUser.setId(FirebaseDatabase.getInstance().getReference().child("ReportsNews").push().getKey());
        reportUser.setUser(myapplication.getCurrentUser());
        reportUser.setNewsObj(newsObj);
        FirebaseDatabase.getInstance().getReference().child("ReportsNews").child(reportUser.getId()).setValue(reportUser);

        FirebaseDatabase.getInstance().getReference().child("News").child(newsObj.getID()).child("reportMessage").setValue(message);
        FirebaseDatabase.getInstance().getReference().child("News").child(newsObj.getID()).child("reported").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideLoading();

                    AlertDialog.Builder alert = new AlertDialog.Builder(ReportAdOrUserActivity.this);
                    alert.setMessage(getString(R.string.thanks_for_reporting_news))
                            .setTitle(R.string.app_name)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Go back to BrowseFragment
                                    startActivity(new Intent(ReportAdOrUserActivity.this, HomeActivity.class));
                                }
                            })
                            .setIcon(R.drawable.logo);
                    alert.create().show();
                } else {
                    hideLoading();
                    ToastUtils.showMessage(task.getException().getMessage());
                }
            }
        });

    }

    private void reportUser(String message) {


        ReportUser reportUser = new ReportUser();
        reportUser.setMessage(message);
        reportUser.setId(FirebaseDatabase.getInstance().getReference().child("Reports").push().getKey());
        reportUser.setUser(myapplication.getCurrentUser());
        reportUser.setAds(adObj);

        showLoading();
        FirebaseDatabase.getInstance().getReference().child("Reports").child(reportUser.getId()).setValue(reportUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    hideLoading();
                    Configs.hidePD();

                    AlertDialog.Builder alert = new AlertDialog.Builder(ReportAdOrUserActivity.this);
                    alert.setMessage(getString(R.string.thanks_for_reporting_user))
                            .setTitle(R.string.app_name)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Go back to BrowseFragment
                                    startActivity(new Intent(ReportAdOrUserActivity.this, HomeActivity.class));
                                }
                            })
                            .setIcon(R.drawable.logo);
                    alert.create().show();

//                    // Query all Ads posted by this User...
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery(Configs.ADS_CLASS_NAME);
//                    query.whereEqualTo(Configs.ADS_SELLER_POINTER, userObj);
//                    query.findInBackground(new FindCallback<ParseObject>() {
//                        public void done(List<ParseObject> objects, ParseException e) {
//                            if (e == null) {
//                                // ...and report Ads them one by one
//                                for (int i = 0; i < objects.size(); i++) {
//                                    ParseObject adObj;
//                                    adObj = objects.get(i);
//                                    adObj.put(Configs.ADS_IS_REPORTED, true);
//                                    adObj.put(Configs.ADS_REPORT_MESSAGE, "**Automatically reported after reporting the its Seller**");
//                                    adObj.saveInBackground();
//                                }
//                            }
//                        }
//                    });
                    // Error in Cloud Code
                } else {
                    hideLoading();
                    ToastUtils.showMessage(task.getException().getMessage());
                }
                hideLoading();
            }
        });

    }
}
