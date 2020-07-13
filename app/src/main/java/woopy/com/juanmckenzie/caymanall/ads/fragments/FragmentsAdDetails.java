package woopy.com.juanmckenzie.caymanall.ads.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.nekocode.badge.BadgeDrawable;
import me.gujun.android.taggroup.TagGroup;
import woopy.com.juanmckenzie.caymanall.Comments;
import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.InboxActivity;
import woopy.com.juanmckenzie.caymanall.Objects.Activity;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ReportAdOrUserActivity;
import woopy.com.juanmckenzie.caymanall.SendFeedback;
import woopy.com.juanmckenzie.caymanall.UserProfile;
import woopy.com.juanmckenzie.caymanall.WatchVideo;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsDetailsPager;
import woopy.com.juanmckenzie.caymanall.ads.adapters.AdImagesPagerAdapter;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.filters.models.ReportType;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentsAdDetails extends BaseFragment {


    public FragmentsAdDetails() {
        // Required empty public constructor
    }


    public static final String AD_OBJ_ID_KEY = "AD_OBJ_ID_KEY";

    private DotsIndicator dots_indicator;
    private ViewPager imagesVP;

    private ImageView likeIV;
    private TextView likesTV;
    private TextView dateTV;
    private TextView titleTV;
    private TextView priceTV;
    private TextView descriptionTV;
    private TextView conditionTV;
    private TextView categoryTV;
    private TextView locationTV;
    private ImageView avatarIV;
    private TextView usernameTV;
    private TextView joinedTV;
    private TextView verifiedTV;
    private TextView commentTV;
    private TextView feedbackTV;
    private ImageView chatIV;
    private ImageView backIV;
    private Button optionsBtn;
    private TextView playVideoTV;
    private TextView Statustext;
    private TextView view;
    View viewmain;

    private AdImagesPagerAdapter imagesAdapter;

    Configs configs;
    /* Variables */
    private Ads adObj;

    public void Domegic(int posi) {


        configs = (Configs) getActivity().getApplicationContext();
        Bundle extras = getActivity().getIntent().getExtras();
        assert extras != null;

        Bundle bundle = this.getArguments();
        int pos = bundle.getInt("position");
        String objectID = configs.getAdsList().get(posi).getID();

        FirebaseDatabase.getInstance().getReference().child("ads/" + objectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainscroll.setVisibility(View.VISIBLE);
                        hidescroll.setVisibility(View.GONE);
                    }
                }, 1000);
                adObj = dataSnapshot.getValue(Ads.class);
                showAdDetails();
                queryIfYouLikedThisAd();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        try {
            // MARK: - OPTIONS BUTTON ------------------------------------
            optionsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage(getString(R.string.select_option1))
                            .setTitle(R.string.app_name)
                            .setPositiveButton(getString(R.string.report_add), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                        if (adObj.getSellerPointer().getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            Toast.makeText(getActivity(), getString(R.string.current_user_is_creator), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                    // Pass objectID to the other Activity
                                    Intent in = new Intent(getActivity(), ReportAdOrUserActivity.class);
                                    configs.setSelectedAd(adObj);
                                    Bundle extras = new Bundle();
                                    extras.putString(ReportAdOrUserActivity.AD_OBJECT_ID_EXTRA_KEY, adObj.getID());
                                    extras.putString(ReportAdOrUserActivity.REPORT_TYPE_EXTRA_KEY, ReportType.AD.getValue());
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
                                            .setLink(Uri.parse("https://caymanall.page.link?" + "ads/" + adObj.getID()))
                                            .setDomainUriPrefix("https://caymanall.page.link?" + "ads/" + adObj.getID())
                                            .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                                                    .setTitle(adObj.getTitle())
                                                    .setImageUrl(Uri.parse(adObj.getImage1().getImage1024()))
                                                    .setDescription(adObj.getDescription())
                                                    .build())
                                            .setAndroidParameters(androidParameters)
                                            .buildShortDynamicLink()
                                            .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                                                @Override
                                                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                                    if (task.isSuccessful()) {
                                                        hideLoading();

                                                        // Short link created
                                                        Uri shortLink = task.getResult().getShortLink();

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

            // MARK: - CHAT WITH SELLER BUTTON ------------------------------------
            viewmain.findViewById(R.id.startchat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        showLoading();
                        FirebaseDatabase.getInstance().getReference().child("users").child(adObj.getSellerPointer().getFirebaseID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                hideLoading();
                                User user = dataSnapshot.getValue(User.class);
                                assert user != null;
                                if (user.getHasBlocked() != null) {
                                    if (user.getHasBlocked().contains(configs.getCurrentUser().getFirebaseID())) {
                                        Configs.simpleAlert(getString(R.string.sorry) + user.getUsername() + getString(R.string.has_blocked), getActivity());
                                    } else {
                                        configs.setSelectedAd(adObj);
                                        Intent i = new Intent(getActivity(), InboxActivity.class);
                                        Bundle extras = new Bundle();
                                        final String inboxId2 = adObj.getSellerPointer().getFirebaseID() + configs.getCurrentUser().getFirebaseID() + adObj.getID();
                                        configs.setSelectedChatID(inboxId2);
                                        extras.putString("adObjectID", adObj.getID());
                                        extras.putString("sellerObjectID", user.getFirebaseID());
                                        i.putExtras(extras);
                                        startActivity(i);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                hideLoading();
                            }
                        });
                    } else {
                        Configs.loginAlert(getString(R.string.logined_mesage6), getActivity());
                    }
                }
            });


            // MARK: - LIKE AD BUTTON ------------------------------------
            likeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        showLoading();
                        if (!CheckForlike) {
                            configs.getCurrentUser().getLikes().add(adObj.getID());
                            FirebaseDatabase.getInstance().getReference().child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("likes").setValue(configs.getCurrentUser().getLikes());
                            FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID()).child("likes").setValue(adObj.getLikes() + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    hideLoading();
                                }
                            });


                            FirebaseDatabase.getInstance().getReference().child("users").child(adObj.getSellerPointer().getFirebaseID()).child("FCM").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String FCM = dataSnapshot.getValue(String.class);
                                    if (FCM != null) {
                                        HashMap<String, String> rawParameters = new HashMap<>();
                                        rawParameters.put("body", "@" + configs.getCurrentUser().getUsername() + getString(R.string.liked_your_add) + adObj.getTitle());
                                        rawParameters.put("title", getString(R.string.app_name));

                                        rawParameters.put("page", "openadd");
                                        rawParameters.put("addid", adObj.getID());

                                        sendPushToSingleInstance(getActivity(), rawParameters, FCM);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            Activity activity = new Activity();

                            activity.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
                            activity.setCurrUser(configs.getCurrentUser());
                            activity.setOtherUser(adObj.getSellerPointer());
                            activity.setText("@" + configs.getCurrentUser().getUsername() + getString(R.string.liked_your_add) + adObj.getTitle());

                            String Id = FirebaseDatabase.getInstance().getReference().child("users").child(activity.getOtherUser().getFirebaseID()).child("activities").push().getKey();
                            activity.setID(Id);
                            FirebaseDatabase.getInstance().getReference().child("users").child(activity.getOtherUser().getFirebaseID()).child("activities").child(Id).setValue(activity);

                        } else {
                            configs.getCurrentUser().getLikes().remove(adObj.getID());
                            FirebaseDatabase.getInstance().getReference().child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("likes").setValue(configs.getCurrentUser().getLikes());
                            FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID()).child("likes").setValue(adObj.getLikes() - 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    hideLoading();
                                }
                            });

                        }
                        CheckForlike = !CheckForlike;
                        queryIfYouLikedThisAd();

                        // YOU'RE NOT LOGGED IN!
                    } else {
                        Configs.loginAlert(getString(R.string.logined_mesage1), getActivity());
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        viewmain.findViewById(R.id.applyforjob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configs = (Configs) getActivity().getApplicationContext();
                showLoading();
                FirebaseDatabase.getInstance().getReference().child("ads").child(adObj.getID()).child("applications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        hideLoading();
                        Formobject formobject = dataSnapshot.getValue(Formobject.class);
                        if (formobject != null) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            builder.setMessage(getString(R.string.already))
                                    .setTitle(R.string.app_name)
                                    .setCancelable(true)
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            android.app.AlertDialog dialog = builder.create();
                            dialog.setIcon(R.drawable.logo);
                            dialog.show();
                        } else {


                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                            builder.setMessage(getString(R.string.if_a_question))
                                    .setTitle(R.string.app_name)
                                    .setCancelable(true)
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            configs.setSelectedAd(adObj);
                                            startActivity(new Intent(getActivity().getApplicationContext(), FormsActivity.class));
                                        }
                                    });
                            android.app.AlertDialog dialog = builder.create();
                            dialog.setIcon(R.drawable.logo);
                            dialog.show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        int pos = bundle.getInt("position");
        Domegic(pos);

    }

    TagGroup tag_group;

    ScrollView mainscroll, hidescroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View main = inflater.inflate(R.layout.fragment_fragments_ad_details, container, false);


        configs = (Configs) getActivity().getApplicationContext();
        Bundle bundle = this.getArguments();
        int pos = bundle.getInt("position");
        if (configs.getAdsList().get(pos).getFeatured()) {
            main = inflater.inflate(R.layout.bannerfeaturedfragment, container, false);
        }

        viewmain = main;
        mainscroll = main.findViewById(R.id.main);
        hidescroll = main.findViewById(R.id.mainhide);
        tag_group = main.findViewById(R.id.tag_group);

        main.findViewById(R.id.groupview).setVisibility(View.GONE);
        tag_group.setVisibility(View.GONE);

        likeIV = main.findViewById(R.id.aad_like_iv);
        imagesVP = main.findViewById(R.id.aad_images_vp);
        dots_indicator = main.findViewById(R.id.dots_indicator);
        likesTV = main.findViewById(R.id.aad_likes_tv);
        dateTV = main.findViewById(R.id.aad_date_tv);
        titleTV = main.findViewById(R.id.aad_title_tv);

        view = main.findViewById(R.id.view);
        priceTV = main.findViewById(R.id.aad_price_tv);
        descriptionTV = main.findViewById(R.id.aad_description_tv);
        conditionTV = main.findViewById(R.id.aad_condition_tv);
        categoryTV = main.findViewById(R.id.aad_category_tv);
        locationTV = main.findViewById(R.id.aad_location_tv);
        avatarIV = main.findViewById(R.id.aad_avatar_iv);
        usernameTV = main.findViewById(R.id.aad_username_tv);
        joinedTV = main.findViewById(R.id.aad_joined_tv);
        verifiedTV = main.findViewById(R.id.aad_verified_tv);
        commentTV = main.findViewById(R.id.aad_comment_tv);
        feedbackTV = main.findViewById(R.id.aad_feedback_tv);
        chatIV = main.findViewById(R.id.aad_chat_iv);
        backIV = main.findViewById(R.id.aad_back_iv);
        optionsBtn = main.findViewById(R.id.aad_options_btn);
        playVideoTV = main.findViewById(R.id.aad_play_video_tv);
        Statustext = main.findViewById(R.id.Statustext);

        return main;
    }


    Boolean CheckForlike = false;

    // MARK: - QUERY IF YOU'VE LIKED THIS AD ------------------------------------------------------
    void queryIfYouLikedThisAd() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    configs.setCurrentUser(dataSnapshot.getValue(User.class));
                    for (String likes : configs.getCurrentUser().getLikes()) {
                        if (likes.equals(adObj.getID())) {
                            CheckForlike = true;
                        }
                    }
                    ((AdsDetailsPager) getActivity()).changecheckfrolisk(CheckForlike);
                    if (CheckForlike) {
                        ((AdsDetailsPager) getActivity()).changedrawablelike(R.drawable.heart_white_filled_ic);
                        likesTV.setText(String.valueOf(adObj.getLikes()));
                    } else {
                        ((AdsDetailsPager) getActivity()).changedrawablelike(R.drawable.heart_white_ic);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID()).child("views").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int views = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        views++;
                    }

                    view.setText(views + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        FirebaseDatabase.getInstance().getReference().child("users").child(adObj.getSellerPointer().getFirebaseID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Get username
                usernameTV.setTypeface(Configs.titRegular);
                usernameTV.setText(user.getUsername());

                // Get joined
                joinedTV.setTypeface(Configs.titRegular);
                Date date1 = Calendar.getInstance().getTime();
                date1.setTime(Long.valueOf(user.getJoindate()));
                joinedTV.setText(Configs.timeAgoSinceDate(date1, getActivity()));

                // Get verified
                verifiedTV.setTypeface(Configs.titRegular);
                if (user.getEmailVerified() != null) {
                    if (user.getEmailVerified()) {
                        verifiedTV.setText(getString(R.string.yes));
                    } else {
                        verifiedTV.setText(getString(R.string.no1));
                    }
                } else {
                    verifiedTV.setText(getString(R.string.no2));
                }

                try {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.logo1);
                    requestOptions.error(R.drawable.logo1);
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                            .load(user.getAvatar().getImage1024()).into(avatarIV).clearOnDetach();
                } catch (Exception ex) {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public float dp2px(float dipValue) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    // MARK: - SHOW AD DETAILS ---------------------------------------------------------------------
    void showAdDetails() {


        imagesAdapter = new AdImagesPagerAdapter(adObj, getActivity(), imagesVP, new AdImagesPagerAdapter.OnImageClickListener() {
            @Override
            public void onImageClicked(String imageFieldKey) {
                openImageFullScreen(imageFieldKey);
            }
        });
        imagesVP.setAdapter(imagesAdapter);
        dots_indicator.setViewPager(imagesVP);

        // Set likes number
        int likesNr = (int) adObj.getLikes();
        likesTV.setText(String.valueOf(likesNr));

        if (adObj.getCategory().equals("Jobs")) {
            viewmain.findViewById(R.id.applyforjob).setVisibility(View.VISIBLE);
        } else {
            viewmain.findViewById(R.id.applyforjob).setVisibility(View.GONE);
        }

        // Get title
        titleTV.setTypeface(Configs.titRegular);
        Statustext.setTypeface(Configs.titRegular);
        view.setTypeface(Configs.titRegular);

        if (adObj.getTags() != null && adObj.getTags().size() != 0) {
            viewmain.findViewById(R.id.groupview).setVisibility(View.VISIBLE);
            tag_group.setVisibility(View.VISIBLE);
            tag_group.setTags(adObj.getTags());
        }

        FirebaseDatabase.getInstance().getReference().child("ads/" + adObj.getID()).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (adObj.getADStatus().equals("Pending")) {

            try {
                BadgeDrawable drawable3 = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    drawable3 = new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                            .badgeColor(getActivity().getApplicationContext().getColor(R.color.colorPrimary))
                            .text1(adObj.getADStatus())
                            .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                            .strokeWidth(1)
                            .build();
                } else {
                    drawable3 = new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                            .badgeColor(Color.BLUE)
                            .text1(adObj.getADStatus())
                            .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                            .strokeWidth(1)
                            .build();
                }
                SpannableString spannableString =
                        new SpannableString(TextUtils.concat(
                                drawable3.toSpannable()
                        ));

                Statustext.setText(spannableString);
            } catch (Exception ignored) {

            }
        } else {

            final BadgeDrawable drawable3 =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                            .badgeColor(Color.RED)
                            .text1(adObj.getADStatus())
                            .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                            .strokeWidth(1)
                            .build();
            SpannableString spannableString =
                    new SpannableString(TextUtils.concat(
                            drawable3.toSpannable()
                    ));

            Statustext.setText(spannableString);
        }


        titleTV.setText(adObj.getTitle());
        // Get date
        dateTV.setTypeface(Configs.titRegular);
        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.parseLong(adObj.getCreatedAt()));
        dateTV.setText(Configs.timeAgoSinceDate(date, getActivity()));

        // Get price
        priceTV.setTypeface(Configs.titRegular);
        priceTV.setText(adObj.getCurrency() + String.valueOf(adObj.getPrice()));

        if (adObj.getCategory().toLowerCase().equals("jobs")) {
            priceTV.setVisibility(View.GONE);
        }

        // Get condition
        conditionTV.setTypeface(Configs.titRegular);
        conditionTV.setText(adObj.getCondition());

        // Get category
        categoryTV.setTypeface(Configs.titRegular);
        categoryTV.setText(adObj.getCategory());


        // Get Location (City, Country)
        ParseGeoPoint gp = new ParseGeoPoint(adObj.getLocation());
        Location adLocation = new Location("dummyprovider");
        adLocation.setLatitude(gp.getLatitude());
        adLocation.setLongitude(gp.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(adLocation.getLatitude(), adLocation.getLongitude(), 1);
            if (Geocoder.isPresent()) {
                Address returnAddress = addresses.get(0);
                String city = returnAddress.getLocality();
                String country = returnAddress.getCountryName();

                if (city == null) {
                    city = "";
                }

                // Show City/Country
                locationTV.setTypeface(Configs.titRegular);
                locationTV.setText(city + ", " + country);

            } else {
//                Toast.makeText(getActivity(), "Geocoder not present!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Configs.simpleAlert(e.getMessage(), getActivity());
        }


        // Get video
        playVideoTV.setTypeface(Configs.titSemibold);
        if (adObj.getVideo() != null) {
            playVideoTV.setText(getString(R.string.video_watch));
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
                configs.setSelectedAd(adObj);
                Intent i = new Intent(getActivity(), WatchVideo.class);
                Bundle extras = new Bundle();
                extras.putString("objectID", adObj.getID());
                i.putExtras(extras);
                startActivity(i);
            }
        });


        // Get description
        descriptionTV.setTypeface(Configs.titRegular);
        descriptionTV.setText(adObj.getDescription());


        // SELLERS DETAILS -------------------------------------


        final User sellerPointer = adObj.getSellerPointer();


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                .load(adObj.getSellerPointer().getAvatar()).into(avatarIV).clearOnDetach();

        // TAP ON AVATAR -> SHOW USER PROFILE
        avatarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass objectID to the other Activity
                Intent i = new Intent(getActivity(), UserProfile.class);
                Bundle extras = new Bundle();
                extras.putString("objectID", sellerPointer.getFirebaseID());
                i.putExtras(extras);
                startActivity(i);
            }
        });


        // Get username
        usernameTV.setTypeface(Configs.titRegular);
        usernameTV.setText(sellerPointer.getUsername());

        // Get joined
        joinedTV.setTypeface(Configs.titRegular);
        Date date1 = Calendar.getInstance().getTime();
        date1.setTime(Long.valueOf(sellerPointer.getJoindate()));
        joinedTV.setText(Configs.timeAgoSinceDate(date1, getActivity()));

        // Get verified
        verifiedTV.setTypeface(Configs.titRegular);
        if (sellerPointer.getEmailVerified() != null) {
            if (sellerPointer.getEmailVerified()) {
                verifiedTV.setText(getString(R.string.yes));
            } else {
                verifiedTV.setText(getString(R.string.no3));
            }
        } else {
            verifiedTV.setText(getString(R.string.no4));
        }


        // MARK: - COMMENTS BUTTON ------------------------------------
        commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    configs.setSelectedAd(adObj);
                    Intent i = new Intent(getActivity(), Comments.class);
                    Bundle extras = new Bundle();
                    extras.putString("objectID", adObj.getID());
                    i.putExtras(extras);
                    startActivity(i);

                } else {
                    Configs.loginAlert(getString(R.string.login_mesage5), getActivity());
                }
            }
        });


        // MARK: - SEND FEEDBACK BUTTON ------------------------------------
        feedbackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {


                    if (configs.getCurrentUser().getFeedbacks().contains(adObj.getID())) {
                        Configs.simpleAlert(getString(R.string.already_feedback), getActivity());
                    } else {
                        configs.setSelectedAd(adObj);
                        Intent i = new Intent(getActivity(), SendFeedback.class);
                        Bundle extras = new Bundle();
                        extras.putString("objectID", adObj.getID());
                        extras.putString("sellerID", sellerPointer.getFirebaseID());
                        i.putExtras(extras);
                        startActivity(i);
                    }

                } else {
                    Configs.loginAlert(getString(R.string.logined_mesage6), getActivity());
                }
            }
        });
    }

    public static void sendPushToSingleInstance(final Context activity, final HashMap dataValue, final String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/) {


        final String url = "https://fcm.googleapis.com/fcm/send";
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

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Map<String, Object> rawParameters = new Hashtable<>();
                rawParameters.put("data", new JSONObject(dataValue));
                rawParameters.put("to", instanceIdToken);
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

    // OPEN TAPPED IMAGE INTO FULL SCREEN ACTIVITY
    void openImageFullScreen(String imageName) {
        Intent i = new Intent(getActivity(), FullScreenPreview.class);
        Bundle extras = new Bundle();
        extras.putString("imageName", imageName);
        extras.putString("objectID", adObj.getID());
        i.putExtras(extras);
        startActivity(i);
    }
}


