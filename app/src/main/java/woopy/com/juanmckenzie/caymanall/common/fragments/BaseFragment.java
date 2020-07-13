package woopy.com.juanmckenzie.caymanall.common.fragments;

import android.app.Dialog;

import androidx.fragment.app.Fragment;

import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class BaseFragment extends Fragment {

    private Dialog progressDialog;

    protected void showLoading() {
        try {
            progressDialog = Configs.buildProgressLoadingDialog(getActivity());
            progressDialog.show();
        } catch (Exception ignored) {

        }
    }

    protected void hideLoading() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception ignored) {

        }

    }
}
