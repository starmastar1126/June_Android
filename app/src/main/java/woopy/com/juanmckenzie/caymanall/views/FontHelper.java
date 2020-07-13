package woopy.com.juanmckenzie.caymanall.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.collection.SimpleArrayMap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FontHelper {
    public static final String OTF = ".otf";
    public static final String TTF = ".otf";
    private static final SimpleArrayMap<String, Typeface> cache = new SimpleArrayMap();

    @Retention(RetentionPolicy.SOURCE)
    public @interface FontType {
    }

    private FontHelper() {
    }

    public static Typeface get(Context context, String name) {
        return get(context, name, OTF);
    }

    public static Typeface get(Context context, String name, String fontType) {
        synchronized (cache) {
            if (cache.containsKey(name)) {
                Typeface typeface = (Typeface) cache.get(name);
                return typeface;
            }
            Typeface t = Typeface.createFromAsset(context.getAssets(),  name + fontType);
            cache.put(name, t);
            return t;
        }
    }
}
