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
import woopy.com.juanmckenzie.caymanall.databinding.FragmentMilitaryBinding;
import woopy.com.juanmckenzie.caymanall.databinding.FragmentPreviousEmploymentBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class MilitaryService extends BaseFragment {


    public MilitaryService() {
        // Required empty public constructor
    }


    Formobject formobject;
    FragmentMilitaryBinding binding;


    EditText mname, from, mto, mrankofdischarge, mtypeofdischarge, mifotherthem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_military, container, false);
        formobject = new Formobject();


        View view = binding.getRoot();


        mname = view.findViewById(R.id.mname);
        from = view.findViewById(R.id.mfrom);
        mto = view.findViewById(R.id.mto);
        mrankofdischarge = view.findViewById(R.id.mrank);
        mtypeofdischarge = view.findViewById(R.id.mdischarge);
        mifotherthem = view.findViewById(R.id.mifotherthenhonorable);


        configs = (Configs) getActivity().getApplicationContext();
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
        } else {
            binding.setJobobj(formobject);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CanMove()) {
                    ((FormsActivity) getActivity()).moveto(5);
                }
            }
        }, 400);

    }


    public void Save() {

        formobject = configs.getSelectedform();


        if (mname.getText().toString().equals("") || from.getText().toString().equals("") || mto.getText().toString().equals("")
                || mrankofdischarge.getText().toString().equals("") || mtypeofdischarge.getText().toString().equals("") || mifotherthem.getText().toString().equals("")) {
            return;
        }

        formobject.setMname(mname.getText().toString());
        formobject.setMfrom(from.getText().toString());
        formobject.setMto(mto.getText().toString());
        formobject.setMrankofdischarge(mrankofdischarge.getText().toString());
        formobject.setMtypeofdischarge(mtypeofdischarge.getText().toString());
        formobject.setMifotherthem(mifotherthem.getText().toString());


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
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                hideLoading();
                formobject = dataSnapshot.getValue(Formobject.class);
                if (formobject == null)
                    formobject = new Formobject();

                formobject.setMname(mname.getText().toString());
                formobject.setMfrom(from.getText().toString());
                formobject.setMto(mto.getText().toString());
                formobject.setMrankofdischarge(mrankofdischarge.getText().toString());
                formobject.setMtypeofdischarge(mtypeofdischarge.getText().toString());
                formobject.setMifotherthem(mifotherthem.getText().toString());

                binding.setJobobj(formobject);
                configs.setSelectedform(formobject);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Boolean CanMove() {
        return true;
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
