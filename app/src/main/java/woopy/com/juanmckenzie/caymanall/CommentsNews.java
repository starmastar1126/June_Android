package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.Context;
import android.content.pm.ActivityInfo;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import woopy.com.juanmckenzie.caymanall.Objects.CommentsObjNews;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class CommentsNews extends AppCompatActivity {

    /* Views */
    TextView adTitleTxt;
    EditText commEditText;
    ListView commListView;


    /* Variables */
    EventObj adObj;
    List<CommentsObjNews> commentsArray;
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
        adObj = configs.getSelectedEvent();
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
                    Configs.simpleAlert(getString(R.string.you_must_type_something), CommentsNews.this);

                } else {
                    dismissKeyboard();
                    Configs.showPD(getString(R.string.please_wait), CommentsNews.this);

                    final CommentsObjNews commObj = new CommentsObjNews();

                    commObj.setID(FirebaseDatabase.getInstance().getReference().child("NewsComments").child(adObj.getID()).push().getKey());
                    commObj.setAdPointer(adObj);
                    commObj.setComment(commEditText.getText().toString());
                    commObj.setUserPointer(configs.getCurrentUser());
                    commObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
                    FirebaseDatabase.getInstance().getReference().child("NewsComments").child(adObj.getID()).child(commObj.getID()).setValue(commObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference().child("News").child(adObj.getID()).child("comments").setValue(adObj.getCommentsObjcs().size());
                            adObj.setComments(adObj.getCommentsObjcs().size());


                            commEditText.setText("");
                            Configs.hidePD();


                            HashMap<String, String> rawParameters = new HashMap<>();

                            final String pushMessage = "@" + configs.getCurrentUser().getUsername() + getString(R.string.commented_on_your_news) + adObj.getTitle() + "'";
                            rawParameters.put("body", pushMessage);
                            rawParameters.put("title", getString(R.string.app_name));
                            rawParameters.put("addid", adObj.getID());
                            rawParameters.put("page", "newscomment");
                            sendPushToSingleInstance(getApplicationContext(), rawParameters, adObj.getSellerPointer().getFCM());


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
        Configs.showPD(getString(R.string.please_wait), CommentsNews.this);


        FirebaseDatabase.getInstance().getReference().child("NewsComments").child(adObj.getID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adObj.setCommentsObjcs(new ArrayList<CommentsObjNews>());
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommentsObjNews commentsobj = dataSnapshot1.getValue(CommentsObjNews.class);
                    adObj.getCommentsObjcs().add(commentsobj);
                }

                commentsArray = adObj.getCommentsObjcs();
                Configs.hidePD();


                // CUSTOM LIST ADAPTER
                class ListAdapter extends BaseAdapter {
                    private Context context;

                    public ListAdapter(Context context, List<CommentsObjNews> objects) {
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
                        final CommentsObjNews cObj = commentsArray.get(position);


                        TextView uTxt = finalCell.findViewById(R.id.cCommUsernameTxt);
                        uTxt.setTypeface(Configs.titSemibold);
                        uTxt.setText(cObj.getUserPointer().getUsername());

                        // Get avatar
                        final ImageView anImage = finalCell.findViewById(R.id.cCommAvatarImg);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.logo1);
                        requestOptions.error(R.drawable.logo1);
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                        Glide.with(CommentsNews.this).setDefaultRequestOptions(requestOptions)
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
                commListView.setAdapter(new ListAdapter(CommentsNews.this, commentsArray));


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
            public byte[] getBody() throws AuthFailureError {
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
