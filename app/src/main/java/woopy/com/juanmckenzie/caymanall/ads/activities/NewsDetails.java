package woopy.com.juanmckenzie.caymanall.ads.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Calendar;
import java.util.Date;

import woopy.com.juanmckenzie.caymanall.CommentsNews;
import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ReportAdOrUserActivity;
import woopy.com.juanmckenzie.caymanall.WatchVideo;
import woopy.com.juanmckenzie.caymanall.ads.adapters.EventsImagesPagerAdapter;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.models.ReportType;
import woopy.com.juanmckenzie.caymanall.selledit.activities.CreateNews;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class NewsDetails extends BaseActivity {


    public static final String AD_OBJ_ID_KEY = "AD_OBJ_ID_KEY";

    private DotsIndicator dots_indicator;
    private ViewPager imagesVP;
    private TextView dateTV;
    private TextView titleTV;
    private TextView descriptionTV;
    private TextView locationTV;
    private ImageView backIV;
    private TextView playVideoTV;
    private EventsImagesPagerAdapter imagesAdapter;

    Configs configs;
    /* Variables */
    private EventObj adObj;

    ScrollView mainscroll, hidescroll;
    private Button optionsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);


        configs = (Configs) getApplicationContext();
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        initViews();

        mainscroll = findViewById(R.id.main);
        hidescroll = findViewById(R.id.mainhide);

        mainscroll.setVisibility(View.GONE);
        hidescroll.setVisibility(View.VISIBLE);

        if (configs.getSelectedEvent().getSellerPointer().getFirebaseID().equals(FirebaseAuth.getInstance().getUid())) {
            findViewById(R.id.Edit).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.Edit).setVisibility(View.GONE);
        }

        findViewById(R.id.Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent adDetailsIntent = new Intent(NewsDetails.this, CreateNews.class);
                adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, configs.getSelectedEvent().getID());
                startActivity(adDetailsIntent);

            }
        });
        FirebaseDatabase.getInstance().getReference().child("News/" + configs.getSelectedEvent().getID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adObj = dataSnapshot.getValue(EventObj.class);
                // Call queries
                try {

                } catch (Exception ignored) {

                }
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

        findViewById(R.id.aad_comment_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    configs.setSelectedEvent(adObj);
                    Intent i = new Intent(NewsDetails.this, CommentsNews.class);
                    Bundle extras = new Bundle();
                    extras.putString("objectID", adObj.getID());
                    i.putExtras(extras);
                    startActivity(i);

                } else {
                    Configs.loginAlert(getString(R.string.logined_message), NewsDetails.this);
                }
            }
        });
    }


    private void initViews() {
        imagesVP = findViewById(R.id.aad_images_vp);
        dots_indicator = findViewById(R.id.dots_indicator);
        dateTV = findViewById(R.id.aad_date_tv);
        titleTV = findViewById(R.id.aad_title_tv);
        descriptionTV = findViewById(R.id.aad_description_tv);
        locationTV = findViewById(R.id.aad_location_tv);
        backIV = findViewById(R.id.aad_back_iv);
        playVideoTV = findViewById(R.id.aad_play_video_tv);
        optionsBtn = findViewById(R.id.aad_options_btn);

    }

    Boolean CheckForlike = false;

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
        titleTV.setText(adObj.getTitle());

        TextView view = findViewById(R.id.view);
        view.setText(adObj.getTitle());

        try {
            view.setText(adObj.getViews().values().size() + getString(R.string.views1));
        } catch (Exception ex) {

        }


        // Get date
        dateTV.setTypeface(Configs.titRegular);
        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.parseLong(adObj.getCreatedAt()));
        dateTV.setText(Configs.timeAgoSinceDate(date, getApplicationContext()));


        // Get video
        playVideoTV.setTypeface(Configs.titSemibold);
        if (adObj.getVideo() != null) {
            playVideoTV.setText(getString(R.string.watch_video1));
            playVideoTV.setEnabled(true);
        } else {
            playVideoTV.setText(getString(R.string.watch_video_nan));
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
                Intent i = new Intent(NewsDetails.this, WatchVideo.class);
                Bundle extras = new Bundle();
                extras.putString("objectID", adObj.getID());
                i.putExtras(extras);
                startActivity(i);
            }
        });


        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(NewsDetails.this);
                alert.setMessage(getString(R.string.select_option1))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.report_news), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    if (adObj.getSellerPointer().getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.current_user_is_creator), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                // Pass objectID to the other Activity
                                Intent in = new Intent(NewsDetails.this, ReportAdOrUserActivity.class);
                                configs.setSelectedEvent(adObj);
                                Bundle extras = new Bundle();
                                extras.putString(ReportAdOrUserActivity.AD_OBJECT_ID_EXTRA_KEY, adObj.getID());
                                extras.putString(ReportAdOrUserActivity.REPORT_TYPE_EXTRA_KEY, ReportType.NEWS.getValue());
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
                                        .setLink(Uri.parse("https://caymanall.page.link?" + "News/" + adObj.getID()))
                                        .setDomainUriPrefix("https://caymanall.page.link?" + "News/" + adObj.getID())
                                        .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                                                .setTitle(adObj.getTitle())
                                                .setImageUrl(Uri.parse(adObj.getImage1().getImage1024()))
                                                .setDescription(adObj.getDescription())
                                                .build())
                                        .setAndroidParameters(androidParameters)
                                        .buildShortDynamicLink()
                                        .addOnCompleteListener(NewsDetails.this, new OnCompleteListener<ShortDynamicLink>() {
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
                                                    startActivity(Intent.createChooser(intent, "Share on..."));

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
        Intent i = new Intent(NewsDetails.this, FullScreenPreview.class);
        Bundle extras = new Bundle();
        extras.putString("imageName", imageName);
        extras.putString("objectID", adObj.getID());
        i.putExtras(extras);
        startActivity(i);
    }
}
