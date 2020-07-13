package woopy.com.juanmckenzie.caymanall.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 12/08/2018
 * All rights reserved
 */
public class MediaPickerDialog extends Dialog {

    public static final int CAMERA_OPTION_INDEX = 0;
    public static final int GALLERY_OPTION_INDEX = 1;

    private OnOptionSelectedListener onOptionSelectedListener;
    private String cameraOptionTitle;

    private TextView cameraTV;
    private TextView galleryTV;
    private TextView cancelTV;

    public MediaPickerDialog(@NonNull Context context) {
        super(context);
    }

    public MediaPickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public MediaPickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_media_picker);

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

    public void setCameraOptionTitle(String cameraOptionTitle) {
        this.cameraOptionTitle = cameraOptionTitle;
    }

    private void initViews() {
        cameraTV = findViewById(R.id.dmp_camera_tv);
        galleryTV = findViewById(R.id.dmp_gallery_tv);
        cancelTV = findViewById(R.id.dmp_cancel_tv);

        if (!TextUtils.isEmpty(cameraOptionTitle)) {
            cameraTV.setText(cameraOptionTitle);
        }

        cameraTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(CAMERA_OPTION_INDEX);
                }
                dismiss();
            }
        });
        galleryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(GALLERY_OPTION_INDEX);
                }
                dismiss();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
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
