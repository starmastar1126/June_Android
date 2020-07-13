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

public class MyPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 6;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    ApplicantInformation applicantInformation = new ApplicantInformation();
    Education education = new Education();
    References references = new References();
    PreviousEmployment previousEmployment = new PreviousEmployment();
    MilitaryService militaryService = new MilitaryService();
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


    public Boolean CanMove(int position) {

        if (position == 0) {
            return applicantInformation.CanMove();
        }

        if (position == 1) {
            return education.CanMove();
        }

        if (position == 2) {
            return references.CanMove();
        }

        if (position == 3) {
            return previousEmployment.CanMove();
        }
        if (position == 4) {
            return militaryService.CanMove();
        }
        return false;
    }


    public void SaveAll() {
        try {
            applicantInformation.Form();
        } catch (Exception ignored) {

        }
        try {
            education.Form();
        } catch (Exception ignored) {

        }
        try {
            references.Form();
        } catch (Exception ignored) {

        }
        try {
            previousEmployment.Form();
        } catch (Exception ignored) {

        }

    }

    public void Save(int position) {

//        if (position == 0) {
//            applicantInformation.Save();
//        }
//        if (position == 1) {
//            education.Save();
//        }
//        if (position == 2) {
//            references.Save();
//        }
//        if (position == 3) {
//            previousEmployment.Save();
//        }
//        if (position == 4) {
//            militaryService.Save();
//        }
    }

}
