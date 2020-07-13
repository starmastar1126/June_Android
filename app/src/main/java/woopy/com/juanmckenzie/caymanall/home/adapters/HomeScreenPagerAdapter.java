package woopy.com.juanmckenzie.caymanall.home.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import woopy.com.juanmckenzie.caymanall.home.fragments.AccountFragment;
import woopy.com.juanmckenzie.caymanall.home.fragments.BrowseFragment;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public class HomeScreenPagerAdapter extends FragmentStatePagerAdapter {

    public final static int FRAGMENTS_COUNT = 4;

    public final static int HOME_FRAGMENT_POSITION = 0;
    public final static int LIKES_FRAGMENT_POSITION = 1;
    public final static int ACTIVITIES_FRAGMENT_POSITION = 2;
    public final static int ACCOUNT_FRAGMENT_POSITION = 3;

    public Fragment Events, News;

    public HomeScreenPagerAdapter(FragmentManager fm, Fragment Event, Fragment New) {
        super(fm);
        Events = Event;
        News = New;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HOME_FRAGMENT_POSITION:
                return new BrowseFragment();
            case LIKES_FRAGMENT_POSITION:
                return Events;
            case ACTIVITIES_FRAGMENT_POSITION:
                return News;
            case ACCOUNT_FRAGMENT_POSITION:
                return new AccountFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }
}
