package woopy.com.juanmckenzie.caymanall.formsjobs.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class Disclaimer extends BaseFragment {


    public Disclaimer() {
        // Required empty public constructor
    }


    @Override
    protected void showLoading() {
        super.showLoading();
    }


    Configs configs;
    Formobject formobject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_disclaimer, container, false);


        showLoading();
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideLoading();
                formobject = dataSnapshot.getValue(Formobject.class);
                if (formobject != null) {
                    if (formobject.getCompelted()) {
                        view.findViewById(R.id.completed).setVisibility(View.GONE);
                        view.findViewById(R.id.needtohide).setVisibility(View.GONE);
                        view.findViewById(R.id.needtoshoe).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.apply).setVisibility(View.VISIBLE);
                    } else {
                        view.findViewById(R.id.completed).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.needtohide).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.needtoshoe).setVisibility(View.GONE);
                        view.findViewById(R.id.apply).setVisibility(View.GONE);
                    }
                } else {
                    view.findViewById(R.id.completed).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.needtohide).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.needtoshoe).setVisibility(View.GONE);
                    view.findViewById(R.id.apply).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        view.findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configs = (Configs) getActivity().getApplicationContext();
                showLoading();
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        formobject = dataSnapshot.getValue(Formobject.class);
                        formobject.setUser(configs.getCurrentUser());
                        formobject.setAddId(configs.getSelectedAd().getID());
                        formobject.setCreatedat(Calendar.getInstance().getTimeInMillis() + "");
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("applications").child(configs.getSelectedAd().getID()).setValue(formobject);

                        FirebaseDatabase.getInstance().getReference().child("ads").child(configs.getSelectedAd().getID()).child("applications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(formobject).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideLoading();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(getString(R.string.your_form_has_been_submitted))
                                        .setTitle(R.string.app_name)
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ((FormsActivity) getActivity()).sendFCM(configs.getSelectedAd().getSellerPointer().getFirebaseID(), configs.getCurrentUser().getUsername(), configs.getSelectedAd().getTitle(), configs.getSelectedAd().getID());
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.setIcon(R.drawable.logo);
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        view.findViewById(R.id.completed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoading();
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").child("compelted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideLoading();
                    }
                });
                view.findViewById(R.id.completed).setVisibility(View.GONE);
                view.findViewById(R.id.needtohide).setVisibility(View.GONE);
                view.findViewById(R.id.needtoshoe).setVisibility(View.VISIBLE);
                view.findViewById(R.id.apply).setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    public Boolean CanMove() {
        return false;
    }

    public void Save() {

    }
}
