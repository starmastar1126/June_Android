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
import woopy.com.juanmckenzie.caymanall.databinding.FragmentReferencesBinding;
import woopy.com.juanmckenzie.caymanall.databinding.ViewFragmentReferencesBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewReferences extends BaseFragment {


    public ViewReferences() {
        // Required empty public constructor
    }


    Formobject formobject;
    ViewFragmentReferencesBinding binding;

    EditText rfullnmae1, rrelationship1, rcompany1, rphone1, raddress1, rfullnmae2, rrelationship2, rcompany2, rphone2, raddress2, rfullnmae3, rrelationship3, rcompany3, rphone3, raddress3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.view_fragment_references, container, false);
        formobject = new Formobject();

        View view = binding.getRoot();

        rfullnmae1 = view.findViewById(R.id.fullname1);
        rrelationship1 = view.findViewById(R.id.Relationship1);
        rcompany1 = view.findViewById(R.id.Company1);
        rphone1 = view.findViewById(R.id.Phone1);
        raddress1 = view.findViewById(R.id.Address1);

        rfullnmae2 = view.findViewById(R.id.fullname2);
        rrelationship2 = view.findViewById(R.id.Relationship2);
        rcompany2 = view.findViewById(R.id.Company2);
        rphone2 = view.findViewById(R.id.Phone2);
        raddress2 = view.findViewById(R.id.Address2);

        rfullnmae3 = view.findViewById(R.id.fullname3);
        rrelationship3 = view.findViewById(R.id.Relationship3);
        rcompany3 = view.findViewById(R.id.Company3);
        rphone3 = view.findViewById(R.id.Phone3);
        raddress3 = view.findViewById(R.id.Address3);

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
