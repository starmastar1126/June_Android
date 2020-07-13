package woopy.com.juanmckenzie.caymanall.formsjobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.ChatsT;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.adapters.ApplicationsAdapter;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.home.adapters.MyAdsAdapter;
import woopy.com.juanmckenzie.caymanall.home.fragments.MyAdsActivity;
import woopy.com.juanmckenzie.caymanall.selledit.activities.SellEditItemActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class JobApplications extends BaseActivity {


    private LinearLayout addAdsLL;
    private TextView addAdsTV;
    private RecyclerView myAdsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);


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

        FirebaseDatabase.getInstance().getReference().child("ads").child(configs.getSelectedAd().getID()).child("applications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                applications = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Formobject formobject = dataSnapshot1.getValue(Formobject.class);
                    applications.add(formobject);
                }

                Collections.sort(applications, new Comparator<Formobject>() {
                    public int compare(Formobject obj1, Formobject obj2) {
                        return Long.valueOf(obj2.getCreatedat()).compareTo(Long.valueOf(obj1.getCreatedat())); // To compare integer values
                    }
                });

                hideLoading();
                if (applications.isEmpty()) {
                    addAdsTV.setVisibility(View.VISIBLE);
                } else {
                    addAdsTV.setVisibility(View.GONE);
                    myAdsRV.setLayoutManager(new LinearLayoutManager(JobApplications.this));
                    myAdsRV.setAdapter(new ApplicationsAdapter(applications, JobApplications.this));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoading();
            }
        });

    }


}
