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
import woopy.com.juanmckenzie.caymanall.databinding.FragmentPreviousEmploymentBinding;
import woopy.com.juanmckenzie.caymanall.databinding.FragmentReferencesBinding;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousEmployment extends BaseFragment {


    public PreviousEmployment() {
        // Required empty public constructor
    }


    Formobject formobject;
    FragmentPreviousEmploymentBinding binding;
    SwitchMultiButton question1form3;


    EditText pename, pephone, peaddress, pesupervisor, pestartingsallery, pepejobtittle,
            peendingsallery, peresponsibilities, pefromdate, peenddate, pereasonforleaving;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_previous_employment, container, false);
        formobject = new Formobject();


        View view = binding.getRoot();
        configs = (Configs) getActivity().getApplicationContext();


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
                    ((FormsActivity) getActivity()).moveto(4);
                }
            }
        }, 400);

    }


    public void Save() {

        formobject = configs.getSelectedform();


        if (pename.getText().toString().equals("") || pephone.getText().toString().equals("") || peaddress.getText().toString().equals("")
                || pesupervisor.getText().toString().equals("") || pestartingsallery.getText().toString().equals("")
                || peresponsibilities.getText().toString().equals("") || pepejobtittle.getText().toString().equals("")
                || pefromdate.getText().toString().equals("") || pereasonforleaving.getText().toString().equals("")) {

//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage("You must fill all the fields!")
//                    .setTitle(R.string.app_name)
//                    .setPositiveButton("OK", null);
//            AlertDialog dialog = builder.create();
//            dialog.setIcon(R.drawable.logo);
//            dialog.show();
            return;
        }

        formobject.setPename(pename.getText().toString());
        formobject.setPephone(pephone.getText().toString());
        formobject.setPeaddress(peaddress.getText().toString());
        formobject.setPesupervisor(pesupervisor.getText().toString());
        formobject.setPestartingsallery(pestartingsallery.getText().toString());
        formobject.setPejobtittle(pepejobtittle.getText().toString());
        formobject.setPeendingsallery(peendingsallery.getText().toString());
        formobject.setPeresponsibilities(peresponsibilities.getText().toString());
        formobject.setPefromdate(pefromdate.getText().toString());
        formobject.setPeenddate(peenddate.getText().toString());
        formobject.setPereasonforleaving(pereasonforleaving.getText().toString());


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

                formobject.setPename(pename.getText().toString());
                formobject.setPephone(pephone.getText().toString());
                formobject.setPeaddress(peaddress.getText().toString());
                formobject.setPesupervisor(pesupervisor.getText().toString());
                formobject.setPestartingsallery(pestartingsallery.getText().toString());
                formobject.setPejobtittle(pepejobtittle.getText().toString());
                formobject.setPeendingsallery(peendingsallery.getText().toString());
                formobject.setPeresponsibilities(peresponsibilities.getText().toString());
                formobject.setPefromdate(pefromdate.getText().toString());
                formobject.setPeenddate(peenddate.getText().toString());
                formobject.setPereasonforleaving(pereasonforleaving.getText().toString());

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
            if (pename.getText().toString().equals("") || pephone.getText().toString().equals("") || peaddress.getText().toString().equals("")
                    || pesupervisor.getText().toString().equals("") || pestartingsallery.getText().toString().equals("") || peresponsibilities.getText().toString().equals("") || pepejobtittle.getText().toString().equals("")
                    || pefromdate.getText().toString().equals("") || pereasonforleaving.getText().toString().equals("")) {
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
