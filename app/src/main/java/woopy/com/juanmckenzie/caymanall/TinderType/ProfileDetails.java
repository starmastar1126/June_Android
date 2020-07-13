package woopy.com.juanmckenzie.caymanall.TinderType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.devs.readmoreoption.ReadMoreOption;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import me.gujun.android.taggroup.TagGroup;
import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Adapters.SliderAdapterExample;
import woopy.com.juanmckenzie.caymanall.TinderType.Adapters.SliderAdapterExamplecopy;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.TUser;
import woopy.com.juanmckenzie.caymanall.UserProfile;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.ads.activities.EventDetails;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;

public class ProfileDetails extends BaseActivity {


    TextView name, gander, aboutme, joinedTV, milesaway, education, Hobbiesheading, Languagesheading;
    SliderView image;
    ImageView avatarIV, verified;
    Configs configs;
    TUser i;
    List<String> strings;
    TagGroup hobies, langulage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);


        configs = (Configs) getApplicationContext();

        hobies = findViewById(R.id.tag_groupLanguages);
        langulage = findViewById(R.id.tag_groupHobbies);


        Hobbiesheading = findViewById(R.id.Hobbiesheading);
        Languagesheading = findViewById(R.id.Languagesheading);
        education = findViewById(R.id.education);

        hobies.setTags(configs.getSelectedUser().getHobies());
        langulage.setTags(configs.getSelectedUser().getLanguages());


        if (configs.getSelectedUser().getHobies().size() != 0) {
            hobies.setVisibility(View.VISIBLE);
            Hobbiesheading.setVisibility(View.VISIBLE);
        } else {
            hobies.setVisibility(View.GONE);
            Hobbiesheading.setVisibility(View.GONE);
        }


        if (configs.getSelectedUser().getLanguages().size() != 0) {
            langulage.setVisibility(View.VISIBLE);
            Languagesheading.setVisibility(View.VISIBLE);
        } else {
            langulage.setVisibility(View.GONE);
            Languagesheading.setVisibility(View.GONE);
        }

        findViewById(R.id.upBackButt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GPSTracker gpsTracker = new GPSTracker(this);


        Configs configs = (Configs) getApplicationContext();
        if (configs.getSelectedUser() == null) {
            finish();
        }


        i = configs.getSelectedUser();

        if (i.getEducation() != null) {
            education.setVisibility(View.VISIBLE);
            education.setText(i.getEducation());
        } else {
            education.setVisibility(View.GONE);
        }


        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.valueOf(i.getJoindate()));
        joinedTV = findViewById(R.id.joinedTV);
        joinedTV.setText(Configs.timeAgoSinceDate(date, getApplicationContext()));

        avatarIV = findViewById(R.id.fa_avatar_iv);

        Glide.with(Objects.requireNonNull(ProfileDetails.this))
                .load(i.getAvatar()).into(avatarIV).clearOnDetach();
        name = findViewById(R.id.name);
        verified = findViewById(R.id.verified);
        milesaway = findViewById(R.id.milesaway);
        gander = findViewById(R.id.Gander1);
        image = findViewById(R.id.imageSlider1);
        aboutme = findViewById(R.id.aboutme);


        name.setText(i.getUsername() + "| " + i.getGander());
        gander.setText(i.getGander());
        aboutme.setText(i.getAboutMe());
        double distance = distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), Objects.requireNonNull(i).getLatlong().getLatitude(), i.getLatlong().getLongitude());
        milesaway.setText(String.format("%.2f", distance) + getApplicationContext().getString(R.string.miles_away));

        if (i.getShowdistance()) {
            milesaway.setVisibility(View.VISIBLE);
        } else {
            milesaway.setVisibility(View.GONE);
        }

        strings = new ArrayList<>();

        if (i.getAvatar().getUrl() != null) {
            if (!i.getAvatar().equals("")) {
                strings.add(i.getAvatar().getUrl());
            }
        }
        if (!i.getImage2().equals("")) {
            strings.add(i.getImage2().getImage512());
        }
        if (!i.getImage3().equals("")) {
            strings.add(i.getImage3().getImage512());
        }

        SliderAdapterExamplecopy adapter = new SliderAdapterExamplecopy(this, strings);
        image.setSliderAdapter(adapter);

        image.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Intent i = new Intent(ProfileDetails.this, FullScreenPreview.class);
                Bundle extras = new Bundle();
                extras.putString("imageName", strings.get(position));
                i.putExtras(extras);
                startActivity(i);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(configs.getSelectedUser().getFirebaseID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                i = dataSnapshot.getValue(TUser.class);
                gander.setText(i.getGander());
                name.setText(i.getUsername() + "| " + i.getGander());
                gander.setText(i.getGander());
                aboutme.setText(i.getAboutMe());
                double distance = distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), Objects.requireNonNull(i).getLatlong().getLatitude(), i.getLatlong().getLongitude());
                milesaway.setText(String.format("%.2f", distance) + getApplicationContext().getString(R.string.miles_away));
                if (i.getShowdistance()) {
                    milesaway.setVisibility(View.VISIBLE);
                } else {
                    milesaway.setVisibility(View.GONE);
                }


                if (i.getEducation() != null) {
                    education.setVisibility(View.VISIBLE);
                    education.setText(i.getEducation());
                } else {
                    education.setVisibility(View.GONE);
                }


                if (i.getAboutMe() != null) {
                    if (i.getAboutMe().equals("")) {
                        i.setAboutMe("---");
                        aboutme.setText(getString(R.string.this_user_not_provided_bio));
                    }
                } else {
                    i.setAboutMe("---");
                    aboutme.setText(getString(R.string.this_user_not_provided_bio));
                }

                strings = new ArrayList<>();

                if (i.getAvatar() != null) {
                    if (!i.getAvatar().equals("")) {
                        strings.add(i.getAvatar().getUrl());
                    }
                }
                if (!i.getImage2().equals("")) {
                    strings.add(i.getImage2().getImage512());
                }
                if (!i.getImage3().equals("")) {
                    strings.add(i.getImage3().getImage512());
                }


                if (i.getEmailVerified()) {
                    verified.setImageResource(R.drawable.verifiedyes);
                } else {
                    verified.setImageResource(R.drawable.verifiedno);

                }

                SliderAdapterExamplecopy adapter = new SliderAdapterExamplecopy(ProfileDetails.this, strings);
                image.setSliderAdapter(adapter);

                image.setOnIndicatorClickListener(new DrawController.ClickListener() {
                    @Override
                    public void onIndicatorClicked(int position) {
                        Intent i = new Intent(ProfileDetails.this, FullScreenPreview.class);
                        Bundle extras = new Bundle();
                        extras.putString("imageName", strings.get(position));
                        i.putExtras(extras);
                        startActivity(i);
                    }
                });


                strings = new ArrayList<>();

                if (i.getAvatar() != null) {
                    if (!i.getAvatar().equals("")) {
                        strings.add(i.getAvatar().getUrl());
                    }
                }
                if (!i.getImage2().equals("")) {
                    strings.add(i.getImage2().getImage512());
                }
                if (!i.getImage3().equals("")) {
                    strings.add(i.getImage3().getImage512());
                }
