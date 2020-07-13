package woopy.com.juanmckenzie.caymanall.TinderType.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.TUser;
import woopy.com.juanmckenzie.caymanall.TinderType.ProfileDetails;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class TinderCardAdapter extends RecyclerView.Adapter<TinderCardAdapter.ListItem> {
    private List<TUser> items = new ArrayList<>();
    private Context context;

    public TinderCardAdapter(List<TUser> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItem(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tinder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItem holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<TUser> getItems() {
        return items;
    }

    TUser tUser;

    public void removeTopItem() {
        tUser = items.get(0);
        items.remove(0);
        notifyDataSetChanged();
    }

    public TUser getlastUser() {
        return tUser;
    }

    public class ListItem extends RecyclerView.ViewHolder {
        TextView name, gander, miles;
        SliderView image;

        LinearLayout details;

        public ListItem(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            gander = itemView.findViewById(R.id.gander);
            image = itemView.findViewById(R.id.imageSlider);
            details = itemView.findViewById(R.id.details);
            miles = itemView.findViewById(R.id.miles);
        }

        public void bind(TUser i) {
            name.setText(i.getUsername());
            gander.setText(i.getGander());

            List<String> strings = new ArrayList<>();

            if (i.getAvatar() != null) {
                if (!i.getAvatar().equals("")) {
                    strings.add(i.getAvatar().getUrl());
                }
            }
            if (!i.getImage2().equals("")) {
                strings.add(i.getImage2().getImage512());
            }
            if (!i.getImage3().equals("")) {
                strings.add(i.getImage3().getImage512());
            }

            SliderAdapterExample adapter = new SliderAdapterExample(context, strings);
            image.setSliderAdapter(adapter);
            if (i.getShowdistance()) {
                miles.setVisibility(View.VISIBLE);
            } else {
                miles.setVisibility(View.GONE);
            }
            miles.setText(String.format("%.2f", i.getDistance()) + context.getString(R.string.miles_away));
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Configs configs = (Configs) context.getApplicationContext();
                    configs.setSelectedUser(i);
                    context.startActivity(new Intent(context, ProfileDetails.class));
                }
            });


        }
    }
}