package woopy.com.juanmckenzie.caymanall.utils;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import woopy.com.juanmckenzie.caymanall.R;


public class MarshMallowPermission {

    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }



    // CHECK PERMISSIONS -------------------------------------------------------------------------
    public boolean checkPermissionForLocation() {
        int result = ContextCompat.checkSelfPermission(UIUtils.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForRecord() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForReadExternalStorage() {
        int result = ContextCompat.checkSelfPermission(UIUtils.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForWriteExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }





    // REQUEST PERMISSIONS -------------------------------------------------------------------------
    public void requestPermissionForLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Toast.makeText(activity, "Location permission is needed to search Ads nearby your location. Please enable Location in App Settings.", Toast.LENGTH_LONG).show();
            Configs.simpleAlert(activity.getString(R.string.permission_location), activity);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void requestPermissionForRecord() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(activity, activity.getString(R.string.permission_audio), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.RECORD_AUDIO}, 2);
        }
    }


    public void requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, activity.getString(R.string.permission_storage), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
    }

    public void requestPermissionForWriteExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, activity.getString(R.string.permission_external_storage), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
        }
    }

    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity, activity.getString(R.string.permission_camera), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.CAMERA}, 5);
        }
    }



}//@end
