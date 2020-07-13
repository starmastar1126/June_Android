package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class BannerView extends AppCompatActivity {

    /* Views */
    ImageView prevImage;
    TextView Message;

    /* Variables */
    Banner adObj;

    Configs configs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bannerviewactivity);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Init views
        prevImage = findViewById(R.id.fspImage);
        configs = (Configs) getApplicationContext();


        Message = findViewById(R.id.Message);
        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        adObj = configs.getSelecteBanner();
        String imageName = adObj.getImage1().getImage1024();

        Message.setText(adObj.getTitle());


        if (!adObj.getUrl().equals("")) {
            String url = "https://" + adObj.getUrl();
            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                url = "http://" + url;
            }

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).setDefaultRequestOptions(requestOptions)
                .load(imageName).into(prevImage).clearOnDetach();


        Glide.with(getApplicationContext())
                .setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(imageName)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        findViewById(R.id.ibc_loading_pb).setVisibility(View.GONE);
                        prevImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        findViewById(R.id.ibc_loading_pb).setVisibility(View.GONE);
                        prevImage.setImageBitmap(null);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }// end onCreate()


}//@end
