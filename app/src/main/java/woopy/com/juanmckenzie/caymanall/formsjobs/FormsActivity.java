package woopy.com.juanmckenzie.caymanall.formsjobs;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import woopy.com.juanmckenzie.caymanall.formsjobs.adapters.MyPagerAdapter;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;

public class FormsActivity extends BaseActivity {


    MyPagerAdapter adapterViewPager;
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Started = false;
            }
        }, 3000);


//        showLoading();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                hideLoading();
//
//                if (adapterViewPager.CanMove(0)) {
//                    vpPager.setCurrentItem(1);
//                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (vpPager.getCurrentItem() == 1)
//                            if (adapterViewPager.CanMove(1)) {
//                                vpPager.setCurrentItem(2);
//                            }
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (vpPager.getCurrentItem() == 2)
//                                    if (adapterViewPager.CanMove(2)) {
//                                        vpPager.setCurrentItem(3);
//                                    }
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        if (vpPager.getCurrentItem() == 4)
//                                            if (adapterViewPager.CanMove(4)) {
//                                                vpPager.setCurrentItem(5);
//                                            }
//
//                                    }
//                                }, 1000);
//                            }
//                        }, 1000);
//                    }
//                }, 1000);
//
//
//            }
//        }, 1000);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                adapterViewPager.SaveAll();
            }

            @Override
            public void onPageSelected(int position) {
                if (!adapterViewPager.CanMove(position - 1)) {
                    vpPager.setCurrentItem(position - 1);
                    if (position > 1) {
                        Toast.makeText(getApplicationContext(), getString(R.string.complete_current_form), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterViewPager.SaveAll();
    }

    public void sendFCM(String FirebaseID, final String Username, final String JobTittle, final String ID) {
        showLoading();
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseID).child("FCM").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String FCM = dataSnapshot.getValue(String.class);
                if (FCM != null) {
                    HashMap<String, String> rawParameters = new HashMap<>();
                    rawParameters.put("body", "@" + Username + getString(R.string.applied_for_your_ad) + JobTittle);
                    rawParameters.put("title", getString(R.string.app_name));

                    rawParameters.put("page", "jobs");
                    rawParameters.put("addid", ID);

                    sendPushToSingleInstance(getApplicationContext(), rawParameters, FCM);
                } else {
                    hideLoading();
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendPushToSingleInstance(final Context activity, final HashMap dataValue, final String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/) {


        final String url = "https://fcm.googleapis.com/fcm/send";
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        FormsActivity.this.finish();
                        hideLoading();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FormsActivity.this.finish();
                        hideLoading();
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


    Boolean Started = true;

    public void moveto(int pos) {
        if (Started)
            vpPager.setCurrentItem(pos);
    }

}
