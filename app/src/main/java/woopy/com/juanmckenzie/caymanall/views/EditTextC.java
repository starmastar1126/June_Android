package woopy.com.juanmckenzie.caymanall.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class EditTextC extends AppCompatEditText {
    public EditTextC(Context context) {
        super(context);
        setFont();
    }

    public EditTextC(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public EditTextC(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            setTypeface(FontHelpersecons.get(getContext(), "font/Titillium-Semibold"));

        }
    }
}
