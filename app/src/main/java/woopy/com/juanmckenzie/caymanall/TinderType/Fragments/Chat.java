package woopy.com.juanmckenzie.caymanall.TinderType.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Adapters.TinderChatAdapter;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.ChatsT;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends BaseFragment {


    public Chat() {
        // Required empty public constructor
    }

    private RecyclerView likesRV;
    List<ChatsT> chatsArray;
    ValueEventListener valueEventListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        likesRV = view.findViewById(R.id.fml_likes_rv);


        showLoading();
        configs = (Configs) getActivity().getApplicationContext();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsArray = new ArrayList<>();
                hideLoading();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChatsT chats = dataSnapshot1.getValue(ChatsT.class);

                    if (chats != null)
                        if (chats.getInboxID() != null)
                            if (chats.getInboxID().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                chatsArray.add(chats);
                }


                Collections.sort(chatsArray, new Comparator<ChatsT>() {
                    public int compare(ChatsT obj1, ChatsT obj2) {
                        return Long.valueOf(obj2.getCreatedAt()).compareTo(Long.valueOf(obj1.getCreatedAt())); // To compare integer values
                    }
                });
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("bandage").setValue(false);
                if (tinderChatAdapter == null) {
                    likesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    tinderChatAdapter = new TinderChatAdapter(chatsArray, getActivity());
                    likesRV.setAdapter(tinderChatAdapter);
                } else {
                    tinderChatAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        return view;
    }

    @Override
    public void onResume() {
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").addValueEventListener(valueEventListener);
        super.onResume();
    }

    @Override
    public void onPause() {
        FirebaseDatabase.getInstance().getReference().child("chatstindertype").removeEventListener(valueEventListener);
        super.onPause();
    }

    Configs configs;
    TinderChatAdapter tinderChatAdapter = null;


}
