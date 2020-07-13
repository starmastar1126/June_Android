package woopy.com.juanmckenzie.caymanall.resturents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest.permission;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBanners;
import woopy.com.juanmckenzie.caymanall.home.adapters.AdsListAdapter;
import woopy.com.juanmckenzie.caymanall.resturents.Modal.PlacesDetails_Modal;
import woopy.com.juanmckenzie.caymanall.resturents.Response.DistanceResponse;
import woopy.com.juanmckenzie.caymanall.resturents.Response.PlacesResponse;
import woopy.com.juanmckenzie.caymanall.resturents.Response.PlacesResponse.CustomA;
import woopy.com.juanmckenzie.caymanall.resturents.Response.PlacesResponse.Root;
import woopy.com.juanmckenzie.caymanall.resturents.Response.Places_details;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.resturents.Modal.PlacesDetails_Modal;
import woopy.com.juanmckenzie.caymanall.resturents.Response.PlacesResponse;
import woopy.com.juanmckenzie.caymanall.resturents.adapters.ResturentsListAdapter;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;

public class ResturentsactivityActivity extends BaseActivity {


    APIInterface apiService;
    public String latLngString;
    public double source_lat, source_long;

    AnimatedRecyclerView adsRV;
    public static final String PREFS_FILE_NAME = "sharedPreferences";
    ArrayList<PlacesResponse.CustomA> results;

    protected Location mLastLocation;

    private static final String TAG = "MainActivity";

    public ArrayList<PlacesDetails_Modal> details_modal;


    GPSTracker gpsTracker;
    private long radius = 3 * 1000;

    private static final int MY_PERMISION_CODE = 10;

