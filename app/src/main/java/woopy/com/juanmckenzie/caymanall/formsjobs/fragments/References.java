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
import woopy.com.juanmckenzie.caymanall.databinding.FragmentEducationBinding;
import woopy.com.juanmckenzie.caymanall.databinding.FragmentReferencesBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class References extends BaseFragment {


    public References() {
        // Required empty public constructor
    }


    Formobject formobject;
    FragmentReferencesBinding binding;

    EditText rfullnmae1, rrelationship1, rcompany1, rphone1, raddress1, rfullnmae2, rrelationship2, rcompany2, rphone2, raddress2, rfullnmae3, rrelationship3, rcompany3, rphone3, raddress3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_references, container, false);
        formobject = new Formobject();


        View view = binding.getRoot();
        configs = (Configs) getActivity().getApplicationContext();


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
                    ((FormsActivity) getActivity()).moveto(3);
                }
            }
        }, 400);
    }

    public void Save() {

        formobject = configs.getSelectedform();

        if (rfullnmae1.getText().toString().equals("") || rrelationship1.getText().toString().equals("") || rcompany1.getText().toString().equals("")
                || rphone1.getText().toString().equals("") || raddress1.getText().toString().equals("") || rfullnmae2.getText().toString().equals("") || rrelationship2.getText().toString().equals("") || rcompany2.getText().toString().equals("")
                || rphone2.getText().toString().equals("") || raddress2.getText().toString().equals("") || rfullnmae3.getText().toString().equals("") || rrelationship3.getText().toString().equals("") || rcompany3.getText().toString().equals("")
                || rphone3.getText().toString().equals("") || raddress3.getText().toString().equals("")) {
            return;
        }

        formobject.setRfullnmae1(rfullnmae1.getText().toString());
        formobject.setRrelationship1(rrelationship1.getText().toString());
        formobject.setRcompany1(rcompany1.getText().toString());
        formobject.setRphone1(rphone1.getText().toString());
        formobject.setRaddress1(raddress1.getText().toString());


        formobject.setRfullnmae2(rfullnmae2.getText().toString());
        formobject.setRrelationship2(rrelationship2.getText().toString());
        formobject.setRcompany2(rcompany2.getText().toString());
        formobject.setRphone2(rphone2.getText().toString());
        formobject.setRaddress2(raddress2.getText().toString());


        formobject.setRfullnmae3(rfullnmae3.getText().toString());
        formobject.setRrelationship3(rrelationship3.getText().toString());
        formobject.setRcompany3(rcompany3.getText().toString());
        formobject.setRphone3(rphone3.getText().toString());
        formobject.setRaddress3(raddress3.getText().toString());


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

                formobject.setRfullnmae1(rfullnmae1.getText().toString());
                formobject.setRrelationship1(rrelationship1.getText().toString());
                formobject.setRcompany1(rcompany1.getText().toString());
                formobject.setRphone1(rphone1.getText().toString());
                formobject.setRaddress1(raddress1.getText().toString());


                formobject.setRfullnmae2(rfullnmae2.getText().toString());
                formobject.setRrelationship2(rrelationship2.getText().toString());
                formobject.setRcompany2(rcompany2.getText().toString());
                formobject.setRphone2(rphone2.getText().toString());
                formobject.setRaddress2(raddress2.getText().toString());


                formobject.setRfullnmae3(rfullnmae3.getText().toString());
                formobject.setRrelationship3(rrelationship3.getText().toString());
                formobject.setRcompany3(rcompany3.getText().toString());
                formobject.setRphone3(rphone3.getText().toString());
                formobject.setRaddress3(raddress3.getText().toString());

                binding.setJobobj(formobject);
                configs.setSelectedform(formobject);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public Boolean CanMove() {

        try {
            if (rfullnmae1.getText().toString().equals("") || rrelationship1.getText().toString().equals("") || rcompany1.getText().toString().equals("")
                    || rphone1.getText().toString().equals("") || raddress1.getText().toString().equals("") || rfullnmae2.getText().toString().equals("") || rrelationship2.getText().toString().equals("") || rcompany2.getText().toString().equals("")
                    || rphone2.getText().toString().equals("") || raddress2.getText().toString().equals("") || rfullnmae3.getText().toString().equals("") || rrelationship3.getText().toString().equals("") || rcompany3.getText().toString().equals("")
                    || rphone3.getText().toString().equals("") || raddress3.getText().toString().equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
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
