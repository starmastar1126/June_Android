package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.filters.FilterAdapter;
import woopy.com.juanmckenzie.caymanall.filters.models.SortBy;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;

public class SortByActivity extends AppCompatActivity {

    public static final String SELECTED_SORT_BY_EXTRA_KEY = "SELECTED_SORT_BY_EXTRA_KEY";

    private ImageView backIV;
    private ImageView doneIV;
    private RecyclerView sortByRV;

    /* Variables */
    private List<String> sortByArr = new ArrayList<>();
    private String selectedSort = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_by);

        selectedSort = getIntent().getStringExtra(SELECTED_SORT_BY_EXTRA_KEY);
        if (selectedSort == null) {
            selectedSort = "";
        }

        // Init sortBy Array
        for (int i = 0; i < SortBy.values().length; i++) {
            sortByArr.add(SortBy.values()[i].getValue());
        }

        initViews();
        setUpViews();
    }

    private void initViews() {
        backIV = findViewById(R.id.asb_back_iv);
        doneIV = findViewById(R.id.asb_done_iv);
        sortByRV = findViewById(R.id.asb_categories_rv);
    }

    private void setUpViews() {
        sortByRV.setLayoutManager(new LinearLayoutManager(this));
        sortByRV.setAdapter(new FilterAdapter(sortByArr, selectedSort, new FilterAdapter.OnFilterSelectedListener() {
            @Override
            public void onFilterSelected(String filter) {
                selectedSort = filter;
            }
        }));

        // MARK: - DONE BUTTON ------------------------------------
        doneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedSort.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(SELECTED_SORT_BY_EXTRA_KEY, selectedSort);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    ToastUtils.showMessage(getString(R.string.select_an_option));
                }
            }
        });

        // MARK: - CANCEL BUTTON ------------------------------------
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
