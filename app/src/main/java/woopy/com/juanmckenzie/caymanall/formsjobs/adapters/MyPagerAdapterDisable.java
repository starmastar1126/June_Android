package woopy.com.juanmckenzie.caymanall.formsjobs.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.ApplicantInformation;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.Disclaimer;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.Education;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.MilitaryService;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.PreviousEmployment;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.References;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.ViewApplicantInformation;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.ViewEducation;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.ViewMilitaryService;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.ViewPreviousEmployment;
import woopy.com.juanmckenzie.caymanall.formsjobs.fragments.ViewReferences;

public class MyPagerAdapterDisable extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 5;

    public MyPagerAdapterDisable(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    ViewApplicantInformation applicantInformation = new ViewApplicantInformation();
    ViewEducation education = new ViewEducation();
    ViewReferences references = new ViewReferences();
    ViewPreviousEmployment previousEmployment = new ViewPreviousEmployment();
    ViewMilitaryService militaryService = new ViewMilitaryService();
    Disclaimer disclaimer = new Disclaimer();

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return applicantInformation;
            case 1:
                return education;
            case 2:
                return references;
            case 3:
                return previousEmployment;
            case 4:
                return militaryService;
            case 5:
                return disclaimer;
            default:
                return null;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Applicant Information";
        }
        if (position == 1) {
            return "Education";
        }
        if (position == 2) {
            return "References";
        }
        if (position == 3) {
            return "Previous Employment";
        }
        if (position == 4) {
            return "Military Service";
        }
        if (position == 5) {
            return "Disclaimer";
        }
        return "";

    }


}
