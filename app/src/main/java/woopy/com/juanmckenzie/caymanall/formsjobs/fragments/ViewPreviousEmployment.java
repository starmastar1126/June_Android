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
import woopy.com.juanmckenzie.caymanall.databinding.FragmentPreviousEmploymentBinding;
import woopy.com.juanmckenzie.caymanall.databinding.ViewFragmentPreviousEmploymentBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPreviousEmployment extends BaseFragment {


    public ViewPreviousEmployment() {
        // Required empty public constructor
    }


    Formobject formobject;
    ViewFragmentPreviousEmploymentBinding binding;
    SwitchMultiButton question1form3;


    EditText pename, pephone, peaddress, pesupervisor, pestartingsallery, pepejobtittle,
            peendingsallery, peresponsibilities, pefromdate, peenddate, pereasonforleaving;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.view_fragment_previous_employment, container, false);
        formobject = new Formobject();


        View view = binding.getRoot();


        pename = view.findViewById(R.id.pename);
        pephone = view.findViewById(R.id.pephone);
        peaddress = view.findViewById(R.id.peaddress);
        pesupervisor = view.findViewById(R.id.pesupervisor);
        pestartingsallery = view.findViewById(R.id.pestartingsalery);
        peendingsallery = view.findViewById(R.id.peendingsalery);
        peresponsibilities = view.findViewById(R.id.peresponsibilities);
        pefromdate = view.findViewById(R.id.pefromdate);
        peenddate = view.findViewById(R.id.peenddate);
        pereasonforleaving = view.findViewById(R.id.peleavingreason);
        pepejobtittle = view.findViewById(R.id.pejobtittle);

        question1form3 = view.findViewById(R.id.question1form3);

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
