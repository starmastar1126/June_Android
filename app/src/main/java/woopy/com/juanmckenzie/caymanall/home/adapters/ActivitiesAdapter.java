package woopy.com.juanmckenzie.caymanall.home.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Activity;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

/**
 * @author sergiupuhalschi
 * @since 28/10/2018
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityVH> {

    private List<Activity> activityList;
    private ActivityItemListener activityItemListener;
    Context context;

    public ActivitiesAdapter(List<Activity> activityList, Context context, ActivityItemListener activityItemListener) {
        this.activityList = activityList;
        this.activityItemListener = activityItemListener;
        this.context = context;
    }

    public void addMoreActivities(List<Activity> moreActivities) {
        int itemCountBeforeAdding = getItemCount();
        this.activityList.addAll(moreActivities);
        notifyItemRangeInserted(itemCountBeforeAdding, this.activityList.size());
    }

    public void removeActivity(Activity activityObj) {
        int posToRemove = getActivityPos(activityObj);
        if (posToRemove < 0 || posToRemove > activityList.size() - 1) {
            return;
        }
        activityList.remove(posToRemove);
        notifyItemRemoved(posToRemove);
    }

    private int getActivityPos(Activity activityObj) {
        for (int i = 0; i < activityList.size(); i++) {
            Activity obj = activityList.get(i);
            if (obj == activityObj) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public ActivityVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cell_activity, parent, false);
        return new ActivityVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityVH holder, int position) {
        Activity actObj = activityList.get(position);

        holder.actTxt.setText(actObj.getText());
        Date date = Calendar.getInstance().getTime();
        date.setTime(Long.valueOf(actObj.getCreatedAt()));
        holder.dateTxt.setText(Configs.timeAgoSinceDate(date,context));

        holder.avImage.setImageResource(0);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo1);
        requestOptions.error(R.drawable.logo1);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(actObj.getOtherUser().getAvatar()).into(holder.avImage).clearOnDetach();
    }

    @Override
    public int getItemCount() {
        return activityList != null ? activityList.size() : 0;
    }

    private void loadUserAvatar(ParseObject actObj, final ImageView avatarIV) {
        actObj.getParseObject(Configs.ACTIVITY_OTHER_USER).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject userPointer, ParseException e) {
                Configs.getParseImage(avatarIV, userPointer, Configs.USER_AVATAR);
            }
        });
    }

    public class ActivityVH extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        TextView actTxt;
        TextView dateTxt;
        ImageView avImage;

        public ActivityVH(View itemView) {
            super(itemView);
            initViews();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void initViews() {
            actTxt = itemView.findViewById(R.id.actTextTxt);
            dateTxt = itemView.findViewById(R.id.actDateTxt);
            avImage = itemView.findViewById(R.id.actAvatarImg);

            actTxt.setTypeface(Configs.titRegular);
            dateTxt.setTypeface(Configs.titRegular);
        }

        @Override
        public void onClick(View v) {
            if (activityItemListener != null) {
                Activity activityObj = activityList.get(getAdapterPosition());
                activityItemListener.onActivityClicked(activityObj);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (activityItemListener != null) {
                Activity activityObj = activityList.get(getAdapterPosition());
                activityItemListener.onActivityLongClicked(activityObj);
            }
            return false;
        }
    }

    public interface ActivityItemListener {

        void onActivityClicked(Activity activityObj);

        void onActivityLongClicked(Activity activityObj);
    }
}
