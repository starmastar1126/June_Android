package woopy.com.juanmckenzie.caymanall.formsjobs.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lib.kingja.switchbutton.SwitchMultiButton;
import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ads.activities.EventDetails;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.databinding.ViewFragmentApplicantInformationBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewApplicantInformation extends BaseFragment {


    public ViewApplicantInformation() {
        // Required empty public constructor
    }

    Configs configs;

    Formobject formobject;
    ViewFragmentApplicantInformationBinding binding;
    ImageView profile;

    EditText firstname, lastname, middlename, streetaddress, appartmentunit, city, state, zipcode, phonenumber, emai, dateavailable, desiresallery,
            possitionalliedfor;
    SwitchMultiButton question1from1, question2from1, question3from1, question4from1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.view_fragment_applicant_information, container, false);
        formobject = new Formobject();
        View view = binding.getRoot();

        configs = (Configs) getActivity().getApplicationContext();

        profile = view.findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FullScreenPreview.class);
                Bundle extras = new Bundle();
                extras.putString("imageName", formobject.getProfile().getUrl());
                i.putExtras(extras);
                startActivity(i);
            }
        });

        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        middlename = view.findViewById(R.id.middlename);
        streetaddress = view.findViewById(R.id.StreetAddress);
        appartmentunit = view.findViewById(R.id.ApartmentUnit);
        city = view.findViewById(R.id.City);
        zipcode = view.findViewById(R.id.ZIPCode);
        state = view.findViewById(R.id.State);
        phonenumber = view.findViewById(R.id.PhoneNumber);
        emai = view.findViewById(R.id.Email);
        dateavailable = view.findViewById(R.id.DateAvailable);
        desiresallery = view.findViewById(R.id.DesiredSalary);
        possitionalliedfor = view.findViewById(R.id.PositionAppliedfor);


        question1from1 = view.findViewById(R.id.question1from1);
        question2from1 = view.findViewById(R.id.question2from1);
        question3from1 = view.findViewById(R.id.question3from1);
        question4from1 = view.findViewById(R.id.question4from1);

//        showLoading();
        formquery();


        return view;

    }

    public void formquery() {
        FirebaseDatabase.getInstance().getReference().child("ads").child(configs.getSelectedform().getAddId()).child("applications").child(configs.getSelectedform().getUser().getFirebaseID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                hideLoading();
                Formobject formobjectonline = dataSnapshot.getValue(Formobject.class);
                if (formobjectonline != null) {
                    binding.setJobobj(formobjectonline);
                    question1from1.setSelectedTab(formobjectonline.getQuestion1from1());
                    question2from1.setSelectedTab(formobjectonline.getQuestion2from1());
                    question3from1.setSelectedTab(formobjectonline.getQuestion3from1());
                    question4from1.setSelectedTab(formobjectonline.getQuestion4from1());
                    formobject = formobjectonline;
                } else {

                    binding.setJobobj(formobject);
                    getActivity().finish();
                    return;

                }


                Glide.with(getActivity())
                        .asBitmap()
                        .load(formobject.getProfile().getImage1024())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                profile.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                profile.setImageDrawable(errorDrawable);
                                super.onLoadFailed(errorDrawable);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public Boolean CanMove() {

        if (firstname.getText().toString().equals("") || lastname.getText().toString().equals("") || middlename.getText().toString().equals("")
                || streetaddress.getText().toString().equals("") || appartmentunit.getText().toString().equals("") || city.getText().toString().equals("")
                || state.getText().toString().equals("") || zipcode.getText().toString().equals("") || phonenumber.getText().toString().equals("")
                || emai.getText().toString().equals("") || dateavailable.getText().toString().equals("") || desiresallery.getText().toString().equals("") ||
                possitionalliedfor.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }


}
