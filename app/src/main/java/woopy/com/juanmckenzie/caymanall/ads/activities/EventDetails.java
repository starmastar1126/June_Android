package woopy.com.juanmckenzie.caymanall.ads.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.parse.ParseGeoPoint;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ReportAdOrUserActivity;
import woopy.com.juanmckenzie.caymanall.WatchVideo;
import woopy.com.juanmckenzie.caymanall.ads.adapters.EventsImagesPagerAdapter;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.models.ReportType;
import woopy.com.juanmckenzie.caymanall.selledit.activities.CreateEvents;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

public class EventDetails extends BaseActivity implements OnMapReadyCallback {


    public static final String AD_OBJ_ID_KEY = "AD_OBJ_ID_KEY";

    private DotsIndicator dots_indicator;
    private ViewPager imagesVP;
    private TextView dateTV;
    private TextView titleTV;
    private TextView priceTV;
    private TextView descriptionTV;
    private TextView locationTV, aad_Attending_tv;
    private TextView Website;
    private TextView Phone;
    private ImageView backIV;
    private TextView playVideoTV;
    private EventsImagesPagerAdapter imagesAdapter;
    private Button optionsBtn;

    RadioButton Attend;
    ScrollView mainscroll, hidescroll;

    Configs configs;
    /* Variables */
    private EventObj adObj;

    private void setConditionRB(RadioButton radioButton, boolean isChecked) {
        if (isChecked) {
            radioButton.setChecked(isChecked);
            radioButton.setTextColor(UIUtils.getColor(R.color.white));
            radioButton.setBackgroundResource(R.drawable.shapeselected);
        } else {
            radioButton.setChecked(isChecked);
            radioButton.setTextColor(UIUtils.getColor(R.color.black));
            radioButton.setBackgroundResource(R.drawable.shapeunselected);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        mainscroll = findViewById(R.id.main);
        hidescroll = findViewById(R.id.mainhide);

        mainscroll.setVisibility(View.GONE);
        hidescroll.setVisibility(View.VISIBLE);


        configs = (Configs) getApplicationContext();
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        initViews();

        Attend = findViewById(R.id.Attend);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.msMapView);
        mapFragment.getMapAsync(this);
        MapsInitializer.initialize(this);


        if (configs.getSelectedEvent().getSellerPointer().getFirebaseID().equals(FirebaseAuth.getInstance().getUid())) {
            findViewById(R.id.Edit).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.Edit).setVisibility(View.GONE);
        }

