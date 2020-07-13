package woopy.com.juanmckenzie.caymanall.TinderType.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<String> images;

    public SliderAdapterExample(Context context, List<String> Images) {
        this.context = context;
        this.images = Images;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Glide.with(context)
                .asBitmap()
                .load(images.get(position))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        viewHolder.ibc_loading_pb.setVisibility(View.GONE);
                        viewHolder.image.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        viewHolder.ibc_loading_pb.setVisibility(View.GONE);
                        viewHolder.image.setImageBitmap(null);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return images.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView image;
        ProgressBar ibc_loading_pb;
        View itemView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Image);
            ibc_loading_pb = itemView.findViewById(R.id.ibc_loading_pb);
            this.itemView = itemView;
        }
    }
}