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
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import woopy.com.juanmckenzie.caymanall.BannerAll;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.promotions;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class SliderAdapterInList extends SliderViewAdapter<SliderAdapterInList.SliderAdapterVH> {
    private Context context;
    private List<Banner> promotions;

    public SliderAdapterInList(Context context, List<Banner> Images) {
        this.context = context;
        this.promotions = Images;
    }

    @Override
    public SliderAdapterInList.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bannerinlist, null);
        return new SliderAdapterInList.SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterInList.SliderAdapterVH viewHolder, int position) {


        viewHolder.titleTV.setTypeface(Configs.titSemibold);
        viewHolder.titleTV.setText(promotions.get(position).getTitle());
        Glide.with(context).load(promotions.get(position).getImage1().getImage1024()).into(viewHolder.imageIV);
        viewHolder.imageIV.setVisibility(View.VISIBLE);
        viewHolder.ibc_loading_pb.setVisibility(View.GONE);
        viewHolder.viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://" + promotions.get(position).getUrl();
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseDatabase.getInstance().getReference().child("Banners").child(promotions.get(position).getID()).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return promotions.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {


        private TextView titleTV, viewmore;
        ProgressBar ibc_loading_pb;
        GifImageView imageIV;
        View itemView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.ial_title_tv);
            imageIV = itemView.findViewById(R.id.ial_image_iv);
            viewmore = itemView.findViewById(R.id.viewmore);
            ibc_loading_pb = itemView.findViewById(R.id.ibc_loading_pb);
            this.itemView = itemView;
        }
    }
}