    private boolean Permission_is_granted = false;
    public String mAddressOutput;
    public RelativeLayout rl_layout;
    public String Location_type = "ROOFTOP";
    List<Banner> banners = new ArrayList<>();
    private ResturentsListAdapter resturentsListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturentsactivity);

        gpsTracker = new GPSTracker(ResturentsactivityActivity.this);


        FirebaseDatabase.getInstance().getReference().child("Banners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bannerssnap) {
                banners = new ArrayList<>();
                for (DataSnapshot bannerssnap1 : bannerssnap.getChildren()) {

                    Banner adsbanner = bannerssnap1.getValue(Banner.class);
                    Date date = Calendar.getInstance().getTime();
                    date.setTime(Long.valueOf(adsbanner.getCreatedAt()));

                    if (AdsListActivity.getUnsignedDiffInDays(date, Calendar.getInstance().getTime()) < Integer.parseInt(adsbanner.getDays())) {
                        banners.add(adsbanner);
                    }
                }

                SliderView image = findViewById(R.id.imageSlider);
                SliderAdapterBanners adapter = new SliderAdapterBanners(ResturentsactivityActivity.this, banners);
                image.setSliderAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        findViewById(R.id.aad_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Log.i("On Create", "true");

        shimmerText = findViewById(R.id.promotions);
        shimmerText.startShimmer();


        apiService = ApiClient.getClient().create(APIInterface.class);

        adsRV = findViewById(R.id.aal_ads_rv);


        getUserLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    private void fetchStores(String placeType) {

        Call<PlacesResponse.Root> call = apiService.doPlaces(latLngString, radius, placeType, ApiClient.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<PlacesResponse.Root> call, Response<PlacesResponse.Root> response) {
                PlacesResponse.Root root = (Root) response.body();


                if (response.isSuccessful()) {

                    switch (root.status) {
                        case "OK":

                            results = root.customA;

                            details_modal = new ArrayList<PlacesDetails_Modal>();
                            String photourl;
                            Log.i(TAG, "fetch stores");


                            for (int i = 0; i < results.size(); i++) {

                                CustomA info = (CustomA) results.get(i);

                                String place_id = results.get(i).place_id;


                                if (results.get(i).photos != null) {

                                    String photo_reference = results.get(i).photos.get(0).photo_reference;

                                    photourl = ApiClient.base_url + "place/photo?maxwidth=200&photoreference=" + photo_reference +
                                            "&key=" + ApiClient.GOOGLE_PLACE_API_KEY;

                                } else {
                                    photourl = "NA";
                                }

                                fetchDistance(info, place_id, photourl);


                                Log.i("Coordinates  ", info.geometry.locationA.lat + " , " + info.geometry.locationA.lng);
                                Log.i("Names  ", info.name);

                            }

                            break;
                        case "ZERO_RESULTS":
                            Toast.makeText(getApplicationContext(), getString(R.string.no_matches_found_near_you), Toast.LENGTH_SHORT).show();
                            hideLoading();
                            shimmerText.setVisibility(View.GONE);
                            shimmerText.stopShimmer();
                            break;
                        case "OVER_QUERY_LIMIT":
                            Toast.makeText(getApplicationContext(), "You have reached the Daily Quota of Requests", Toast.LENGTH_SHORT).show();
                            hideLoading();
                            shimmerText.setVisibility(View.GONE);
                            shimmerText.stopShimmer();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            hideLoading();
                            shimmerText.setVisibility(View.GONE);
                            shimmerText.stopShimmer();
                            break;
                    }

                } else if (response.code() != 200) {
                    shimmerText.setVisibility(View.GONE);
                    shimmerText.stopShimmer();
                    Toast.makeText(getApplicationContext(), "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                shimmerText.setVisibility(View.GONE);
                shimmerText.stopShimmer();
                Toast.makeText(getApplicationContext(), "Error in Fetching Details,Please Refresh", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }


    private void fetchDistance(final PlacesResponse.CustomA info, final String place_id, final String photourl) {

        Log.i(TAG, "Distance API call start");

        Call<DistanceResponse> call = apiService.getDistance(latLngString, info.geometry.locationA.lat + "," + info.geometry.locationA.lng, ApiClient.GOOGLE_PLACE_API_KEY);

        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {

                DistanceResponse resultDistance = (DistanceResponse) response.body();

                if (response.isSuccessful()) {

                    Log.i(TAG, resultDistance.status);

                    if ("OK".equalsIgnoreCase(resultDistance.status)) {
                        DistanceResponse.InfoDistance row1 = resultDistance.rows.get(0);
                        DistanceResponse.InfoDistance.DistanceElement element1 = row1.elements.get(0);

                        if ("OK".equalsIgnoreCase(element1.status)) {

                            DistanceResponse.InfoDistance.ValueItem itemDistance = element1.distance;

                            String total_distance = itemDistance.text;

                            fetchPlace_details(info, place_id, total_distance, info.name, photourl);
                        }

                    }

                } else if (response.code() != 200) {
                    shimmerText.setVisibility(View.GONE);
                    shimmerText.stopShimmer();
                    Toast.makeText(getApplicationContext(), "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                shimmerText.setVisibility(View.GONE);
                shimmerText.stopShimmer();
                Toast.makeText(getApplicationContext(), "Error in Fetching Details,Please Refresh", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }

    ShimmerFrameLayout shimmerText;

    private void fetchPlace_details(final PlacesResponse.CustomA info, final String place_id, final String totaldistance, final String name, final String photourl) {

        Call<Places_details> call = apiService.getPlaceDetails(place_id, ApiClient.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<Places_details>() {
            @Override
            public void onResponse(Call<Places_details> call, Response<Places_details> response) {

                Places_details details = (Places_details) response.body();

                if ("OK".equalsIgnoreCase(details.status)) {

                    String address = details.result.formatted_adress;
                    String phone = details.result.international_phone_number;
                    float rating = details.result.rating;

                    if (!photourl.equals("NA"))
                        details_modal.add(new PlacesDetails_Modal(address, phone, rating, totaldistance, name, photourl, place_id));

                    Log.i("details : ", info.name + "  " + address);

                    if (details_modal.size() == results.size()) {
                        Collections.sort(details_modal, new Comparator<PlacesDetails_Modal>() {
                            @Override
                            public int compare(PlacesDetails_Modal lhs, PlacesDetails_Modal rhs) {
                                return lhs.distance.compareTo(rhs.distance);
                            }
                        });
                    }
                    if (details_modal.size() == 1) {
                        hideLoading();
                        shimmerText.stopShimmer();
                        shimmerText.setVisibility(View.GONE);

                        adsRV.setVisibility(View.VISIBLE);
                        final GridLayoutManager layoutManager = new GridLayoutManager(ResturentsactivityActivity.this, 2);
                        resturentsListAdapter = new ResturentsListAdapter(details_modal, getApplicationContext(), new ResturentsListAdapter.OnAdClickListener() {
                            @Override
                            public void onAdClicked(PlacesDetails_Modal adObj) {
                                Configs configs = (Configs) getApplicationContext();
                                configs.setSelectedPlaceID(adObj.ID);
                                startActivity(new Intent(getApplicationContext(), ResturentsDetails.class));
                            }
                        });
                        adsRV.setLayoutManager(layoutManager);
                        adsRV.setAdapter(resturentsListAdapter);
                        adsRV.scheduleLayoutAnimation();
                    } else {
                        if (details_modal.size() != 0)
                            resturentsListAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
            }
        });

    }


    private void fetchCurrentAddress(final String latLngString) {

        Call<Places_details> call = apiService.getCurrentAddress(latLngString, ApiClient.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<Places_details>() {
            @Override
            public void onResponse(Call<Places_details> call, Response<Places_details> response) {

                Places_details details = (Places_details) response.body();

                if ("OK".equalsIgnoreCase(details.status)) {

                    mAddressOutput = details.results.get(0).formatted_adress;
                    Log.i("Addr Current and coord.", mAddressOutput + latLngString);
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
            }
        });

    }


    private void getUserLocation() {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                        ACCESS_COARSE_LOCATION)) {
                    showAlert();

                } else {

                    if (isFirstTimeAskingPermission(this, permission.ACCESS_FINE_LOCATION)) {
                        firstTimeAskingPermission(this,
                                permission.ACCESS_FINE_LOCATION, false);
                        ActivityCompat.requestPermissions(this,
                                new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},
                                MY_PERMISION_CODE);
                    } else {

                        Toast.makeText(this, "You won't be able to access the features of this App", Toast.LENGTH_LONG).show();
                        hideLoading();                        //Permission disable by device policy or user denied permanently. Show proper error message
                    }


                }


            } else Permission_is_granted = true;
        } else {

            latLngString = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
            mLastLocation = new Location("provider");
            mLastLocation.setLatitude(gpsTracker.getLatitude());
            mLastLocation.setLongitude(gpsTracker.getLongitude());
            fetchCurrentAddress(latLngString);

            Log.i(TAG, latLngString + "");

            fetchStores("restaurant");
        }
    }


    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime) {
        SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }

    public static boolean isFirstTimeAskingPermission(Context context, String permission) {
        return context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).getBoolean(permission, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("On request permiss", "executed");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case MY_PERMISION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permission_is_granted = true;
                    getUserLocation();
                } else {
                    showAlert();
                    Permission_is_granted = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.gps_on_message), Toast.LENGTH_LONG).show();
                    hideLoading();

                }
                break;

        }
    }


    @Override
    protected void onStart() {
        Log.i("on start", "true");
        super.onStart();
    }

    @Override
    protected void onResume() {

        Log.i("on resume", "true");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("on pause", "true");

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings are OFF \nPlease Enable Location")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {


                        ActivityCompat.requestPermissions(ResturentsactivityActivity.this,
                                new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},
                                MY_PERMISION_CODE);


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }


}