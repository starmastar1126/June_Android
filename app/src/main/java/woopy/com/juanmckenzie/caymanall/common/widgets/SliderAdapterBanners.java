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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import woopy.com.juanmckenzie.caymanall.BannerAll;
import woopy.com.juanmckenzie.caymanall.BannerView;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.promotions;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class SliderAdapterBanners extends SliderViewAdapter<SliderAdapterBanners.SliderAdapterVH> {

    private Context context;
    private List<Banner> promotions;

    public SliderAdapterBanners(Context context, List<Banner> Images) {
        this.context = context;
        this.promotions = Images;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Glide.with(context).load(promotions.get(position).getImage1().getImage1024()).into(viewHolder.gifView);
        viewHolder.gifView.setVisibility(View.VISIBLE);
        viewHolder.promotionsimage.setVisibility(View.GONE);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseDatabase.getInstance().getReference().child("Banners").child(promotions.get(position).getID()).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Configs configs = (Configs) context.getApplicationContext();
                configs.setSelecteBanner(promotions.get(position));
                configs.setBanners(promotions);
                configs.setSelectedbanner(position);
                Intent intent = new Intent(context, BannerAll.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getCount() {
        return promotions.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView promotionsimage;
        View itemView;
        GifImageView gifView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            promotionsimage = itemView.findViewById(R.id.promotionsimage);
            gifView = itemView.findViewById(R.id.gif);
            this.itemView = itemView;
        }
    }
}