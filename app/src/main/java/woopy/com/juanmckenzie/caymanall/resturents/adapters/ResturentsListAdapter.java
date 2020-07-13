package woopy.com.juanmckenzie.caymanall.resturents.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;
import java.util.Objects;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.resturents.Modal.PlacesDetails_Modal;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author cubycode
 * @since 05/08/2018
 * All rights reserved
 */
public class ResturentsListAdapter extends RecyclerView.Adapter<ResturentsListAdapter.AdsListVH> {

    private List<PlacesDetails_Modal> adsList;
    private OnAdClickListener onAdClickListener;
    Context context;

    public ResturentsListAdapter(List<PlacesDetails_Modal> adsList, Context context, OnAdClickListener onAdClickListener) {
        this.adsList = adsList;
        this.onAdClickListener = onAdClickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public AdsListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_resturent, parent, false);
        return new AdsListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsListVH holder, int position) {
        final PlacesDetails_Modal adObj = adsList.get(position);

        holder.name.setTypeface(Configs.titSemibold);
        holder.name.setText(adObj.name);
        holder.reviews.setTypeface(Configs.titSemibold);
        holder.reviews.setText(adObj.rating + "");
        holder.reviews.setVisibility(View.VISIBLE);
        holder.ibc_loading_pb.setVisibility(View.VISIBLE);

        if (adObj.photourl.equals("NA")) {
            holder.itemView.setVisibility(View.GONE);
        }

        holder.ibc_loading_pb.setVisibility(View.GONE);
        Glide.with(context)
                .asBitmap()
                .load(adObj.photourl)
                .into(holder.imageIV);
    }

    @Override
    public int getItemCount() {
        return adsList != null ? adsList.size() : 0;
    }

    public class AdsListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private ImageView imageIV;
        private TextView reviews;
        ProgressBar ibc_loading_pb;

        public AdsListVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.ial_title_tv);
            imageIV = itemView.findViewById(R.id.ial_image_iv);
            reviews = itemView.findViewById(R.id.review);
            ibc_loading_pb = itemView.findViewById(R.id.ibc_loading_pb);
        }

        private void loadImage() {


        }

        @Override
        public void onClick(View v) {
            if (onAdClickListener != null) {
                PlacesDetails_Modal clickedAdObj = adsList.get(getAdapterPosition());
                onAdClickListener.onAdClicked(clickedAdObj);
            }
        }
    }

    public interface OnAdClickListener {

        void onAdClicked(PlacesDetails_Modal adObj);
    }
}
