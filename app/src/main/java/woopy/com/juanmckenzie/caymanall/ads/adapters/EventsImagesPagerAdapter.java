package woopy.com.juanmckenzie.caymanall.ads.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 07/08/2018
 * All rights reserved
 */
public class EventsImagesPagerAdapter extends PagerAdapter {

    private EventObj adObject;
    private OnImageClickListener onImageClickListener;
    private Bitmap firstImageBmp = null;

    List<String> Images = new ArrayList<>();
    Context context;

    public EventsImagesPagerAdapter(EventObj adObject, Context context, OnImageClickListener onImageClickListener) {
        this.context = context;
        Images = new ArrayList<>();
        if (adObject.getImage1().getUrl() != null) {
            Images.add(adObject.getImage1().getImage1024());
        }
        if (adObject.getImage2().getUrl() != null) {
            Images.add(adObject.getImage2().getImage1024());
        }
        if (adObject.getImage3().getUrl() != null) {
            Images.add(adObject.getImage3().getImage1024());
        }
        if (adObject.getImage4().getUrl() != null) {
            Images.add(adObject.getImage4().getImage1024());
        }
        if (adObject.getImage5().getUrl() != null) {
            Images.add(adObject.getImage5().getImage1024());
        }
        if (adObject.getImage6().getUrl() != null) {
            Images.add(adObject.getImage6().getImage1024());
        }

        this.adObject = adObject;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_ad_image, container, false);
        final String adImage = Images.get(position);
        final ImageView imageIV = layout.findViewById(R.id.iai_image_iv);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(adImage).into(new CustomViewTarget(imageIV) {

            @Override
            protected void onResourceCleared(@Nullable Drawable placeholder) {
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                imageIV.setImageBitmap(null);
                imageIV.setOnClickListener(null);
            }

            @Override
            public void onResourceReady(@NonNull Object resource, @Nullable Transition transition) {
                firstImageBmp = ((BitmapDrawable) resource).getBitmap();
                imageIV.setImageBitmap(firstImageBmp);
                imageIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onImageClickListener != null) {
                            onImageClickListener.onImageClicked(adImage);
                        }
                    }
                });
            }
        });

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    public Bitmap getFirstImageBmp() {
        return firstImageBmp;
    }


    public interface OnImageClickListener {

        void onImageClicked(String imageFieldKey);
    }
}
