package woopy.com.juanmckenzie.caymanall.landing;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.webkit.WebView;

import woopy.com.juanmckenzie.caymanall.R;

public class TermsOfUse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_of_use);

        // Set back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set Title of the ActionBar
        getSupportActionBar().setTitle(getString(R.string.terms_of_use));


        // Init webView
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/tou.html");


    }// end onCreate()


    // MENU BUTTONS --------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            // DEFAULT BACK BUTTON
            case android.R.id.home:
                this.finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}//@end
