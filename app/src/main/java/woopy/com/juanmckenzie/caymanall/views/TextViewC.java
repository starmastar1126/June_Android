package woopy.com.juanmckenzie.caymanall.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewC extends AppCompatTextView {
    public TextViewC(Context context) {
        super(context);
        setFont();
    }

    public TextViewC(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public TextViewC(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        if (!isInEditMode()) {
            setTypeface(FontHelper.get(getContext(), "font/Titillium-Semibold"));

        }
    }
}
