package woopy.com.juanmckenzie.caymanall.home.fragments;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Activity;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.home.adapters.ActivitiesAdapter;

public class ActivityActivity extends BaseActivity implements ActivitiesAdapter.ActivityItemListener {

    private RecyclerView activitiesRV;
    private TextView tipTV;

    // Pagination
    private ActivitiesAdapter activityListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        activitiesRV = ActivityActivity.this.findViewById(R.id.fa_activities_rv);
        tipTV = ActivityActivity.this.findViewById(R.id.fa_tip_tv);

        queryActivity();
        findViewById(R.id.ase_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    List<Activity> activities = new ArrayList<>();

    // MARK: - QUERY ACTIVITY ---------------------------------------------------------------
    private void queryActivity() {
        showLoading();


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("activities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activities = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Activity activity = dataSnapshot1.getValue(Activity.class);
                    activities.add(activity);
                }
                setUpActivityList(activities);
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoading();
            }
        });

    }


    private void setUpActivityList(List<Activity> activityList) {
        if (activityList.isEmpty()) {
            tipTV.setVisibility(View.GONE);
            return;
        }

        activityListAdapter = new ActivitiesAdapter(activityList, getApplicationContext(), this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityActivity.this);
        activitiesRV.setLayoutManager(layoutManager);
        activitiesRV.setAdapter(activityListAdapter);

    }

    @Override
    public void onActivityClicked(Activity activityObj) {

//        Intent i = new Intent(getApplicationContext(), UserProfile.class);
//        Bundle extras = new Bundle();
//        extras.putString("objectID", activityObj.getOtherUser().getFirebaseID());
//        i.putExtras(extras);
//        startActivity(i);
    }

    @Override
    public void onActivityLongClicked(final Activity activityObj) {

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("activities").child(activityObj.getID()).removeValue();
        activityListAdapter.removeActivity(activityObj);
        Toast.makeText(getApplicationContext(), "Activity removed!", Toast.LENGTH_SHORT).show();
    }
}
