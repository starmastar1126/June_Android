package woopy.com.juanmckenzie.caymanall.TinderType.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.varunest.sparkbutton.SparkButton;


import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import woopy.com.juanmckenzie.caymanall.DistanceMapActivity;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Adapters.TinderCardAdapter;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.MessageT;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.TUser;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.userReact;
import woopy.com.juanmckenzie.caymanall.TinderType.TinderHome;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.GPSTracker;
import woopy.com.juanmckenzie.caymanall.utils.PermissionsUtils;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends BaseFragment implements LocationListener {


    public Home() {
        // Required empty public constructor
    }

    private static final int SET_LOCATION_REQ_CODE = 400;
    private TinderCardAdapter adapter;
    Configs myapplication;
    TextView cityCountryTV;
    private TextView distanceTxt;
    List<TUser> tUsers = new ArrayList<>();
    GPSTracker gpsTracker;
    SparkButton Dislike, LIke, Refresh;
    TUser lastuser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Dislike = view.findViewById(R.id.Dislike);
        LIke = view.findViewById(R.id.Like);
        Refresh = view.findViewById(R.id.Refrash);
        Refresh.setAlpha(0.3f);
        lastuser = null;

        gpsTracker = new GPSTracker(getActivity());
        myapplication = (Configs) getActivity().getApplicationContext();
        cityCountryTV = view.findViewById(R.id.alCityCountryTV);
        distanceTxt = view.findViewById(R.id.alDistanceTxt);
        recyclerView = view.findViewById(R.id.recycler_view);

        cityCountryTV.setTypeface(Configs.titSemibold);
        distanceTxt.setTypeface(Configs.titRegular);

        view.findViewById(R.id.aal_location_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!((TinderHome) getActivity()).gettUser().getPaid()) {
                    paypayoneer();
                } else {
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


            }
        });

        loadAdsFromChosenLocation();


        return view;
    }

    private static final int SELECT_PAYMENT = 110;

    public void paypayoneer() {
        PayPalConfiguration config = new PayPalConfiguration()

                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

                .clientId("<YOUR_CLIENT_ID>");

        PayPalPayment payment = new PayPalPayment(new BigDecimal("5.00"), "USD", getString(R.string.super_account_payment),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, SELECT_PAYMENT);
    }

    private void loadAdsFromChosenLocation() {
        // Get ads from a chosen location
        if (currentLocation != null) {
            getCityCountryNames();
        } else {
            getCurrentLocation();
        }
    }

    private Location currentLocation;
    private LocationManager locationManager;
    private float distanceInMiles = Configs.distanceInMiles;


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

                queryusers();

            } else {
                Toast.makeText(getActivity(), "Geocoder not present!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
//            ToastUtils.showMessage(e.getMessage());
        }
    }


    Boolean LastCheck = false;
    TUser Currentuser;
    RecyclerView recyclerView;

    public void queryusers() {
        showLoading();
        try {
            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tUsers = new ArrayList<>();
                    Currentuser = dataSnapshot.getValue(TUser.class);
                    if (tUsers != null) {
                        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                hideLoading();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    try {
                                        TUser tUser = dataSnapshot1.getValue(TUser.class);

                                        if (currentLocation != null && tUser.getLatlong() != null) {
                                            double distance = distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), Objects.requireNonNull(tUser).getLatlong().getLatitude(), tUser.getLatlong().getLongitude());
                                            if (distance <= distanceInMiles) {
                                                tUser.setDistance(distance);
                                                if (!tUser.getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                    userReact userReact = Currentuser.getLikesbyme().get(tUser.getFirebaseID());
                                                    if (userReact == null) {

                                                        TUser tUser1 = ((TinderHome) getActivity()).gettUser();
                                                        if (tUser1.getExpectinggander().equals("both")) {
                                                            if (tUser.getVisibility() != null && tUser.getVisibility())
                                                                tUsers.add(tUser);
                                                        } else if (tUser1.getExpectinggander().equals("Male")) {
                                                            if (tUser.getGander().equals("Male")) {
                                                                if (tUser.getVisibility() != null && tUser.getVisibility())
                                                                    tUsers.add(tUser);
                                                            }
                                                        } else {
                                                            if (!tUser.getGander().equals("Male")) {
                                                                if (tUser.getVisibility() != null && tUser.getVisibility())
                                                                    tUsers.add(tUser);
                                                            }
                                                        }


                                                    }
                                                }

                                            }
                                        }
                                    } catch (Exception ex) {

                                    }
                                }
                                adapter = new TinderCardAdapter(tUsers, getActivity());
                                SwipeableTouchHelperCallback swipeableTouchHelperCallback =
                                        new SwipeableTouchHelperCallback(new OnItemSwiped() {
                                            @Override
                                            public void onItemSwiped() {
                                                adapter.removeTopItem();
                                                userReact userReact = new userReact();
                                                userReact.setID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                userReact.setDate(Calendar.getInstance().getTimeInMillis() + "");
                                                userReact.setLike(LastCheck);
                                                userReact.setUserID(adapter.getlastUser().getFirebaseID());

                                                String ID = adapter.getlastUser().getFirebaseID();
                                                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("likesbyme").child(ID).setValue(userReact);


                                                if (LastCheck) {
                                                    userReact userReact1 = adapter.getlastUser().getLikesbyme().get(Currentuser.getFirebaseID());


                                                    if (userReact1 != null && userReact1.getLike()) {

                                                        String inboxId2 = adapter.getlastUser().getFirebaseID() + Currentuser.getFirebaseID();
                                                        final MessageT iObj = new MessageT();
                                                        TUser currentUser = Currentuser;
                                                        // Save Message to Inbox Class
                                                        iObj.setSender(currentUser);
                                                        iObj.setReciver(adapter.getlastUser());
                                                        iObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
                                                        iObj.setID(FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("Messages").push().getKey());
                                                        iObj.setMmessage(getString(R.string.you_have_found_a_match));
                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child(iObj.getID()).setValue(iObj);


                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("mmessage").setValue(iObj.getMmessage());
                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("reads").setValue(false);
                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("readr").setValue(false);
                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("sender").setValue(Currentuser);
                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("receiver").setValue(iObj.getReciver());
                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("inboxID").setValue(inboxId2);
                                                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("createdAt").setValue(Calendar.getInstance().getTimeInMillis() + "");


                                                        FirebaseDatabase.getInstance().getReference().child("users").child(adapter.getlastUser().getFirebaseID()).child("bandage").setValue(true);
                                                        FirebaseDatabase.getInstance().getReference().child("users").child(Currentuser.getFirebaseID()).child("bandage").setValue(true);

                                                        lastuser = null;
                                                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                        alert.setMessage(getString(R.string.you_have_found_a_match) + adapter.getlastUser().getUsername())
                                                                .setTitle(R.string.app_name)
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        showLoading();
                                                                        HashMap<String, String> rawParameters = new HashMap<>();
                                                                        rawParameters.put("body", getString(R.string.you_have_found_a_match) + Currentuser.getFullName() + ", open app to make your relationship stronger :)");
                                                                        rawParameters.put("title", getString(R.string.app_name));
                                                                        rawParameters.put("page", "chatpage");
                                                                        rawParameters.put("userid", FirebaseAuth.getInstance().getUid());
                                                                        NotificationSednToallusers(getActivity(), rawParameters, adapter.getlastUser().getFCM());

                                                                    }
                                                                })
                                                                .setCancelable(false)
                                                                .setIcon(R.drawable.logo);
                                                        alert.create().show();

                                                    }
                                                }


                                            }


                                            @Override
                                            public void onItemSwipedLeft() {

                                                LastCheck = false;

                                                Log.e("SWIPE", "LEFT");
                                            }

                                            @Override
                                            public void onItemSwipedRight() {

                                                LastCheck = true;

                                                Log.e("SWIPE", "RIGHT");
                                            }

                                            @Override
                                            public void onItemSwipedUp() {
                                                LastCheck = true;
                                                Log.e("SWIPE", "UP");
                                            }

                                            @Override
                                            public void onItemSwipedDown() {
                                                LastCheck = false;
                                                Log.e("SWIPE", "DOWN");
                                            }
                                        }) {
                                            @Override
                                            public int getAllowedSwipeDirectionsMovementFlags(RecyclerView.ViewHolder viewHolder) {
                                                return ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
                                            }
                                        };
                                final swipeable.com.layoutmanager.touchelper.ItemTouchHelper itemTouchHelper = new swipeable.com.layoutmanager.touchelper.ItemTouchHelper(swipeableTouchHelperCallback);
                                itemTouchHelper.attachToRecyclerView(recyclerView);
                                recyclerView.setLayoutManager(new SwipeableLayoutManager().setAngle(10)
                                        .setAnimationDuratuion(450)
                                        .setMaxShowCount(3)
                                        .setScaleGap(0.1f)
                                        .setTransYGap(0));
                                recyclerView.setAdapter(adapter = new TinderCardAdapter(tUsers, getActivity()));

                                Dislike.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        if (adapter.getItemCount() != 0) {
                                            LIke.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LIke.setEnabled(true);
                                                }
                                            }, 450);

                                            lastuser = adapter.getItems().get(0);
                                            Refresh.setAlpha(1f);
                                            itemTouchHelper.swipe(recyclerView.findViewHolderForAdapterPosition(0), ItemTouchHelper.LEFT);
                                        }


                                    }
                                });
                                LIke.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (adapter.getItemCount() != 0) {
                                            Dislike.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Dislike.setEnabled(true);
                                                }
                                            }, 450);
                                            lastuser = adapter.getItems().get(0);
                                            Refresh.setAlpha(1f);
                                            itemTouchHelper.swipe(recyclerView.findViewHolderForAdapterPosition(0), ItemTouchHelper.RIGHT);
                                        }
                                    }
                                });


                                Refresh.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (lastuser != null) {
                                            if (!((TinderHome) getActivity()).gettUser().getPaid()) {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                alert.setMessage(getString(R.string.rewind_is_only_available_for))
                                                        .setTitle(R.string.app_name)
                                                        .setPositiveButton(getString(R.string.continue_message), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                paypayoneer();
                                                            }
                                                        })
                                                        .setNegativeButton(getString(R.string.cancel), null)
                                                        .setCancelable(false)
                                                        .setIcon(R.drawable.logo);
                                                alert.create().show();
                                            } else {

                                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                alert.setMessage(getString(R.string.rewind_mesage))
                                                        .setTitle(R.string.app_name)
                                                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                showLoading();
                                                                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("likesbyme").child(lastuser.getFirebaseID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        hideLoading();
                                                                        Refresh.setAlpha(0.3f);
                                                                        lastuser = null;
                                                                        queryusers();
                                                                    }
                                                                });

                                                            }
                                                        })

                                                        .setNegativeButton(getString(R.string.cancel), null)
                                                        .setIcon(R.drawable.logo);
                                                alert.create().show();


                                            }
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {

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


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentLocation == null) {
                        currentLocation = new Location("provider");
                        currentLocation.setLatitude(gpsTracker.getLatitude());
                        currentLocation.setLongitude(gpsTracker.getLongitude());
                        getCityCountryNames();
                    }

                }
            }, 2000);
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
            return;
        }


        locationManager.removeUpdates(this);
        currentLocation = location;

        if (currentLocation != null) {
            getCityCountryNames();
            // NO GPS location found!
        } else {
            Configs.simpleAlert(getString(R.string.location_message6), getActivity());

            // Set New York City as default currentLocation
            currentLocation = new Location("provider");
            currentLocation.setLatitude(Configs.DEFAULT_LOCATION.latitude);
            currentLocation.setLongitude(Configs.DEFAULT_LOCATION.longitude);

            // Set distance and city labels
            String distFormatted = String.format("%.0f", distanceInMiles);
            distanceTxt.setText(distFormatted + getString(R.string.mi_from));
            cityCountryTV.setText(getString(R.string.new_york_usa));

            // Call query
            queryusers();
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


    @Override
    public void onResume() {
        try {
            if (((TinderHome) Objects.requireNonNull(getActivity())).gettUser().getVisibility()) {
                getActivity().findViewById(R.id.checkvisibility).setVisibility(View.GONE);
            } else {
                getActivity().findViewById(R.id.checkvisibility).setVisibility(View.VISIBLE);
            }

        } catch (Exception ignored) {

        }


        super.onResume();
    }

    public void Result(Float res, Location selected) {
        Location chosenLocation = selected;
        if (chosenLocation != null) {
            currentLocation = chosenLocation;

        }
        distanceInMiles = res;
        loadAdsFromChosenLocation();
    }


    public void NotificationSednToallusers(final Context activity, final HashMap dataValue, final String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/) {


        final String url = "https://fcm.googleapis.com/fcm/send";
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                    }
                }) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Map<String, Object> rawParameters = new Hashtable<>();
                rawParameters.put("data", new JSONObject(dataValue));
                rawParameters.put("to", instanceIdToken);
                return new JSONObject(rawParameters).toString().getBytes();
            }

            ;

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAA-cK_PpE:APA91bHdEBTxLv8k9f-Ylr0QKbHaa5CMe3DTyjnEc1ZSWvNw6od3JuHV-nwcrCmJ_aNPTL1Y9zX6BJGjcTyZInJUlzjB_XSjo5kbmE3HMf8wZe8xvq2Ix9JMrQm4zTdNuH1yfQ_RdBNB");
                return headers;
            }

        };

        Volley.newRequestQueue(activity).add(myReq);
    }

}
