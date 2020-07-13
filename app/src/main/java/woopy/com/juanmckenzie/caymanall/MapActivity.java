package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String CHOSEN_LOCATION_EXTRA_KEY = "CHOSEN_LOCATION_EXTRA_KEY";

    /* Views */
    private GoogleMap mapView;

    /* Variables */
    private double latitude, longitude;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Init Google Map

        gpsTracker = new GPSTracker(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.msMapView);
        mapFragment.getMapAsync(this);
        MapsInitializer.initialize(this);

        // MARK: - CLOSE BUTTON ------------------------------------
        Button closeButt = findViewById(R.id.msCloseButt);
        closeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // MARK: - OK BUTTON ------------------------------------
        Button okButt = findViewById(R.id.msOkButt);
        okButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude != 0) {
                    if (calculateDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), latitude, longitude) < 100) {
                        // Set chosenLocation's coordinates
                        Location chosenLocation = new Location("provider");
                        chosenLocation.setLatitude(latitude);
                        chosenLocation.setLongitude(longitude);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(CHOSEN_LOCATION_EXTRA_KEY, chosenLocation);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Configs.simpleAlert(getString(R.string.you_can_choose_location_in_100_miles), MapActivity.this);

                    }

                } else {
                    // No Location set
                    Configs.simpleAlert(getString(R.string.you_must_move_the_map), MapActivity.this);
                }
            }
        });
    }

    // MARK: - ON MAP READY ----------------------------------------------------------------
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mapView = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // MapView settings
        mapView.setMyLocationEnabled(false);
        mapView.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Zoom the Google Map to Deafult Location
        mapView.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())));
        mapView.animateCamera(CameraUpdateFactory.zoomTo(10));


        // Move Map to change Location's coordinates
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cp = googleMap.getCameraPosition();
                Log.i("log-", "NEW LATITUDE: " + String.valueOf(cp.target.latitude));
                Log.i("log-", "NEW LONGITUDE: " + String.valueOf(cp.target.longitude));

                latitude = cp.target.latitude;
                longitude = cp.target.longitude;
            }
        });
    }

    private double calculateDistance(Double latitude, Double longitude, double e, double f) {
        double d2r = Math.PI / 180;

        double dlong = (longitude - f) * d2r;
        double dlat = (latitude - e) * d2r;
        double a = Math.pow(Math.sin(dlat / 2.0), 2) + Math.cos(e * d2r)
                * Math.cos(latitude * d2r) * Math.pow(Math.sin(dlong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367 * c;
        return d;
    }
}
