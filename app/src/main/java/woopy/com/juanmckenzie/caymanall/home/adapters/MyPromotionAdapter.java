package woopy.com.juanmckenzie.caymanall.home.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.promotions;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class MyPromotionAdapter extends RecyclerView.Adapter<MyPromotionAdapter.MyAdsVH> {

    private List<promotions> myAds;
    private OnMyAdClickListener onMyAdClickListener;
    Context context;

    public MyPromotionAdapter(List<promotions> myAds, Context context, OnMyAdClickListener onMyAdClickListener) {
        this.myAds = myAds;
        this.onMyAdClickListener = onMyAdClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_my_banner, parent, false);
        return new MyAdsVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdsVH holder, int position) {
        promotions adObj = myAds.get(position);

        // Get ad title
        holder.name.setTypeface(Configs.titSemibold);
        holder.name.setText(adObj.getMessage());
        Date date1 = Calendar.getInstance().getTime();
        date1.setTime(Long.valueOf(adObj.getCreatedAt()));
        Date date2 = Calendar.getInstance().getTime();
        long diff = date2.getTime() - date1.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        Long left = 30 - days;
        holder.daysletf.setText(left + context.getString(R.string.days_left));

        try {
            holder.views.setText(adObj.getViews().values().size() + context.getString(R.string.views1));
        } catch (Exception ex) {
            holder.views.setText(context.getString(R.string.zero_views));
        }
        Glide.with(context)
                .load(adObj.getImage1().getImage1024()).into(holder.Image).clearOnDetach();


    }

    @Override
    public int getItemCount() {
        return myAds != null ? myAds.size() : 0;
    }

    class MyAdsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView Image;
        private TextView name, daysletf, views;

        public MyAdsVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            Image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            daysletf = itemView.findViewById(R.id.daysleft);
            views = itemView.findViewById(R.id.views);
        }

        @Override
        public void onClick(View v) {
            if (onMyAdClickListener != null) {
                promotions adObj = myAds.get(getAdapterPosition());
                onMyAdClickListener.onAdClicked(adObj);
            }
        }
    }

    public interface OnMyAdClickListener {

        void onAdClicked(promotions adObject);
    }
}
