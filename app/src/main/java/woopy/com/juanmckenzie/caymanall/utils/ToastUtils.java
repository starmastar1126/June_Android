package woopy.com.juanmckenzie.caymanall.utils;

import android.widget.Toast;

/**
 * @author cubycode
 * @since 05/08/2018
 * All rights reserved
 */
public class ToastUtils {

    public static void showMessage(String message) {
        Toast.makeText(UIUtils.getAppContext(), message, Toast.LENGTH_LONG)
                .show();
    }
}
