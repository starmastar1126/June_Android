package woopy.com.juanmckenzie.caymanall.home.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import woopy.com.juanmckenzie.caymanall.Comments;
import woopy.com.juanmckenzie.caymanall.CommentsNews;
import woopy.com.juanmckenzie.caymanall.DistanceMapActivity;
import woopy.com.juanmckenzie.caymanall.Feedbacks;
import woopy.com.juanmckenzie.caymanall.InboxActivity;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.Purchase;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.InboxActivityTinderType;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.ChatsT;
import woopy.com.juanmckenzie.caymanall.TinderType.TinderHome;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.ads.activities.EventDetails;
import woopy.com.juanmckenzie.caymanall.ads.activities.NewsDetails;
import woopy.com.juanmckenzie.caymanall.common.CreatePickerDialog;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.JobApplications;
import woopy.com.juanmckenzie.caymanall.home.fragments.AccountFragment;
import woopy.com.juanmckenzie.caymanall.home.fragments.EventsFragment;
import woopy.com.juanmckenzie.caymanall.home.fragments.NewsFragment;
import woopy.com.juanmckenzie.caymanall.selledit.activities.CreateEvents;
import woopy.com.juanmckenzie.caymanall.selledit.activities.CreateNews;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;
import woopy.com.juanmckenzie.caymanall.wizard.WizardActivity;
import woopy.com.juanmckenzie.caymanall.home.adapters.BottomNavigationAdapter;
import woopy.com.juanmckenzie.caymanall.home.adapters.HomeScreenPagerAdapter;
import woopy.com.juanmckenzie.caymanall.selledit.activities.SellEditItemActivity;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public class HomeActivity extends AppCompatActivity implements BottomNavigationAdapter.BottomNavigationListener {

    private static final int INITIAL_SELECTED_TAB_POSITION = 0;

    private ViewPager contentVP;
    private RecyclerView bottomNavigationRV;
    Configs myapplication;

    private HomeScreenPagerAdapter pagerAdapter;
    private BottomNavigationAdapter bottomNavigationAdapter;
    private boolean isUserLoggedIn;
    FirebaseAuth auth;
    Dialog dialog;


    private DrawerLayout mDrawer;

    @Override
    public void onNewIntent(Intent intent) {
        myapplication = (Configs) getApplicationContext();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("page")) {
                // extract the extra-data in the Notification
                String page = extras.getString("page");
                final String chatid = extras.getString("chatid");
                final String userid = extras.getString("userid");
                String addid = extras.getString("addid");

                if (page.equals("chattindertype")) {
                    FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(chatid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ChatsT chatObj = dataSnapshot.getValue(ChatsT.class);
                            myapplication.setSelectedChatID(chatObj.getInboxID());
                            myapplication.setSelectedChat(chatObj);
                            Intent i = new Intent(getApplicationContext(), InboxActivityTinderType.class);
                            startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                if (page.equals("chattindertype1")) {
                    FirebaseDatabase.getInstance().getReference().child("ads").child(addid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Ads ads = dataSnapshot.getValue(Ads.class);
                            if (ads != null) {
                                Intent i = new Intent(HomeActivity.this, InboxActivity.class);
                                myapplication.setSelectedChatID(chatid);
                                myapplication.setSelectedAd(ads);
                                startActivity(i);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                if (page.equals("chatpage")) {

                    if (userid != null) {

                        try {
                            if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                if (myapplication.getCurrentUser().getVisibility() == null) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                                    alert.setMessage(getString(R.string.by_entering_this_category_your_agree))
                                            .setTitle(R.string.app_name)
                                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("visibility").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            myapplication.setSecondtabs(true);
                                                            myapplication.setSecondtabs(true);
                                                            Intent adsListIntent = new Intent(HomeActivity.this, TinderHome.class);
                                                            startActivity(adsListIntent);
                                                        }
                                                    });

                                                }
                                            })
                                            .setIcon(R.drawable.logo);
                                    alert.create().show();

                                } else {
                                    myapplication.setSecondtabs(true);
                                    Intent adsListIntent = new Intent(HomeActivity.this, TinderHome.class);
                                    startActivity(adsListIntent);
                                }
                            } else {
                                myapplication.setSecondtabs(true);
                                Intent adsListIntent = new Intent(HomeActivity.this, TinderHome.class);
                                startActivity(adsListIntent);
                            }
                        } catch (Exception ex) {
                            myapplication.setSecondtabs(true);
                            Intent adsListIntent = new Intent(HomeActivity.this, TinderHome.class);
                            startActivity(adsListIntent);
                        }
                    }


                }


                if (page.equals("openadd")) {
                    Intent adDetailsIntent = new Intent(HomeActivity.this, AdDetailsActivity.class);
                    adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, addid);
                    startActivity(adDetailsIntent);
                }

                if (page.equals("openevent")) {

                    FirebaseDatabase.getInstance().getReference().child("Events").child(addid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            EventObj ads = dataSnapshot.getValue(EventObj.class);
                            if (ads != null) {
                                Intent i = new Intent(HomeActivity.this, EventDetails.class);
                                i.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, addid);
                                myapplication.setSelectedEvent(ads);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                if (page.equals("comments")) {

                    FirebaseDatabase.getInstance().getReference().child("ads").child(addid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Ads ads = dataSnapshot.getValue(Ads.class);
                            if (ads != null) {
                                Intent i = new Intent(HomeActivity.this, Comments.class);
                                myapplication.setSelectedChatID(chatid);
                                myapplication.setSelectedAd(ads);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                if (page.equals("jobs")) {

                    FirebaseDatabase.getInstance().getReference().child("ads").child(addid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Ads ads = dataSnapshot.getValue(Ads.class);
                            if (ads != null) {
                                Intent i = new Intent(HomeActivity.this, JobApplications.class);
                                myapplication.setSelectedChatID(chatid);
                                myapplication.setSelectedAd(ads);
                                startActivity(i);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                if (page.equals("feedbacks")) {
                    Intent i = new Intent(getApplicationContext(), Feedbacks.class);
                    i.putExtras(extras);
                    startActivity(i);
                }

                if (page.equals("newscomment")) {

                    FirebaseDatabase.getInstance().getReference().child("News").child(addid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            EventObj ads = dataSnapshot.getValue(EventObj.class);
                            if (ads != null) {
                                Intent i = new Intent(HomeActivity.this, CommentsNews.class);
                                myapplication.setSelectedEvent(ads);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
        setContentView(R.layout.activity_home);


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        String path;
                        String url;
                        if (pendingDynamicLinkData != null) {
                            path = deepLink.getPath();
                            url = deepLink.toString();
                            String[] atrrt = url.split("\\?");
                            String Id = url.split("\\?")[1];
//                            String code = url.split("\\?")[2];
                            dialog.show();
                            FirebaseDatabase.getInstance().getReference().child(Id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    if (Id.toLowerCase().contains("ads")) {
                                        dialog.dismiss();
                                        Ads ads = dataSnapshot.getValue(Ads.class);
                                        myapplication.setSelectedAd(ads);
                                        Intent adDetailsIntent = new Intent(HomeActivity.this, AdDetailsActivity.class);
                                        adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, ads.getID());
                                        startActivity(adDetailsIntent);

                                    }
                                    if (Id.toLowerCase().contains("events")) {
                                        dialog.dismiss();
                                        EventObj ads = dataSnapshot.getValue(EventObj.class);
                                        myapplication.setSelectedEvent(ads);
                                        Intent adDetailsIntent = new Intent(HomeActivity.this, EventDetails.class);
                                        adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, ads.getID());
                                        startActivity(adDetailsIntent);

                                    }
                                    if (Id.toLowerCase().contains("news")) {
                                        dialog.dismiss();
                                        EventObj ads = dataSnapshot.getValue(EventObj.class);
                                        myapplication.setSelectedEvent(ads);
                                        Intent adDetailsIntent = new Intent(HomeActivity.this, NewsDetails.class);
                                        adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, ads.getID());
                                        startActivity(adDetailsIntent);

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    dialog.dismiss();
                                }
                            });
                        }

                        if (pendingDynamicLinkData != null) {
                            path = pendingDynamicLinkData.getLink().getPath();
                        }


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });


        dialog = Configs.buildProgressLoadingDialog(this);
        auth = FirebaseAuth.getInstance();
        myapplication = (Configs) getApplicationContext();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (auth.getCurrentUser() == null) {
            isUserLoggedIn = false;
        } else {
            isUserLoggedIn = true;
        }

        setUpContent();
    }

    String TAG = "FCM";

    Boolean Started = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            Started = true;
                            auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    if (auth.getCurrentUser() == null) {
                                        isUserLoggedIn = false;
                                    } else {

                                        FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                myapplication.setCurrentUser(dataSnapshot.getValue(User.class));
//

                                                if (myapplication.getCurrentUser() != null) {


                                                    if (Started) {
                                                        String token = task.getResult().getToken();
                                                        SaveUserlocation();
                                                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FCM").setValue(token);

                                                    }
                                                    Started = false;
                                                    if (myapplication.getCurrentUser().getReported()) {
                                                        FirebaseAuth.getInstance().signOut();

                                                        try {
                                                            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                                                            alert.setMessage(getString(R.string.sorry_your_account_is_blocked))
                                                                    .setTitle(R.string.app_name)
                                                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                                                                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            startActivity(homeIntent);
                                                                        }
                                                                    })
                                                                    .setIcon(R.drawable.logo);
                                                            alert.create().show();
                                                        } catch (Exception ignore) {

                                                        }

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        isUserLoggedIn = true;
                                    }
                                }
                            });
                            // Get new Instance ID token
                        }
                    });
        }
