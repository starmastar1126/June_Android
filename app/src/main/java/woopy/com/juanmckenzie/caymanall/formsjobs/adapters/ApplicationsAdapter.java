package woopy.com.juanmckenzie.caymanall.formsjobs.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import woopy.com.juanmckenzie.caymanall.Objects.Ads;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsView;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author cubycode
 * @since 05/08/2018
 * All rights reserved
 */
public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.AdsListVH> {

    private List<Formobject> adsList;
    private OnAdClickListener onAdClickListener;
    Context context;

    public ApplicationsAdapter(List<Formobject> adsList, Context con) {
        this.adsList = adsList;
        this.onAdClickListener = onAdClickListener;
        this.context = con;
    }

    public void addMoreAds(List<Formobject> moreAds) {
        int itemCountBeforeAdding = getItemCount();
        this.adsList.addAll(moreAds);
        notifyItemRangeInserted(itemCountBeforeAdding, this.adsList.size());
    }

    @NonNull
    @Override
    public AdsListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_applications, parent, false);
        return new AdsListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsListVH holder, int position) {
        final Formobject adObj = adsList.get(position);

        holder.username.setTypeface(Configs.titSemibold);
        holder.username.setText(adObj.getUser().getEmail());


        holder.email.setTypeface(Configs.titSemibold);
        holder.email.setText(adObj.getUser().getUsername());


        // Get date
        holder.time.setTypeface(Configs.titRegular);
        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.valueOf(adObj.getCreatedat()));
        holder.time.setText(Configs.timeAgoSinceDate(date,context));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Configs configs = (Configs) context.getApplicationContext();
                configs.setSelectedform(adObj);
                context.startActivity(new Intent(context, FormsView.class));
            }
        });
        holder.loadImage();
    }

    @Override
    public int getItemCount() {
        return adsList != null ? adsList.size() : 0;
    }

    public class AdsListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView username, email, time;
        private ImageView imageIV;

        public AdsListVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            email = itemView.findViewById(R.id.username);
            username = itemView.findViewById(R.id.email);
            time = itemView.findViewById(R.id.ima_time_tv);
            imageIV = itemView.findViewById(R.id.profile);
        }

        private void loadImage() {
            final Formobject adObj = adsList.get(getAdapterPosition());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.logo1);
            requestOptions.error(R.drawable.logo1);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(Objects.requireNonNull(context)).setDefaultRequestOptions(requestOptions)
                    .load(adObj.getUser().getAvatar()).into(imageIV).clearOnDetach();
        }

        @Override
        public void onClick(View v) {
            if (onAdClickListener != null) {
                Formobject clickedAdObj = adsList.get(getAdapterPosition());
                onAdClickListener.onAdClicked(clickedAdObj);
            }
        }
    }

    public interface OnAdClickListener {

        void onAdClicked(Formobject adObj);
    }
}
