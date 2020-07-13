package woopy.com.juanmckenzie.caymanall.home.enums;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public enum BottomNavigationTab {

    HOME(R.drawable.homeu, R.drawable.home, R.string.home_tab_browse, false),
    LIKES(R.drawable.newsu, R.drawable.news, R.string.home_tab_likes, false),
    SELL(R.drawable.newaddicon, R.drawable.newaddicon, 0, true),
    ACTIVITIES(R.drawable.eventsu, R.drawable.events, R.string.home_tab_activity, false),
    ACCOUNT(R.drawable.profileu, R.drawable.profile, R.string.home_tab_account, false);

    public static final float SPECIAL_ITEM_WIDTH_RATIO = 1.0f;

    @DrawableRes
    private int selectedResId;
    @DrawableRes
    private int unselectedResId;
    @StringRes
    private int titleResId;
    private boolean isSpecialItem;

    BottomNavigationTab(@DrawableRes int unselectedResId,
                        @DrawableRes int selectedResId,
                        @StringRes int titleResId,
                        boolean isSpecialItem) {
        this.unselectedResId = unselectedResId;
        this.selectedResId = selectedResId;
        this.titleResId = titleResId;
        this.isSpecialItem = isSpecialItem;
    }

    public static int getSpecialItemCount() {
        int counter = 0;
        for (BottomNavigationTab current : BottomNavigationTab.values()) {
            if (current.isSpecialItem()) {
                counter++;
            }
        }

        return counter;
    }

    public boolean isSpecialItem() {
        return isSpecialItem;
    }

    public int getUnselectedResId() {
        return unselectedResId;
    }

    public int getSelectedResId() {
        return selectedResId;
    }

    public int getTitleResId() {
        return titleResId;
    }
}
