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

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.databinding.FragmentMilitaryBinding;
import woopy.com.juanmckenzie.caymanall.databinding.ViewFragmentMilitaryBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMilitaryService extends BaseFragment {


    public ViewMilitaryService() {
        // Required empty public constructor
    }


    Formobject formobject;
    ViewFragmentMilitaryBinding binding;


    EditText mname, from, mto, mrankofdischarge, mtypeofdischarge, mifotherthem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.view_fragment_military, container, false);
        formobject = new Formobject();


        View view = binding.getRoot();


        mname = view.findViewById(R.id.mname);
        from = view.findViewById(R.id.mfrom);
        mto = view.findViewById(R.id.mto);
        mrankofdischarge = view.findViewById(R.id.mrank);
        mtypeofdischarge = view.findViewById(R.id.mdischarge);
        mifotherthem = view.findViewById(R.id.mifotherthenhonorable);


//        showLoading();
        formquery();


        return view;


    }


    public void formquery() {

        Configs configs;
        configs = (Configs) getActivity().getApplicationContext();
        if (configs.getSelectedform().getAddId() != null)
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
