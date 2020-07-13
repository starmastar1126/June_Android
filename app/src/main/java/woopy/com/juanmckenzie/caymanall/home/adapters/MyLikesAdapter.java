package woopy.com.juanmckenzie.caymanall.home.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class MyLikesAdapter extends RecyclerView.Adapter<MyLikesAdapter.MyLikesVH> {

    private List<Ads> likesObjects;
    private MyLikesClickListener myLikesClickListener;

    Context context;

    public MyLikesAdapter(List<Ads> likesObjects, Context context, MyLikesClickListener myLikesClickListener) {
        this.likesObjects = likesObjects;
        this.myLikesClickListener = myLikesClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyLikesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_my_likes, parent, false);
        return new MyLikesVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLikesVH holder, int position) {
        Ads likeObj = likesObjects.get(position);
        holder.bind(likeObj);
    }

    @Override
    public int getItemCount() {
        return likesObjects != null ? likesObjects.size() : 0;
    }

    class MyLikesVH extends RecyclerView.ViewHolder {

        private RelativeLayout adRL;
        private ImageView imageIV;
        private TextView priceTV;
        private TextView titleTV;
        private ImageView unlikeIV;

        public MyLikesVH(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            adRL = itemView.findViewById(R.id.iml_ad_rl);
            imageIV = itemView.findViewById(R.id.iml_image_iv);
            priceTV = itemView.findViewById(R.id.iml_price_tv);
            titleTV = itemView.findViewById(R.id.iml_title_tv);
            unlikeIV = itemView.findViewById(R.id.iml_remove_iv);

            titleTV.setTypeface(Configs.titRegular);
            priceTV.setTypeface(Configs.titRegular);
        }

        private void bind(Ads likeObj) {
            loadAdObject(likeObj);
        }

        private void loadAdObject(Ads likeObj) {
            if (likeObj != null) {
                unlikeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myLikesClickListener != null) {
                            Ads likeObj = likesObjects.get(getAdapterPosition());
                            myLikesClickListener.onUnlikeClicked(likeObj);
                        }
                    }
                });
                if (likeObj.getReported()) {
                    setUpReportedAd();
                } else {
                    setUpAd(likeObj);
                }
            } else {
                setUpReportedAd();
            }
        }

        private void setUpAd(final Ads adObject) {
            adRL.setEnabled(true);
            titleTV.setText(adObject.getTitle());
            priceTV.setText(adObject.getCurrency() + String.valueOf(adObject.getPrice()));

            adRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myLikesClickListener != null) {
                        myLikesClickListener.onAdClicked(adObject.getID());
                    }
                }
            });
            unlikeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myLikesClickListener != null) {
                        Ads likeObj = likesObjects.get(getAdapterPosition());
                        myLikesClickListener.onUnlikeClicked(likeObj);
                    }
                }
            });

            Glide.with(context)
                    .load(adObject.getImage1().getImage1024()).into(imageIV).clearOnDetach();

        }

        private void setUpReportedAd() {
            imageIV.setImageResource(R.drawable.report_image);
            titleTV.setText(context.getString(R.string.nan1));
            priceTV.setText(context.getString(R.string.nan2));
            adRL.setEnabled(false);
        }
    }

    public interface MyLikesClickListener {

        void onAdClicked(String adObjId);

        void onUnlikeClicked(Ads likeObject);
    }
}
