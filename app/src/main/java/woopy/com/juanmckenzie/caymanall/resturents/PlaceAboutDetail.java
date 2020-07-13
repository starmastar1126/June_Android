package woopy.com.juanmckenzie.caymanall.resturents;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.ads.adapters.AdImagesPagerAdapter;
import woopy.com.juanmckenzie.caymanall.resturents.Modal.Place;
import woopy.com.juanmckenzie.caymanall.resturents.adapters.resturentImagesPagerAdapter;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceAboutDetail extends Fragment implements OnMapReadyCallback {


    private static final int PERMISSION_REQUEST_CODE = 100;
    /**
     * All references of the views
     */

    private GoogleMap mGoogleMap;
    private boolean mMapReady = false;
    private Place mCurrentPlace;

    private TextView mLocationAddressTextView;
    private TextView mLocationPhoneTextView;
    private TextView mLocationWebsiteTextView;
    private DotsIndicator dots_indicator;
    private ViewPager imagesVP;
    private resturentImagesPagerAdapter imagesAdapter;


    private TextView mLocationOpeningStatusTextView;

    public PlaceAboutDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_about_detail, container, false);

        MapFragment mapFragment = (MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        imagesVP = rootView.findViewById(R.id.aad_images_vp);
        dots_indicator = rootView.findViewById(R.id.dots_indicator);


        mLocationAddressTextView = (TextView) rootView.findViewById(R.id.location_address_text_view);
        mLocationPhoneTextView = (TextView) rootView.findViewById(R.id.location_phone_number_text_view);
        mLocationWebsiteTextView = (TextView) rootView.findViewById(R.id.location_website_text_view);
        mLocationOpeningStatusTextView = (TextView) rootView.findViewById(R.id.location_status_text_view);


        rootView.findViewById(R.id.location_share_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Short link created
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mCurrentPlace.getPlaceShareLink());
                startActivity(Intent.createChooser(intent, getString(R.string.share_on)));
            }
        });

        ((ImageView) rootView.findViewById(R.id.small_location_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        ((ImageView) rootView.findViewById(R.id.small_phone_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        ((ImageView) rootView.findViewById(R.id.small_website_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        ((ImageView) rootView.findViewById(R.id.small_location_status_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        mCurrentPlace = getArguments().getParcelable(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY);


        rootView.findViewById(R.id.location_phone_container).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Check runtime permission for Android M and high level SDK
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                if (shouldShowRequestPermissionRationale(
                                        android.Manifest.permission.CALL_PHONE)) {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle(R.string.call_permission_title)
                                            .setMessage(R.string.call_permission_message)
                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            })
                                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            }).show();
                                } else
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                            PERMISSION_REQUEST_CODE);
                            } else
                                makeCallToPlace();
                        } else
                            makeCallToPlace();
                    }
                });
        rootView.findViewById(R.id.location_website_container).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurrentPlace.getPlaceWebsite().charAt(0) != 'h')
                            Toast.makeText(getActivity(), R.string.website_not_registered_string,
                                    Toast.LENGTH_SHORT).show();
                        else {
                            Intent websiteUrlIntent = new Intent(Intent.ACTION_VIEW);
                            websiteUrlIntent.setData(Uri.parse(mCurrentPlace.getPlaceWebsite()));
                            getActivity().startActivity(websiteUrlIntent);
                        }
                    }
                }
        );
        if (mCurrentPlace != null) {
            mLocationAddressTextView.setText(mCurrentPlace.getPlaceAddress());
            mLocationPhoneTextView.setText(mCurrentPlace.getPlacePhoneNumber());
            mLocationWebsiteTextView.setText(mCurrentPlace.getPlaceWebsite());
            mLocationOpeningStatusTextView.setText(mCurrentPlace.getPlaceOpeningHourStatus());

            Configs configs = (Configs) getActivity().getApplicationContext();
            imagesAdapter = new resturentImagesPagerAdapter(configs.getImages(), getActivity(), new resturentImagesPagerAdapter.OnImageClickListener() {
                @Override
                public void onImageClicked(String imageFieldKey) {
                    openImageFullScreen(imageFieldKey);
                }
            });
            imagesVP.setAdapter(imagesAdapter);
            dots_indicator.setViewPager(imagesVP);
        }


        return rootView;
    }


    // OPEN TAPPED IMAGE INTO FULL SCREEN ACTIVITY
    void openImageFullScreen(String imageName) {
        Intent i = new Intent(getActivity(), FullScreenPreview.class);
        Bundle extras = new Bundle();
        extras.putString("imageName", imageName);
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                makeCallToPlace();
        }
    }

    private void makeCallToPlace() {
        if (mCurrentPlace.getPlacePhoneNumber().charAt(0) != '+')
            Toast.makeText(getActivity(), R.string.phone_number_not_registered_string,
                    Toast.LENGTH_SHORT).show();
        else {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:" + mCurrentPlace.getPlacePhoneNumber()));
            getContext().startActivity(phoneIntent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMapReady = true;
        mGoogleMap = googleMap;

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(mCurrentPlace.getPlaceLatitude()
                        , mCurrentPlace.getPlaceLongitude()))
                .zoom(13)
                .bearing(0)
                .tilt(0)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mGoogleMap.addMarker(new MarkerOptions()
                .position((new LatLng(
                        mCurrentPlace.getPlaceLatitude(), mCurrentPlace.getPlaceLongitude()))));
    }

}