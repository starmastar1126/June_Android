package woopy.com.juanmckenzie.caymanall.utils;

import com.parse.ParseUser;

/**
 * @author cubycode
 * @since 01/08/2018
 * All rights reserved
 */
public class SessionUtils {

    public static boolean isUserLoggedIn() {
        return ParseUser.getCurrentUser().getUsername() != null;
    }
}
