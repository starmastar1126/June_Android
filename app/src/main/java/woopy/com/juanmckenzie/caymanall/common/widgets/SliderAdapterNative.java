package woopy.com.juanmckenzie.caymanall.common.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pl.droidsonroids.gif.GifImageView;
import woopy.com.juanmckenzie.caymanall.Objects.promotions;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class SliderAdapterNative extends SliderViewAdapter<SliderAdapterNative.SliderAdapterVH> {

    private Context context;
    private List<promotions> promotions;

    public SliderAdapterNative(Context context, List<promotions> Images) {
        this.context = context;
        this.promotions = Images;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion_native, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {


        FirebaseDatabase.getInstance().getReference().child("promotionsall").child(promotions.get(position).getId()).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        viewHolder.promotionstext.setText(promotions.get(position).getMessage());
        viewHolder.opensite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://" + promotions.get(position).getWebsite();
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });


        try {
            if (promotions.get(position).getImage1().getUrl() == null)
                return;

            if (promotions.get(position).getType().equals("img")) {
                Glide.with(context)
                        .asBitmap()
                        .load(promotions.get(position).getImage1().getImage1024())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                viewHolder.ibc_loading_pb.setVisibility(View.GONE);
                                viewHolder.promotionsimage.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                viewHolder.ibc_loading_pb.setVisibility(View.GONE);
                                viewHolder.promotionsimage.setImageBitmap(null);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });


            } else {
                Glide.with(context).load(promotions.get(position).getImage1().getUrl()).transform(new CenterInside(), new RoundedCorners(24))
                        .into(viewHolder.gifView);
                viewHolder.gifView.setVisibility(View.VISIBLE);
                viewHolder.promotionsimage.setVisibility(View.GONE);
                viewHolder.ibc_loading_pb.setVisibility(View.GONE);
            }
        } catch (Exception ex) {

        }


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return promotions.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        TextView opensite;
        TextView promotionstext;
        ImageView promotionsimage;
        ProgressBar ibc_loading_pb;
        View itemView;
        GifImageView gifView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            promotionsimage = itemView.findViewById(R.id.promotionsimage);
            ibc_loading_pb = itemView.findViewById(R.id.ibc_loading_pb);
            promotionstext = itemView.findViewById(R.id.promotionstext);
            opensite = itemView.findViewById(R.id.opensite);
            gifView = itemView.findViewById(R.id.gif);
            this.itemView = itemView;
        }
    }
}