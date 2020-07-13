package woopy.com.juanmckenzie.caymanall.landing;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.EditProfileActivity;
import woopy.com.juanmckenzie.caymanall.Objects.Categories;
import woopy.com.juanmckenzie.caymanall.Objects.SubCategories;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.wizard.WizardActivity;

public class SplashScreen extends AppCompatActivity {

    private static int splashInterval = 2000;
    Configs myapplication;


    List<Categories> categoriesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (!Configs.isConnected()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
                    alert.setMessage(getString(R.string.please_check_your_internat_connnection))
                            .setTitle(R.string.app_name)
                            .setPositiveButton(getString(R.string.ok5), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setIcon(R.drawable.logo);
                    alert.create().show();
                    return;
                }

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                Network activeNetwork = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    activeNetwork = connectivityManager.getActiveNetwork();

                    NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
                    boolean vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                    if (vpnInUse) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
                        alert.setMessage(getString(R.string.please_disable_vpn))
                                .setTitle(R.string.app_name)
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setIcon(R.drawable.logo);
                        alert.create().show();
                    } else {
                        dojobdone();
                    }
                } else {
                    dojobdone();
                }


            }
        }, 2000);

    }

    public void dojobdone() {
        myapplication = (Configs) getApplicationContext();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        myapplication.setCurrentUser(dataSnapshot.getValue(User.class));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                                startActivity(i);
                                this.finish();
                            }

                            private void finish() {
                            }
                        }, splashInterval);
                    } catch (Exception Ex) {
                        FirebaseAuth.getInstance().signOut();
                        AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
                        alert.setMessage("sorry try to login via another account this user data has some issue.")
                                .setTitle(R.string.app_name)
                                .setPositiveButton(getString(R.string.login), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getApplicationContext(), WizardActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(getString(R.string.cancel), null)
                                .setIcon(R.drawable.logo);
                        alert.create().show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(i);
                    this.finish();
                }

                private void finish() {
                }
            }, splashInterval);
        }

    }
}
