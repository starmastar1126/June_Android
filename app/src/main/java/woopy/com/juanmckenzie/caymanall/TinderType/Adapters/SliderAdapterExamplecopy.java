package woopy.com.juanmckenzie.caymanall.TinderType.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import woopy.com.juanmckenzie.caymanall.FullScreenPreview;
import woopy.com.juanmckenzie.caymanall.InboxActivity;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class SliderAdapterExamplecopy extends SliderViewAdapter<SliderAdapterExamplecopy.SliderAdapterVH1> {

    private Context context;
    private List<String> images;

    public SliderAdapterExamplecopy(Context context, List<String> Images) {
        this.context = context;
        this.images = Images;
    }

    @Override
    public SliderAdapterVH1 onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider1, null);
        return new SliderAdapterVH1(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH1 viewHolder, int position) {

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


        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullScreenPreview.class);
                Bundle extras = new Bundle();
                extras.putString("imageName", images.get(position));
                i.putExtras(extras);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return images.size();
    }

    class SliderAdapterVH1 extends SliderViewAdapter.ViewHolder {

        ImageView image;
        ProgressBar ibc_loading_pb;
        View itemView;

        public SliderAdapterVH1(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Image);
            ibc_loading_pb = itemView.findViewById(R.id.ibc_loading_pb);
            this.itemView = itemView;
        }
    }
}