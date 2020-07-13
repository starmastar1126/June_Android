package woopy.com.juanmckenzie.caymanall.home.fragments;

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
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.home.adapters.MyAdsAdapter;
import woopy.com.juanmckenzie.caymanall.selledit.activities.SellEditItemActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class MyAdsActivity extends AppCompatActivity {


    private LinearLayout addAdsLL;
    private TextView addAdsTV;
    private RecyclerView myAdsRV;
    DatabaseReference dbref;
    Dialog dialog;
    Configs myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);


        dbref = FirebaseDatabase.getInstance().getReference("users/");
        dialog = Configs.buildProgressLoadingDialog(this);
        myapplication = (Configs) getApplicationContext();


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

    List<Ads> MyAds = new ArrayList<>();

    // MARK: - QUERY MY ADS ------------------------------------------------------------------
    private void queryMyAds() {
        dialog.show();

        FirebaseDatabase.getInstance().getReference().child("ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                MyAds = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Ads ads = dataSnapshot1.getValue(Ads.class);
                    if (ads.getSellerPointer().getFirebaseID().equals(myapplication.getCurrentUser().getFirebaseID()))
                        if (ads.getAllDone())
                            MyAds.add(ads);
                }

                dialog.dismiss();
                if (MyAds.isEmpty()) {
                    addAdsTV.setVisibility(View.VISIBLE);
                    addAdsLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startSellEditItemScreen(null);
                        }
                    });
                } else {
                    addAdsTV.setVisibility(View.GONE);
                    addAdsLL.setOnClickListener(null);
                    myAdsRV.setLayoutManager(new LinearLayoutManager(MyAdsActivity.this));
                    myAdsRV.setAdapter(new MyAdsAdapter(MyAds, MyAdsActivity.this, new MyAdsAdapter.OnMyAdClickListener() {
                        @Override
                        public void onAdClicked(Ads adObject) {
                            startSellEditItemScreen(adObject);
                        }
                    }));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });

    }

    private void startSellEditItemScreen(Ads adObjid) {
        if (adObjid != null) {
            myapplication.setSelectedAd(adObjid);
            Intent i = new Intent(MyAdsActivity.this, SellEditItemActivity.class);
            Bundle extras = new Bundle();
            extras.putString(SellEditItemActivity.EDIT_AD_OBJ_ID_EXTRA_KEY, adObjid.getID());
            i.putExtras(extras);
            startActivity(i);
        } else {
            Intent i = new Intent(MyAdsActivity.this, SellEditItemActivity.class);
            Bundle extras = new Bundle();
            extras.putString(SellEditItemActivity.EDIT_AD_OBJ_ID_EXTRA_KEY, null);
            i.putExtras(extras);
            startActivity(i);
        }

    }

}