//        setUpContent();
    }

    private void initViews() {
        contentVP = findViewById(R.id.ah_content_vp);
        bottomNavigationRV = findViewById(R.id.ah_bottom_navigation_rv);
    }

    private void setUpContent() {
        initViews();
        setUpViewPager();
        setUpBottomNavigation();
    }


    public EventsFragment Events;
    public NewsFragment News;

    private void setUpViewPager() {
        Events = new EventsFragment();
        News = new NewsFragment();
        pagerAdapter = new HomeScreenPagerAdapter(getSupportFragmentManager(), Events, News);
        contentVP.setAdapter(pagerAdapter);
        contentVP.setOffscreenPageLimit(1);
    }

    private void setUpBottomNavigation() {
        ViewTreeObserver vto = bottomNavigationRV.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpBottomNavigationRV();
                bottomNavigationRV.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setUpBottomNavigationRV() {
        bottomNavigationAdapter = new BottomNavigationAdapter(this, INITIAL_SELECTED_TAB_POSITION);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        bottomNavigationRV.setAdapter(bottomNavigationAdapter);
        bottomNavigationRV.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onTabSelected(int pos) {
        try {

            if (!isUserLoggedIn && pos != INITIAL_SELECTED_TAB_POSITION) {
                startActivity(new Intent(this, WizardActivity.class));
                return false;
            }

            contentVP.setCurrentItem(pos);
        } catch (Exception ex) {

        }
        return true;
    }

    public void OpenDrawer() {
        if (!mDrawer.isDrawerOpen(GravityCompat.START)) mDrawer.openDrawer(GravityCompat.START);
        else mDrawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public void onSpecialTabSelected() {
        if (!isUserLoggedIn) {
            startActivity(new Intent(this, WizardActivity.class));
            return;
        }

        CreatePickerDialog dialog = new CreatePickerDialog(this);
        dialog.setOnOptionSelectedListener(new CreatePickerDialog.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int index) {
                if (index == CreatePickerDialog.AdC) {
                    startActivity(new Intent(HomeActivity.this, SellEditItemActivity.class));
                } else if (index == CreatePickerDialog.EventC) {
                    startActivity(new Intent(HomeActivity.this, CreateEvents.class));
                } else if (index == CreatePickerDialog.NewsC) {
                    startActivity(new Intent(HomeActivity.this, CreateNews.class));
                } else if (index == CreatePickerDialog.BannerC) {
                    startActivity(new Intent(HomeActivity.this, Purchase.class));
                }
            }
        });
        dialog.show();


        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Location chosenLocation = data.getParcelableExtra(DistanceMapActivity.LOCATION_EXTRA_KEY);
            float distanceInMiles = data.getFloatExtra(DistanceMapActivity.DISTANCE_EXTRA_KEY, Configs.distanceInMiles);
            try {
                Events.Result(distanceInMiles, chosenLocation);
                News.Result(distanceInMiles, chosenLocation);
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    GPSTracker gpsTracker;

    public void SaveUserlocation() {
        gpsTracker = new GPSTracker(this);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latlong").child("latitude").setValue(gpsTracker.getLatitude());
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latlong").child("longitude").setValue(gpsTracker.getLongitude());
        subs(gpsTracker.getLatitude(), gpsTracker.getLongitude());
    }

    private void subs(Double lat, Double lng) {
        try {
            Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
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
//                locationTV.setText(city + ", " + country);//                locationTV.setText(city + ", " + country);
                FirebaseDatabase.getInstance().getReference().child("areas").child(FirebaseDatabase.getInstance().getReference().child("areas").push().getKey()).setValue(country);
                FirebaseDatabase.getInstance().getReference().child("emaillistwitharea").child(country).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                FirebaseDatabase.getInstance().getReference().child("emaillistwitharea").child("all").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());


                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("country").setValue(country);
                String Location = city + ", " + country + " , " + locality + " , " + adminArea;
                if (Location.toLowerCase().contains("cayman islands") || Location.toLowerCase().contains("cayman")) {
                    FirebaseMessaging.getInstance().subscribeToTopic("CaymanAllIslands");
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic("CaymanAllIslandsTest");

                }
            } else {
//                Toast.makeText(getApplicationContext(), "Location could not be retrieved!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
//            ToastUtils.showMessage(e.getMessage());
        }
    }

}
