package woopy.com.juanmckenzie.caymanall.home.fragments;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.ParseUser;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.nekocode.badge.BadgeDrawable;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.Objects.promotions;
import woopy.com.juanmckenzie.caymanall.Purchase;
import woopy.com.juanmckenzie.caymanall.PurchasePromotion;
import woopy.com.juanmckenzie.caymanall.Settings;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterNative;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterpromotions;
import woopy.com.juanmckenzie.caymanall.formsjobs.JobApplicationsRecived;
import woopy.com.juanmckenzie.caymanall.formsjobs.JobApplicationsSended;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.Feedbacks;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.home.activities.HomeActivity;
import woopy.com.juanmckenzie.caymanall.EditProfileActivity;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

public class AccountFragment extends BaseFragment {

    private TextView usernameTV;
    private TextView fullnameTV;
    private ImageView avatarIV;
    private TextView joinedTV;
    private TextView verifiedTV;
    private Configs myapplication;
    Dialog dialog;
    SliderView image;

    TextView fa_verified_title_tv;

    LinearLayout editProfileTV, fa_edit_purchase_tv, fa_edit_settings_tv, contactus, fa_edit_promotions_tv, logoutTV, feedbacksTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = Configs.buildProgressLoadingDialog(getActivity());
        myapplication = (Configs) Objects.requireNonNull(getActivity()).getApplicationContext();

        fa_verified_title_tv = getActivity().findViewById(R.id.fa_verified_title_tv);


        view.findViewById(R.id.promote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PurchasePromotion.class));
            }
        });

        view.findViewById(R.id.promote1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PurchasePromotion.class));
            }
        });
        initViews();
        setUpViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getUserDetails();
        } catch (Exception e) {
        }
    }


    List<promotions> promotions;

    private void initViews() {

        image = getActivity().findViewById(R.id.image);
        usernameTV = getActivity().findViewById(R.id.fa_username_tv);
        fullnameTV = getActivity().findViewById(R.id.fa_fullname_tv);
        avatarIV = getActivity().findViewById(R.id.fa_avatar_iv);
        joinedTV = getActivity().findViewById(R.id.fa_joined_tv);
        verifiedTV = getActivity().findViewById(R.id.fa_verified_tv);
        editProfileTV = getActivity().findViewById(R.id.fa_edit_profile_tv);
        fa_edit_purchase_tv = getActivity().findViewById(R.id.fa_edit_purchase_tv);
        fa_edit_settings_tv = getActivity().findViewById(R.id.fa_edit_settings_tv);
        contactus = getActivity().findViewById(R.id.contactus);
        feedbacksTV = getActivity().findViewById(R.id.fa_feedbacks_tv);
        logoutTV = getActivity().findViewById(R.id.fa_logout_tv);
        fa_edit_promotions_tv = getActivity().findViewById(R.id.fa_edit_promotions_tv);


        getActivity().findViewById(R.id.fa_verified_title_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (useremailveri) {
                    return;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(getString(R.string.do_you_want_to_send_varification_email))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.show();

                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), getString(R.string.email_sent), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        })

                        .setNegativeButton(getString(R.string.cancel), null)
                        .setIcon(R.drawable.logo);
                alert.create().show();

            }
        });

        getActivity().findViewById(R.id.fa_verified_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (useremailveri) {
                    return;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(getString(R.string.do_you_want_to_send_varification_email))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.show();
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), getString(R.string.email_sent), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        })

                        .setNegativeButton(getString(R.string.cancel), null)
                        .setIcon(R.drawable.logo);
                alert.create().show();

            }
        });

    }

    private void setUpViews() {


        fa_edit_promotions_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyPromotions.class);
                startActivity(i);
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contactusActivity.class);
                startActivity(i);
            }
        });

        fa_edit_purchase_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MybannersActivity.class);
                startActivity(i);
            }
        });

        fa_edit_settings_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Settings.class);
                startActivity(i);
            }
        });

        // MARK: - FEEDBACKS BUTTON ------------------------------------
        feedbacksTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Feedbacks.class);
                Bundle extras = new Bundle();
                i.putExtras(extras);
                startActivity(i);
            }
        });

        // MARK: - EDIT PROFILE BUTTON ------------------------------------
        editProfileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });


        getActivity().findViewById(R.id.mylikes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), MyLikesActivity.class));
            }
        });

        getActivity().findViewById(R.id.myaads).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), MyAdsActivity.class));
            }
        });

        getActivity().findViewById(R.id.Activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ActivityActivity.class));
            }
        });


        // MARK: - LOGOUT BUTTON ------------------------------------
        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage(getString(R.string.are_you_sure_you_want_to_login))
                        .setTitle(R.string.app_name)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.show();
                                String FirebaseId = myapplication.getCurrentUser().getFirebaseID();
                                myapplication.setCurrentUser(new User());
                                FirebaseAuth.getInstance().signOut();
                                dialog.dismiss();
                                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(homeIntent);
