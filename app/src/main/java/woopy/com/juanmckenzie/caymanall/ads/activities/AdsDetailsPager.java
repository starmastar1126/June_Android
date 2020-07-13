package woopy.com.juanmckenzie.caymanall.ads.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import woopy.com.juanmckenzie.caymanall.Objects.Activity;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Image;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ads.fragments.FragmentsAdDetails;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class AdsDetailsPager extends BaseActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    Boolean CheckForlike = false;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_details_pager);


        findViewById(R.id.aad_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        Configs configs = (Configs) getApplicationContext();
        pagerAdapter = new PagerAds(getSupportFragmentManager(), configs.getAdsList(), getApplicationContext());
        mPager.setAdapter(pagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(configs.getSelectedadposition());


        likeIV = findViewById(R.id.aad_like_iv);
        likeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ads adObj = configs.getAdsList().get(mPager.getCurrentItem());
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

                                    sendPushToSingleInstance(AdsDetailsPager.this, rawParameters, FCM);
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
                        configs.getAdsList().get(mPager.getCurrentItem()).getLikedBy().remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

                    if (CheckForlike) {
                        changedrawablelike(R.drawable.heart_white_filled_ic);
                    } else {
                        changedrawablelike(R.drawable.heart_white_ic);
                    }


                    // YOU'RE NOT LOGGED IN!
                } else {
                    Configs.loginAlert(getString(R.string.you_must_be_logged), AdsDetailsPager.this);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public List<FragmentsAdDetails> mFragmentList = new ArrayList<>();

    public class PagerAds extends FragmentPagerAdapter {
        List<Ads> adsall;
        Context con;


        public PagerAds(FragmentManager fragmentManager, List<Ads> ads, Context context) {
            super(fragmentManager);
            this.con = context;
            adsall = ads;
            mFragmentList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return adsall.size();
        }

        @Override
        public Fragment getItem(int position) {

            Configs configs = (Configs) con;
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            configs.setSelectedadposition(position);
            FragmentsAdDetails fragmentsAdDetails = new FragmentsAdDetails();
            fragmentsAdDetails.setArguments(bundle);
            mFragmentList.add(fragmentsAdDetails);
            return fragmentsAdDetails;

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    ImageView likeIV;

    public void changecheckfrolisk(Boolean check) {
        CheckForlike = check;
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


    public void changedrawablelike(int res) {
        likeIV.setImageResource(res);
    }


}