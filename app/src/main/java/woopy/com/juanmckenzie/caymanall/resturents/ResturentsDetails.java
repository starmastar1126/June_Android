package woopy.com.juanmckenzie.caymanall.resturents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.resturents.Modal.Place;
import woopy.com.juanmckenzie.caymanall.resturents.Modal.PlaceUserRating;
import woopy.com.juanmckenzie.caymanall.resturents.Response.Resbj;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class ResturentsDetails extends BaseActivity {

    public static final String TAG = ResturentsDetails.class.getSimpleName();
    private String mCurrentPlaceDetailUrl;
    private ArrayList<PlaceUserRating> mPlaceUserRatingsArrayList = new ArrayList<>();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    Configs configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        configs = (Configs) getApplicationContext();

        showLoading();
        String currentPlaceDetailId = configs.getSelectedPlaceID();
        mCurrentPlaceDetailUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.LOCATION_DETAIL_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_PLACE_ID_TAG + "=" +
                currentPlaceDetailId + "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;
        Log.d(TAG, mCurrentPlaceDetailUrl);


        findViewById(R.id.aad_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        //Method to get the all details about place
        getCurrentPlaceAllDetails(mCurrentPlaceDetailUrl);
    }


    private void getCurrentPlaceAllDetails(final String currentPlaceDetailUrl) {
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                currentPlaceDetailUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        try {
                            JSONObject rootJsonObject = response.getJSONObject("result");

                            String currentPlaceId = rootJsonObject.getString("place_id");
                            Double currentPlaceLatitude = rootJsonObject
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lat");
                            Double currentPlaceLongitude = rootJsonObject
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lng");
                            String currentPlaceName = rootJsonObject.getString("name");
                            String currentPlaceOpeningHourStatus = rootJsonObject
                                    .has("opening_hours") ? rootJsonObject.getJSONObject("opening_hours")
                                    .getString("open_now") : "Status Not Available";
                            if (currentPlaceOpeningHourStatus.equals("true"))
                                currentPlaceOpeningHourStatus = "Opened";
                            else if (currentPlaceOpeningHourStatus.equals("false"))
                                currentPlaceOpeningHourStatus = "Closed";

                            Double currentPlaceRating = rootJsonObject.has("rating") ?
                                    rootJsonObject.getDouble("rating") : 0;
                            String currentPlaceAddress = rootJsonObject.has("formatted_address") ?
                                    rootJsonObject.getString("formatted_address") :
                                    "Address Not Available";
                            String currentPlacePhoneNumber = rootJsonObject
                                    .has("international_phone_number") ? rootJsonObject
                                    .getString("international_phone_number") :
                                    "Phone Number Not Registered";
                            String currentPlaceWebsite = rootJsonObject.has("website") ?
                                    rootJsonObject.getString("website") :
                                    "Website Not Registered";
                            String currentPlaceShareLink = rootJsonObject.getString("url");
//                            mPlaceShareUrl = currentPlaceShareLink;

                            Place currentPlaceDetail = new Place(
                                    currentPlaceId,
                                    currentPlaceLatitude,
                                    currentPlaceLongitude,
                                    currentPlaceName,
                                    currentPlaceOpeningHourStatus,
                                    currentPlaceRating,
                                    currentPlaceAddress,
                                    currentPlacePhoneNumber,
                                    currentPlaceWebsite, currentPlaceShareLink);

                            if (rootJsonObject.has("reviews")) {

                                JSONArray reviewJsonArray = rootJsonObject.getJSONArray("reviews");
                                for (int i = 0; i < reviewJsonArray.length(); i++) {
                                    JSONObject currentUserRating = (JSONObject) reviewJsonArray.
                                            get(i);

                                    PlaceUserRating currentPlaceUserRating = new PlaceUserRating(
                                            currentUserRating.getString("author_name"),
                                            currentUserRating.getString("profile_photo_url"),
                                            currentUserRating.getDouble("rating"),
                                            currentUserRating.getString("relative_time_description"),
                                            currentUserRating.getString("text"));

                                    mPlaceUserRatingsArrayList.add(currentPlaceUserRating);
                                }
                            }

                            Gson Gson = new Gson();

                            try {
                                Configs configs = (Configs) getApplicationContext();
                                configs.setImages(new ArrayList<>());
                                Resbj.Result resbj = Gson.fromJson(rootJsonObject.toString(), Resbj.Result.class);
                                TextView aad_toolbar_title_tv = findViewById(R.id.aad_toolbar_title_tv);
                                aad_toolbar_title_tv.setText(resbj.getName());
                                resbj.getPhotos().size();
                                for (int i = 0; i < resbj.getPhotos().size(); i++) {
                                    Map<String, String> newMap = new HashMap<String, String>((Map<? extends String, ? extends String>) resbj.getPhotos().get(i));
                                    HashMap<String, String> hashMap = new HashMap((Map) resbj.getPhotos().get(i));

                                    configs.getImages().add(hashMap.get("photo_reference"));
                                }
                                String as = "";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Bundle currentPlaceDetailData = new Bundle();
                            currentPlaceDetailData.putParcelable(
                                    GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, currentPlaceDetail);
                            Bundle currentPlaceUserRatingDetail = new Bundle();
                            currentPlaceUserRatingDetail.putParcelableArrayList(
                                    GoogleApiUrl.CURRENT_LOCATION_USER_RATING_KEY,
                                    mPlaceUserRatingsArrayList);

                            setupViewPager(mViewPager, currentPlaceDetailData,
                                    currentPlaceUserRatingDetail);

                            mTabLayout.setupWithViewPager(mViewPager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        Log.d(TAG, error.getMessage());
                    }
                });
        //Adding request to request queue
        Configs.getInstance().addToRequestQueue(placeJsonObjectRequest, jsonArrayTag);
    }

    private void setupViewPager(ViewPager viewPager, Bundle currentPlaceDetailData,
                                Bundle currentPlaceUserRatingDetail) {
        ViewPagerAdapter viewpagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        viewpagerAdapter.setBundleData(currentPlaceDetailData, currentPlaceUserRatingDetail);
        viewPager.setAdapter(viewpagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        Bundle placeAboutFragmentBundle;
        Bundle placeReviewFragmentBundle;

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PlaceAboutDetail placeAboutDetailFragment = new PlaceAboutDetail();
                    placeAboutDetailFragment.setArguments(placeAboutFragmentBundle);
                    return placeAboutDetailFragment;
                case 1:
                    PlaceReviewDetail placeReviewDetailFragment = new PlaceReviewDetail();
                    placeReviewDetailFragment.setArguments(placeReviewFragmentBundle);
                    return placeReviewDetailFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        private void setBundleData(Bundle aboutFragmentBundle, Bundle reviewFragmentBundle) {
            placeAboutFragmentBundle = aboutFragmentBundle;
            placeReviewFragmentBundle = reviewFragmentBundle;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "About";
                case 1:
                    return "Reviews";
            }
            return null;
        }
    }
}