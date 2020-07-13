package woopy.com.juanmckenzie.caymanall.common.activities;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class BaseActivity extends AppCompatActivity {

    private Dialog progressDialog;

    protected void showLoading() {
        try {
            progressDialog = Configs.buildProgressLoadingDialog(this);
            if (progressDialog != null)
                progressDialog.show();
        } catch (Exception ignored) {

        }
    }

    protected void hideLoading() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception ex) {

        }
    }
}