//                                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseId).child("FCM").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        dialog.dismiss();
//                                        Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
//                                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(homeIntent);
//                                    }
//                                });


                            }
                        })

                        .setNegativeButton(getString(R.string.cancel), null)
                        .setIcon(R.drawable.logo);
                alert.create().show();
            }
        });


        FirebaseDatabase.getInstance().getReference().child("promotionsall").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                promotions = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    promotions promotion = dataSnapshot1.getValue(promotions.class);
                    promotions.add(promotion);
                }

                image = getActivity().findViewById(R.id.imageSliderFinal);
                image.setVisibility(View.VISIBLE);
                SliderAdapterNative adapter = new SliderAdapterNative(getActivity(), promotions);
                image.setSliderAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    // MARK: - GET USER'S DETAILS ----------------------------------------------------------------
    private void getUserDetails() {


        // get username
        usernameTV.setTypeface(Configs.titSemibold);
        usernameTV.setText("@" + myapplication.getCurrentUser().getUsername());

        // Get fullname
        fullnameTV.setTypeface(Configs.titSemibold);
        fullnameTV.setText(myapplication.getCurrentUser().getFullName());

        // Get joined since
        joinedTV.setTypeface(Configs.titRegular);
        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.valueOf(myapplication.getCurrentUser().getJoindate()));
        joinedTV.setText(Configs.timeAgoSinceDate(date, getActivity()));

        // Get verified
        verifiedTV.setTypeface(Configs.titRegular);


        getActivity().findViewById(R.id.jobaplicationrecived).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JobApplicationsRecived.class));

            }
        });

        getActivity().findViewById(R.id.applicationssended).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JobApplicationsSended.class));
            }
        });

        if (!myapplication.getCurrentUser().getEmailVerified()) {
            FirebaseAuth.getInstance().getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    try {
                        useremailveri = FirebaseAuth.getInstance().getCurrentUser().isEmailVerified();
                        if (!useremailveri) {
                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("emailVerified").setValue(false);
                            if (myapplication.getCurrentUser().getID() == null) {
                                verifiedTV.setText(getString(R.string.no4));

                                try {
                                    final BadgeDrawable drawableBanner =
                                            new BadgeDrawable.Builder()
                                                    .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                                                    .badgeColor(UIUtils.getColor(R.color.red))
                                                    .text1(getString(R.string.send_varification_email))
                                                    .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                                                    .strokeWidth(1)
                                                    .build();
                                    SpannableString spannablebanner =
                                            new SpannableString(TextUtils.concat(
                                                    getString(R.string.no4),
                                                    drawableBanner.toSpannable()
                                            ));

                                    verifiedTV.setText(spannablebanner);
                                } catch (Exception ignored) {

                                }

                            } else {
                                verifiedTV.setText(getString(R.string.yes));
                            }


                        } else {
                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("emailVerified").setValue(true);

                            verifiedTV.setText(getString(R.string.yes));
                        }
                    } catch (Exception ex) {

                    }
                }
            });

        } else {
            verifiedTV.setText(getString(R.string.yes));
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(Objects.requireNonNull(getActivity())).setDefaultRequestOptions(requestOptions)
                .load(myapplication.getCurrentUser().getAvatar().getUrl()).into(avatarIV).clearOnDetach();
    }

    public float dp2px(float dipValue) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    boolean useremailveri;
}
