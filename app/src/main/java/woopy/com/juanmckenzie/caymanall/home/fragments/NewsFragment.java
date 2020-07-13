package woopy.com.juanmckenzie.caymanall.home.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import woopy.com.juanmckenzie.caymanall.DistanceMapActivity;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.ads.activities.NewsDetails;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBanners;
import woopy.com.juanmckenzie.caymanall.home.adapters.NewsAdapter;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;
import woopy.com.juanmckenzie.caymanall.utils.PermissionsUtils;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements LocationListener {


    private LinearLayout noLikesRL;
    private RecyclerView likesRV;
    private AdView adView;
    Configs myapplication;
    TextView cityCountryTV;

    private TextView distanceTxt;
    List<Banner> banners = new ArrayList<>();


    public NewsFragment() {
        // Required empty public constructor
    }

    GPSTracker gpsTracker;

    View loading;

    private static final int SET_LOCATION_REQ_CODE = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        loading = view.findViewById(R.id.loading);

        gpsTracker = new GPSTracker(getActivity());
        myapplication = (Configs) getActivity().getApplicationContext();
        cityCountryTV = view.findViewById(R.id.alCityCountryTV);
        distanceTxt = view.findViewById(R.id.alDistanceTxt);
        cityCountryTV.setTypeface(Configs.titSemibold);
        distanceTxt.setTypeface(Configs.titRegular);


        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading.setVisibility(View.VISIBLE);
                likesRV.setVisibility(View.GONE);
                loadAdsFromChosenLocation();
                refreshLayout.setRefreshing(false);
            }
        });


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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        view.findViewById(R.id.aal_location_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DistanceMapActivity.class);
                Bundle extras = new Bundle();
                extras.putDouble("latitude", Configs.DEFAULT_LOCATION.latitude);
                extras.putDouble("longitude", Configs.DEFAULT_LOCATION.longitude);
                if (gpsTracker != null)
                    if (gpsTracker.getLatitude() != 0) {
                        extras.putDouble("latitude", gpsTracker.getLatitude());
                        extras.putDouble("longitude", gpsTracker.getLongitude());
                    }

                i.putExtras(extras);
                startActivityForResult(i, SET_LOCATION_REQ_CODE);
            }
        });

        noLikesRL = view.findViewById(R.id.mlNoLikesLayout);
        adView = view.findViewById(R.id.admobBanner);
        likesRV = view.findViewById(R.id.fml_likes_rv);


        loadAdsFromChosenLocation();

        return view;
    }


    List<EventObj> News = new ArrayList<>();

    // MARK: - QUERY ADS ------------------------------------------------------------------
    public void queryLikes() {
        noLikesRL.setVisibility(View.GONE);

        if (currentLocation == null) {
            // Set New York City as default currentLocation
            currentLocation = new Location("provider");
            currentLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            currentLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);

            // Set distance and city labels
            String distFormatted = String.format("%.0f", distanceInMiles);
            distanceTxt.setText(distFormatted + getString(R.string.mi_from));
            cityCountryTV.setText(getString(R.string.new_york_usa));
        }


        FirebaseDatabase.getInstance().getReference().child("News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                News = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    EventObj ads = dataSnapshot1.getValue(EventObj.class);


                    if (currentLocation != null) {
                        double distance = distance(currentLocation.getLatitude(), currentLocation.getLongitude(), Objects.requireNonNull(ads).getLocation().getLatitude(), ads.getLocation().getLongitude());
                        if (distance <= distanceInMiles) {
                            if (ads.getAllDone())
                                if (!ads.getReported())
                                    News.add(ads);
                        }

                    }

                }


                // Show/hide noLikesView
                try {
                    setupEvents();
                } catch (Exception e) {

                }
                if (News.size() == 0) {
                    loading.setVisibility(View.GONE);
                    likesRV.setVisibility(View.VISIBLE);
                } else {
                    noLikesRL.setVisibility(View.GONE);
                    likesRV.setVisibility(View.VISIBLE);
                    try {
                        loading.setVisibility(View.GONE);
                        likesRV.setVisibility(View.VISIBLE);
                    } catch (NullPointerException ignored) {

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void setupEvents() {

        Collections.reverse(News);
        likesRV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        likesRV.setAdapter(new NewsAdapter(News, banners, Objects.requireNonNull(getActivity()).getApplicationContext(), new NewsAdapter.OnMyAdClickListener() {
            @Override
            public void onAdClicked(EventObj adObjId) {


                myapplication.setSelectedEvent(adObjId);
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    Intent adDetailsIntent = new Intent(getActivity(), NewsDetails.class);
                    adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, adObjId.getID());
                    startActivity(adDetailsIntent);

                } else {
                    Intent adDetailsIntent = new Intent(getActivity(), NewsDetails.class);
                    adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, adObjId.getID());
                    startActivity(adDetailsIntent);
                }

            }

        }));
    }

    private Location currentLocation;
    private LocationManager locationManager;
    private float distanceInMiles = Configs.distanceInMiles;


    private void loadAdsFromChosenLocation() {
        // Get ads from a chosen location
        if (currentLocation != null) {
            getCityCountryNames();
        } else {
            getCurrentLocation();
        }
    }

    // MARK: - GET CITY AND COUNTRY NAMES | CALL QUERY ADS -----------------------------------
    private void getCityCountryNames() {
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (Geocoder.isPresent()) {
                Address returnAddress = addresses.get(0);
                String city = returnAddress.getLocality();
                String country = returnAddress.getCountryName();

                if (city == null) {
                    city = "";
                }
                // Show City/Country
                cityCountryTV.setText(city + ", " + country);

                // Set distance
                String distFormatted = String.format("%.0f", distanceInMiles);
                distanceTxt.setText(distFormatted + getString(R.string.mi_from));

                queryLikes();

            } else {
                Toast.makeText(getActivity(), "Geocoder not present!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            ToastUtils.showMessage(e.getMessage());
        }
    }


    // MARK: - GET CURRENT LOCATION ------------------------------------------------------
    protected void getCurrentLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);

        if (currentLocation != null) {
            getCityCountryNames();

            // Try to find your current Location one more time
        } else {
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 0.62137);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private String[] locationPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    public void onLocationChanged(Location location) {

        try {
            if (PermissionsUtils.hasPermissions(getActivity(), locationPermissions)) {
                return;
            }

        } catch (Exception ignored) {

        }
        locationManager.removeUpdates(this);
        currentLocation = location;

        if (currentLocation != null) {
            getCityCountryNames();
            // NO GPS location found!
        } else {
            Configs.simpleAlert(getString(R.string.location_message3), getActivity());

            // Set New York City as default currentLocation
            currentLocation = new Location("provider");
            currentLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            currentLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);

            // Set distance and city labels
            String distFormatted = String.format("%.0f", distanceInMiles);
            distanceTxt.setText(distFormatted + getString(R.string.mi_from));
            cityCountryTV.setText(getString(R.string.new_york_usa));

            // Call query
            queryLikes();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void Result(Float res, Location selected) {
        Location chosenLocation = selected;
        if (chosenLocation != null) {
            currentLocation = chosenLocation;
        }
        distanceInMiles = res;
        loadAdsFromChosenLocation();
    }


}
