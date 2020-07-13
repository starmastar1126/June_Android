package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.Context;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import woopy.com.juanmckenzie.caymanall.Objects.Activity;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.CommentsObj;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class Comments extends AppCompatActivity {

    /* Views */
    TextView adTitleTxt;
    EditText commEditText;
    ListView commListView;


    /* Variables */
    Ads adObj;
    List<CommentsObj> commentsArray;
    Configs configs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        configs = (Configs) getApplicationContext();

        // Init views
        adTitleTxt = findViewById(R.id.commAdNameTxt);
        adTitleTxt.setTypeface(Configs.titRegular);
        commEditText = findViewById(R.id.commCommentEditText);
        commEditText.setTypeface(Configs.titRegular);
        commListView = findViewById(R.id.commListView);


        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        adObj = configs.getSelectedAd();
        if (adObj == null) {
            finish();
        }
        // Set Ad title
        adTitleTxt.setText(adObj.getTitle());


        // Call query
        queryComments();


        // MARK: - SEND COMMENT BUTTON ------------------------------------
        Button sendButt = findViewById(R.id.commSendButt);
        sendButt.setTypeface(Configs.titSemibold);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (commEditText.getText().toString().matches("")) {
                    Configs.simpleAlert(getString(R.string.you_must_type_something), Comments.this);

                } else {
                    dismissKeyboard();
                    Configs.showPD(getString(R.string.please_wait), Comments.this);

                    final CommentsObj commObj = new CommentsObj();
                    commObj.setID(FirebaseDatabase.getInstance().getReference().child("adsComments").child(adObj.getID()).push().getKey());
                    commObj.setAdPointer(adObj);
                    commObj.setComment(commEditText.getText().toString());
                    commObj.setUserPointer(configs.getCurrentUser());
                    commObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
                    FirebaseDatabase.getInstance().getReference().child("adsComments").child(adObj.getID()).child(commObj.getID()).setValue(commObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference().child("ads").child(adObj.getID()).child("comments").setValue(adObj.getCommentsAll().size());
                            adObj.setComments(adObj.getCommentsAll().size());


                            commEditText.setText("");
                            Configs.hidePD();


                            final String pushMessage = "@" + configs.getCurrentUser().getUsername() + getString(R.string.commented_on_your) + adObj.getTitle() + "'";

                            FirebaseDatabase.getInstance().getReference().child("users").child(adObj.getSellerPointer().getFirebaseID()).child("FCM").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String FCM = dataSnapshot.getValue(String.class);
                                    if (FCM != null) {
                                        HashMap<String, String> rawParameters = new HashMap<>();
                                        final String pushMessage = "@" + configs.getCurrentUser().getUsername() + getString(R.string.commented_on_your) + adObj.getTitle() + "'";
                                        rawParameters.put("body", pushMessage);
                                        rawParameters.put("title", getString(R.string.app_name));

                                        rawParameters.put("page", "comments");
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


                        }
                    });


                }// end IF

            }
        });


        // MARK: - REFRESH BUTTON ------------------------------------
        Button refreshButt = findViewById(R.id.commRefreshButt);
        refreshButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard();
                queryComments();
            }
        });


        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.commBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }// end onCreate()


    // MARK: - QUERY COMMENTS --------------------------------------------------------------------
    void queryComments() {
        Configs.showPD(getString(R.string.please_wait), Comments.this);


        FirebaseDatabase.getInstance().getReference().child("adsComments").child(adObj.getID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adObj.setCommentsAll(new ArrayList<CommentsObj>());
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommentsObj commentsobj = dataSnapshot1.getValue(CommentsObj.class);
                    adObj.getCommentsAll().add(commentsobj);
                }

                commentsArray = adObj.getCommentsAll();
                Configs.hidePD();


                // CUSTOM LIST ADAPTER
                class ListAdapter extends BaseAdapter {
                    private Context context;

                    public ListAdapter(Context context, List<CommentsObj> objects) {
                        super();
                        this.context = context;
                    }


                    // CONFIGURE CELL
                    @Override
                    public View getView(int position, View cell, ViewGroup parent) {
                        if (cell == null) {
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            assert inflater != null;
                            cell = inflater.inflate(R.layout.cell_comment, null);
                        }
                        final View finalCell = cell;

                        // Get Parse object
                        final CommentsObj cObj = commentsArray.get(position);


                        TextView uTxt = finalCell.findViewById(R.id.cCommUsernameTxt);
                        uTxt.setTypeface(Configs.titSemibold);
                        uTxt.setText(cObj.getUserPointer().getUsername());

                        // Get avatar
                        final ImageView anImage = finalCell.findViewById(R.id.cCommAvatarImg);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.logo1);
                        requestOptions.error(R.drawable.logo1);
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                        Glide.with(Comments.this).setDefaultRequestOptions(requestOptions)
                                .load(cObj.getUserPointer().getAvatar()).into(anImage).clearOnDetach();


                        // Get comment
                        TextView commTxt = finalCell.findViewById(R.id.cCommCommentTxt);
                        commTxt.setTypeface(Configs.titRegular);
                        commTxt.setText(cObj.getComment());

                        // Get date
                        TextView dateTxt = finalCell.findViewById(R.id.cCommDateTxt);
                        dateTxt.setTypeface(Configs.titRegular);
                        Date date = Calendar.getInstance().getTime();
                        date.setTime(Long.valueOf(cObj.getCreatedAt()));
                        dateTxt.setText(Configs.timeAgoSinceDate(date,getApplicationContext()));


                        return cell;
                    }

                    @Override
                    public int getCount() {
                        return commentsArray.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return commentsArray.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }
                }


                // Init ListView and set its adapter
                commListView.setAdapter(new ListAdapter(Comments.this, commentsArray));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Configs.hidePD();
            }
        });

    }


    // MARK: - DISMISS KEYBOARD
    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commEditText.getWindowToken(), 0);
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

}//@end
