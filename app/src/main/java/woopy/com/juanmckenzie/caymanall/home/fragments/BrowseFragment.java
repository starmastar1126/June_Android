package woopy.com.juanmckenzie.caymanall.home.fragments;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Chats;
import woopy.com.juanmckenzie.caymanall.Objects.Categories;
import woopy.com.juanmckenzie.caymanall.Objects.SubCategories;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.home.adapters.BrowseCategoriesAdapter;
import woopy.com.juanmckenzie.caymanall.utils.Constants;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

public class BrowseFragment extends Fragment {

    private EditText searchTxt;
    private RecyclerView categoriesRV;
    ImageView chatButt;
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_browse, container, false);


        adddata();

        chatButt = view.findViewById(R.id.hChatButt);
        searchTxt = view.findViewById(R.id.hSearchTxt);
        categoriesRV = view.findViewById(R.id.fb_categories_rv);
        configs = (Configs) getActivity().getApplicationContext();
        categories = configs.getCategoriesList();

        refreshLayout = view.findViewById(R.id.refreshLayout);
        categoriesRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        try {
            categoriesRV.setAdapter(new BrowseCategoriesAdapter(categories, getActivity()));
        } catch (Exception ignored) {

        }
        setUpViews();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adddata();

                chatButt = view.findViewById(R.id.hChatButt);
                searchTxt = view.findViewById(R.id.hSearchTxt);
                categoriesRV = view.findViewById(R.id.fb_categories_rv);
                configs = (Configs) getActivity().getApplicationContext();
                categories = configs.getCategoriesList();

                refreshLayout = view.findViewById(R.id.refreshLayout);
                categoriesRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                try {
                    categoriesRV.setAdapter(new BrowseCategoriesAdapter(categories, getActivity()));
                } catch (Exception ignored) {

                }
                setUpViews();
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private static boolean m_iAmVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

        if (m_iAmVisible) {
            Log.d("BrowserVisiblitycheck", "this fragment is now visible");
        } else {
            Log.d("BrowserVisiblitycheck", "this fragment is now invisible");
        }
    }


    private void setUpViews() {
        searchTxt.setTypeface(Configs.titRegular);
        // MARK: - SEARCH ADS BY KEYWORDS --------------------------------------------------------
        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!searchTxt.getText().toString().matches("")) {

                        UIUtils.hideKeyboard(searchTxt);

                        // Pass strings to AdsListActivity.java
                        Intent adsListIntent = new Intent(getActivity(), AdsListActivity.class);
                        adsListIntent.putExtra(AdsListActivity.SEARCH_QUERY_KEY, searchTxt.getText().toString());
                        adsListIntent.putExtra(AdsListActivity.CATEGORY_NAME_KEY, Constants.BrowseCategories.DEFAULT_CATEGORY_NAME);
                        startActivity(adsListIntent);

                        return true;
                    }

                    // No text -> No search
                } else {
                    ToastUtils.showMessage(getString(R.string.you_must_type_something));
                }

                return false;
            }
        });

        // EditText TextWatcher delegate
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int closeDrawable;
                if (s.length() == 0) {
                    closeDrawable = 0;
                } else {
                    closeDrawable = R.drawable.close_white_ic;
                }
                searchTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, closeDrawable, 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        final Drawable imgClearButton = ContextCompat.getDrawable(getActivity(), R.drawable.close_white_ic);
        searchTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (searchTxt.getText().length() > 0) {
                    if (event.getX() > searchTxt.getWidth() - searchTxt.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                        searchTxt.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        // MARK: - CHATS BUTTON ------------------------------------
        chatButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getActivity(), Chats.class));
                } else {
                    Configs.loginAlert(getString(R.string.login_message_5), getActivity());
                }
            }
        });


    }

    List<Categories> categories = new ArrayList<>();
    Configs configs;

    @Override
    public void onResume() {
        super.onResume();
    }


    Configs myapplication;
    List<Categories> categoriesList = new ArrayList<>();


    public void adddata() {

        myapplication = (Configs) getActivity().getApplicationContext();
        if (myapplication.getCategoriesList().size() != 0)
            return;


        Categories categories = new Categories();
        categories.setCategory("Beauty");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/55c740ae11a9e4866cdebc5776b6127d_6977ab0ec6bd78ac8f3b5ce6de5ae915_beauty.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());

        SubCategories subCategories = new SubCategories();


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories.setName("Fragrance, Skincare");
        subCategories = new SubCategories();
        subCategories.setName("Makeup");
        categories.getSubCategories().add(subCategories);
        subCategories = new SubCategories();
        subCategories.setName("Hair Care");
        categories.getSubCategories().add(subCategories);
        subCategories = new SubCategories();
        subCategories.setName("Bath & Shower");
        categories.getSubCategories().add(subCategories);
        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);

        categoriesList.add(categories);

        categories = new Categories();
        categories.setCategory("Books");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/aaf9fa1d58ffd9e4608bc41760242258_5a493c5925cac900d69d29d2d053d8fa_books.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Books");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Calendars");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Card Decks");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Sheet Music");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Magazines");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Journals");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other Publications");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);

        categories = new Categories();
        categories.setCategory("Business Products & Services (B2B)");
        categories.setImageUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/bussiness.png?alt=media&token=c37643d5-89ce-4a96-9925-cc85a4621527");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Business-relevant products and services");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);
        categoriesList.add(categories);


        categories = new Categories();
        categories.setCategory("Find Partner");
        categories.setImageUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/f98da02186fe3baafd376f651dbb53a3be9898e8.jpg?alt=media&token=22a4e4cc-9749-43bc-9731-3f60face755b");
        categories.setSubCategories(new ArrayList<SubCategories>());
        categoriesList.add(categories);


        categories = new Categories();
        categories.setCategory("Business");
        categories.setImageUrl("https://smallbiztrends.com/wp-content/uploads/2018/03/shutterstock_705804559-850x476.jpg");

        subCategories = new SubCategories();
        subCategories.setName("Accounting");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Airport");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Amusement Parks");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Aquariums");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Art Galleries");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Atm");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Bakeries");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Banks");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Bars");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Beauty Salons");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Bicycle Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Bicycle Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Bowling Alley");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Bus Stations");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Cafe&#39;s");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Campground");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Car Dealers");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Car Rentals");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Car Repair");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Car Wash");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Casinos");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Church");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("City Hall");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Clothing Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Convenience Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Dentist");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Department Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Doctors");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Electricians");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Electronics Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Embassy");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Florist");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Funeral Homes");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Furniture Stores");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Gas Stations");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Gyms");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Hair Care");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Hardware Stores");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Home Goods Store");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Hospitals");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Insurance Agencies");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Jewelry Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Laundry");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Lawyers");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Library");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Liquor Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Local Government Office");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Locksmiths");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Lodging");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Meal Delivery");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Meal Delivery");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Mosque");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Movie Rentals");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Movie Theaters");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Moving Companies");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Museums");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Night Clubs");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Painters");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Parks");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Parkings");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Pet Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Pharmacies");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Pharmacies");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Plumbers");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Post Office");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Real Estate Agencies");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Restaurants");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Roofing Contractors");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("RV Parks");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Salons");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Schools");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Shoe Stores");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Shopping Mall");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("SPA");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Stadium");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Storage");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Subway Stations");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Synagogue");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Taxi Stands");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Train Station");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Transit Station");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Travel Agencies");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Universities");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Veterinary Care");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Zoo");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);

        categories = new Categories();
        categories.setCategory("Restaurants");
        categories.setImageUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Gastronomi%CC%81a-BC.png?alt=media&token=b171c9c2-8ccc-4deb-8a19-8cc3f5eb48f0");
        categories.setSubCategories(new ArrayList<SubCategories>());


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Bicycles & Motorcycles");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/d62dd02da76de6576abe23493352f280_96e30f94b0aed386960553094b768df5_bkies.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Parts");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Repairs");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("For Sale");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Rent");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);

        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Electronics & Accessories");
        categories.setImageUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/Electronics1.jpg?alt=media&token=15aa12c3-b621-4886-93be-52ce41735623");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Cell phones");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Laptops");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Desktops");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Tablets");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Cameras");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Accessories");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);

        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Craft & Design");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/afb54c869e54bb9ffd084a686e5afef2_4d451a5a11408c45dc0453db7b421a99_craft.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());

        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Paintings");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Drawings");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Mixed Media (Two-Dimensional)");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Limited Edition Prints and Photographs");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Collectibles");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);

        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Furniture & Home");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/57df8a3b946901f315d959695aad6457_833ee3d454e6cfa43f2612f593d045d3_furniture.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Kitchen");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Dining");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Furniture");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Décor");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Bedding");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Bath");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Home Appliances");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Generators");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Music Instruments");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/63a6c1c7eeab90760dec69b559450d22_30713aba4d5b28379eec00868225c4ca_music.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Guitars");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Pianos");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Turntables");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Recording Equipments");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Outdoors & Gardening");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/2d980d853def09ea0a194263bc279db1_badd449c574d1f033e170101a2d15433_out.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Patio");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Lawn");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Garden");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Landscaping");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Sports");
        categories.setImageUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/deea8f61afdab04b13bc46033ec91c02_e74325cafb376956bde9e6ea4e2e5e61_cars%20(1).png?alt=media&token=c0bd4a27-de25-4c79-b533-27be75481607");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Exercise & Fitness");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Athletic Apparel");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Cycling");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Action Sports Boating & Fishing");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Games & Toys");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/e2de21cdebfc4c36e34a61c5bb94f4f4_1e8382f2a942bae62fe5208f5497626b_toys.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());

        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Infant and Preschool");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Learning and Exploration Toys");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Action Figures");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Dolls");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Board Games");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);

        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Housing");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/6cb291e725ed70206decad6991324973_0824f23de58c0e5058a2caa21abbbab2_house.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Rental (Land, Condos, Apartments, Houses, Hotels, Offices, Buildings, Other)");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("For Sale (Land, Condos, Apartments, Houses, Offices, Buildings, Other)");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Rent To Own, Staycation");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Vehicles & Watercrafts");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/deea8f61afdab04b13bc46033ec91c02_e74325cafb376956bde9e6ea4e2e5e61_cars.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Trucks");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("SUV’s");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Convertibles, Sports Cars");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Vans");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Vehicle Parts");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Boats");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Trailers");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Jet Skies");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Engines");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Kids Stuff");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/145d4eee20358258a86b23b4c2857197_e39d88bb752d05a9b866dd4e265a6216_kids.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());


        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Clothing");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Community");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/a73a0d3c09ac5672091a5e378b948afc_e109a6a03a48212841e7504eebc41d9b_comm.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());

        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Meetings");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Fish Fry");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Events");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Garage Sales");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Fund Raising");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("Jobs");
        categories.setImageUrl("https://firebasestorage.googleapis.com/v0/b/caymanall.appspot.com/o/jobs.png?alt=media&token=abbc04c6-13c1-47eb-88b2-7ea2d8b83c3a");
        categories.setSubCategories(new ArrayList<SubCategories>());

        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Agriculture");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Caretaker");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Food and natural Resources");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Architecture and Construction");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Arts");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Audio/Video Technology and Communications");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Business Management and Administration");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Cleaning and Maintenance");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Consulting");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Craft");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Education and Training");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Entertainment");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Fashion");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Finance");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Government and Public Administration");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Healthcare");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Hospitality and Tourism");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Human Resources, Industrial");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Information Technology");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Journalism, Law");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Public Safety");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Corrections and Security");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Manufacturing");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Marketing");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Sales and Service");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Mechanic");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Media, Science");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Technology");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Engineering and Mathematics");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Transportation");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Distribution and Logistics");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);


        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("For Him");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/271e3e2487c2914d723d77e914759939_0cf25c559ae5666de523f3287ade8525_forhim.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());

        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Clothing");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Wearable");
        categories.getSubCategories().add(subCategories);


        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);

        categoriesList.add(categories);
        categories = new Categories();
        categories.setCategory("For Her");
        categories.setImageUrl("https://parsefiles.back4app.com/jlc6syU9Hz1znUqHMCdgu5Fp4Sh3pwPvDjw2tpyk/ca2c352e1bf560736a38e39ea3c75efb_036d39b4b8ad2a017f8b193d2a3f8d78_forher.jpg");
        categories.setSubCategories(new ArrayList<SubCategories>());

        subCategories = new SubCategories();
        subCategories.setName("All");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Clothing");
        categories.getSubCategories().add(subCategories);

        subCategories = new SubCategories();
        subCategories.setName("Other");
        categories.getSubCategories().add(subCategories);

        categoriesList.add(categories);


        myapplication.setCategoriesList(categoriesList);

    }
}
