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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class Feedbacks extends AppCompatActivity {

    /* Variables */
    User userObj;
    List<woopy.com.juanmckenzie.caymanall.Objects.Feedbacks> feedbacksArray;
    Configs configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbacks);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        configs = (Configs) getApplicationContext();

        // Get userObjectID from previous .java
        Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("userObjectID");
        if (objectID == null) {
            userObj = configs.getCurrentUser();
            queryFeedbacks();
        } else {
            queryFeedbacksother();
        }


        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.feedsBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }// end onCreate()


    // MARK: - QUERY FEEDBACKS ------------------------------------------------------------
    void queryFeedbacks() {
        Configs.showPD(getString(R.string.please_wait), Feedbacks.this);


        FirebaseDatabase.getInstance().getReference().child("feedbacks").child(configs.getCurrentUser().getFirebaseID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                feedbacksArray = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    woopy.com.juanmckenzie.caymanall.Objects.Feedbacks feedbacks = dataSnapshot1.getValue(woopy.com.juanmckenzie.caymanall.Objects.Feedbacks.class);
                    feedbacksArray.add(feedbacks);
                }


                // CUSTOM LIST ADAPTER
                class ListAdapter extends BaseAdapter {
                    private Context context;

                    public ListAdapter(Context context, List<woopy.com.juanmckenzie.caymanall.Objects.Feedbacks> objects) {
                        super();
                        this.context = context;
                    }


                    // CONFIGURE CELL
                    @Override
                    public View getView(int position, View cell, ViewGroup parent) {
                        if (cell == null) {
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            assert inflater != null;
                            cell = inflater.inflate(R.layout.cell_feedback, null);
                        }
                        final View finalCell = cell;


                        // Get Parse object
                        final woopy.com.juanmckenzie.caymanall.Objects.Feedbacks fObj = feedbacksArray.get(position);

                        // Get Ad title
                        TextView adTitleTxt = finalCell.findViewById(R.id.feedsAdTitleTxt);
                        adTitleTxt.setTypeface(Configs.titSemibold);
                        adTitleTxt.setText(fObj.getAdTitle());

                        // Get Feedback text
                        TextView feedTxt = finalCell.findViewById(R.id.feedsFeedbackTxt);
                        feedTxt.setTypeface(Configs.titRegular);
                        feedTxt.setText(fObj.getText());

                        // Get Date & Author
                        TextView byDateTxt = finalCell.findViewById(R.id.feedsByDateTxt);
                        byDateTxt.setTypeface(Configs.titSemibold);
                        Date date = Calendar.getInstance().getTime();
                        date.setTime(Long.valueOf(fObj.getCreatedAt()));
                        String dateStr = Configs.timeAgoSinceDate(date,getApplicationContext());
                        byDateTxt.setText("@" + fObj.getReviewerPointer().getUsername() + " • " + dateStr);

                        // Get stars image
                        ImageView starsImg = finalCell.findViewById(R.id.feedsStarsImg);
                        int[] starImages = new int[]{R.drawable.star0, R.drawable.star1,
                                R.drawable.star2, R.drawable.star3, R.drawable.star4, R.drawable.star5
                        };
                        if (Integer.parseInt(fObj.getStars()) == 0) {
                            starsImg.setImageResource(starImages[0]);
                        } else {
                            starsImg.setImageResource(starImages[Integer.parseInt(fObj.getStars())]);
                        }


                        return cell;
                    }

                    @Override
                    public int getCount() {
                        return feedbacksArray.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return feedbacksArray.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }
                }

                // Init ListView and set its adapter
                ListView aList = findViewById(R.id.feedsListView);
                aList.setAdapter(new ListAdapter(Feedbacks.this, feedbacksArray));

                Configs.hidePD();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Configs.hidePD();
            }
        });


    }

    void queryFeedbacksother() {
        Configs.showPD(getString(R.string.please_wait), Feedbacks.this);


        FirebaseDatabase.getInstance().getReference().child("feedbacks").child(getIntent().getExtras().getString("userObjectID")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                feedbacksArray = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    woopy.com.juanmckenzie.caymanall.Objects.Feedbacks feedbacks = dataSnapshot1.getValue(woopy.com.juanmckenzie.caymanall.Objects.Feedbacks.class);
                    feedbacksArray.add(feedbacks);
                }


                // CUSTOM LIST ADAPTER
                class ListAdapter extends BaseAdapter {
                    private Context context;

                    public ListAdapter(Context context, List<woopy.com.juanmckenzie.caymanall.Objects.Feedbacks> objects) {
                        super();
                        this.context = context;
                    }


                    // CONFIGURE CELL
                    @Override
                    public View getView(int position, View cell, ViewGroup parent) {
                        if (cell == null) {
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            assert inflater != null;
                            cell = inflater.inflate(R.layout.cell_feedback, null);
                        }
                        final View finalCell = cell;


                        // Get Parse object
                        final woopy.com.juanmckenzie.caymanall.Objects.Feedbacks fObj = feedbacksArray.get(position);

                        // Get Ad title
                        TextView adTitleTxt = finalCell.findViewById(R.id.feedsAdTitleTxt);
                        adTitleTxt.setTypeface(Configs.titSemibold);
                        adTitleTxt.setText(fObj.getAdTitle());

                        // Get Feedback text
                        TextView feedTxt = finalCell.findViewById(R.id.feedsFeedbackTxt);
                        feedTxt.setTypeface(Configs.titRegular);
                        feedTxt.setText(fObj.getText());

                        // Get Date & Author
                        TextView byDateTxt = finalCell.findViewById(R.id.feedsByDateTxt);
                        byDateTxt.setTypeface(Configs.titSemibold);
                        Date date = Calendar.getInstance().getTime();
                        date.setTime(Long.valueOf(fObj.getCreatedAt()));
                        String dateStr = Configs.timeAgoSinceDate(date,getApplicationContext());
                        byDateTxt.setText("@" + fObj.getReviewerPointer().getUsername() + " • " + dateStr);

                        // Get stars image
                        ImageView starsImg = finalCell.findViewById(R.id.feedsStarsImg);
                        int[] starImages = new int[]{R.drawable.star0, R.drawable.star1,
                                R.drawable.star2, R.drawable.star3, R.drawable.star4, R.drawable.star5
                        };
                        if (Integer.parseInt(fObj.getStars()) == 0) {
                            starsImg.setImageResource(starImages[0]);
                        } else {
                            starsImg.setImageResource(starImages[Integer.parseInt(fObj.getStars())]);
                        }


                        return cell;
                    }

                    @Override
                    public int getCount() {
                        return feedbacksArray.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return feedbacksArray.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }
                }

                // Init ListView and set its adapter
                ListView aList = findViewById(R.id.feedsListView);
                aList.setAdapter(new ListAdapter(Feedbacks.this, feedbacksArray));

                Configs.hidePD();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Configs.hidePD();
            }
        });


    }


}//@end
