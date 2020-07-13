package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import woopy.com.juanmckenzie.caymanall.Objects.Activity;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Feedbacks;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class SendFeedback extends AppCompatActivity {

    /* Views */
    Button sButt1, sButt2, sButt3, sButt4, sButt5;
    EditText reviewTxt;


    /* Variables */
    Ads adObj;
    User sellerObj;
    int starNr = 0;
    Configs configs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_feedback);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Init views
        reviewTxt = findViewById(R.id.sfReviewTxt);


        configs = (Configs) getApplicationContext();

        // MARK: - STAR BUTTONS
        sButt1 = findViewById(R.id.starButt1);
        sButt2 = findViewById(R.id.starButt2);
        sButt3 = findViewById(R.id.starButt3);
        sButt4 = findViewById(R.id.starButt4);
        sButt5 = findViewById(R.id.starButt5);

        sButt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starNr = 1;
                sButt1.setBackgroundResource(R.drawable.full_star);
                sButt2.setBackgroundResource(R.drawable.empty_star);
                sButt3.setBackgroundResource(R.drawable.empty_star);
                sButt4.setBackgroundResource(R.drawable.empty_star);
                sButt5.setBackgroundResource(R.drawable.empty_star);
            }
        });
        sButt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starNr = 2;
                sButt1.setBackgroundResource(R.drawable.full_star);
                sButt2.setBackgroundResource(R.drawable.full_star);
                sButt3.setBackgroundResource(R.drawable.empty_star);
                sButt4.setBackgroundResource(R.drawable.empty_star);
                sButt5.setBackgroundResource(R.drawable.empty_star);
            }
        });
        sButt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starNr = 3;
                sButt1.setBackgroundResource(R.drawable.full_star);
                sButt2.setBackgroundResource(R.drawable.full_star);
                sButt3.setBackgroundResource(R.drawable.full_star);
                sButt4.setBackgroundResource(R.drawable.empty_star);
                sButt5.setBackgroundResource(R.drawable.empty_star);
            }
        });
        sButt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starNr = 4;
                sButt1.setBackgroundResource(R.drawable.full_star);
                sButt2.setBackgroundResource(R.drawable.full_star);
                sButt3.setBackgroundResource(R.drawable.full_star);
                sButt4.setBackgroundResource(R.drawable.full_star);
                sButt5.setBackgroundResource(R.drawable.empty_star);
            }
        });
        sButt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starNr = 5;
                sButt1.setBackgroundResource(R.drawable.full_star);
                sButt2.setBackgroundResource(R.drawable.full_star);
                sButt3.setBackgroundResource(R.drawable.full_star);
                sButt4.setBackgroundResource(R.drawable.full_star);
                sButt5.setBackgroundResource(R.drawable.full_star);
            }
        });


        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("objectID");
        String sellerID = extras.getString("sellerID");
        // Get adObj
        adObj = configs.getSelectedAd();


        // Get sellerObj
        sellerObj = adObj.getSellerPointer();


        TextView usernameTxt = findViewById(R.id.sfUsernameTxt);
        usernameTxt.setTypeface(Configs.titRegular);
        usernameTxt.setText(getString(R.string.to) + sellerObj.getUsername());


        // MARK: - SEND FEEDBACK BUTTON
        Button sendButt = findViewById(R.id.sfSendButt);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard();
                Configs.showPD(getString(R.string.please_wait), SendFeedback.this);

                if (reviewTxt.getText().toString().matches("") || starNr == 0) {
                    Configs.hidePD();
                    Configs.simpleAlert(getString(R.string.you_must_rate), SendFeedback.this);


                } else {
                    Feedbacks fObj = new Feedbacks();

                    fObj.setStars(starNr + "");
                    fObj.setText(reviewTxt.getText().toString());
                    fObj.setAdTitle(adObj.getTitle());
                    fObj.setReviewerPointer(configs.getCurrentUser());
                    fObj.setSellerPointer(sellerObj);
                    fObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
                    fObj.setID(FirebaseDatabase.getInstance().getReference().child("feedbacks").child(sellerObj.getFirebaseID()).push().getKey());

                    FirebaseDatabase.getInstance().getReference().child("feedbacks").child(sellerObj.getFirebaseID()).child(fObj.getID()).setValue(fObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Configs.hidePD();

                            configs.getCurrentUser().getFeedbacks().add(adObj.getID());
                            FirebaseDatabase.getInstance().getReference().child("users").child(sellerObj.getFirebaseID()).child("Feedbacks").setValue(configs.getCurrentUser().getFeedbacks());
                            final String pushMessage = "@" + configs.getCurrentUser().getUsername() + getString(R.string.sent_you_a) + starNr + getString(R.string.star_feedback_for) + adObj.getTitle() + "'";

                            FirebaseDatabase.getInstance().getReference().child("users").child(adObj.getSellerPointer().getFirebaseID()).child("FCM").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String FCM = dataSnapshot.getValue(String.class);
                                    if (FCM != null) {
                                        HashMap<String, String> rawParameters = new HashMap<>();
                                        final String pushMessage = "@" + configs.getCurrentUser().getUsername() +getString(R.string.sent_you_a)+ starNr +  getString(R.string.star_feedback_for)  + adObj.getTitle() + "'";
                                        rawParameters.put("body", pushMessage);
                                        rawParameters.put("title", getString(R.string.app_name));

                                        rawParameters.put("page", "feedbacks");
                                        rawParameters.put("addid", adObj.getID());
                                        sendPushToSingleInstance(getApplicationContext(), rawParameters, FCM);
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
                            activity.setText(pushMessage);

                            String Id = FirebaseDatabase.getInstance().getReference().child("users").child(activity.getOtherUser().getFirebaseID()).child("activities").push().getKey();
                            activity.setID(Id);
                            FirebaseDatabase.getInstance().getReference().child("users").child(activity.getOtherUser().getFirebaseID()).child("activities").child(Id).setValue(activity);


                            // Fire Alert
                            AlertDialog.Builder alert = new AlertDialog.Builder(SendFeedback.this);
                            alert.setMessage(getString(R.string.thanks_for_your_feedback))
                                    .setTitle(R.string.app_name)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(R.drawable.logo);
                            alert.create().show();


                        }
                    });


                }/// end IF

            }
        });


        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.sfBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }// end onCreate()


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

    // DISMISS KEYBOARD
    void dismissKeyboard() {
        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(reviewTxt.getWindowToken(), 0);
    }


}//@end
