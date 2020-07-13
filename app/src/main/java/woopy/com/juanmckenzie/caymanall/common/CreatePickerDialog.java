package woopy.com.juanmckenzie.caymanall.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.nekocode.badge.BadgeDrawable;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

/**
 * @author cubycode
 * @since 12/08/2018
 * All rights reserved
 */
public class CreatePickerDialog extends Dialog {

    public static final int EventC = 0;
    public static final int NewsC = 1;
    public static final int BannerC = 3;
    public static final int AdC = 2;

    private OnOptionSelectedListener onOptionSelectedListener;
    private String cameraOptionTitle;

    private LinearLayout Ad;
    private LinearLayout Event;
    private LinearLayout News;
    private LinearLayout Addbanner;
    private TextView Ad1, Cancel;
    private TextView Event1;
    private TextView News1;
    private TextView Addbanner1;

    Context context;

    public CreatePickerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CreatePickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public CreatePickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_chooser);

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

    public float dp2px(float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }


    private void initViews() {


        Event = findViewById(R.id.CreateEvent);
        News = findViewById(R.id.CreateNews);
        Ad = findViewById(R.id.CreateAd);
        Cancel = findViewById(R.id.Cancel);
        Addbanner = findViewById(R.id.Addbanner);


        Event1 = findViewById(R.id.CreateEvent1);
        News1 = findViewById(R.id.CreateNews1);
        Ad1 = findViewById(R.id.CreateAd1);
        Addbanner1 = findViewById(R.id.Addbanner1);


        final BadgeDrawable drawableBanner =
                new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                        .badgeColor(UIUtils.getColor(R.color.red))
                        .text1(context.getString(R.string.paid))
                        .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                        .strokeWidth(1)
                        .build();
        SpannableString spannablebanner =
                new SpannableString(TextUtils.concat(
                        "",
                        drawableBanner.toSpannable()
                ));

        Addbanner1.setText(spannablebanner);


        final BadgeDrawable drawableEvent =
                new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                        .badgeColor(UIUtils.getColor(R.color.colorPrimary))
                        .text1(context.getString(R.string.free))
                        .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                        .strokeWidth(1)
                        .build();
        SpannableString spannableEvent =
                new SpannableString(TextUtils.concat(
                        "",
                        drawableEvent.toSpannable()
                ));

        Event1.setText(spannableEvent);


        final BadgeDrawable drawableadd =
                new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                        .badgeColor(UIUtils.getColor(R.color.colorPrimary))
                        .text1(context.getString(R.string.free))
                        .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                        .strokeWidth(1)
                        .build();
        SpannableString spannableadd =
                new SpannableString(TextUtils.concat(
                        "",
                        drawableadd.toSpannable()
                ));

        Ad1.setText(spannableadd);


        final BadgeDrawable drawablenews =
                new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                        .badgeColor(UIUtils.getColor(R.color.colorPrimary))
                        .text1(context.getString(R.string.free))
                        .padding(dp2px(5), dp2px(5), dp2px(5), dp2px(5), dp2px(5))
                        .strokeWidth(1)
                        .build();
        SpannableString spannablenews =
                new SpannableString(TextUtils.concat(
                        "",
                        drawablenews.toSpannable()
                ));

        News1.setText(spannablenews);

        Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(EventC);
                }
                dismiss();
            }
        });
        Ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(AdC);
                }
                dismiss();
            }
        });
        News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(NewsC);
                }
                dismiss();
            }
        });
        Addbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(BannerC);
                }
                dismiss();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnOptionSelectedListener {

        void onOptionSelected(int index);
    }
}
