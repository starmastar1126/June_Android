package woopy.com.juanmckenzie.caymanall.wizard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ParseFacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.landing.LoginActivity;
import woopy.com.juanmckenzie.caymanall.landing.TermsOfUse;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public class WizardActivity extends BaseActivity {

    private static final int CHANGE_PAGE_TIME_MILLIS = 3000;
    private static final int[] dotsDrawableRes = {R.drawable.dots1, R.drawable.dots2, R.drawable.dots3};

    private Button facebookBtn;
    private ImageView dotsIV;
    private ViewPager contentVP;

    private Handler timerHandler = new Handler();
    private int page = 0;
    private CallbackManager mCallbackManager;
    User user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Configs myApplication;

    Dialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        initViews();
        setUpChangePageTimer();
        setUpWizardViewPager();
        mAuth = FirebaseAuth.getInstance();

        myApplication = (Configs) getApplicationContext();
        mCallbackManager = CallbackManager.Factory.create();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        progress = Configs.buildProgressLoadingDialog(this);

        facebookBtn.setTypeface(Configs.titSemibold);
        facebookBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(WizardActivity.this);
                alert.setTitle(getString(R.string.do_you_accpet_our_terms))
                        .setIcon(R.drawable.logo)
                        .setItems(new CharSequence[]{
                                getString(R.string.yes),
                                getString(R.string.read_terms)
                        }, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    // SIGN IN WITH FACEBOOK
                                    case 0:
                                        progress.show();
                                        LoginFacebook();
                                        break;
                                    // OPEN TERMS OF SERVICE
                                    case 1:
                                        startActivity(new Intent(WizardActivity.this, TermsOfUse.class));
                                        break;

                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null);
                alert.create().show();


            }
        });

        // This code generates a KeyHash that you'll have to copy from your Logcat console and paste it into Key Hashes field in the 'Settings' section of your Facebook Android App
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("log-", "keyhash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
        }

        // MARK: - SIGN UP BUTTON ------------------------------------
        Button signupButt = findViewById(R.id.signupButt);
        signupButt.setTypeface(Configs.titRegular);
        signupButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WizardActivity.this, LoginActivity.class));
            }
        });

        // MARK: - TERMS OF SERVICE BUTTON ------------------------------------
        Button tosButt = findViewById(R.id.tosButt);
        tosButt.setTypeface(Configs.titRegular);
        tosButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WizardActivity.this, TermsOfUse.class));
            }
        });

        // MARK: - DISMISS BUTTON ------------------------------------
        Button dismissButt = findViewById(R.id.wDismissButt);
        dismissButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }


    private void initViews() {
        dotsIV = findViewById(R.id.dotsImg);
        contentVP = findViewById(R.id.viewpager);
        facebookBtn = findViewById(R.id.facebookButt);
    }

    private void setUpChangePageTimer() {
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page > 2) {
                    timerHandler.removeCallbacksAndMessages(null);
                } else {
                    contentVP.setCurrentItem(page++);
                }
            }
        }, CHANGE_PAGE_TIME_MILLIS);
    }

    private void setUpWizardViewPager() {
        contentVP.setAdapter(new WizardPagerAdapter());
        contentVP.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position < dotsDrawableRes.length) {
                    dotsIV.setImageResource(dotsDrawableRes[position]);
                }
            }
        });
    }


    String TAG = "CaymanAll";
    FirebaseAuth.AuthStateListener authStateListener;

    public void LoginFacebook() {
        Collection<String> readPermissions = new ArrayList<>();
        readPermissions.add("public_profile");
        readPermissions.add("email");

        LoginManager.getInstance().logInWithReadPermissions(this, readPermissions);
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                progress.dismiss();
                progress.hide();
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                progress.dismiss();
                progress.hide();
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();


                    }

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

                                @Override
                                public void onCompleted(final JSONObject object, GraphResponse response) {
                                    try {
                                        progress.dismiss();
                                    } catch (Exception Ex) {
                                    }


                                    try {

                                        user = new User();
                                        user.setID(object.getString("id"));
                                        user.setEmail(object.getString("email"));
                                        user.setFullName(object.getString("name"));
                                        user.setUsername(object.getString("name"));
                                        ImageObj imageObj = new ImageObj();
                                        imageObj.setUrl("https://graph.facebook.com/" + user.getID() + "/picture?type=large");
                                        imageObj.setID("facebook");
                                        user.setAvatar(imageObj);
                                        user.setFirebaseID(FirebaseAuth.getInstance().getUid());
                                        user.setJoindate(Calendar.getInstance().getTime().getTime() + "");
                                        user.setEmailVerified(true);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    progress.show();
                                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    // Get user value
                                                    User User1 = new User();
                                                    User1 = dataSnapshot.getValue(User.class);
                                                    if (User1 == null) {

                                                        progress.dismiss();
                                                        myApplication.setSessionUserID(user.getFirebaseID());
                                                        user.setAboutMe("");
                                                        user.setHasBlocked(new ArrayList<String>());
                                                        user.setReported(false);
                                                        user.setJoindate(Calendar.getInstance().getTimeInMillis() + "");
                                                        user.setEmailVerified(true);
                                                        user.setFirebaseID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                                        startActivity(new Intent(WizardActivity.this, HomeActivity.class));
                                                        finish();

                                                    } else {
                                                        progress.dismiss();
                                                        startActivity(new Intent(WizardActivity.this, HomeActivity.class));
                                                        finish();

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    progress.dismiss();
                                                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                                }
                                            });

                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,email,name,address,birthday,picture");
                            request.setParameters(parameters);
                            request.executeAsync();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        } else {


                            try {
                                progress.dismiss();
                            } catch (Exception Ex) {
                            }                          // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }


}
