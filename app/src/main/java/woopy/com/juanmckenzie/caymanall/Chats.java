package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class Chats extends AppCompatActivity {


    /* Variables */
    List<woopy.com.juanmckenzie.caymanall.Objects.Chats> chatsArray;


    @Override
    protected void onStart() {
        super.onStart();

        // Call query
        queryChats();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // MARK: - BACK BUTTON ------------------------------------
        Button backButt = findViewById(R.id.chBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Init AdMob banner
        AdView mAdView = findViewById(R.id.admobBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }// end onCreate()


    Configs configs;

    // MARK: - QUERY CHATS ----------------------------------------------------------------------
    void queryChats() {

        Configs.showPD(getString(R.string.please_wait_3), Chats.this);

        configs = (Configs) getApplicationContext();
        FirebaseDatabase.getInstance().getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsArray = new ArrayList<>();
                Configs.hidePD();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    woopy.com.juanmckenzie.caymanall.Objects.Chats chats = dataSnapshot1.getValue(woopy.com.juanmckenzie.caymanall.Objects.Chats.class);


                    if (chats.getInboxID() != null)
                        if (chats.getInboxID().contains(configs.getCurrentUser().getFirebaseID()))
                            chatsArray.add(chats);
                }


                // CUSTOM LIST ADAPTER
                class ListAdapter extends BaseAdapter {
                    private Context context;

                    public ListAdapter(Context context, List<woopy.com.juanmckenzie.caymanall.Objects.Chats> objects) {
                        super();
                        this.context = context;
                    }

                    // CONFIGURE CELL
                    @Override
                    public View getView(int position, View cell, ViewGroup parent) {
                        if (cell == null) {
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            assert inflater != null;
                            cell = inflater.inflate(R.layout.cell_chats, null);
                        }
                        final View finalCell = cell;


                        // Get Parse object
                        final woopy.com.juanmckenzie.caymanall.Objects.Chats chatsObj = chatsArray.get(position);


                        // Get Ad Image1
                        final ImageView adImage = finalCell.findViewById(R.id.cchatsAdImg);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.logo1);
                        requestOptions.error(R.drawable.logo1);
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);

                        Glide.with(Chats.this).setDefaultRequestOptions(requestOptions)
                                .load(chatsObj.getAdPointer().getImage1()).into(adImage).clearOnDetach();

                        // Get Ad Title
                        TextView adTitleTxt = finalCell.findViewById(R.id.cchatsAdTitleTxt);
                        adTitleTxt.setTypeface(Configs.titSemibold);
                        adTitleTxt.setText(chatsObj.getAdPointer().getTitle());


                        // Get Sender's username
                        TextView senderTxt = finalCell.findViewById(R.id.cchatsSenderTxt);
                        senderTxt.setTypeface(Configs.titRegular);

                        if (chatsObj.getSender().getFirebaseID().matches(configs.getCurrentUser().getFirebaseID())) {
                            senderTxt.setText(getString(R.string.you_wrote));
                        } else {
                            senderTxt.setText("@" + chatsObj.getSender().getUsername());
                        }

                        // Get date
                        TextView dateTxt = finalCell.findViewById(R.id.cchatsDateTxt);
                        dateTxt.setTypeface(Configs.titRegular);
                        Date date = Calendar.getInstance().getTime();
                        date.setTime(Long.valueOf(chatsObj.getCreatedAt()));
                        dateTxt.setText(Configs.timeAgoSinceDate(date, getApplicationContext()));

                        // Get last Message
                        TextView lastMessTxt = finalCell.findViewById(R.id.cchatsLastMessTxt);
                        lastMessTxt.setTypeface(Configs.titRegular);
                        lastMessTxt.setText(chatsObj.getMmessage());


                        return cell;
                    }

                    @Override
                    public int getCount() {
                        return chatsArray.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return chatsArray.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }
                }


                // Init ListView and set its adapter
                ListView aList = findViewById(R.id.chatsListView);
                aList.setAdapter(new ListAdapter(Chats.this, chatsArray));
                aList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        Configs.showPD(getString(R.string.please_wait_4), Chats.this);
                        final woopy.com.juanmckenzie.caymanall.Objects.Chats chatObj = chatsArray.get(position);
                        String OrderuserID = "";
                        if (!chatObj.getSender().getFirebaseID().matches(configs.getCurrentUser().getFirebaseID())) {
                            OrderuserID = chatObj.getSender().getFirebaseID();
                        } else {
                            OrderuserID = chatObj.getReceiver().getFirebaseID();
                        }
                        FirebaseDatabase.getInstance().getReference().child("users").child(OrderuserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Configs.hidePD();
                                User otherUser = dataSnapshot.getValue(User.class);
                                User currentUser = configs.getCurrentUser();
                                List<String> blockedUsers = otherUser.getHasBlocked();

                                // otherUser user has blocked you
                                if (blockedUsers.contains(currentUser.getFirebaseID())) {
                                    Configs.simpleAlert(getString(R.string.sorry) + otherUser.getUsername() + getString(R.string.has_blocked1), Chats.this);

                                    // You can chat with otherUser
                                } else {
                                    Intent i = new Intent(Chats.this, InboxActivity.class);
                                    configs.setSelectedChatID(chatObj.getInboxID());
                                    configs.setSelectedAd(chatObj.getAdPointer());
                                    startActivity(i);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Configs.hidePD();
                            }
                        });


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}//@end
