package woopy.com.juanmckenzie.caymanall.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 12/08/2018
 * All rights reserved
 */
public class CreateOptionsDialog extends Dialog {

    private OnOptionSelectedListener onOptionSelectedListener;
    ListView listView;
    String[] values;
    Context context;

    public CreateOptionsDialog(@NonNull Context context, String[] values) {
        super(context);
        this.values = values;
        this.context = context;
    }

    public CreateOptionsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CreateOptionsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_option);

        initViews();
    }

    @Override
    public void show() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.show();
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener;
    }


    private void initViews() {
        listView = (ListView) findViewById(R.id.lisview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.item_category_other, R.id.ic_title_tv, values);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                onOptionSelectedListener.onOptionSelected(itemValue);

            }

        });

    }

    public interface OnOptionSelectedListener {

        void onOptionSelected(String index);
    }
}
