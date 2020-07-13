package woopy.com.juanmckenzie.caymanall.formsjobs.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


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
import woopy.com.juanmckenzie.caymanall.databinding.FragmentApplicantInformationBinding;
import woopy.com.juanmckenzie.caymanall.databinding.FragmentEducationBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class Education extends BaseFragment {


    public Education() {
        // Required empty public constructor
    }


    Formobject formobject;
    FragmentEducationBinding binding;

    EditText hname, haddress, hfrom, hto, hdeploma, cname, caddress, cfrom, cto, cdeploma, oname, oaddress, ofrom, oto, odeploma;
    SwitchMultiButton question1from2, question2from2, question3from2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_education, container, false);
        formobject = new Formobject();


        View view = binding.getRoot();

        configs = (Configs) getActivity().getApplicationContext();
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


        formquery();


        binding.getRoot().findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Save();

            }
        });

        return view;


    }


    public void formquery() {
        formobject = configs.getSelectedform();
        if (formobject != null) {
            binding.setJobobj(formobject);
            question1from2.setSelectedTab(formobject.getQuestion1from2());
            question2from2.setSelectedTab(formobject.getQuestion2from2());
            question3from2.setSelectedTab(formobject.getQuestion3from2());
        } else {
            binding.setJobobj(formobject);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CanMove()) {
                    ((FormsActivity) getActivity()).moveto(2);
                }
            }
        }, 400);
    }

    public void Save() {

        formobject = configs.getSelectedform();


        if (hname.getText().toString().equals("") || haddress.getText().toString().equals("") || hdeploma.getText().toString().equals("") || cname.getText().toString().equals("") || caddress.getText().toString().equals("")
                || cdeploma.getText().toString().equals("") || oname.getText().toString().equals("") || oaddress.getText().toString().equals("")
                || odeploma.getText().toString().equals("")) {
            return;
        }

        formobject.setHname(hname.getText().toString());
        formobject.setHaddress(haddress.getText().toString());
        formobject.setHfrom(hfrom.getText().toString());
        formobject.setHto(hto.getText().toString());
        formobject.setHdeploma(hdeploma.getText().toString());

        formobject.setCname(cname.getText().toString());
        formobject.setCaddress(caddress.getText().toString());
        formobject.setCfrom(cfrom.getText().toString());
        formobject.setCto(cto.getText().toString());
        formobject.setCdeploma(cdeploma.getText().toString());


        formobject.setOname(oname.getText().toString());
        formobject.setOaddress(oaddress.getText().toString());
        formobject.setOfrom(ofrom.getText().toString());
        formobject.setOto(oto.getText().toString());
        formobject.setOdeploma(odeploma.getText().toString());


        formobject.setQuestion1from2(question1from2.getSelectedTab());
        formobject.setQuestion2from2(question2from2.getSelectedTab());
        formobject.setQuestion3from2(question3from2.getSelectedTab());


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(formobject).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                hideLoading();

            }
        });
    }

    Configs configs;

    public void Form() {


        if (!isCurrentVisible) {
            return;
        }
        formobject = configs.getSelectedform();
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                hideLoading();
                formobject = dataSnapshot.getValue(Formobject.class);

                if (formobject == null)
                    formobject = new Formobject();

                formobject.setHname(hname.getText().toString());
                formobject.setHaddress(haddress.getText().toString());
                formobject.setHfrom(hfrom.getText().toString());
                formobject.setHto(hto.getText().toString());
                formobject.setHdeploma(hdeploma.getText().toString());

                formobject.setCname(cname.getText().toString());
                formobject.setCaddress(caddress.getText().toString());
                formobject.setCfrom(cfrom.getText().toString());
                formobject.setCto(cto.getText().toString());
                formobject.setCdeploma(cdeploma.getText().toString());


                formobject.setOname(oname.getText().toString());
                formobject.setOaddress(oaddress.getText().toString());
                formobject.setOfrom(ofrom.getText().toString());
                formobject.setOto(oto.getText().toString());
                formobject.setOdeploma(odeploma.getText().toString());


                formobject.setQuestion1from2(question1from2.getSelectedTab());
                formobject.setQuestion2from2(question2from2.getSelectedTab());
                formobject.setQuestion3from2(question3from2.getSelectedTab());

                binding.setJobobj(formobject);
                configs.setSelectedform(formobject);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public Boolean CanMove() {

        if (hname.getText().toString().equals("") || haddress.getText().toString().equals("") || hdeploma.getText().toString().equals("") || cname.getText().toString().equals("") || caddress.getText().toString().equals("")
                || cdeploma.getText().toString().equals("") || oname.getText().toString().equals("") || oaddress.getText().toString().equals("")
                || odeploma.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isCurrentVisible = false;

    @Override
    public void onStart() {
        super.onStart();
        isCurrentVisible = true;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && isCurrentVisible) {
            this.Save();
        }
    }

}
