package woopy.com.juanmckenzie.caymanall.TinderType;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import woopy.com.juanmckenzie.caymanall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsF extends Fragment {


    public DetailsF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);


        return view;
    }

}
