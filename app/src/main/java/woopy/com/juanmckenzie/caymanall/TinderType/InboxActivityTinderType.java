package woopy.com.juanmckenzie.caymanall.TinderType;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.eyalbira.loadingdots.LoadingDots;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import woopy.com.juanmckenzie.caymanall.BannerView;
import woopy.com.juanmckenzie.caymanall.BuildConfig;
import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.InboxActivity;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.Message;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.ChatsT;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.MessageT;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.TUser;
import woopy.com.juanmckenzie.caymanall.UserProfile;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBanners;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.Constants;
import woopy.com.juanmckenzie.caymanall.utils.ExifUtil;
import woopy.com.juanmckenzie.caymanall.utils.MarshMallowPermission;

public class InboxActivityTinderType extends AppCompatActivity {

    /* Views */
    Button optionsButt, sendButt, uploadPicButt;
    TextView usernameTxt, username, usergender;
    EditText messageTxt;
    RecyclerView iListView;
    ImageView imgPreview, UserImage;

    /* Variables */
    TUser userObj;
    TUser currentUser;
    List<Message> inboxArray;
    String lastMessageStr = null;
    Timer refreshTimerForInboxQuery = new Timer();
    Bitmap imageToSend = null;
    Configs configs;

    LoadingDots loadingDots;
    String inboxId2;
    List<Banner> banners = new ArrayList<>();
    ValueEventListener dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newinboxlayout);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Init views
        optionsButt = findViewById(R.id.inOptionsButt);
        uploadPicButt = findViewById(R.id.inUploadPicButt);
        sendButt = findViewById(R.id.inSendButt);
        sendButt.setTypeface(Configs.titSemibold);
        usernameTxt = findViewById(R.id.inUsernameTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        messageTxt = findViewById(R.id.inMessageTxt);
        messageTxt.setTypeface(Configs.titRegular);
        loadingDots = findViewById(R.id.dost);

        username = findViewById(R.id.username);
        usergender = findViewById(R.id.gender);
        UserImage = findViewById(R.id.userimage);

        findViewById(R.id.inAdBoxLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configs.setSelectedUser(userObj);
                startActivity(new Intent(getApplicationContext(), ProfileDetails.class));
            }
        });

        messageTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                FirebaseDatabase.getInstance().getReference().child("chatdots").child(inboxId2).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(Calendar.getInstance().getTimeInMillis());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iListView = findViewById(R.id.inInboxListView);
        configs = (Configs) getApplicationContext();


        FirebaseDatabase.getInstance().getReference().child("Banners").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                banners = new ArrayList<>();
                for (DataSnapshot bannerssnap1 : dataSnapshot.getChildren()) {

                    Banner adsbanner = bannerssnap1.getValue(Banner.class);
                    Date date = Calendar.getInstance().getTime();
                    date.setTime(Long.valueOf(adsbanner.getCreatedAt()));

                    if (AdsListActivity.getUnsignedDiffInDays(date, Calendar.getInstance().getTime()) < Integer.parseInt(adsbanner.getDays())) {
                        banners.add(adsbanner);
                    }
                }

                SliderView image = findViewById(R.id.imageSlider);
                SliderAdapterBanners adapter = new SliderAdapterBanners(InboxActivityTinderType.this, banners);
                image.setSliderAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Hide imgPreview
        imgPreview = findViewById(R.id.inImagePreview);
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(imgPreview.getLayoutParams());
        marginParams.setMargins(0, 2000, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        imgPreview.setLayoutParams(layoutParams);

        // Hide imgPreview on click
        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(imgPreview.getLayoutParams());
                marginParams.setMargins(0, 2000, 0, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                imgPreview.setLayoutParams(layoutParams);
            }
        });


        if (configs.getSelectedChat().getSender().getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            // Get userObj
            userObj = configs.getSelectedChat().getReceiver();
            currentUser = configs.getSelectedChat().getSender();
        } else {
            currentUser = configs.getSelectedChat().getReceiver();
            userObj = configs.getSelectedChat().getSender();
        }


        final User currUser = configs.getCurrentUser();
        final List<String> hasBlocked = currUser.getHasBlocked();
        if (hasBlocked.contains(userObj.getFirebaseID())) {
            findViewById(R.id.inBottomLayout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.inBottomLayout).setVisibility(View.VISIBLE);
        }

        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(InboxActivityTinderType.this).setDefaultRequestOptions(requestOptions)
                .load(userObj.getAvatar()).into(UserImage).clearOnDetach();


        username.setText(userObj.getUsername());
        usergender.setText(userObj.getGander());


        inboxId2 = configs.getSelectedChatID();
        // Call query
        Configs.showPD("Please Wait ...", InboxActivityTinderType.this);
        queryInbox();


        dots = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (loadingDots.getVisibility() == View.INVISIBLE) {
                    Long Lasttime = dataSnapshot.getValue(Long.class);
                    if (Lasttime != null) {
                        Long diff = TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis()) - TimeUnit.MILLISECONDS.toSeconds(Lasttime);
                        if (diff < 5)
                            loadingDots.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDots.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);
                    } else {
                        loadingDots.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        FirebaseDatabase.getInstance().getReference().child("chatdots").child(inboxId2).child(userObj.getFirebaseID()).addValueEventListener(dots);


        // Get User's username
        usernameTxt.setText("@" + userObj.getUsername());


        // Start refresh Timer for Inbox query
//        startRefreshTimer();

        // MARK: - UPLOAD IMAGE BUTTON ------------------------------------
        uploadPicButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Options options = Options.init()
                        .setRequestCode(GALLERY)                                                 //Request code for activity results
                        .setCount(1)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                                //Front Facing camera on start
                        .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion
                Pix.start(InboxActivityTinderType.this, options);

            }
        });


        // MARK: - SEND MESSAGE BUTTON ---------------------------------------------
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageTxt.getText().toString().matches("")) {
                    Configs.simpleAlert(getString(R.string.your_must_type_something), InboxActivityTinderType.this);
                } else {
                    sendMessage();
                }
            }
        });


        // OPTIONS BUTTON -------------------------------------------------
        optionsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final User currUser = configs.getCurrentUser();
                final List<String> hasBlocked = currUser.getHasBlocked();

                // Set blockUser  Action title
                String blockTitle = null;
                if (hasBlocked.contains(userObj.getFirebaseID())) {
                    blockTitle = getString(R.string.unblock_user);
                } else {
                    blockTitle = getString(R.string.block_user);
                }


                AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivityTinderType.this);
                final String finalBlockTitle = blockTitle;
                alert.setMessage(getString(R.string.select_option1))
                        .setTitle(R.string.app_name)
                        // BLOCK/UNBLOCK USER ------------------------------------------------------
                        .setNegativeButton(blockTitle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Block User
                                if (finalBlockTitle.matches("Block User")) {
                                    hasBlocked.add(userObj.getFirebaseID());
                                    currUser.setHasBlocked(hasBlocked);

                                    FirebaseDatabase.getInstance().getReference().child("users").child(userObj.getFirebaseID()).child("hasBlocked").setValue(currUser.getHasBlocked()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivityTinderType.this);
                                            alert.setMessage(getString(R.string.your_are_blocked_this_user) + userObj.getUsername())
                                                    .setTitle(R.string.app_name)
                                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            finish();
                                                        }
                                                    })
                                                    .setIcon(R.drawable.logo);
                                            alert.create().show();
                                        }
                                    });


                                    // Unblock User
                                } else {

                                    hasBlocked.remove(userObj.getFirebaseID());
                                    currUser.setHasBlocked(hasBlocked);
                                    FirebaseDatabase.getInstance().getReference().child("users").child(userObj.getFirebaseID()).child("hasBlocked").setValue(currUser.getHasBlocked()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            final User currUser = configs.getCurrentUser();
                                            final List<String> hasBlocked = currUser.getHasBlocked();
                                            if (hasBlocked.contains(userObj.getFirebaseID())) {
                                                findViewById(R.id.inBottomLayout).setVisibility(View.GONE);
                                            } else {
                                                findViewById(R.id.inBottomLayout).setVisibility(View.VISIBLE);
                                            }
                                            Configs.simpleAlert(getString(R.string.you_are_unblocked) + userObj.getUsername(), InboxActivityTinderType.this);

                                        }
                                    });

                                }
                            }
                        })


                        // DELETE CHAT -------------------------------------------------------------
                        .setNeutralButton("Delete Chat", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivityTinderType.this);
                                alert.setMessage(getString(R.string.are_you_sure_you_want) + userObj.getUsername() + getString(R.string.will_not_be_able_to_see_these_messages))
                                        .setTitle(R.string.app_name)
                                        .setPositiveButton(getString(R.string.delete_chat), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        finish();
                                                    }
                                                });


                                            }
                                        })// end alert

                                        .setNegativeButton(getString(R.string.cancel), null)
                                        .setIcon(R.drawable.logo);
                                alert.create().show();
                            }
                        })


                        .setIcon(R.drawable.logo);
                alert.create().show();


            }
        });


        // BACK BUTTON -------------------------------------------------
        Button backButt = findViewById(R.id.inBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshTimerForInboxQuery.cancel();
                imageToSend = null;
                finish();
            }
        });


        // Init AdMob banner
        AdView mAdView = findViewById(R.id.admobBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }// end onCreate()


    ChatsT chatObj;

    // MARK: - QUERY INBOX --------------------------------------------------------
    void queryInbox() {
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Configs.hidePD();
                chatObj = dataSnapshot.getValue(ChatsT.class);
                loadingDots.setVisibility(View.INVISIBLE);
                if (chatObj == null) {
                    chatObj = new ChatsT();
                    chatObj.setSender(currentUser);
                    chatObj.setReceiver(userObj);
                    chatObj.setInboxID(inboxId2);
                    chatObj.setMmessage(messageTxt.getText().toString());

                } else {

                    FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("Messages").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Boolean Start = false;
                            if (inboxArray == null) {
                                Start = true;
                            }

                            inboxArray = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Message message = dataSnapshot1.getValue(Message.class);
                                inboxArray.add(message);
                            }


                            if (Start) {
                                LinearLayoutManager layoutManager =
                                        new LinearLayoutManager(InboxActivityTinderType.this);
                                layoutManager.setSmoothScrollbarEnabled(true);
                                iListView.setLayoutManager(layoutManager);
                                adapter = new InboxListAdapter(InboxActivityTinderType.this);
                                iListView.setAdapter(adapter);

                            } else {
                                adapter.notifyDataSetChanged();
                            }

                            if (inboxArray.size() != 0) {
                                scrollListViewToBottom();
                            }
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


    }


    void scrollListViewToBottom() {
        if (inboxArray != null)
            iListView.smoothScrollToPosition(inboxArray.size() - 1);
    }

    InboxListAdapter adapter;

    public class InboxListAdapter extends RecyclerView.Adapter<InboxListAdapter.AdsListVH> {
        Context context;

        public InboxListAdapter(Context context) {
            this.context = context;
        }

        public void addMoreAds(List<Message> moreAds) {
            int itemCountBeforeAdding = getItemCount();
            inboxArray.addAll(moreAds);
            notifyItemRangeInserted(itemCountBeforeAdding, inboxArray.size());
        }

        @NonNull
        @Override
        public AdsListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.cell_inbox, parent, false);
            return new AdsListVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdsListVH holder, int position) {


            // Get Parse object
            final Message iObj = inboxArray.get(position);

            holder.messTxt.setTypeface(Configs.titLight);
            holder.messTxt.setText(iObj.getMmessage());
            holder.smessTxt.setTypeface(Configs.titLight);
            holder.smessTxt.setText(iObj.getMmessage());


            // CELL WITH MESSAGE FROM CURRENT USER (SENDER) -------------------------------
            if (!iObj.getSender().getFirebaseID().matches(configs.getCurrentUser().getFirebaseID())) {
                holder.senderCell.setVisibility(View.VISIBLE);
                holder.receiverCell.setVisibility(View.GONE);

                // Get username
                holder.susernTxt.setTypeface(Configs.titSemibold);
                holder.susernTxt.setText("@" + iObj.getSender().getUsername());
                FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("Messages").child(iObj.getID()).child("readed").setValue(true);
                // Get date

                holder.sdateTxt.setTypeface(Configs.titLight);
                Date date = Calendar.getInstance().getTime();
                date.setTime(Long.valueOf(iObj.getCreatedAt()));
                holder.sdateTxt.setText(Configs.timeAgoSinceDate(date, context));

                // Get avatar


                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.logo1);
                requestOptions.error(R.drawable.logo1);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(InboxActivityTinderType.this).setDefaultRequestOptions(requestOptions)
                        .load(iObj.getSender().getAvatar().getImage1024()).into(holder.sanImage).clearOnDetach();


                // THIS MESSAGE HAS AN IMAGE ------------------------
                if (iObj.getImage() != null) {


                    holder.sinImage.setClipToOutline(true);
                    holder.sinImage.setVisibility(View.VISIBLE);
                    holder.smessTxt.setVisibility(View.GONE);

                    Glide.with(InboxActivityTinderType.this).setDefaultRequestOptions(requestOptions)
                            .load(iObj.getImage().getImage1024()).into(holder.sinImage).clearOnDetach();


                    holder.sinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(InboxActivityTinderType.this, FullScreenPreview.class);
                            Bundle extras = new Bundle();
                            extras.putString("imageName", iObj.getImage().getUrl());
                            i.putExtras(extras);
                            startActivity(i);
                        }
                    });

                } else {
                    holder.smessTxt.setVisibility(View.VISIBLE);
                    holder.sinImage.setVisibility(View.GONE);
                }


                // CELL WITH MESSAGE FROM RECEIVER ---------------------------------------------
            } else {


                if (iObj.getReceived()) {
                    holder.received.setVisibility(View.VISIBLE);
                    if (iObj.getReaded()) {
                        holder.readed.setVisibility(View.VISIBLE);
                        holder.received.setVisibility(View.GONE);
                    } else {
                        holder.readed.setVisibility(View.GONE);
                        holder.received.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.received.setVisibility(View.GONE);
                    if (iObj.getReaded()) {
                        holder.readed.setVisibility(View.VISIBLE);
                        holder.received.setVisibility(View.GONE);
                    } else {
                        holder.readed.setVisibility(View.GONE);
                        holder.received.setVisibility(View.GONE);
                    }
                }

                holder.senderCell.setVisibility(View.GONE);
                holder.receiverCell.setVisibility(View.VISIBLE);

                // Get username
                holder.usernTxt.setTypeface(Configs.titSemibold);
                holder.usernTxt.setText("@" + iObj.getReciver().getUsername());


                holder.dateTxt.setTypeface(Configs.titLight);
                Date date = Calendar.getInstance().getTime();
                date.setTime(Long.valueOf(iObj.getCreatedAt()));
                holder.dateTxt.setText(Configs.timeAgoSinceDate(date, context));


                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.logo1);
                requestOptions.error(R.drawable.logo1);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(InboxActivityTinderType.this).setDefaultRequestOptions(requestOptions)
                        .load(iObj.getSender().getAvatar().getImage1024()).into(holder.anImage).clearOnDetach();


                // THIS MESSAGE HAS AN IMAGE ------------------------
                if (iObj.getImage() != null) {

                    holder.inImage.setClipToOutline(true);
                    holder.inImage.setVisibility(View.VISIBLE);
                    holder.messTxt.setVisibility(View.GONE);

                    Glide.with(InboxActivityTinderType.this).setDefaultRequestOptions(requestOptions)
                            .load(iObj.getImage().getImage1024()).into(holder.inImage).clearOnDetach();


                    holder.inImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(InboxActivityTinderType.this, FullScreenPreview.class);
                            Bundle extras = new Bundle();
                            extras.putString("imageName", iObj.getImage().getUrl());
                            i.putExtras(extras);
                            startActivity(i);
                        }
                    });

                } else {
                    holder.messTxt.setVisibility(View.VISIBLE);
                    holder.inImage.setVisibility(View.GONE);
                }

            }


        }

        @Override
        public int getItemCount() {
            return inboxArray != null ? inboxArray.size() : 0;
        }

        public class AdsListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

            // Init cells layouts
            LinearLayout senderCell;
            RelativeLayout receiverCell;
            TextView usernTxt;
            TextView messTxt;
            ImageView inImage, sinImage;
            TextView dateTxt;
            ImageView anImage, sanImage;
            ImageView received, readed;
            TextView susernTxt;
            TextView smessTxt;
            TextView sdateTxt;

            public AdsListVH(View finalCell) {
                super(finalCell);
                itemView.setOnClickListener(this);
                senderCell = finalCell.findViewById(R.id.senderCell);
                receiverCell = finalCell.findViewById(R.id.receiverCell);
                usernTxt = finalCell.findViewById(R.id.rUsernameTxt);
                messTxt = finalCell.findViewById(R.id.rMessTxt);
                inImage = finalCell.findViewById(R.id.rImage);
                dateTxt = finalCell.findViewById(R.id.rDateTxt);
                anImage = finalCell.findViewById(R.id.rAvatarImg);
                susernTxt = finalCell.findViewById(R.id.sUsernameTxt);
                sdateTxt = finalCell.findViewById(R.id.sDateTxt);
                smessTxt = finalCell.findViewById(R.id.sMessTxt);
                sinImage = finalCell.findViewById(R.id.sImage);
                sanImage = finalCell.findViewById(R.id.sAvatarImg);
                received = finalCell.findViewById(R.id.received);
                readed = finalCell.findViewById(R.id.readed);

            }

            @Override
            public void onClick(View v) {

            }
        }

    }

    // MARK: - SEND MESSAGE ------------------------------------------------------------------------
    void sendMessage() {
        final MessageT iObj = new MessageT();

        // Save Message to Inbox Class
        iObj.setSender(currentUser);
        iObj.setReciver(userObj);
        iObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
        iObj.setID(FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("Messages").push().getKey());
        iObj.setMmessage(messageTxt.getText().toString());
        String messageStr = messageTxt.getText().toString();
        lastMessageStr = messageStr;
        messageTxt.setText("");


        // SEND AN IMAGE (if it exists) ------------------
        if (imageToSend != null) {
            Configs.showPD(getString(R.string.sending_image), InboxActivityTinderType.this);
            // Save image
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageToSend.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            lastMessageStr = "[Picture]";

            ImageObj imageObj = new ImageObj();
            imageObj.setID(Calendar.getInstance().getTimeInMillis() + "");

            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Files/" + imageObj.getID());
            UploadTask uploadTask = ref.putBytes(byteArray);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageObj.setUrl(downloadUri.toString());
                        Log.d("ImageUrlFirebase", downloadUri.toString());
                    }
                    iObj.setImage(imageObj);
                    FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("Messages").child(iObj.getID()).setValue(iObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dismisskeyboard();
                                imageToSend = null;
                                Log.i("log-", "IMAGE TO SEND: " + imageToSend);
                                // Call save LastMessage
                                saveLastMessageInChats();

                                scrollListViewToBottom();

                                HashMap<String, String> rawParameters = new HashMap<>();
                                final String pushMessage = "@" + configs.getCurrentUser().getUsername() + " | " + lastMessageStr;
                                rawParameters.put("body", pushMessage);
                                rawParameters.put("show", pushMessage);
                                rawParameters.put("page", "chattindertype");
                                rawParameters.put("chatid", inboxId2);
                                rawParameters.put("notificationid", iObj.getID());
                                rawParameters.put("title", getString(R.string.app_name));
                                FirebaseDatabase.getInstance().getReference().child("users").child(userObj.getFirebaseID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        TUser tUser = dataSnapshot.getValue(TUser.class);
                                        if (tUser != null)
                                            sendPushToSingleInstance(InboxActivityTinderType.this, rawParameters, tUser.getFCM());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                // error on Saving
                            } else {
                                Configs.hidePD();
                                Configs.simpleAlert(task.getException().getMessage(), InboxActivityTinderType.this);
                            }
                        }
                    });

                }
            });
        } else {
//            Configs.showPD("Sending...", InboxActivityTinderType.this);
            FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("Messages").child(iObj.getID()).setValue(iObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                    Configs.hidePD();
                    if (task.isSuccessful()) {
                        dismisskeyboard();
                        imageToSend = null;
                        Log.i("log-", "IMAGE TO SEND: " + imageToSend);

                        // Call save LastMessage
                        saveLastMessageInChats();
                        scrollListViewToBottom();

                        HashMap<String, String> rawParameters = new HashMap<>();
                        final String pushMessage = "@" + configs.getCurrentUser().getUsername() + " | '" + lastMessageStr;
                        rawParameters.put("body", pushMessage);
                        rawParameters.put("title", getString(R.string.app_name));
                        rawParameters.put("page", "chattindertype");
                        rawParameters.put("chatid", inboxId2);
                        rawParameters.put("notificationid", iObj.getID());
                        FirebaseDatabase.getInstance().getReference().child("users").child(userObj.getFirebaseID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                TUser tUser = dataSnapshot.getValue(TUser.class);
                                if (tUser != null)
                                    sendPushToSingleInstance(InboxActivityTinderType.this, rawParameters, tUser.getFCM());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        // error on Saving
                    } else {
                        Configs.hidePD();
                        Configs.simpleAlert(task.getException().getMessage(), InboxActivityTinderType.this);
                    }
                }
            });
        }


    }


    // MARK: - SAVE LAST MESSAGE IN CHATS CLASS -------------------------------------------------
    void saveLastMessageInChats() {

        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child(userObj.getFirebaseID()).setValue(true);
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("reads").setValue(true);
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("readr").setValue(false);
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("mmessage").setValue(lastMessageStr);
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("sender").setValue(currentUser);
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("receiver").setValue(userObj);
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("inboxID").setValue(inboxId2);
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(inboxId2).child("createdAt").setValue(Calendar.getInstance().getTimeInMillis() + "");
        FirebaseDatabase.getInstance().getReference().child("users").child(userObj.getFirebaseID()).child("bandage").setValue(true);

    }


    // IMAGE HANDLING METHODS ------------------------------------------------------------------------
    int CAMERA = 0;
    int GALLERY = 1;
    File file;

    private File createEmptyFile() {
        File storageDir = getCacheDir();
        File myDir = null;
        try {
            myDir = File.createTempFile("image", ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("log-", "Image file creation failure: " + e.getMessage());
            e.printStackTrace();
        }
        return myDir;
    }


    // OPEN CAMERA
    public void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = createEmptyFile();

        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                ".provider", file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA);
    }


    // OPEN GALLERY
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), GALLERY);
    }


    // IMAGE PICKED DELEGATE -----------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Bitmap bm = null;

            // Image from Camera
            if (requestCode == CAMERA) {

                try {
                    File f = file;
                    ExifInterface exif = new ExifInterface(f.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }
                    Log.i("log-", "ORIENTATION: " + orientation);

                    Matrix mat = new Matrix();
                    mat.postRotate(angle);

                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
                    bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                } catch (IOException | OutOfMemoryError e) {
                    Log.i("log-", e.getMessage());
                }


                // Image from Gallery
            } else if (requestCode == GALLERY) {
                try {

                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    bm = BitmapFactory.decodeFile(returnValue.get(0));
                    imageToSend = ExifUtil.rotateBitmap(returnValue.get(0), imageToSend);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            imageToSend = bm;

            // Send message with image
            sendMessage();
        }
    }
    //---------------------------------------------------------------------------------------------


    // RESET imageToSend TO NULL
    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageToSend = null;
    }


    // MARK: - DISMISS KEYBOARD
    public void dismisskeyboard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(messageTxt.getWindowToken(), 0);
        messageTxt.setText("");
    }

    public static void sendPushToSingleInstance(final Context activity, final HashMap dataValue, final String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/) {


        final String url = "https://fcm.googleapis.com/fcm/send";
        StringRequest myReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
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

    @Override
    protected void onResume() {
        configs.setShownotification(false);
        if (!configs.getSelectedChat().getSender().getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(configs.getSelectedChat().getInboxID()).child("reads").setValue(false);

        } else {
            FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(configs.getSelectedChat().getInboxID()).child("readr").setValue(false);
        }


        FirebaseDatabase.getInstance().getReference().removeEventListener(dots);
        super.onResume();
    }

    @Override
    protected void onStop() {
        configs.setShownotification(true);
        if (!configs.getSelectedChat().getSender().getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(configs.getSelectedChat().getInboxID()).child("reads").setValue(false);

        } else {
            FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(configs.getSelectedChat().getInboxID()).child("readr").setValue(false);
        }
        super.onStop();
    }


}
