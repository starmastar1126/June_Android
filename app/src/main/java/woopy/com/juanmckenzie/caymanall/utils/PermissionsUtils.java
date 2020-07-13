package woopy.com.juanmckenzie.caymanall.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;

/**
 * @author cubycode
 * @since 02/08/2018
 * All rights reserved
 */
public class PermissionsUtils {

    public static boolean hasPermissions(Context context, String... permissions) {
        boolean arePermissionsGranted = true;

        for (String permission : permissions) {
            if (!(ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED)) {
                arePermissionsGranted = false;
            }
        }

        return arePermissionsGrantedAutomatically() || arePermissionsGranted;
    }

    private static boolean arePermissionsGrantedAutomatically() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }
}
