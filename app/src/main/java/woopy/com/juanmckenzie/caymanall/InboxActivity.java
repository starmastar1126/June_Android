package woopy.com.juanmckenzie.caymanall;

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
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.Chats;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.Inbox;
import woopy.com.juanmckenzie.caymanall.Objects.Message;
import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.TinderType.InboxActivityTinderType;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdDetailsActivity;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBanners;
import woopy.com.juanmckenzie.caymanall.filters.models.ReportType;
import woopy.com.juanmckenzie.caymanall.home.adapters.AdsListAdapter;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.MarshMallowPermission;

public class InboxActivity extends AppCompatActivity {

    /* Views */
    Button optionsButt, sendButt, uploadPicButt;
    TextView usernameTxt, adTitleTxt, adPriceTxt;
    EditText messageTxt;
    RecyclerView iListView;
    ImageView imgPreview;

    /* Variables */
    User userObj;
    Ads adObj;
    List<Message> inboxArray;
    List<Message> chatsArray;
    String lastMessageStr = null;
    Timer refreshTimerForInboxQuery = new Timer();
    Bitmap imageToSend = null;
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    Configs configs;

    List<Banner> banners = new ArrayList<>();
    String inboxId2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_activity);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Init views
        optionsButt = findViewById(R.id.inOptionsButt);
        uploadPicButt = findViewById(R.id.inUploadPicButt);
        sendButt = findViewById(R.id.inSendButt);
        sendButt.setTypeface(Configs.titSemibold);
        usernameTxt = findViewById(R.id.inUsernameTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        adTitleTxt = findViewById(R.id.inAdTitleTxt);
        adTitleTxt.setTypeface(Configs.titSemibold);
        adPriceTxt = findViewById(R.id.inAdPriceTxt);
        adPriceTxt.setTypeface(Configs.titRegular);
        messageTxt = findViewById(R.id.inMessageTxt);
        messageTxt.setTypeface(Configs.titRegular);

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
                SliderAdapterBanners adapter = new SliderAdapterBanners(InboxActivity.this, banners);
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


        // Get userObj
        userObj = configs.getSelectedAd().getSellerPointer();

        // Get adObj
        adObj = configs.getSelectedAd();
        inboxId2 = configs.getSelectedChatID();
        // Call query
        Configs.showPD(getString(R.string.please_wait), InboxActivity.this);
        queryInbox();


        // Get User's username
        usernameTxt.setText("@" + userObj.getUsername());

        // Get AD details
        adTitleTxt.setText(adObj.getTitle());
        adPriceTxt.setText(adObj.getCurrency() + String.valueOf(adObj.getPrice()));

        // Get Image
        final ImageView adImage = findViewById(R.id.inAdImg);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).setDefaultRequestOptions(requestOptions)
                .load(adObj.getImage1()).into(adImage).clearOnDetach();


        // TAP AD BOX -> SHOW AD DETAILS
        RelativeLayout adBox = findViewById(R.id.inAdBoxLayout);
        adBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configs.setSelectedAd(adObj);
                Intent i = new Intent(InboxActivity.this, AdDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString(AdDetailsActivity.AD_OBJ_ID_KEY, adObj.getID());
                i.putExtras(extras);
                startActivity(i);
            }
        });

        // Start refresh Timer for Inbox query
        startRefreshTimer();

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
                Pix.start(InboxActivity.this, options);
            }
        });


        // MARK: - SEND MESSAGE BUTTON ---------------------------------------------
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageTxt.getText().toString().matches("")) {
                    Configs.simpleAlert(getString(R.string.your_must_type_something), InboxActivity.this);
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


                AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
                final String finalBlockTitle = blockTitle;
                alert.setMessage(getString(R.string.select_option))
                        .setTitle(R.string.app_name)

                        // REPORT USER -------------------------------------------------------------
                        .setPositiveButton(getString(R.string.report_user), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                // Pass objectID to the other Activity
                                Intent in = new Intent(InboxActivity.this, ReportAdOrUserActivity.class);
                                Bundle extras = new Bundle();
                                configs.setSelectedAd(adObj);
                                extras.putString(ReportAdOrUserActivity.AD_OBJECT_ID_EXTRA_KEY, adObj.getID());
                                extras.putString(ReportAdOrUserActivity.USER_OBJECT_ID_EXTRA_KEY, userObj.getFirebaseID());
                                extras.putString(ReportAdOrUserActivity.REPORT_TYPE_EXTRA_KEY, ReportType.USER.getValue());
                                in.putExtras(extras);
                                startActivity(in);
                            }
                        })


                        // BLOCK/UNBLOCK USER ------------------------------------------------------
                        .setNegativeButton(blockTitle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Block User
                                if (finalBlockTitle.matches(getString(R.string.block_user))) {
                                    hasBlocked.add(userObj.getFirebaseID());
                                    currUser.setHasBlocked(hasBlocked);

                                    FirebaseDatabase.getInstance().getReference().child("users").child(userObj.getFirebaseID()).child("hasBlocked").setValue(currUser.getHasBlocked()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
                                            alert.setMessage(getString(R.string.you_bocked_this_user) + userObj.getUsername())
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
                                            Configs.simpleAlert(getString(R.string.you_are_blocked) + userObj.getUsername(), InboxActivity.this);

                                        }
                                    });

                                }
                            }
                        })


                        // DELETE CHAT -------------------------------------------------------------
                        .setNeutralButton(getString(R.string.delete_chat), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
                                alert.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_chat) + userObj.getUsername() + getString(R.string.will_not_be_able_to_see_these_messages))
                                        .setTitle(R.string.app_name)
                                        .setPositiveButton(getString(R.string.delete_chat), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

//                                                // Delete all Inbox messages
//                                                for (int j = 0; j < inboxArray.size(); j++) {
//                                                    ParseObject iObj = new ParseObject(Configs.INBOX_CLASS_NAME);
//                                                    iObj = inboxArray.get(j);
//                                                    iObj.deleteInBackground(new DeleteCallback() {
//                                                        @Override
//                                                        public void done(ParseException e) {
//                                                            Log.i("log-", "DELETED INBOX ITEM: ");
//                                                        }
//                                                    });
//                                                }


                                                // Delete Chat in Chats class
                                                String inboxId2 = userObj.getFirebaseID() + configs.getCurrentUser().getFirebaseID() + adObj.getID();

                                                FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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


    // TIMER THAT RECALLS THE QUERY FOR INBOX MESSAGES
    void startRefreshTimer() {
        int delay = 15000;
        refreshTimerForInboxQuery.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Call query
                        queryInbox();

                    }
                });
            }
        }, delay, delay);
    }


    Chats chatObj;
    InboxListAdapter adapter;

    // MARK: - QUERY INBOX --------------------------------------------------------
    void queryInbox() {


        FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Configs.hidePD();
                chatObj = dataSnapshot.getValue(Chats.class);
                if (chatObj == null) {
                    chatObj = new Chats();
                    chatObj.setSender(configs.getCurrentUser());
                    chatObj.setReceiver(userObj);
                    chatObj.setAdPointer(adObj);
                    chatObj.setInboxID(inboxId2);
                    chatObj.setMmessage(messageTxt.getText().toString());


                } else {

                    FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("Messages").addValueEventListener(new ValueEventListener() {
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
                                        new LinearLayoutManager(InboxActivity.this);
                                layoutManager.setSmoothScrollbarEnabled(true);
                                iListView.setLayoutManager(layoutManager);
                                adapter = new InboxListAdapter(InboxActivity.this);
                                iListView.setAdapter(adapter);

                            } else {
                                adapter.notifyDataSetChanged();
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

//    // INBOX LIST ADAPTER ----------------------------------------------------------------------
//    class InboxListAdapter extends BaseAdapter {
//        private Context context;
//
//        public InboxListAdapter(Context context, List<Message> objects) {
//            super();
//            this.context = context;
//        }
//
//
//        // CONFIGURE CELL
//        @Override
//        public View getView(int position, View cell, ViewGroup parent) {
//            if (cell == null) {
//                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                assert inflater != null;
//                cell = inflater.inflate(R.layout.cell_inbox, null);
//            }
//            final View finalCell = cell;
//
//            // Get Parse object
//            final Message iObj = inboxArray.get(position);
//
//
//            // Init cells layouts
//            final RelativeLayout senderCell = finalCell.findViewById(R.id.senderCell);
//            final RelativeLayout receiverCell = finalCell.findViewById(R.id.receiverCell);
//
//
//            // CELL WITH MESSAGE FROM CURRENT USER (SENDER) -------------------------------
//            if (!iObj.getSender().getFirebaseID().matches(configs.getCurrentUser().getFirebaseID())) {
//                senderCell.setVisibility(View.VISIBLE);
//                receiverCell.setVisibility(View.GONE);
//
//                // Get username
//                TextView usernTxt = finalCell.findViewById(R.id.sUsernameTxt);
//                usernTxt.setTypeface(Configs.titSemibold);
//                usernTxt.setText("@" + iObj.getSender().getUsername());
//
//                // Get message
//                TextView messTxt = finalCell.findViewById(R.id.sMessTxt);
//                messTxt.setTypeface(Configs.titLight);
//                messTxt.setText(iObj.getMmessage());
//
//                // Get date
//                TextView dateTxt = finalCell.findViewById(R.id.sDateTxt);
//                dateTxt.setTypeface(Configs.titLight);
//                Date date = Calendar.getInstance().getTime();
//                date.setTime(Long.valueOf(iObj.getCreatedAt()));
//                dateTxt.setText(Configs.timeAgoSinceDate(date));
//
//                // Get avatar
//                final ImageView anImage = finalCell.findViewById(R.id.sAvatarImg);
//
//                final RequestOptions requestOptions = new RequestOptions();
//                requestOptions.placeholder(R.drawable.logo1);
//                requestOptions.error(R.drawable.logo1);
//                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//                Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
//                        .load(iObj.getSender().getAvatar()).into(anImage).clearOnDetach();
//
//
//                // THIS MESSAGE HAS AN IMAGE ------------------------
//                if (iObj.getImage() != null) {
//
//                    final ImageView inImage = finalCell.findViewById(R.id.sImage);
//                    inImage.setClipToOutline(true);
//                    inImage.setVisibility(View.VISIBLE);
//                    messTxt.setVisibility(View.INVISIBLE);
//
//                    Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
//                            .load(iObj.getImage()).into(inImage).clearOnDetach();
//
//
//                    inImage.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent i = new Intent(InboxActivity.this, FullScreenPreview.class);
//                            Bundle extras = new Bundle();
//                            extras.putString("imageName", iObj.getImage());
//                            extras.putString("objectID", adObj.getID());
//                            i.putExtras(extras);
//                            startActivity(i);
//                        }
//                    });
//
//                } else {
//                    final ImageView inImage = finalCell.findViewById(R.id.sImage);
//                    inImage.setVisibility(View.GONE);
//                }
//
//
//                // CELL WITH MESSAGE FROM RECEIVER ---------------------------------------------
//            } else {
//                senderCell.setVisibility(View.GONE);
//                receiverCell.setVisibility(View.VISIBLE);
//
//                // Get username
//                TextView usernTxt = finalCell.findViewById(R.id.rUsernameTxt);
//                usernTxt.setTypeface(Configs.titSemibold);
//                usernTxt.setText("@" + iObj.getReciver().getUsername());
//
//                // Get message
//                TextView messTxt = finalCell.findViewById(R.id.rMessTxt);
//                messTxt.setTypeface(Configs.titLight);
//                messTxt.setText(iObj.getMmessage());
//
//                // Get date
//                TextView dateTxt = finalCell.findViewById(R.id.rDateTxt);
//                dateTxt.setTypeface(Configs.titLight);
//                Date date = Calendar.getInstance().getTime();
//                date.setTime(Long.valueOf(iObj.getCreatedAt()));
//                dateTxt.setText(Configs.timeAgoSinceDate(date));
//
//
//                final ImageView anImage = finalCell.findViewById(R.id.rAvatarImg);
//
//                final RequestOptions requestOptions = new RequestOptions();
//                requestOptions.placeholder(R.drawable.logo1);
//                requestOptions.error(R.drawable.logo1);
//                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//                Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
//                        .load(iObj.getSender().getAvatar()).into(anImage).clearOnDetach();
//
//
//                // THIS MESSAGE HAS AN IMAGE ------------------------
//                if (iObj.getImage() != null) {
//
//                    final ImageView inImage = finalCell.findViewById(R.id.rImage);
//                    inImage.setClipToOutline(true);
//                    inImage.setVisibility(View.VISIBLE);
//                    messTxt.setVisibility(View.INVISIBLE);
//
//                    Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
//                            .load(iObj.getImage()).into(inImage).clearOnDetach();
//
//
//                    inImage.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent i = new Intent(InboxActivity.this, FullScreenPreview.class);
//                            Bundle extras = new Bundle();
//                            extras.putString("imageName", iObj.getImage());
//                            extras.putString("objectID", adObj.getID());
//                            i.putExtras(extras);
//                            startActivity(i);
//                        }
//                    });
//
//                } else {
//                    final ImageView inImage = finalCell.findViewById(R.id.rImage);
//                    inImage.setVisibility(View.GONE);
//                }
//
//            }
//
//
//            return cell;
//        }
//
//        @Override
//        public int getCount() {
//            return inboxArray.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return inboxArray.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//    }


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
                holder.sdateTxt.setText(Configs.timeAgoSinceDate(date, getApplicationContext()));

                // Get avatar


                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.logo1);
                requestOptions.error(R.drawable.logo1);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
                        .load(iObj.getSender().getAvatar().getImage1024()).into(holder.sanImage).clearOnDetach();


                // THIS MESSAGE HAS AN IMAGE ------------------------
                if (iObj.getImage() != null) {


                    holder.sinImage.setClipToOutline(true);
                    holder.sinImage.setVisibility(View.VISIBLE);
                    holder.smessTxt.setVisibility(View.GONE);

                    Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
                            .load(iObj.getImage().getImage1024()).into(holder.sinImage).clearOnDetach();


                    holder.sinImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(InboxActivity.this, FullScreenPreview.class);
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
                holder.dateTxt.setText(Configs.timeAgoSinceDate(date, getApplicationContext()));


                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.logo1);
                requestOptions.error(R.drawable.logo1);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
                        .load(iObj.getSender().getAvatar().getImage1024()).into(holder.anImage).clearOnDetach();


                // THIS MESSAGE HAS AN IMAGE ------------------------
                if (iObj.getImage() != null) {

                    holder.inImage.setClipToOutline(true);
                    holder.inImage.setVisibility(View.VISIBLE);
                    holder.messTxt.setVisibility(View.GONE);

                    Glide.with(InboxActivity.this).setDefaultRequestOptions(requestOptions)
                            .load(iObj.getImage().getImage1024()).into(holder.inImage).clearOnDetach();


                    holder.inImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(InboxActivity.this, FullScreenPreview.class);
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
        final Message iObj = new Message();
        final User currentUser = configs.getCurrentUser();


        // Save Message to Inbox Class
        iObj.setSender(currentUser);
        iObj.setReciver(userObj);
        iObj.setCreatedAt(Calendar.getInstance().getTimeInMillis() + "");
        iObj.setID(FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("Messages").push().getKey());
        iObj.setMmessage(messageTxt.getText().toString());
        String messageStr = messageTxt.getText().toString();
        lastMessageStr = messageStr;


        // SEND AN IMAGE (if it exists) ------------------
        if (imageToSend != null) {
            Configs.showPD(getString(R.string.sending_image), InboxActivity.this);

            // Save image
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageToSend.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            lastMessageStr = "[Picture]";


            ImageObj imageObj = new ImageObj();
            imageObj.setID(iObj.getID() + "");
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Files/" + iObj.getID());
            UploadTask uploadTask = ref.putBytes(byteArray);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageObj.setUrl(downloadUri.toString());
                        iObj.setImage(imageObj);
                        Log.d("ImageUrlFirebase", downloadUri.toString());
                    }

                    FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("Messages").child(iObj.getID()).setValue(iObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dismisskeyboard();
                                imageToSend = null;
                                Log.i("log-", "IMAGE TO SEND: " + imageToSend);

                                // Call save LastMessage
                                saveLastMessageInChats();

                                iListView.invalidateItemDecorations();
                                iListView.refreshDrawableState();
                                scrollListViewToBottom();

                                HashMap<String, String> rawParameters = new HashMap<>();
                                final String pushMessage = "@" + configs.getCurrentUser().getUsername() + " | '" + adObj.getTitle() + "':\n" + lastMessageStr;
                                rawParameters.put("body", pushMessage);
                                rawParameters.put("title", getString(R.string.app_name));


                                rawParameters.put("page", "chattindertype1");
                                rawParameters.put("chatid", inboxId2);
                                rawParameters.put("addid", adObj.getID());


                                sendPushToSingleInstance(InboxActivity.this, rawParameters, userObj.getFCM());
                                // error on Saving
                            } else {
                                Configs.hidePD();
                                Configs.simpleAlert(task.getException().getMessage(), InboxActivity.this);
                            }
                        }
                    });

                }
            });
        } else {
            Configs.showPD(getString(R.string.sending), InboxActivity.this);
            FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("Messages").child(iObj.getID()).setValue(iObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Configs.hidePD();
                    if (task.isSuccessful()) {
                        dismisskeyboard();
                        imageToSend = null;
                        Log.i("log-", "IMAGE TO SEND: " + imageToSend);

                        // Call save LastMessage
                        saveLastMessageInChats();

                        iListView.invalidateItemDecorations();
                        iListView.refreshDrawableState();
                        scrollListViewToBottom();

                        HashMap<String, String> rawParameters = new HashMap<>();
                        final String pushMessage = "@" + configs.getCurrentUser().getUsername() + " | '" + adObj.getTitle() + "':\n" + lastMessageStr;
                        rawParameters.put("body", pushMessage);
                        rawParameters.put("title", getString(R.string.app_name));


                        rawParameters.put("page", "chattindertype1");
                        rawParameters.put("chatid", inboxId2);
                        rawParameters.put("addid", adObj.getID());

                        sendPushToSingleInstance(InboxActivity.this, rawParameters, userObj.getFCM());

                        // error on Saving
                    } else {
                        Configs.hidePD();
                        Configs.simpleAlert(task.getException().getMessage(), InboxActivity.this);
                    }
                }
            });
        }


    }


    // MARK: - SAVE LAST MESSAGE IN CHATS CLASS -------------------------------------------------
    void saveLastMessageInChats() {

        FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("mmessage").setValue(lastMessageStr);
        FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("sender").setValue(configs.getCurrentUser());
        FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("receiver").setValue(userObj);
        FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("inboxID").setValue(inboxId2);
        FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("adPointer").setValue(adObj);
        FirebaseDatabase.getInstance().getReference().child("chats").child(inboxId2).child("createdAt").setValue(Calendar.getInstance().getTimeInMillis() + "");


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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageTxt.getWindowToken(), 0);
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


}//@end
