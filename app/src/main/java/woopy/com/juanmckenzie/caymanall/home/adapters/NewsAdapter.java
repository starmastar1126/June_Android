package woopy.com.juanmckenzie.caymanall.home.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.Objects.EventObj;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterInList;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyAdsVH> {

    private List<EventObj> myAds;
    private OnMyAdClickListener onMyAdClickListener;
    Context context;
    private static final int LIST_AD_DELTA = 5;
    private static final int CONTENT = 0;
    private static final int AD = 1;

    List<Banner> promotionslist;

    public NewsAdapter(List<EventObj> myAds, List<Banner> promotions, Context context, OnMyAdClickListener onMyAdClickListener) {
        this.myAds = myAds;
        this.promotionslist = promotions;
        this.onMyAdClickListener = onMyAdClickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public MyAdsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_news_list, parent, false);
        return new MyAdsVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdsVH holder, int position) {


        if (getItemViewType(position) == CONTENT) {
            holder.mainlayout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            EventObj adObj = myAds.get(getRealPosition(position));

            // Get ad title
            holder.titleTV.setTypeface(Configs.titSemibold);
            holder.titleTV.setText(adObj.getTitle());

            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                FirebaseDatabase.getInstance().getReference().child("News").child(myAds.get(getRealPosition(position)).getID()).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());


            Glide.with(context)
                    .load(adObj.getImage1().getImage1024()).into(holder.pictureIV).clearOnDetach();
        } else {

            holder.mainlayout.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            SliderAdapterInList adapter = new SliderAdapterInList(context, promotionslist);
            holder.image.setSliderAdapter(adapter);
        }


    }


    @Override
    public int getItemViewType(int position) {
        if (position > 0 && position % LIST_AD_DELTA == 0) {
            return AD;
        }
        return CONTENT;
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
        if (myAds.size() > 0 && LIST_AD_DELTA > 0 && myAds.size() > LIST_AD_DELTA) {
            additionalContent = myAds.size() / LIST_AD_DELTA;
        }
        return myAds.size() + additionalContent;
    }

    class MyAdsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView pictureIV;
        private TextView titleTV;
        SliderView image;

        LinearLayout mainlayout;

        public MyAdsVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            pictureIV = itemView.findViewById(R.id.ima_picture_iv);
            titleTV = itemView.findViewById(R.id.Tittle);
            image = itemView.findViewById(R.id.image);
            mainlayout = itemView.findViewById(R.id.mainlayout);
        }

        @Override
        public void onClick(View v) {
            if (onMyAdClickListener != null) {
                EventObj adObj = myAds.get(getRealPosition(getAdapterPosition()));
                onMyAdClickListener.onAdClicked(adObj);
            }
        }
    }

    public interface OnMyAdClickListener {

        void onAdClicked(EventObj adObject);
    }
}
