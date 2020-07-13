package woopy.com.juanmckenzie.caymanall.home.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.animation.content.Content;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.smarteist.autoimageslider.SliderView;

import java.util.List;
import java.util.Objects;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBanners;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterInList;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterpromotions;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 05/08/2018
 * All rights reserved
 */
public class AdsListAdapter extends RecyclerView.Adapter<AdsListAdapter.AdsListVH> {

    private List<Ads> adsList;
    List<Banner> promotionslist;
    private OnAdClickListener onAdClickListener;
    Context context;

    private static final int LIST_AD_DELTA = 5;
    private static final int CONTENT = 0;
    private static final int AD = 1;


    public AdsListAdapter(List<Ads> adsList, List<Banner> promotions, Context context, OnAdClickListener onAdClickListener) {
        this.adsList = adsList;
        this.promotionslist = promotions;
        this.onAdClickListener = onAdClickListener;
        this.context = context;
    }

    public void addMoreAds(List<Ads> moreAds) {
        int itemCountBeforeAdding = getItemCount();
        this.adsList.addAll(moreAds);
        notifyItemRangeInserted(itemCountBeforeAdding, this.adsList.size());
    }

    @NonNull
    @Override
    public AdsListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ads_list, parent, false);
        return new AdsListVH(view);
    }


    @Override
    public int getItemViewType(int position) {
        if (position > 0 && position % LIST_AD_DELTA == 0) {
            return AD;
        }
        return CONTENT;
    }


    @Override
    public void onBindViewHolder(@NonNull AdsListVH holder, int position) {


        if (getItemViewType(position) == CONTENT) {

            holder.mainlayout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            final Ads adObj = adsList.get(getRealPosition(position));


            holder.titleTV.setTypeface(Configs.titSemibold);
            holder.titleTV.setText(adObj.getTitle());
            holder.ibc_loading_pb.setVisibility(View.VISIBLE);

            if (!adObj.getType()) {
                holder.banner.setTypeface(Configs.titSemibold);
                holder.banner.setText("Paid Banner");
                holder.priceTV.setVisibility(View.GONE);
                holder.banner.setVisibility(View.VISIBLE);
            } else {
                holder.banner.setVisibility(View.GONE);
                holder.priceTV.setVisibility(View.VISIBLE);
                holder.priceTV.setTypeface(Configs.titSemibold);
                holder.priceTV.setText(adObj.getCurrency() + String.valueOf(adObj.getPrice()));
            }

            if (adObj.getCategory().toLowerCase().equals("jobs")) {
                holder.price.setVisibility(View.GONE);
            } else {
                holder.price.setVisibility(View.VISIBLE);
                holder.price.setTypeface(Configs.titSemibold);
                holder.price.setText(adObj.getCurrency() + String.valueOf(adObj.getPrice()));

            }

            if (adObj.getADStatus().equals("Pending")) {
                holder.Sold.setVisibility(View.GONE);
            } else {
                holder.Sold.setVisibility(View.VISIBLE);
            }


            if (adObj.getPaymentdone()) {
                holder.cardView.setBackground(ContextCompat.getDrawable(context, R.drawable.item_broder));
                holder.titleTV.setTextColor(Color.WHITE);
                holder.permium.setVisibility(View.VISIBLE);
            }
            if (adObj.getPaymentdone()) {
                holder.mainlayout.setBackground(ContextCompat.getDrawable(context, R.drawable.item_broder));
                holder.titleTV.setTextColor(Color.WHITE);
                holder.permium.setVisibility(View.VISIBLE);
            }


            if (adObj.getImage1() != null) {
                holder.ibc_loading_pb.setVisibility(View.GONE);
                Glide.with(context)
                        .asBitmap()
                        .load(adObj.getImage1().getImage1024())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.ibc_loading_pb.setVisibility(View.GONE);
                                holder.imageIV.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                holder.ibc_loading_pb.setVisibility(View.GONE);
                                holder.imageIV.setImageBitmap(null);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });


            }
        } else {
            holder.mainlayout.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            SliderAdapterInList adapter = new SliderAdapterInList(context, promotionslist);
            holder.image.setSliderAdapter(adapter);
        }

    }

    private int getRealPosition(int position) {
        if (LIST_AD_DELTA == 0) {
            return position;
        } else {
            return position - position / LIST_AD_DELTA;
        }
    }

    @Override
    public int getItemCount() {
        int additionalContent = 0;
        if (adsList.size() > 0 && LIST_AD_DELTA > 0 && adsList.size() > LIST_AD_DELTA) {
            additionalContent = adsList.size() / LIST_AD_DELTA;
        }
        return adsList.size() + additionalContent;
    }

    public class AdsListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTV, price;
        private ImageView imageIV, Sold, permium;
        private TextView priceTV;
        private TextView banner;
        private CardView cardView;
        ProgressBar ibc_loading_pb;
        SliderView image;

        RelativeLayout mainlayout;

        public AdsListVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTV = itemView.findViewById(R.id.ial_title_tv);
            imageIV = itemView.findViewById(R.id.ial_image_iv);
            priceTV = itemView.findViewById(R.id.ial_price_tv);
            banner = itemView.findViewById(R.id.banner);
            price = itemView.findViewById(R.id.price);
            Sold = itemView.findViewById(R.id.Sold);
            permium = itemView.findViewById(R.id.permium);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardvirew);
            mainlayout = itemView.findViewById(R.id.mainlayout);
            ibc_loading_pb = itemView.findViewById(R.id.ibc_loading_pb);
        }


        @Override
        public void onClick(View v) {
            if (onAdClickListener != null) {
                Ads clickedAdObj = adsList.get(getRealPosition(getAdapterPosition()));
                onAdClickListener.onAdClicked(clickedAdObj, getRealPosition(getAdapterPosition()));
            }
        }
    }

    public interface OnAdClickListener {

        void onAdClicked(Ads adObj, int positon);
    }
}
