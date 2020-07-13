package woopy.com.juanmckenzie.caymanall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Subscribe;

/**
 * @author cubycode
 * @since 11/08/2018
 * All rights reserved
 */
public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.FilterVH> {

    private List<Subscribe> categories;
    private OnFilterSelectedListener onFilterSelectedListener;
    private int selectedCategoryPosition = -1;

    public SubscribeAdapter(List<Subscribe> filterTitles, String selectedFilter,
                            OnFilterSelectedListener onFilterSelectedListener) {
        this.categories = filterTitles;
        this.onFilterSelectedListener = onFilterSelectedListener;
    }

    @NonNull
    @Override
    public FilterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_subs, parent, false);
        return new FilterVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterVH holder, final int position) {
        final Subscribe category = categories.get(position);
        holder.titleTV.setText(category.getCategory());
        for (String s : category.getSubcategory())
            holder.titleTV.setText(holder.titleTV.getText().toString() + " > " + s);


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (String s : category.getSubcategory()) {
                    String UNSUB = category.getCategory() + " > " + s;
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(UNSUB.toString().replaceAll("[^A-Za-z0-9]", ""));
                }
                FirebaseDatabase.getInstance().getReference().child("Subscribes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subs")
                        .child(position + "").removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    class FilterVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTV;
        private ImageView remove;

        public FilterVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTV = itemView.findViewById(R.id.ic_title_tv);
            remove = itemView.findViewById(R.id.remove);
        }

        @Override
        public void onClick(View v) {


            selectedCategoryPosition = getAdapterPosition();
            Subscribe selectedCategory = categories.get(selectedCategoryPosition);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public class FilterItemHolder {

        private String name;

        public FilterItemHolder(boolean isSelected, String name) {
            this.name = name;
        }
    }

    public interface OnFilterSelectedListener {

        void onFilterSelected(String filter);
    }
}
