package woopy.com.juanmckenzie.caymanall;

/*-------------------------------

    - woopy -

    Created by cubycode @2017
    All Rights reserved

-------------------------------*/

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import uk.co.senab.photoview.PhotoViewAttacher;
import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class FullScreenPreview extends AppCompatActivity {

    /* Views */
    ImageView prevImage;

    /* Variables */

    Configs configs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_preview);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Init views
        prevImage = findViewById(R.id.fspImage);
        configs = (Configs) getApplicationContext();


        // Get objectID from previous .java
        Bundle extras = getIntent().getExtras();
        String imageName = extras.getString("imageName");

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


        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(prevImage);
        pAttacher.update();

        pAttacher.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                finish();
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }
        });


    }// end onCreate()


}//@end
