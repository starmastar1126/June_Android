package woopy.com.juanmckenzie.caymanall.wizard;

import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.wizard.models.WizardPage;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public class WizardPagerAdapter extends PagerAdapter {

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(collection.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_wizard_page, collection, false);

        ImageView backgroundIV = layout.findViewById(R.id.iwp_background_iv);
        TextView descriptionTV = layout.findViewById(R.id.iwp_description_tv);

        WizardPage page = WizardPage.values()[position];

        backgroundIV.setImageResource(page.getBackgroundResId());
        descriptionTV.setText(page.getDescriptionResId());

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return WizardPage.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
