package woopy.com.juanmckenzie.caymanall.ads.activities;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import woopy.com.juanmckenzie.caymanall.BannerView;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBanners;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterpromotions;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity;
import woopy.com.juanmckenzie.caymanall.Chats;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity1;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.DistanceMapActivity;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.SortByActivity;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.filters.models.SortBy;
import woopy.com.juanmckenzie.caymanall.home.adapters.AdsListAdapter;
import woopy.com.juanmckenzie.caymanall.utils.Constants;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;
import woopy.com.juanmckenzie.caymanall.utils.PermissionsUtils;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

public class AdsListActivity extends AppCompatActivity implements LocationListener {

    public static final String CATEGORY_NAME_KEY = "CATEGORY_NAME_KEY";
    public static final String SEARCH_QUERY_KEY = "SEARCH_QUERY_KEY";

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1;
    private static final int CATEGORY_REQ_CODE = 2;
    private static final int CATEGORY_REQ_CODE1 = 22;
    private static final int SORT_BY_REQ_CODE = 3;
    private static final int SET_LOCATION_REQ_CODE = 4;

    private String[] locationPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private EditText searchTxt;
    private TextView categoryTV, categoryTV1, sortByTV, cityCountryTV;
    private RelativeLayout categoryButtonRL, categoryButtonRL1, sortByButtonRL, cityCountryButtonRL;
    private TextView distanceTxt, noSearchTxt;
    private RelativeLayout noResultsLayout;
    private ImageView backBtn;
    private ImageView chatButt;
    private AnimatedRecyclerView adsRV;

    private String searchString;
    private String selectedCategory, selectedCategorysub = "All";
    private SortBy sortBy;

    private Location currentLocation;
    private LocationManager locationManager;
    private float distanceInMiles = Configs.distanceInMiles;

    // Pagination
    private AdsListAdapter adsListAdapter;
    private int querySkip = 0;
    private boolean isNextPageLoading;
    private boolean allAdsLoaded;

