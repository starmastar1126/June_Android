package woopy.com.juanmckenzie.caymanall;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Subscribe;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity2;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.Constants;

public class Settings extends BaseActivity {


    public static final String EDIT_AD_OBJ_ID_EXTRA_KEY = "AD_OBJ_ID_KEY";

    private static final int UPLOADING_IMAGE_SIZE = 800;

    private static final int SELECT_LOCATION_REQ_CODE = 11;
    private static final int SELECT_PAYMENT = 110;
    private static final int CATEGORY_REQ_CODE = 2;
    private static final int CATEGORY_REQ_CODE1 = 22;


    private String[] locationPermissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] cameraPermissions = {android.Manifest.permission.CAMERA};
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};


    private String selectedCategory;
    ArrayList<String> selectedCategorysub;

    Switch Switch;
    Configs myapplication;

    Boolean check = false;
    private RecyclerView categoriesRV;
    List<Subscribe> subscribeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch = findViewById(R.id.Switch);

        myapplication = (Configs) getApplicationContext();

        selectedCategorysub = new ArrayList<>();
        categoriesRV = findViewById(R.id.categoriesRV);

        findViewById(R.id.fa_add_ads_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(Settings.this, CategoriesActivity.class);
                categoriesIntent.putExtra(CategoriesActivity.INCLUDE_ALL_CATEGORY_EXTRA_KEY, false);
                categoriesIntent.putExtra(CategoriesActivity.INCLUDE_EVENT_CATEGORY_EXTRA_KEY, true);
                categoriesIntent.putExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY, selectedCategory);
                startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE);
            }
        });

        showLoading();
        FirebaseDatabase.getInstance().getReference().child("Subscribes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                subscribeList = new ArrayList<>();
                hideLoading();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Subscribe subscribe = dataSnapshot1.getValue(Subscribe.class);
                    subscribeList.add(subscribe);
                    Switch.setChecked(subscribe.getCheck());
                }


                categoriesRV.setLayoutManager(new LinearLayoutManager(Settings.this));
                categoriesRV.setAdapter(new SubscribeAdapter(subscribeList, selectedCategory,
                        new SubscribeAdapter.OnFilterSelectedListener() {
                            @Override
                            public void onFilterSelected(String filter) {
//                                selectedCategory = filter;
                                Log.i("log-", "SELECTED CATEGORY: " + selectedCategory);
                            }
                        }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoading();
            }
        });

        findViewById(R.id.Click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Switch.isChecked()) {
                    Switch.setChecked(true);
                    showLoading();
                    for (Subscribe subscribe : subscribeList) {
                        subscribe.setCheck(true);
                        for (String s : subscribe.getSubcategory()) {
                            String UNSUB = subscribe.getCategory() + " > " + s;
                            FirebaseMessaging.getInstance().subscribeToTopic(UNSUB.toString().replaceAll("[^A-Za-z0-9]", ""));
                        }
                        FirebaseDatabase.getInstance().getReference().child("Subscribes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subs").setValue(subscribeList).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                    hideLoading();

                } else {
                    Switch.setChecked(false);
                    showLoading();
                    for (Subscribe subscribe : subscribeList) {
                        subscribe.setCheck(false);
                        for (String s : subscribe.getSubcategory()) {
                            String UNSUB = subscribe.getCategory() + " > " + s;
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(UNSUB.toString().replaceAll("[^A-Za-z0-9]", ""));
                        }
                        FirebaseDatabase.getInstance().getReference().child("Subscribes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subs").setValue(subscribeList).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                    hideLoading();

                }

            }
        });
        findViewById(R.id.ase_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // IMAGE/VIDEO PICKED DELEGATE ------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CATEGORY_REQ_CODE:
                    selectedCategory = data.getStringExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY);
                    myapplication.setSelectedCategory(selectedCategory);
                    if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME)) {
                        myapplication.setSelectedCategory(selectedCategory);
                        Intent categoriesIntent = new Intent(Settings.this, CategoriesActivity2.class);
                        categoriesIntent.putExtra(CategoriesActivity2.INCLUDE_ALL_CATEGORY_EXTRA_KEY, false);
                        categoriesIntent.putExtra(CategoriesActivity.INCLUDE_EVENT_CATEGORY_EXTRA_KEY, true);
                        categoriesIntent.putExtra(CategoriesActivity2.SELECTED_CATEGORY_EXTRA_KEY, selectedCategorysub);
                        startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE1);

                    }
                    break;
                case CATEGORY_REQ_CODE1:
                    selectedCategorysub = data.getStringArrayListExtra(CategoriesActivity2.SELECTED_CATEGORY_EXTRA_KEY);
                    if (Switch.isChecked()) {
                        for (String s : selectedCategorysub) {
                            String UNSUB = selectedCategory + " > " + s;
                            FirebaseMessaging.getInstance().subscribeToTopic(UNSUB.toString().replaceAll("[^A-Za-z0-9]", ""));
                        }
                    }


                    showLoading();
                    Subscribe subscribe = new Subscribe();
                    subscribe.setCategory(selectedCategory);
                    subscribe.setSubcategory(selectedCategorysub);
                    subscribe.setFCM(myapplication.getCurrentUser().getFCM());
                    subscribe.setCheck(true);
                    subscribeList.add(subscribe);
                    FirebaseDatabase.getInstance().getReference().child("Subscribes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subs").setValue(subscribeList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();

                        }
                    });
                    break;

            }
        }
    }

}
