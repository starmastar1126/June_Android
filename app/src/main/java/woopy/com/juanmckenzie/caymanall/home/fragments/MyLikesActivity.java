package woopy.com.juanmckenzie.caymanall.home.fragments;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.home.adapters.MyLikesAdapter;

public class MyLikesActivity extends AppCompatActivity {

    private LinearLayout noLikesRL;
    private RecyclerView likesRV;
    private AdView adView;
    Configs myapplication;
    Dialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_likes);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        dialog = Configs.buildProgressLoadingDialog(this);
        myapplication = (Configs) getApplicationContext();
        initViews();

        // Call query
        try {
            queryLikes();
        } catch (Exception ignored) {

        }

        findViewById(R.id.ase_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Init AdMob banner

    }


    private void initViews() {
        noLikesRL = MyLikesActivity.this.findViewById(R.id.mlNoLikesLayout);
        adView = MyLikesActivity.this.findViewById(R.id.admobBanner);
        likesRV = MyLikesActivity.this.findViewById(R.id.fml_likes_rv);
    }

    List<Ads> MyLikes = new ArrayList<>();

    // MARK: - QUERY ADS ------------------------------------------------------------------
    void queryLikes() {
        dialog.show();
        noLikesRL.setVisibility(View.GONE);


        FirebaseDatabase.getInstance().getReference().child("ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                MyLikes = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Ads ads = dataSnapshot1.getValue(Ads.class);

                    if (myapplication.getCurrentUser().getLikes() != null)
                        if (myapplication.getCurrentUser().getLikes().contains(ads.getID()))
                            MyLikes.add(ads);

                }


                dialog.dismiss();

                // Show/hide noLikesView
                if (MyLikes.size() == 0) {
                    noLikesRL.setVisibility(View.VISIBLE);
                    likesRV.setVisibility(View.GONE);
                } else {
                    noLikesRL.setVisibility(View.GONE);
                    likesRV.setVisibility(View.VISIBLE);
                    try {
                        setUpLikedAds(MyLikes);
                    } catch (NullPointerException ignored) {

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setUpLikedAds(List<Ads> adObjects) {


        likesRV.setLayoutManager(new GridLayoutManager(MyLikesActivity.this, 2));
        likesRV.setAdapter(new MyLikesAdapter(adObjects, Objects.requireNonNull(MyLikesActivity.this).getApplicationContext(), new MyLikesAdapter.MyLikesClickListener() {
            @Override
            public void onAdClicked(String adObjId) {
                Intent adDetailsIntent = new Intent(MyLikesActivity.this, AdDetailsActivity.class);
                adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, adObjId);
                startActivity(adDetailsIntent);
            }

            @Override
            public void onUnlikeClicked(Ads likeObject) {
                dialog.show();
                myapplication.getCurrentUser().getLikes().remove(likeObject.getID());
                FirebaseDatabase.getInstance().getReference().child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("likes").setValue(myapplication.getCurrentUser().getLikes());
                FirebaseDatabase.getInstance().getReference().child("ads/" + likeObject.getID()).child("likes").setValue(likeObject.getLikes() - 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        try {
                            queryLikes();
                        } catch (Exception ignored) {
                        }
                    }
                });

            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Call query
        try {
            queryLikes();
        } catch (Exception ignored) {

        }
    }
}
