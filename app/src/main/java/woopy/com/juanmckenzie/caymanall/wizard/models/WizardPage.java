package woopy.com.juanmckenzie.caymanall.wizard.models;

import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public enum WizardPage {

    RED(R.string.wizard_page1, R.drawable.wizard_page1),
    BLUE(R.string.wizard_page2, R.drawable.wizard_page2),
    GREEN(R.string.wizard_page3, R.drawable.wizard_page3);


    private int descriptionResId;
    private int backgroundResId;

    WizardPage(int descriptionResId, int backgroundResId) {
        this.descriptionResId = descriptionResId;
        this.backgroundResId = backgroundResId;
    }

    public int getDescriptionResId() {
        return descriptionResId;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }
}
