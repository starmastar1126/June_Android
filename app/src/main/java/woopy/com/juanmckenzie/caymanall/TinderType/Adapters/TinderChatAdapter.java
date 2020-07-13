package woopy.com.juanmckenzie.caymanall.TinderType.Adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.User;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Fragments.Chat;
import woopy.com.juanmckenzie.caymanall.TinderType.InboxActivityTinderType;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.ChatsT;
import woopy.com.juanmckenzie.caymanall.TinderType.ProfileDetails;
import woopy.com.juanmckenzie.caymanall.resturents.Rv_adapter;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class TinderChatAdapter extends RecyclerView.Adapter<TinderChatAdapter.ListItem> {
    private List<ChatsT> items = new ArrayList<>();
    private Activity context;
    Dialog dialog;
    Configs configs;

    public TinderChatAdapter(List<ChatsT> items, android.app.Activity context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_chats, parent, false);
        ListItem myViewHolder = new ListItem(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListItem holder, final int position) {
        configs = (Configs) context.getApplicationContext();
        dialog = Configs.buildProgressLoadingDialog(context);
        holder.bind(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final ChatsT chatObj = items.get(position);
                String OrderuserID = "";
                if (chatObj.getSender().getFirebaseID().matches(configs.getCurrentUser().getFirebaseID())) {
                    OrderuserID = chatObj.getSender().getFirebaseID();
                } else {
                    OrderuserID = chatObj.getReceiver().getFirebaseID();
                }
                FirebaseDatabase.getInstance().getReference().child("users").child(OrderuserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dialog.dismiss();
                        User otherUser = dataSnapshot.getValue(User.class);
                        User currentUser = configs.getCurrentUser();
                        List<String> blockedUsers = otherUser.getHasBlocked();

                        // otherUser user has blocked you
                        if (blockedUsers.contains(currentUser.getFirebaseID())) {
                            Configs.simpleAlert(context.getString(R.string.sorry) + otherUser.getUsername() + context.getString(R.string.has_blocked_message1), context);

                            // You can chat with otherUser
                        } else {

                            Intent i = new Intent(context, InboxActivityTinderType.class);
                            configs.setSelectedChatID(chatObj.getInboxID());
                            configs.setSelectedChat(chatObj);
                            context.startActivity(i);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<ChatsT> getItems() {
        return items;
    }


    public void removeTopItem() {

        items.remove(0);
        notifyDataSetChanged();
    }

    public class ListItem extends RecyclerView.ViewHolder {
        TextView adTitleTxt, senderTxt, dateTxt, lastMessTxt;
        ImageView adImage;
        CardView cardchat;
        ValueEventListener valueEventListener;

        public ListItem(View itemView) {
            super(itemView);
            adTitleTxt = itemView.findViewById(R.id.cchatsAdTitleTxt);
            lastMessTxt = itemView.findViewById(R.id.cchatsLastMessTxt);
            dateTxt = itemView.findViewById(R.id.cchatsDateTxt);
            senderTxt = itemView.findViewById(R.id.cchatsSenderTxt);
            adImage = itemView.findViewById(R.id.cchatsAdImg);
            cardchat = itemView.findViewById(R.id.cardchat);
        }

        public void bind(ChatsT chatsObj) {


            adImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!chatsObj.getSender().getFirebaseID().matches(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Configs configs = (Configs) context.getApplicationContext();
                        configs.setSelectedUser(chatsObj.getSender());
                        context.startActivity(new Intent(context, ProfileDetails.class));
                    } else {
                        Configs configs = (Configs) context.getApplicationContext();
                        configs.setSelectedUser(chatsObj.getReceiver());
                        context.startActivity(new Intent(context, ProfileDetails.class));
                    }

                }
            });

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        ChatsT chatsObj = dataSnapshot.getValue(ChatsT.class);
                        bindagain(chatsObj);
                    } catch (Exception e) {
                        FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(chatsObj.getInboxID()).removeEventListener(valueEventListener);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            FirebaseDatabase.getInstance().getReference().child("chatstindertype").child(chatsObj.getInboxID()).addValueEventListener(valueEventListener);
            bindagain(chatsObj);

        }


        public void bindagain(ChatsT chatsObj) {

            if (!chatsObj.getSender().getFirebaseID().matches(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                if (chatsObj.getSender().getAvatar().getID().equals("facebook")) {
                    Glide.with(context)
                            .load(chatsObj.getSender().getAvatar().getUrl()).into(adImage).clearOnDetach();
                } else {
                    Glide.with(context)
                            .load(chatsObj.getSender().getAvatar().getImage512()).into(adImage).clearOnDetach();
                }


                if (chatsObj.getReadr()) {
                    cardchat.setBackgroundResource(R.drawable.read);
                    adTitleTxt.setTextColor(context.getResources().getColor(R.color.black));
                    senderTxt.setTextColor(context.getResources().getColor(R.color.black));
                    dateTxt.setTextColor(context.getResources().getColor(R.color.gray));
                    lastMessTxt.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                } else {
                    cardchat.setBackgroundResource(R.drawable.unread);
                    adTitleTxt.setTextColor(context.getResources().getColor(R.color.white));
                    senderTxt.setTextColor(context.getResources().getColor(R.color.white));
                    dateTxt.setTextColor(context.getResources().getColor(R.color.white));
                    lastMessTxt.setTextColor(context.getResources().getColor(R.color.white));
                    lastMessTxt.setTextColor(context.getResources().getColor(R.color.white));

                }
            } else {


                if (chatsObj.getReceiver().getAvatar().getID().equals("facebook")) {
                    Glide.with(context)
                            .load(chatsObj.getReceiver().getAvatar().getUrl()).into(adImage).clearOnDetach();
                } else {
                    Glide.with(context)
                            .load(chatsObj.getReceiver().getAvatar().getImage512()).into(adImage).clearOnDetach();
                }

                if (chatsObj.getReadr()) {
                    cardchat.setBackgroundResource(R.drawable.unread);
                    adTitleTxt.setTextColor(context.getResources().getColor(R.color.white));
                    senderTxt.setTextColor(context.getResources().getColor(R.color.white));
                    dateTxt.setTextColor(context.getResources().getColor(R.color.white));
                    lastMessTxt.setTextColor(context.getResources().getColor(R.color.white));
                    lastMessTxt.setTextColor(context.getResources().getColor(R.color.white));


                } else {
                    cardchat.setBackgroundResource(R.drawable.read);
                    adTitleTxt.setTextColor(context.getResources().getColor(R.color.black));
                    senderTxt.setTextColor(context.getResources().getColor(R.color.black));
                    dateTxt.setTextColor(context.getResources().getColor(R.color.gray));
                    lastMessTxt.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                }
            }


            if (!chatsObj.getSender().getFirebaseID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                adTitleTxt.setTypeface(Configs.titSemibold);
                adTitleTxt.setText(chatsObj.getSender().getUsername());
            } else {
                adTitleTxt.setTypeface(Configs.titSemibold);
                adTitleTxt.setText(chatsObj.getReceiver().getUsername());
            }

            senderTxt.setTypeface(Configs.titRegular);

            if (chatsObj.getSender().getFirebaseID().matches(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                senderTxt.setText(context.getString(R.string.you_wrote));
                if (chatsObj.getMmessage().equals(context.getString(R.string.you_have_found_a_match))) {
                    senderTxt.setText(context.getString(R.string.congratulations));
                }
            } else {
                senderTxt.setText("@" + chatsObj.getSender().getUsername());
                if (chatsObj.getMmessage().equals(context.getString(R.string.you_have_found_a_match))) {
                    senderTxt.setText(context.getString(R.string.congratulations));
                }
            }
            // Get date
            dateTxt.setTypeface(Configs.titRegular);

            Date date = Calendar.getInstance().getTime();
            date.setTime(Long.valueOf(chatsObj.getCreatedAt()));
            dateTxt.setText(Configs.timeAgoSinceDate(date, context));

            // Get last Message
            lastMessTxt.setTypeface(Configs.titRegular);
            lastMessTxt.setText(chatsObj.getMmessage());
        }
    }
}