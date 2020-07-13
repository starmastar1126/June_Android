package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.khizar1556.mkvideoplayer.MKPlayer;
import com.parse.ParseObject;

import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class WatchVideo extends AppCompatActivity {


    /* Variables */
    ParseObject adObj;


    Configs configs;
    MKPlayer mkplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_video);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        configs = (Configs) getApplicationContext();


        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        String objectID = extras.getString("objectID");

        // Get Video URL
        String videoURL = configs.getSelectedAd().getVideo();


        mkplayer = new MKPlayer(this);
        mkplayer.play(videoURL);


//        Log.i("log-", "VIDEO URL: " + videoURL);
//
//        WebView webView = findViewById(R.id.wvWebView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setSupportMultipleWindows(true);
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                setProgressBarIndeterminateVisibility(false);
//                super.onPageFinished(view, url);
//            }
//        });
//        webView.loadUrl(videoURL);
//        webView.setVisibility(View.VISIBLE);


        // MARK: - CANCEL BUTTON ------------------------------------
        Button cancButt = findViewById(R.id.wvBackButt);
        cancButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Init AdMob banner
        AdView mAdView = findViewById(R.id.admobBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }// end onCreate()


    @Override
    protected void onStop() {
        if (mkplayer != null)
            if (mkplayer.isPlaying())
                mkplayer.stop();
        super.onStop();
    }
}//end
