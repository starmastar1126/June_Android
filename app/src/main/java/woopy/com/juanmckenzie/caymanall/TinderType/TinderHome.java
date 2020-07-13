package woopy.com.juanmckenzie.caymanall.TinderType;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.badgedbottomnavigationbar.BadgedBottomNavigationBar;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;

import woopy.com.juanmckenzie.caymanall.DistanceMapActivity;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Fragments.Chat;
import woopy.com.juanmckenzie.caymanall.TinderType.Fragments.Home;
import woopy.com.juanmckenzie.caymanall.TinderType.Fragments.Settings;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.TUser;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.wizard.WizardActivity;


public class TinderHome extends BaseActivity {


    TextView Toolber;
    TUser tUser;
    BadgedBottomNavigationBar _bottomNavigation;

    Configs configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinder_home);


        Toolber = findViewById(R.id.Toolbar);

        findViewById(R.id.Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        configs = (Configs) getApplicationContext();
        findViewById(R.id.aep_save_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.save();
            }
        });

        _bottomNavigation = (BadgedBottomNavigationBar) findViewById(R.id.navigation1);
        _bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), WizardActivity.class));
            finish();
        } else {

            showLoading();
            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    hideLoading();
                    tUser = dataSnapshot.getValue(TUser.class);
                    if (home == null) {
                        home = new Home();
                        try {
                            getSupportFragmentManager().beginTransaction().add(R.id.frame, home).commit();
                        } catch (Exception ignored) {

                        }

                    }
                    if (tUser != null)
                        if (tUser.getBandage()) {
                            _bottomNavigation.showBadge(1);
                        } else {
                            _bottomNavigation.removeBadge(1);
                        }

                    if (configs.getSecondtabs()) {
                        _bottomNavigation.setSelectedItemId(R.id.item2);
                        configs.setSecondtabs(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hideLoading();
                }
            });


        }

    }


    Settings settings;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.item1:
                    home = new Home();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, home).commit();
                    Toolber.setText(getString(R.string.browse));
                    findViewById(R.id.aep_save_iv).setVisibility(View.INVISIBLE);
                    return true;
                case R.id.item2:
                    fragment = new Chat();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                    Toolber.setText(getString(R.string.chats));
                    findViewById(R.id.aep_save_iv).setVisibility(View.INVISIBLE);
                    return true;
                case R.id.item3:
                    settings = new Settings();
                    fragment = settings;
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                    Toolber.setText(getString(R.string.settings));
                    findViewById(R.id.aep_save_iv).setVisibility(View.VISIBLE);
                    return true;

            }
            return false;
        }
    };

    private static final int PICK_IMAGE1_REQ_CODE = 21;
    private static final int PICK_IMAGE2_REQ_CODE = 22;
    private static final int PICK_IMAGE3_REQ_CODE = 23;


    Home home;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            try {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {

                    Configs.simpleAlert(getString(R.string.you_are_now_able_to_browse), TinderHome.this);
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("paid").setValue(true);
                    try {
                        Log.i("paymentExample", confirm.toJSONObject().toString(4));

                        // TODO: send 'confirm' to your server for verification.
                        // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                        // for more details.
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
                Location chosenLocation = data.getParcelableExtra(DistanceMapActivity.LOCATION_EXTRA_KEY);
                if (chosenLocation != null) {
                    float distanceInMiles = data.getFloatExtra(DistanceMapActivity.DISTANCE_EXTRA_KEY, Configs.distanceInMiles);
                    try {
                        home.Result(distanceInMiles, chosenLocation);

                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception ignored) {

            }
//            switch (requestCode) {
//                case PICK_IMAGE1_REQ_CODE:
//                    settings.showmegic(PICK_IMAGE1_REQ_CODE, data);
//                    break;
//                case PICK_IMAGE2_REQ_CODE:
//                    settings.showmegic(PICK_IMAGE2_REQ_CODE, data);
//                    break;
//                case PICK_IMAGE3_REQ_CODE:
//                    settings.showmegic(PICK_IMAGE3_REQ_CODE, data);
//                    break;
//            }
        }

    }


    public TUser gettUser() {
        return tUser;
    }
}