        findViewById(R.id.Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent adDetailsIntent = new Intent(EventDetails.this, CreateEvents.class);
                adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, configs.getSelectedEvent().getID());
                startActivity(adDetailsIntent);

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Events/" + configs.getSelectedEvent().getID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adObj = dataSnapshot.getValue(EventObj.class);
                // Call queries
                showAdDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        ParseGeoPoint gp = new ParseGeoPoint(configs.getSelectedEvent().getLocation());

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(gp.getLatitude(), gp.getLongitude()))
                .title("Event Location"));
        // Zoom the Google Map to Deafult Location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gp.getLatitude(), gp.getLongitude())));

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(false);

    }

    private void initViews() {
        imagesVP = findViewById(R.id.aad_images_vp);
        dots_indicator = findViewById(R.id.dots_indicator);
        dateTV = findViewById(R.id.aad_date_tv);
        titleTV = findViewById(R.id.aad_title_tv);
        priceTV = findViewById(R.id.aad_price_tv);
        descriptionTV = findViewById(R.id.aad_description_tv);
        locationTV = findViewById(R.id.aad_location_tv);
        aad_Attending_tv = findViewById(R.id.aad_Attending_tv);
        Website = findViewById(R.id.Website);
        Phone = findViewById(R.id.Phone);
        backIV = findViewById(R.id.aad_back_iv);
        optionsBtn = findViewById(R.id.aad_options_btn);
        playVideoTV = findViewById(R.id.aad_play_video_tv);
    }

    Boolean CheckForlike = false;
    String Check = null;

    // MARK: - SHOW AD DETAILS ---------------------------------------------------------------------
    void showAdDetails() {
        imagesAdapter = new EventsImagesPagerAdapter(adObj, getApplicationContext(), new EventsImagesPagerAdapter.OnImageClickListener() {
            @Override
            public void onImageClicked(String imageFieldKey) {
                openImageFullScreen(imageFieldKey);
            }
        });
        imagesVP.setAdapter(imagesAdapter);
        dots_indicator.setViewPager(imagesVP);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainscroll.setVisibility(View.VISIBLE);
                hidescroll.setVisibility(View.GONE);
            }
        }, 500);

        // Get title
        titleTV.setTypeface(Configs.titRegular);
        aad_Attending_tv.setTypeface(Configs.titRegular);
        titleTV.setText(adObj.getTitle());


        TextView view = findViewById(R.id.view);
        view.setText(adObj.getTitle());
        try {
            view.setText(adObj.getViews().values().size() + getString(R.string.views1));
        } catch (Exception ex) {
            aad_Attending_tv.setText(getString(R.string._attending));
        }

        try {
            aad_Attending_tv.setText(adObj.getAttend().values().size() + getString(R.string.attending_message));
        } catch (Exception ex) {

        }
        if (adObj.getAttend() != null) {
            Check = adObj.getAttend().get(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        if (Check != null) {
            Attend.setText(getString(R.string.atending_message1));
            setConditionRB(Attend, true);
        } else {
            Attend.setText(getString(R.string.attend));
            setConditionRB(Attend, false);
        }
        Attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Check == null) {
                    FirebaseDatabase.getInstance().getReference().child("Events").child(adObj.getID()).child("attend").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                } else {
                    Check = null;
                    adObj.getAttend().remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseDatabase.getInstance().getReference().child("Events").child(adObj.getID()).child("attend").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }

                if (Check != null) {
                    Attend.setText(getString(R.string.attending_message2));
                    setConditionRB(Attend, true);
                } else {
                    Attend.setText(getString(R.string.attend1));
                    setConditionRB(Attend, false);
                }

                try {
                    aad_Attending_tv.setText(adObj.getAttend().values().size() + getString(R.string.attending_mesage3));
                } catch (Exception ex) {
                    aad_Attending_tv.setText(getString(R.string.zero_attending_message1));

                }
            }
        });


        Date date1 = Calendar.getInstance().getTime();
        date1.setTime(Long.valueOf(adObj.getStartingtime()));
        Date date2 = Calendar.getInstance().getTime();
        long diff = date2.getTime() - date1.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (days < 7)
            adObj.setDaysleft((7 - days) + "");

        // Get date
        dateTV.setTypeface(Configs.titRegular);
        dateTV.setText(adObj.getDaysleft() + getString(R.string.days_left));


        // Get condition
        Phone.setTypeface(Configs.titRegular);
        Phone.setText(adObj.getPhone());

        if (Phone.getText().toString().equals("")) {
            Phone.setText(getString(R.string.nan));
        }

        Website.setTypeface(Configs.titRegular);
        Website.setText(adObj.getWebiste());

        if (Website.getText().toString().equals("")) {
            Website.setText(getString(R.string.nan));
        }


        // Get Location (City, Country)
        ParseGeoPoint gp = new ParseGeoPoint(adObj.getLocation());
        Location adLocation = new Location("dummyprovider");
        adLocation.setLatitude(gp.getLatitude());
        adLocation.setLongitude(gp.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(EventDetails.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(adLocation.getLatitude(), adLocation.getLongitude(), 1);
            if (Geocoder.isPresent()) {
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
                locationTV.setTypeface(Configs.titRegular);
                locationTV.setText(city + ", " + country);

            } else {
//                Toast.makeText(getApplicationContext(), "Geocoder not present!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
//            Configs.simpleAlert(e.getMessage(), EventDetails.this);
        }


        // Get video
        playVideoTV.setTypeface(Configs.titSemibold);
        if (adObj.getVideo() != null) {
            playVideoTV.setText(getString(R.string.watch_video1));
            playVideoTV.setEnabled(true);
        } else {
            playVideoTV.setText(getString(R.string.video_nan));
            playVideoTV.setEnabled(false);
        }

        // Show it in the WatchVideo screen
        playVideoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass objectID to the other Activity
                configs.setSelectedEvent(adObj);
                Ads ads = new Ads();
                ads.setVideo(adObj.getVideo());
                configs.setSelectedAd(ads);
                Intent i = new Intent(EventDetails.this, WatchVideo.class);
                Bundle extras = new Bundle();
                extras.putString("objectID", adObj.getID());
                i.putExtras(extras);
                startActivity(i);
            }
        });


        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EventDetails.this);
                alert.setMessage(getString(R.string.select_option1))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.report_event), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    if (adObj.getSellerPointer().getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.current_user_is_creator), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                // Pass objectID to the other Activity
                                Intent in = new Intent(EventDetails.this, ReportAdOrUserActivity.class);
                                configs.setSelectedEvent(adObj);
                                Bundle extras = new Bundle();
                                extras.putString(ReportAdOrUserActivity.AD_OBJECT_ID_EXTRA_KEY, adObj.getID());
                                extras.putString(ReportAdOrUserActivity.REPORT_TYPE_EXTRA_KEY, ReportType.EVENTS.getValue());
                                in.putExtras(extras);
                                startActivity(in);
                            }
                        })
                        .setNegativeButton(getString(R.string.share), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                showLoading();
//                                    Bitmap firstImageBmp = imagesAdapter.getFirstImageBmp();
//
//                                    File imageFile = FileUtils.createCachedFileToShare(firstImageBmp);
//                                    Uri fileUri = FileUtils.getUri(imageFile);
                                DynamicLink.AndroidParameters androidParameters = new DynamicLink.AndroidParameters.Builder("com.juanmckenzie.CaymanAll").build();

                                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                        .setLink(Uri.parse("https://caymanall.page.link?" + "Events/" + adObj.getID()))
                                        .setDomainUriPrefix("https://caymanall.page.link?" + "Events/" + adObj.getID() + "/Events")
                                        .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                                                .setTitle(adObj.getTitle())
                                                .setImageUrl(Uri.parse(adObj.getImage1().getImage1024()))
                                                .setDescription(adObj.getDescription())
                                                .build())
                                        .setAndroidParameters(androidParameters)
                                        .buildShortDynamicLink()
                                        .addOnCompleteListener(EventDetails.this, new OnCompleteListener<ShortDynamicLink>() {
                                            @Override
                                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                                if (task.isSuccessful()) {
                                                    hideLoading();
                                                    // Short link created
                                                    Uri shortLink = task.getResult().getShortLink();
                                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    intent.setType("text/plain");
                                                    intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                                    startActivity(Intent.createChooser(intent, getString(R.string.share_on)));

                                                } else {
                                                    hideLoading();

                                                }
                                            }
                                        });


                            }
                        })
                        .setNeutralButton(getString(R.string.cancel), null)
                        .setIcon(R.drawable.logo);
                alert.create().show();
            }
        });


        // Get description
        descriptionTV.setTypeface(Configs.titRegular);
        descriptionTV.setText(adObj.getDescription());


    }

    // OPEN TAPPED IMAGE INTO FULL SCREEN ACTIVITY
    void openImageFullScreen(String imageName) {
        Intent i = new Intent(EventDetails.this, FullScreenPreview.class);
        Bundle extras = new Bundle();
        extras.putString("imageName", imageName);
        extras.putString("objectID", adObj.getID());
        i.putExtras(extras);
        startActivity(i);
    }
}
