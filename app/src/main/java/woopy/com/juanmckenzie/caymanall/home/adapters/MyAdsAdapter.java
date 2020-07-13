package woopy.com.juanmckenzie.caymanall.home.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class MyAdsAdapter extends RecyclerView.Adapter<MyAdsAdapter.MyAdsVH> {

    private List<Ads> myAds;
    private OnMyAdClickListener onMyAdClickListener;
    Context context;

    public MyAdsAdapter(List<Ads> myAds, Context context, OnMyAdClickListener onMyAdClickListener) {
        this.myAds = myAds;
        this.onMyAdClickListener = onMyAdClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_my_ad, parent, false);
        return new MyAdsVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdsVH holder, int position) {
        Ads adObj = myAds.get(position);

        // Get ad title
        holder.titleTV.setTypeface(Configs.titSemibold);
        holder.titleTV.setText(adObj.getTitle());

        // Get ad price
        holder.priceTV.setTypeface(Configs.titRegular);
        holder.priceTV.setText(adObj.getCurrency() + String.valueOf(adObj.getPrice()));

        // Get date
        holder.timeAgoTV.setTypeface(Configs.titRegular);
        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.valueOf(adObj.getCreatedAt()));
        holder.timeAgoTV.setText(Configs.timeAgoSinceDate(date, context));


        Glide.with(context)
                .load(adObj.getImage1().getImage1024()).into(holder.pictureIV).clearOnDetach();


    }

    @Override
    public int getItemCount() {
        return myAds != null ? myAds.size() : 0;
    }

    class MyAdsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView pictureIV;
        private TextView titleTV;
        private TextView priceTV;
        private TextView timeAgoTV;

        public MyAdsVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            pictureIV = itemView.findViewById(R.id.ima_picture_iv);
            titleTV = itemView.findViewById(R.id.ima_title_tv);
            priceTV = itemView.findViewById(R.id.ima_price_tv);
            timeAgoTV = itemView.findViewById(R.id.ima_time_tv);
        }

        @Override
        public void onClick(View v) {
            if (onMyAdClickListener != null) {
                Ads adObj = myAds.get(getAdapterPosition());
                onMyAdClickListener.onAdClicked(adObj);
            }
        }
    }

    public interface OnMyAdClickListener {

        void onAdClicked(Ads adObject);
    }
}
