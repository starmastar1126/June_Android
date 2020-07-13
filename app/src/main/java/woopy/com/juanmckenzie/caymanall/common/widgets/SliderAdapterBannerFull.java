package woopy.com.juanmckenzie.caymanall.common.widgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import woopy.com.juanmckenzie.caymanall.BannerView;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class SliderAdapterBannerFull extends SliderViewAdapter<SliderAdapterBannerFull.SliderAdapterVH> {

    private Context context;
    private List<Banner> promotions;

    public SliderAdapterBannerFull(Context context, List<Banner> Images) {
        this.context = context;
        this.promotions = Images;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_full, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Glide.with(context).load(promotions.get(position).getImage1().getImage1024()).into(viewHolder.gifView);
        viewHolder.gifView.setVisibility(View.VISIBLE);
        viewHolder.Message.setText(promotions.get(position).getTitle());

        if (promotions.get(position).getUrl().equals("")) {
            viewHolder.Website.setVisibility(View.GONE);
        } else {
            viewHolder.Website.setVisibility(View.VISIBLE);
        }

        viewHolder.Website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://" + promotions.get(position).getUrl();
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configs configs = (Configs) context.getApplicationContext();
                configs.setSelecteBanner(promotions.get(position));
                context.startActivity(new Intent(context, BannerView.class));
            }
        });


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return promotions.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        TextView Message, Website;
        GifImageView gifView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            gifView = itemView.findViewById(R.id.gif);
            Message = itemView.findViewById(R.id.Message);
            Website = itemView.findViewById(R.id.Website);
            this.itemView = itemView;
        }
    }
}