package woopy.com.juanmckenzie.caymanall.formsjobs;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.adapters.ApplicationsAdapter;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class JobApplicationsSended extends BaseActivity {


    private LinearLayout addAdsLL;
    private TextView addAdsTV;
    private RecyclerView myAdsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applications);


        configs = (Configs) getApplicationContext();


        addAdsLL = findViewById(R.id.fa_myads_ll);
        addAdsTV = findViewById(R.id.fa_add_ads_tv);
        myAdsRV = findViewById(R.id.fa_myads_rv);


        ViewCompat.setNestedScrollingEnabled(myAdsRV, false);


        findViewById(R.id.ase_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Call query
        queryMyAds();
    }

    List<Formobject> applications = new ArrayList<>();
    Configs configs;

    // MARK: - QUERY MY ADS ------------------------------------------------------------------
    private void queryMyAds() {
        showLoading();

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("applications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                applications = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Formobject formobject = dataSnapshot1.getValue(Formobject.class);
                    formobject.setId(dataSnapshot1.getKey());
                    applications.add(formobject);
                }


                Collections.sort(applications, new Comparator<Formobject>() {
                    public int compare(Formobject obj1, Formobject obj2) {
                        return Long.valueOf(obj2.getCreatedat()).compareTo(Long.valueOf(obj1.getCreatedat()));
                    }
                });


                hideLoading();
                if (applications.isEmpty()) {
                    addAdsTV.setVisibility(View.VISIBLE);
                } else {
                    addAdsTV.setVisibility(View.GONE);
                    myAdsRV.setLayoutManager(new LinearLayoutManager(JobApplicationsSended.this));
                    myAdsRV.setAdapter(new ApplicationsAdapter(applications, JobApplicationsSended.this));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoading();
            }
        });

    }


}
