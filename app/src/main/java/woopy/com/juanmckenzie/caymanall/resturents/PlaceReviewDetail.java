package woopy.com.juanmckenzie.caymanall.resturents;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.resturents.Modal.PlaceUserRating;
import woopy.com.juanmckenzie.caymanall.resturents.adapters.PlaceUserRatingAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceReviewDetail extends Fragment {

    /**
     * all references
     */
    private ArrayList<PlaceUserRating> mPlaceUserRatingArrayList = new ArrayList<>();
    private PlaceUserRatingAdapter mPlaceUserRatingAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_review_detail, container, false);
        mPlaceUserRatingArrayList = getArguments()
                .getParcelableArrayList(GoogleApiUrl.CURRENT_LOCATION_USER_RATING_KEY);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        if (mPlaceUserRatingArrayList.size() == 0) {
            rootView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.empty_view).setVisibility(View.GONE);
            mPlaceUserRatingAdapter = new PlaceUserRatingAdapter(getActivity(), mPlaceUserRatingArrayList);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mRecyclerView.setAdapter(mPlaceUserRatingAdapter);
        }
        return rootView;
    }
}