//                queryMyAds();
                adapter = new SliderAdapterExamplecopy(ProfileDetails.this, strings);
                image.setSliderAdapter(adapter);

                image.setOnIndicatorClickListener(new DrawController.ClickListener() {
                    @Override
                    public void onIndicatorClicked(int position) {
                        Intent i = new Intent(ProfileDetails.this, FullScreenPreview.class);
                        Bundle extras = new Bundle();
                        extras.putString("imageName", strings.get(position));
                        i.putExtras(extras);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 0.62137);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    List<Ads> userAdsArray;

    private void queryMyAds() {
        showLoading();

        FirebaseDatabase.getInstance().getReference().child("ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideLoading();

                userAdsArray = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Ads ads = dataSnapshot1.getValue(Ads.class);
                    if (ads.getSellerPointer().getFirebaseID().equals(i.getFirebaseID()))
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
                        dateTxt.setText(Configs.timeAgoSinceDate(date, getApplicationContext()));

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

                if (userAdsArray.size() == 0) {
                    aList.setEmptyView(findViewById(R.id.empty_view));
                }
                aList.setAdapter(new ListAdapter(ProfileDetails.this, userAdsArray));
                aList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        Ads adObj = userAdsArray.get(position);
                        configs.setSelectedAd(adObj);
                        Intent i = new Intent(ProfileDetails.this, AdDetailsActivity.class);
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


}
