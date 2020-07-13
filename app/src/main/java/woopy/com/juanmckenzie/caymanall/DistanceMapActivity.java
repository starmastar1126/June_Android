package woopy.com.juanmckenzie.caymanall;

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
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.MarshMallowPermission;

public class DistanceMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    public static final String LOCATION_EXTRA_KEY = "LOCATION_EXTRA_KEY";
    public static final String DISTANCE_EXTRA_KEY = "DISTANCE_EXTRA_KEY";

    private SeekBar distSeekBar;
    private TextView distanceTxt;
    private PlacesAutocompleteTextView searchTxt;

    private GoogleMap map;
    private float zoom = 14;
    private double radius = 1000;
    private int distance = 50;

    private Location aLocation;
    private LocationManager locationManager;
    MarshMallowPermission mmp = new MarshMallowPermission(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_map);

        // Get location coords from AdsListActivity.java
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Double latitude = extras.getDouble("latitude");
            Double longitude = extras.getDouble("longitude");
            aLocation = new Location("provider");
            aLocation.setLatitude(latitude);
            aLocation.setLongitude(longitude);

            Log.i("log-", "LOCATION PASSED FROM ADS LIST");

            // Init the Map
            initGoogleMap();

            // Set Default Location
        } else {
            aLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            aLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);

            Log.i("log-", "LOCATION IS THE DEFAULT ONE");

            // Init the Map
            initGoogleMap();
        }

        // Init distanceTxt
        distanceTxt = findViewById(R.id.mapDistanceTxt);
        distanceTxt.setTypeface(Configs.titRegular);
        distanceTxt.setText(String.valueOf((int) Configs.distanceInMiles) + getString(R.string.miles_arround_your_location));

        // SEARCH A LOCATION BY CITY OR ADDRESS ------------------------------------
        searchTxt = findViewById(R.id.dmSearchTxt);
        searchTxt.setTypeface(Configs.titRegular);


        searchTxt.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        // do something awesome with the selected place
                    }
                }
        );

        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String addressStr = searchTxt.getText().toString();
                    dismissKeyboard();

                    Geocoder geocoder = new Geocoder(DistanceMapActivity.this, Locale.getDefault());
                    List<android.location.Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(addressStr, 1);
                        if (addresses.size() > 0) {
                            // Set new location's coords
                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();
                            aLocation = new Location("provider");
                            aLocation.setLatitude(latitude);
                            aLocation.setLongitude(longitude);

                            // Refresh the map
                            initGoogleMap();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });

        // SET DISTANCE SEEK BAR ------------------------------------------------
        distSeekBar = (SeekBar) findViewById(R.id.mapDistanceSeekBar);
        distSeekBar.setProgress((int) Configs.distanceInMiles);
        distSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Set minimum value to 5 Miles
                if (progress >= 5) {
                    seekBar.setProgress(progress);
                } else {
                    seekBar.setProgress(5);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                map.clear();

                distance = seekBar.getProgress();
                distanceTxt.setText(String.valueOf(distance) + getString(R.string.miles_arround_your_location));

                // Set Map zoom
                radius = distance * 1609;
                double scale = radius / 500;
                zoom = (int) (14 - Math.log(scale) / Math.log(2));

                // Refresh the Map
                initGoogleMap();
            }
        });

        // MARK: - GET CURRENT LOCATION BUTTON ------------------------------------
        Button clButt = findViewById(R.id.dmCurrLocationButt);
        clButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForLocation()) {
                    mmp.requestPermissionForLocation();
                } else {
                    getCurrentLocation();
                }
            }
        });

        // MARK: - APPLY BUTTON ------------------------------------
        Button applyButt = findViewById(R.id.mapApplyButt);
        applyButt.setTypeface(Configs.titSemibold);
        applyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(LOCATION_EXTRA_KEY, aLocation);
                resultIntent.putExtra(DISTANCE_EXTRA_KEY, (float) distance);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                Log.i("log-", "DISTANCE IN MILES: " + Configs.distanceInMiles);
            }
        });

        // MARK: - CANCEL BUTTON ------------------------------------
        Button cancelButt = findViewById(R.id.mapBackButt);
        cancelButt.setTypeface(Configs.titSemibold);
        cancelButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }// end onCreate()

    // MARK: - GET CURRENT LOCATION ------------------------------------------------------
    protected void getCurrentLocation() {
        searchTxt.setText("");

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        aLocation = locationManager.getLastKnownLocation(provider);

        if (aLocation != null) {
            // Refresh Map
            initGoogleMap();

        } else {
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //remove location callback:
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);

        aLocation = location;

        if (aLocation != null) {
            // Refresh Map
            initGoogleMap();

            // NO GPS location found!
        } else {
            Configs.simpleAlert(getString(R.string.location_message5), DistanceMapActivity.this);

            // Set New York City as default currentLocation
            aLocation = new Location("provider");
            aLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            aLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);

            // Refresh Map
            initGoogleMap();
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

    // MARK: - INIT THE GOOGLE MAP ------------------------------------------------------------------
    void initGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.i("log-", "MAP INITIALIZED!");
    }

    // ON MAP READY ----------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Disable your Location
        map.setMyLocationEnabled(false);

        // Set Map type
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        LatLng coords = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
        Log.i("log-", "LOCATION COORDS: " + coords);

        // Add custom Marker pin
        int pinIcon = getResources().getIdentifier("curr_loc_icon", "drawable", getPackageName());
        map.addMarker(new MarkerOptions().position(coords)
                .title(searchTxt.getText().toString())
                .icon(BitmapDescriptorFactory.fromResource(pinIcon))
        );

        // Zoom the Google Map
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(coords, zoom);
        map.animateCamera(camUpdate);


        // Add a circle to the location
        Circle circle = map.addCircle(new CircleOptions()
                .center(coords)
                .radius(radius)
                .strokeColor(Color.RED)
                .strokeWidth(1)
                .fillColor(Color.parseColor("#67F16060")));
        circle.setCenter(coords);
    }

    // MARK: - DISMISS KEYBOARD
    void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchTxt.getWindowToken(), 0);
    }

    private double calculateDistance(double fromLong, double fromLat,
                                     double toLong, double toLat) {
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }

}
