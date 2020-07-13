package woopy.com.juanmckenzie.caymanall.filters;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Categories;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.activities.BaseActivity;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;

public class CategoriesActivity extends BaseActivity {

    public static final String SELECTED_CATEGORY_EXTRA_KEY = "SELECTED_CATEGORY_EXTRA_KEY";
    public static final String INCLUDE_ALL_CATEGORY_EXTRA_KEY = "INCLUDE_ALL_CATEGORY_EXTRA_KEY";
    public static final String INCLUDE_EVENT_CATEGORY_EXTRA_KEY = "INCLUDE_EVENT_CATEGORY_EXTRA_KEY";


    private ImageView backIV;
    private ImageView doneIV;
    private RecyclerView categoriesRV;

    private List<String> categories;
    private String selectedCategory = "";
    private boolean includeAll = true;
    private boolean includeEvent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        selectedCategory = getIntent().getStringExtra(SELECTED_CATEGORY_EXTRA_KEY);
        includeAll = getIntent().getBooleanExtra(INCLUDE_ALL_CATEGORY_EXTRA_KEY, true);
        includeEvent = getIntent().getBooleanExtra(INCLUDE_EVENT_CATEGORY_EXTRA_KEY, true);
        if (selectedCategory == null) {
            selectedCategory = "";
        }

        initViews();

        // add "All" to categories array
        categories = new ArrayList<>();
        if (includeAll) {
            categories.add("All");
        }

        if (includeEvent) {
            categories.add("Events");
        }
        categories.remove("Find Partner");

        // Call query
        queryCategories();
        setUpViews();
    }

    private void initViews() {
        backIV = findViewById(R.id.ac_back_iv);
        doneIV = findViewById(R.id.ac_done_iv);
        categoriesRV = findViewById(R.id.ac_categories_rv);
    }

    private void setUpViews() {
        // MARK: - DONE BUTTON ------------------------------------
        doneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(selectedCategory)) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(SELECTED_CATEGORY_EXTRA_KEY, selectedCategory);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    ToastUtils.showMessage(getString(R.string.select_category1));
                }
            }
        });

        // MARK: - CANCEL BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories.clear();
                finish();
            }
        });
    }

    // MARK: - QUERY CATEGORIES ---------------------------------------------------------------
    void queryCategories() {

        Configs configs = (Configs) getApplicationContext();

        for (Categories cat : configs.getCategoriesList()) {
            categories.add(cat.getCategory());
        }
        categories.remove("Find Partner");
        categoriesRV.setLayoutManager(new LinearLayoutManager(CategoriesActivity.this));
        categoriesRV.setAdapter(new FilterAdapter(categories, selectedCategory,
                new FilterAdapter.OnFilterSelectedListener() {
                    @Override
                    public void onFilterSelected(String filter) {
                        selectedCategory = filter;
                        Log.i("log-", "SELECTED CATEGORY: " + selectedCategory);
                    }
                }));


    }
}
