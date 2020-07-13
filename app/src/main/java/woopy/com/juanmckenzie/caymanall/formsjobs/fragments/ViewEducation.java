package woopy.com.juanmckenzie.caymanall.formsjobs.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lib.kingja.switchbutton.SwitchMultiButton;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.databinding.ViewFragmentEducationBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEducation extends BaseFragment {


    public ViewEducation() {
        // Required empty public constructor
    }


    Formobject formobject;
    ViewFragmentEducationBinding binding;

    EditText hname, haddress, hfrom, hto, hdeploma, cname, caddress, cfrom, cto, cdeploma, oname, oaddress, ofrom, oto, odeploma;
    SwitchMultiButton question1from2, question2from2, question3from2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.view_fragment_education, container, false);
        formobject = new Formobject();


        View view = binding.getRoot();


        hname = view.findViewById(R.id.hname);
        haddress = view.findViewById(R.id.haddress);
        hfrom = view.findViewById(R.id.hfrom);
        hto = view.findViewById(R.id.hto);
        hdeploma = view.findViewById(R.id.hdiploma);


        cname = view.findViewById(R.id.cname);
        caddress = view.findViewById(R.id.caddresss);
        cfrom = view.findViewById(R.id.cfrom);
        cto = view.findViewById(R.id.cto);
        cdeploma = view.findViewById(R.id.cdeploma);


        oname = view.findViewById(R.id.oname);
        oaddress = view.findViewById(R.id.oaddress);
        ofrom = view.findViewById(R.id.ofrom);
        oto = view.findViewById(R.id.oto);
        odeploma = view.findViewById(R.id.odeploma);

        question1from2 = view.findViewById(R.id.question1form2);
        question2from2 = view.findViewById(R.id.question2from2);
        question3from2 = view.findViewById(R.id.question3form2);


//        showLoading();
        formquery();

        return view;


    }

    public void formquery() {
        Configs configs;
        configs = (Configs) getActivity().getApplicationContext();
        FirebaseDatabase.getInstance().getReference().child("ads").child(configs.getSelectedform().getAddId()).child("applications").child(configs.getSelectedform().getUser().getFirebaseID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                hideLoading();
                Formobject formobjectonline = dataSnapshot.getValue(Formobject.class);
                if (formobjectonline != null) {
                    binding.setJobobj(formobjectonline);
                    question1from2.setSelectedTab(formobjectonline.getQuestion1from2());
                    question2from2.setSelectedTab(formobjectonline.getQuestion2from2());
                    question3from2.setSelectedTab(formobjectonline.getQuestion3from2());
                    formobject = formobjectonline;
                } else {
                    binding.setJobobj(formobject);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}