    GPSTracker gpsTracker;
    ShimmerFrameLayout shimmerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchString = extras.getString(SEARCH_QUERY_KEY);
            selectedCategory = extras.getString(CATEGORY_NAME_KEY);
        }

        shimmerText = findViewById(R.id.promotions);
        shimmerText.startShimmer();

        gpsTracker = new GPSTracker(this);
        configs = (Configs) getApplicationContext();
        configs.setSelectedCategory(selectedCategory);
        selectedCategorysub = "All";
        sortBy = SortBy.RECENT;

        Log.i("log-", "SEARCH TEXT STRING: " + searchString +
                "\nCATEGORY: " + selectedCategory);

        initViews();
        setUpViews();
        FirebaseDatabase.getInstance().getReference().child("Banners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bannerssnap) {
                banners = new ArrayList<>();
                for (DataSnapshot bannerssnap1 : bannerssnap.getChildren()) {

                    Banner adsbanner = bannerssnap1.getValue(Banner.class);
                    Date date = Calendar.getInstance().getTime();
                    date.setTime(Long.valueOf(adsbanner.getCreatedAt()));

                    if (getUnsignedDiffInDays(date, Calendar.getInstance().getTime()) < Integer.parseInt(adsbanner.getDays())) {
                        banners.add(adsbanner);
                    }
                }

                SliderView image = findViewById(R.id.imageSlider);
                SliderAdapterBanners adapter = new SliderAdapterBanners(AdsListActivity.this, banners);
                image.setSliderAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (PermissionsUtils.hasPermissions(this, locationPermissions)) {
            loadAdsFromChosenLocation();
        } else {
            queryAds();
            ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_PERMISSIONS_REQUEST_CODE);
        }


    }

    Configs configs;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CATEGORY_REQ_CODE:

                    selectedCategory = data.getStringExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY);
                    categoryTV.setText(selectedCategory);

                    if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME)) {
                        configs.setSelectedCategory(selectedCategory);
                        Intent categoriesIntent = new Intent(AdsListActivity.this, CategoriesActivity1.class);
                        categoriesIntent.putExtra(CategoriesActivity1.SELECTED_CATEGORY_EXTRA_KEY, selectedCategorysub);
                        startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE1);
                    } else {
                        queryAds();
                    }

                    break;
                case CATEGORY_REQ_CODE1:
                    selectedCategorysub = data.getStringExtra(CategoriesActivity1.SELECTED_CATEGORY_EXTRA_KEY);
                    categoryTV1.setText(selectedCategorysub);
                    categoryTV.setText(selectedCategory + " > " + selectedCategorysub);
                    queryAds();
                    break;
                case SORT_BY_REQ_CODE:
                    String sortByValue = data.getStringExtra(SortByActivity.SELECTED_SORT_BY_EXTRA_KEY);
                    sortBy = SortBy.getForValue(sortByValue);
                    if (sortBy != null) {
                        sortByTV.setText(sortBy.getValue());
                    }
                    queryAds();
                    break;
                case SET_LOCATION_REQ_CODE:
                    Location chosenLocation = data.getParcelableExtra(DistanceMapActivity.LOCATION_EXTRA_KEY);
                    if (chosenLocation != null) {
                        currentLocation = chosenLocation;
                    }
                    distanceInMiles = data.getFloatExtra(DistanceMapActivity.DISTANCE_EXTRA_KEY, Configs.distanceInMiles);
                    loadAdsFromChosenLocation();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadAdsFromChosenLocation();
            }
        }
    }


    private void initViews() {
        searchTxt = findViewById(R.id.alSearchTxt);
        distanceTxt = findViewById(R.id.alDistanceTxt);
        categoryTV = findViewById(R.id.alCategoryTV);
        categoryTV1 = findViewById(R.id._subalCategoryTV);
        sortByTV = findViewById(R.id.alSortByTV);
        cityCountryTV = findViewById(R.id.alCityCountryTV);
        noSearchTxt = findViewById(R.id.alNoSearchTxt);
        noResultsLayout = findViewById(R.id.alNoResultsLayout);
        backBtn = findViewById(R.id.alBackButt);
        chatButt = findViewById(R.id.alChatButt);
        adsRV = findViewById(R.id.aal_ads_rv);
        categoryButtonRL = findViewById(R.id.aal_category_rl);
        categoryButtonRL1 = findViewById(R.id.aal_sub_category_rl);
        sortByButtonRL = findViewById(R.id.aal_sort_rl);
        cityCountryButtonRL = findViewById(R.id.aal_location_rl);
    }

    private void setUpViews() {
        searchTxt.setTypeface(Configs.titRegular);
        distanceTxt.setTypeface(Configs.titRegular);
        categoryTV.setTypeface(Configs.titSemibold);
        categoryTV1.setTypeface(Configs.titSemibold);
        sortByTV.setTypeface(Configs.titSemibold);
        cityCountryTV.setTypeface(Configs.titSemibold);
        noSearchTxt.setTypeface(Configs.titSemibold);
        noResultsLayout.setVisibility(View.GONE);

        // Set search variables for the query
        if (searchString != null) {
            searchTxt.setText(searchString);
            categoryTV.setText(selectedCategory + " > " + selectedCategorysub);
            categoryTV1.setText(selectedCategorysub);
        } else {
            searchTxt.setText(selectedCategory);
            categoryTV.setText(selectedCategory + " > " + selectedCategorysub);
            categoryTV1.setText(selectedCategorysub);
        }


        // Set sort By text
        sortByTV.setText(sortBy.getValue());

        // MARK: - SEARCH ADS BY KEYWORDS --------------------------------------------------------
        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (!searchTxt.getText().toString().matches("")) {
                        selectedCategory = Constants.BrowseCategories.DEFAULT_CATEGORY_NAME;
                        categoryTV.setText(selectedCategory + " > " + selectedCategorysub);
                        categoryTV1.setText(selectedCategorysub);
                        searchString = searchTxt.getText().toString();
                        UIUtils.hideKeyboard(searchTxt);

                        // Call query
                        queryAds();

                        return true;
                    }

                    // No text -> No search
                } else {
                    ToastUtils.showMessage(getString(R.string.type_something));
                }

                return false;
            }
        });

        // EditText TextWatcher delegate
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int closeDrawable;
                if (s.length() == 0) {
                    closeDrawable = 0;
                } else {
                    closeDrawable = R.drawable.close_white_ic;
                }
                searchTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, closeDrawable, 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        final Drawable imgClearButton = ContextCompat.getDrawable(this, R.drawable.close_white_ic);
        searchTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (searchTxt.getText().length() > 0) {
                    if (event.getX() > searchTxt.getWidth() - searchTxt.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                        searchTxt.setText("");
                        searchString = null;
                        selectedCategory = Constants.BrowseCategories.DEFAULT_CATEGORY_NAME;
                        categoryTV.setText(selectedCategory + " > " + selectedCategorysub);
                        categoryTV1.setText(selectedCategorysub);

                        queryAds();
                        return true;
                    }
                }
                return false;
            }
        });

        // MARK: - CHATS BUTTON ------------------------------------
        chatButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(AdsListActivity.this, Chats.class));
                } else {
                    Configs.loginAlert(getString(R.string.login_message), AdsListActivity.this);
                }
            }
        });

        // MARK: - CITY/COUNTRY BUTTON ------------------------------------
        cityCountryButtonRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("log-", "CURR LOCATION: " + currentLocation);

                // if (!distanceTxt.getText().toString().matches("loading...")) {
                if (currentLocation != null) {
                    Double lat = currentLocation.getLatitude();
                    Double lng = currentLocation.getLongitude();
                    Log.i("log-", "LATITUDE: " + lat + "\nLONGITUDE: " + lng);
                    Intent i = new Intent(AdsListActivity.this, DistanceMapActivity.class);
                    Bundle extras = new Bundle();
                    extras.putDouble("latitude", lat);
                    extras.putDouble("longitude", lng);
                    i.putExtras(extras);
                    startActivityForResult(i, SET_LOCATION_REQ_CODE);

                } else {
                    // Set default Location
                    Intent i = new Intent(AdsListActivity.this, DistanceMapActivity.class);
                    Bundle extras = new Bundle();
                    extras.putDouble("latitude", Configs.DEFAULT_LOCATION.latitude);
                    extras.putDouble("longitude", Configs.DEFAULT_LOCATION.longitude);
                    i.putExtras(extras);
                    startActivityForResult(i, SET_LOCATION_REQ_CODE);
                }
                // }
            }
        });

        // MARK: - CHOOSE CATEGORY BUTTON ------------------------------------
        categoryButtonRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoriesIntent = new Intent(AdsListActivity.this, CategoriesActivity.class);
                categoriesIntent.putExtra(CategoriesActivity.SELECTED_CATEGORY_EXTRA_KEY, selectedCategory);
                startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE);
            }
        });

        // MARK: - CHOOSE CATEGORY BUTTON ------------------------------------
        categoryButtonRL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoriesIntent = new Intent(AdsListActivity.this, CategoriesActivity1.class);
                categoriesIntent.putExtra(CategoriesActivity1.SELECTED_CATEGORY_EXTRA_KEY, selectedCategorysub);
                startActivityForResult(categoriesIntent, CATEGORY_REQ_CODE1);
            }
        });

        // MARK: - SORT BY BUTTON ------------------------------------
        sortByButtonRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sortIntent = new Intent(AdsListActivity.this, SortByActivity.class);
                if (sortBy != null) {
                    sortIntent.putExtra(SortByActivity.SELECTED_SORT_BY_EXTRA_KEY, sortBy.getValue());
                }
                startActivityForResult(sortIntent, SORT_BY_REQ_CODE);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void loadAdsFromChosenLocation() {
        // Get ads from a chosen location
        if (currentLocation != null) {
            getCityCountryNames();
        } else {
            getCurrentLocation();
        }
    }

    // MARK: - GET CURRENT LOCATION ------------------------------------------------------
    protected void getCurrentLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);

        if (currentLocation != null) {
            getCityCountryNames();

            // Try to find your current Location one more time
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentLocation == null) {
                        if (!onDestroy) {
                            currentLocation = new Location("provider");
                            currentLocation.setLatitude(gpsTracker.getLatitude());
                            currentLocation.setLongitude(gpsTracker.getLongitude());
                            getCityCountryNames();
                        }

                    }

                }
            }, 2000);
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
    }

    Boolean onDestroy = false;

    @Override
    protected void onStop() {
        onDestroy = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        onDestroy = true;
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (PermissionsUtils.hasPermissions(this, locationPermissions)) {
            return;
        }

        locationManager.removeUpdates(this);
        currentLocation = location;

        if (currentLocation != null) {
            getCityCountryNames();
            // NO GPS location found!
        } else {
            Configs.simpleAlert(getString(R.string.location_message), AdsListActivity.this);

            // Set New York City as default currentLocation
            currentLocation = new Location("provider");
            currentLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            currentLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);

            // Set distance and city labels
            String distFormatted = String.format("%.0f", distanceInMiles);
            distanceTxt.setText(distFormatted + " Mi FROM");
            cityCountryTV.setText("New York, USA");

            // Call query
            queryAds();
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

    // MARK: - GET CITY AND COUNTRY NAMES | CALL QUERY ADS -----------------------------------
    private void getCityCountryNames() {
        try {
            Geocoder geocoder = new Geocoder(AdsListActivity.this, Locale.getDefault());
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
                distanceTxt.setText(distFormatted + " Mi FROM");

                // Call query
                queryAds();

            } else {
//                Toast.makeText(getApplicationContext(), "Geocoder not present!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            ToastUtils.showMessage(e.getMessage());
        }
    }

    List<Ads> adslist = new ArrayList<>();
    List<Banner> banners = new ArrayList<>();
    int initialaddindex = 0;


    private final static long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

    private static long getDateToLong(Date date) {
        return Date.UTC(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
    }


    public static int getSignedDiffInDays(Date beginDate, Date endDate) {
        long beginMS = getDateToLong(beginDate);
        long endMS = getDateToLong(endDate);
        long diff = (endMS - beginMS) / (MILLISECS_PER_DAY);
        return (int) diff;
    }

    public static int getUnsignedDiffInDays(Date beginDate, Date endDate) {
        return Math.abs(getSignedDiffInDays(beginDate, endDate));
    }

    // MARK: - QUERY ADS ------------------------------------------------------------------
    private void queryAds() {
        shimmerText.setVisibility(View.VISIBLE);
        shimmerText.startShimmer();
        adsRV.setVisibility(View.GONE);

        inserted = 0;
        initialaddindex = 0;
        noResultsLayout.setVisibility(View.GONE);

        // Reset to first page
        querySkip = 0;
        allAdsLoaded = false;
        FirebaseDatabase.getInstance().getReference().child("ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adslist = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Boolean check = true;
                    Ads ads = dataSnapshot1.getValue(Ads.class);
                    if (ads.getCategory() != null) {
                        // query by search text
                        if (searchString != null) {
                            // Get keywords
                            String[] one = searchString.toLowerCase().split(" ");
                            List<String> keywords = new ArrayList<>(Arrays.asList(one));
                            Log.d("KEYWORDS", "\n" + keywords + "\n");

                            if (Objects.requireNonNull(ads).getKeywords().containsAll(keywords) || Objects.requireNonNull(ads).getTags().containsAll(keywords)) {
                                check = true;
                            } else {
                                check = false;
                            }
                        }

                        // query by Category
                        if (!selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME)) {
                            if (Objects.requireNonNull(ads).getCategory().equals(selectedCategory) && check) {
                                check = true;
                            } else {
                                check = false;
                            }
                        }


                        // query by Category
                        if (!selectedCategorysub.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME) && !selectedCategory.matches(Constants.BrowseCategories.DEFAULT_CATEGORY_NAME)) {
                            if (Objects.requireNonNull(ads).getSubcategory().equals(selectedCategorysub) && check) {
                                check = true;
                            } else {
                                check = false;
                            }
                        }

                        // query nearby
                        if (currentLocation != null) {
                            double distance = distance(currentLocation.getLatitude(), currentLocation.getLongitude(), Objects.requireNonNull(ads).getLocation().getLatitude(), ads.getLocation().getLongitude());
                            if (check)
                                check = distance <= distanceInMiles;
                        }

                        if (Objects.requireNonNull(ads).getReported()) {
                            check = false;
                        }

                        if (!ads.getAllDone()) {
                            check = false;
                        }


                        if (check) {
                            Date date1 = Calendar.getInstance().getTime();
                            date1.setTime(Long.valueOf(ads.getCreatedAt()));
                            Date date2 = Calendar.getInstance().getTime();
                            long diff = date2.getTime() - date1.getTime();
                            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                            if (days <= 60) {
                                if (ads.getImage1() == null || ads.getImage1().equals("")) {
                                    ImageObj imageObj = new ImageObj();
                                    imageObj.setUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Files%2FNOIMAGE.png?alt=media&token=577b1712-012e-4b88-a368-e7e8794f6d48");
                                    ads.setImage1(imageObj);
                                }
                                adslist.add(ads);
                                configs.setAdsList(adslist);
                            }

                        }

                    }

                }

                Boolean incheck = false;

                // query sortBy
                switch (sortBy) {
                    case RECENT:
                        incheck = true;
                        Collections.sort(adslist, new Comparator<Ads>() {
                            public int compare(Ads obj1, Ads obj2) {
                                return Long.valueOf(obj2.getCreatedAt()).compareTo(Long.valueOf(obj1.getCreatedAt())); // To compare integer values
                            }
                        });
                        Collections.sort(adslist, new Comparator<Ads>() {
                            public int compare(Ads obj1, Ads obj2) {
                                return Boolean.compare(obj1.getPaymentdone(), obj2.getPaymentdone());
                            }
                        });
                        Collections.reverse(adslist);
                        break;
                    case LOWEST_PRICE:
                        incheck = true;
                        Collections.sort(adslist, new Comparator<Ads>() {
                            public int compare(Ads obj1, Ads obj2) {
                                if (Double.valueOf(obj2.getPrice()).compareTo(Double.valueOf(obj1.getPrice())) > 0) {
                                    return -1;
                                } else if (Double.valueOf(obj2.getPrice()).compareTo(Double.valueOf(obj1.getPrice())) < 0) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case HIGHEST_PRICE:
                        incheck = true;
                        Collections.sort(adslist, new Comparator<Ads>() {
                            public int compare(Ads obj1, Ads obj2) {

                                if (Double.valueOf(obj2.getPrice()).compareTo(Double.valueOf(obj1.getPrice())) < 0) {
                                    return -1;
                                } else if (Double.valueOf(obj2.getPrice()).compareTo(Double.valueOf(obj1.getPrice())) > 0) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case NEW:
                        incheck = true;
                        Collections.sort(adslist, new Comparator<Ads>() {
                            public int compare(Ads obj1, Ads obj2) {

                                if (obj1.getCondition().equals("New")) {
                                    return -1;
                                } else if (!obj2.getCondition().equals("New")) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case USED:
                        incheck = true;
                        Collections.sort(adslist, new Comparator<Ads>() {
                            public int compare(Ads obj1, Ads obj2) {
                                if (!obj1.getCondition().equals("New")) {
                                    return -1;
                                } else if (obj2.getCondition().equals("New")) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case MOST_LIKED:
                        incheck = true;
                        Collections.sort(adslist, new Comparator<Ads>() {
                            public int compare(Ads obj1, Ads obj2) {
                                return Integer.valueOf(obj2.getLikes()).compareTo(Integer.valueOf(obj1.getLikes())); // To compare integer values
                            }
                        });
                        break;
                    default:
                        break;
                }


                shimmerText.setVisibility(View.GONE);
                shimmerText.stopShimmer();
                setUpAdList(adslist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                shimmerText.setVisibility(View.GONE);
                shimmerText.stopShimmer();
            }
        });
    }

    int inserted = 0;

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

    private void loadMoreAds() {
        if (adslist.size() == 0) {
            Log.d("log-", "Last query is null");
            return;
        }

        if (isNextPageLoading) {
            Log.d("log-", "Next page is loading");
            return;
        }

        if (allAdsLoaded) {
            Log.d("log-", "All ads are already loaded");
            return;
        }

        isNextPageLoading = true;
        querySkip += Configs.DEFAULT_PAGE_SIZE;

        List<Ads> more = new ArrayList<>();
        for (int i = 0; i < adslist.size(); i++) {
            if (i > querySkip) {
                more.add(adslist.get(i));
            }
        }

        if (adslist.size() <= querySkip) {
            allAdsLoaded = true;
        }
        adsListAdapter.addMoreAds(more);
        isNextPageLoading = false;
    }

    private void setUpAdList(List<Ads> adList) {

        shimmerText.stopShimmer();
        shimmerText.setVisibility(View.GONE);

        // Show/hide noResult view
        if (adList.size() == 0) {
            noResultsLayout.setVisibility(View.VISIBLE);
            adsRV.setVisibility(View.GONE);
        } else {
            adsRV.setVisibility(View.VISIBLE);
            noResultsLayout.setVisibility(View.GONE);

            final GridLayoutManager layoutManager = new GridLayoutManager(AdsListActivity.this, 2);
            adsListAdapter = new AdsListAdapter(adList, banners, getApplicationContext(), new AdsListAdapter.OnAdClickListener() {
                @Override
                public void onAdClicked(Ads adObj, int position) {
                    if (adObj.getType()) {
                        configs.setSelectedadposition(position);

                        Intent adDetailsIntent = new Intent(AdsListActivity.this, AdsDetailsPager.class);
                        adDetailsIntent.putExtra(AdDetailsActivity.AD_OBJ_ID_KEY, adObj.getID());
                        startActivity(adDetailsIntent);
                    }
                }
            });
            adsRV.setLayoutManager(layoutManager);
            adsRV.setAdapter(adsListAdapter);
            adsRV.scheduleLayoutAnimation();
            adsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (lastVisibleItem != -1 &&
                            lastVisibleItem + Configs.DEFAULT_PAGE_THRESHOLD >= totalItemCount) {
                        Log.d("log-", "Total item count: " + totalItemCount +
                                " last visible item pos: " + lastVisibleItem + " Need to load more");
                        loadMoreAds();
                    }
                }
            });
        }
    }
}
