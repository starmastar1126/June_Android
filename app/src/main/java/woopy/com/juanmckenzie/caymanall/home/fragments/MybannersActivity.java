package woopy.com.juanmckenzie.caymanall.home.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Purchase;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.home.adapters.MyBannersAdapter;
import woopy.com.juanmckenzie.caymanall.selledit.activities.SellEditItemActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class MybannersActivity extends AppCompatActivity {


    private LinearLayout addAdsLL;
    private TextView addAdsTV,mlTitleTxt;
    private RecyclerView myAdsRV;
    Dialog dialog;
    Configs myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);


        dialog = Configs.buildProgressLoadingDialog(this);
        myapplication = (Configs) getApplicationContext();


        addAdsLL = findViewById(R.id.fa_myads_ll);
        addAdsTV = findViewById(R.id.fa_add_ads_tv);
        myAdsRV = findViewById(R.id.fa_myads_rv);
        mlTitleTxt=findViewById(R.id.mlTitleTxt);
        mlTitleTxt.setText(getString(R.string.my_banners));


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

    List<Banner> MyAds = new ArrayList<>();

    // MARK: - QUERY MY ADS ------------------------------------------------------------------
    private void queryMyAds() {
        dialog.show();

        FirebaseDatabase.getInstance().getReference().child("Banners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                MyAds = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Banner ads = dataSnapshot1.getValue(Banner.class);

                    if (ads.getSellerPointer().getFirebaseID().equals(myapplication.getCurrentUser().getFirebaseID())) {
                        Date date = Calendar.getInstance().getTime();
                        date.setTime(Long.valueOf(ads.getCreatedAt()));
                        if (AdsListActivity.getUnsignedDiffInDays(date, Calendar.getInstance().getTime()) < Integer.parseInt(ads.getDays())) {
                            MyAds.add(ads);
                        }
                    }


                }

                dialog.dismiss();
                if (MyAds.isEmpty()) {
                    addAdsTV.setVisibility(View.VISIBLE);
                    myAdsRV.setVisibility(View.GONE);
                    addAdsLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startSellEditItemScreen(null);
                        }
                    });
                } else {
                    addAdsTV.setVisibility(View.GONE);
                    addAdsLL.setOnClickListener(null);
                    myAdsRV.setLayoutManager(new LinearLayoutManager(MybannersActivity.this));
                    myAdsRV.setAdapter(new MyBannersAdapter(MyAds, MybannersActivity.this, new MyBannersAdapter.OnMyAdClickListener() {
                        @Override
                        public void onAdClicked(Banner adObject) {
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

    private void startSellEditItemScreen(Banner adObjid) {
        if (adObjid != null) {
            myapplication.setSelecteBanner(adObjid);
            Intent i = new Intent(MybannersActivity.this, Purchase.class);
            Bundle extras = new Bundle();
            extras.putString(SellEditItemActivity.EDIT_AD_OBJ_ID_EXTRA_KEY, adObjid.getID());
            i.putExtras(extras);
            startActivity(i);
        } else {
            Intent i = new Intent(MybannersActivity.this, Purchase.class);
            Bundle extras = new Bundle();
            extras.putString(SellEditItemActivity.EDIT_AD_OBJ_ID_EXTRA_KEY, null);
            i.putExtras(extras);
            startActivity(i);
        }

    }

}
