package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.models.ReportType;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.wizard.WizardActivity;

public class UserProfile extends BaseActivity {


    /* Variables */
    User userObj;
    List<Ads> userAdsArray;
    Configs configs;


    // ON CREATE ------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        configs = (Configs) getApplicationContext();
        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String objectID = extras.getString("objectID");


        FirebaseDatabase.getInstance().getReference().child("users").child(objectID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                userObj = user;
                // Call query
                getUserDetails();


                // MARK: - CHECK FEEDBACKS BUTTON ------------------------------------
                Button cfButt = findViewById(R.id.upCheckFeedbacksButt);
                cfButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(UserProfile.this, Feedbacks.class);
                        Bundle extras = new Bundle();
                        extras.putString("userObjectID", userObj.getFirebaseID());
                        i.putExtras(extras);
                        startActivity(i);
                    }
                });


                // MARK: - OPTIONS BUTTON ------------------------------------
                Button opButt = findViewById(R.id.upOptionsButt);
                opButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // USER IS LOGGED IN
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                            // Check blocked users array
                            final ParseUser currUser = ParseUser.getCurrentUser();
                            final List<String> hasBlocked = currUser.getList(Configs.USER_HAS_BLOCKED);

                            // Set blockUser  Action title
                            String blockTitle = null;
                            if (hasBlocked.contains(userObj.getFirebaseID())) {
                                blockTitle = getString(R.string.unblock_user);
                            } else {
                                blockTitle = getString(R.string.block_user);
                            }


                            AlertDialog.Builder alert = new AlertDialog.Builder(UserProfile.this);
                            final String finalBlockTitle = blockTitle;

                            alert.setMessage(getString(R.string.select_option))
                                    .setTitle(R.string.app_name)


                                    // REPORT USER ------------------------------------------------------------
                                    .setPositiveButton(getString(R.string.report_user), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Pass objectID to the other Activity
                                            Intent in = new Intent(UserProfile.this, ReportAdOrUserActivity.class);
                                            Bundle extras = new Bundle();
                                            extras.putString(ReportAdOrUserActivity.USER_OBJECT_ID_EXTRA_KEY, userObj.getFirebaseID());
                                            extras.putString(ReportAdOrUserActivity.REPORT_TYPE_EXTRA_KEY, ReportType.USER.getValue());
                                            in.putExtras(extras);
                                            startActivity(in);
                                        }
                                    })

                                    // BLOCK/UNBLOCK THIS USER ------------------------------------------------
                                    .setNegativeButton(blockTitle, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            // Block User
                                            if (finalBlockTitle.matches(getString(R.string.block_user))) {
                                                hasBlocked.add(userObj.getFirebaseID());
                                                currUser.put(Configs.USER_HAS_BLOCKED, hasBlocked);
                                                currUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        AlertDialog.Builder alert = new AlertDialog.Builder(UserProfile.this);
                                                        alert.setMessage(getString(R.string.you_blocked_this_user) + userObj.getUsername())
                                                                .setTitle(R.string.app_name)
                                                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        finish();
                                                                    }
                                                                })
                                                                .setIcon(R.drawable.logo);
                                                        alert.create().show();
                                                    }
                                                });

                                                // Unblock User
                                            } else {
                                                hasBlocked.remove(userObj.getFirebaseID());
                                                currUser.put(Configs.USER_HAS_BLOCKED, hasBlocked);
                                                currUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        Configs.simpleAlert(getString(R.string.you_are_blocked_message) + userObj.getUsername(), UserProfile.this);
                                                    }
                                                });
                                            }

                                        }
                                    })

                                    .setNeutralButton("Cancel", null)
                                    .setIcon(R.drawable.logo);
                            alert.create().show();


                            // USER IS NOT LOGGED IN
                        } else {
                            startActivity(new Intent(UserProfile.this, WizardActivity.class));
                        }

                    }
                });


                // MARK: - WEBSITE BUTTON ------------------------------------
                final Button webButt = findViewById(R.id.upWebButt);
                if (userObj.getWebsite() != null) {
                    webButt.setText(userObj.getWebsite());
                } else {
                    webButt.setText("");
                    webButt.setEnabled(false);
                }

                webButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(userObj.getWebsite())));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.upBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Init AdMob banner
        AdView mAdView = findViewById(R.id.admobBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }// end onCreate()


    // MARK: - GET USER'S DETAILS -------------------------------------------------------
    void getUserDetails() {


        if (userObj == null || userObj.getUsername() == null) {
            Toast.makeText(getApplicationContext(), "Profile Not Found :)", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Get username
        TextView usernTxt = findViewById(R.id.upUsernameTxt);
        usernTxt.setTypeface(Configs.titSemibold);
        usernTxt.setText("@" + userObj.getUsername());

        // Get fullname
        TextView fnTxt = findViewById(R.id.upFullNameTxt);
        fnTxt.setTypeface(Configs.titSemibold);
        fnTxt.setText(userObj.getFullName());

        // Get about me
        TextView aboutTxt = findViewById(R.id.upAboutTxt);
        aboutTxt.setTypeface(Configs.titRegular);
        if (userObj.getAboutMe() != null) {
            aboutTxt.setText(userObj.getAboutMe());
        } else {
            aboutTxt.setText(getString(R.string.this_user_not_provided_bio));
        }

        // Get joined since
        TextView joinedTxt = findViewById(R.id.upJoinedTxt);
        joinedTxt.setTypeface(Configs.titRegular);
        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.valueOf(userObj.getJoindate()));
        joinedTxt.setText(Configs.timeAgoSinceDate(date,getApplicationContext()));

        // Get verified
        TextView verifTxt = findViewById(R.id.upVerifiedTxt);
        verifTxt.setTypeface(Configs.titRegular);
        if (userObj.getEmailVerified() != null) {
            if (userObj.getEmailVerified()) {
                verifTxt.setText(getString(R.string.varified_yes));
            } else {
                verifTxt.setText(getString(R.string.varified_no));
            }
        } else {
            verifTxt.setText(getString(R.string.varified_no));
        }

        // Get avatar
        final ImageView avImg = findViewById(R.id.upAvatarImg);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions)
                .load(userObj.getAvatar()).into(avImg).clearOnDetach();


        // Call query
        queryMyAds();
    }

    private void queryMyAds() {
        showLoading();

        FirebaseDatabase.getInstance().getReference().child("ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideLoading();

                userAdsArray = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Ads ads = dataSnapshot1.getValue(Ads.class);
                    if (ads.getSellerPointer().getFirebaseID().equals(userObj.getFirebaseID()))
                        userAdsArray.add(ads);

                }

                // CUSTOM LIST ADAPTER
                class ListAdapter extends BaseAdapter {
                    private Context context;

                    public ListAdapter(Context context, List<Ads> objects) {
                        super();
                        this.context = context;
                    }


                    // CONFIGURE CELL
                    @Override
                    public View getView(int position, View cell, ViewGroup parent) {
                        if (cell == null) {
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            assert inflater != null;
                            cell = inflater.inflate(R.layout.cell_myads, null);
                        }

                        // Get Parse object
                        Ads adObj = userAdsArray.get(position);

                        // Get ad title
                        TextView titletxt = cell.findViewById(R.id.cmaAdTitleTxt);
                        titletxt.setTypeface(Configs.titSemibold);
                        titletxt.setText(adObj.getTitle());

                        // Get ad price
                        TextView priceTxt = cell.findViewById(R.id.cmaPricetxt);
                        priceTxt.setTypeface(Configs.titRegular);
                        priceTxt.setText(adObj.getCurrency() + String.valueOf(adObj.getPrice()));

                        // Get date
                        TextView dateTxt = cell.findViewById(R.id.cmaDatetxt);
                        dateTxt.setTypeface(Configs.titRegular);
                        Date date = Calendar.getInstance().getTime();
                        date.setTime(Long.valueOf(adObj.getCreatedAt()));
                        dateTxt.setText(Configs.timeAgoSinceDate(date,getApplicationContext()));

                        // Get Image
                        final ImageView anImage = cell.findViewById(R.id.cmaImage);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.logo1);
                        requestOptions.error(R.drawable.logo1);
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                        Glide.with(getApplicationContext()).
                                setDefaultRequestOptions(requestOptions)
                                .load(adObj.getImage1()).into(anImage).clearOnDetach();


                        return cell;
                    }

                    @Override
                    public int getCount() {
                        return userAdsArray.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return userAdsArray.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }
                }

                // Init ListView and set its adapter
                ListView aList = findViewById(R.id.upUserAdsListView);
                aList.setAdapter(new ListAdapter(UserProfile.this, userAdsArray));
                aList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        Ads adObj = userAdsArray.get(position);
                        configs.setSelectedAd(adObj);
                        Intent i = new Intent(UserProfile.this, AdDetailsActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString(AdDetailsActivity.AD_OBJ_ID_KEY, adObj.getID());
                        i.putExtras(extras);
                        startActivity(i);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoading();
            }
        });

    }


}//@end
