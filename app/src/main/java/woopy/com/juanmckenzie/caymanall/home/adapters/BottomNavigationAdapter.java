package woopy.com.juanmckenzie.caymanall.home.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.home.enums.BottomNavigationTab;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public class BottomNavigationAdapter extends RecyclerView.Adapter {

    private static final int REGULAR_ITEM_VIEW_TYPE = 0;
    private static final int SPECIAL_ITEM_VIEW_TYPE = 1;

    private static final int SPECIAL_ITEM_POSITION = 2;
    private static final int NO_SELECTION = -1;

    private int selectedPosition = NO_SELECTION;
    private int regularItemWidth;
    private int specialItemWidth;

    private BottomNavigationListener listener;

    public BottomNavigationAdapter(BottomNavigationListener listener, int selectedPosition) {
        this.listener = listener;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return position == SPECIAL_ITEM_POSITION ? SPECIAL_ITEM_VIEW_TYPE : REGULAR_ITEM_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (regularItemWidth == 0) {
            calculateCellWidth(parent);
        }

        int width = getItemWidth(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == REGULAR_ITEM_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_bottom_navigation_regular_tab, parent, false);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            lp.width = width;
            lp.topMargin = UIUtils.getDimension(R.dimen.bottom_navigation_view_height) -
                    UIUtils.getDimension(R.dimen.bottom_navigation_clickable_area);
            view.setLayoutParams(lp);
            return new RegularTabViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_bottom_navigation_special_tab, parent, false);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            return new SpecialTabViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int itemViewType = getItemViewType(position);
        BottomNavigationTab current = (BottomNavigationTab.values()[position]);

        if (itemViewType == REGULAR_ITEM_VIEW_TYPE) {
            RegularTabViewHolder holder = (RegularTabViewHolder) viewHolder;

            boolean isSelected = position == selectedPosition;
            int resId = isSelected ? current.getSelectedResId() : current.getUnselectedResId();
            holder.iconIV.setImageResource(resId);
            holder.titleTV.setText(current.getTitleResId());
        } else {
            SpecialTabViewHolder holder = (SpecialTabViewHolder) viewHolder;
            holder.button.setImageResource(current.getSelectedResId());
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private void calculateCellWidth(ViewGroup parent) {
        int parentWidth = parent.getWidth();
        int itemCount = getItemCount();
        int specialItemCount = BottomNavigationTab.getSpecialItemCount();
        int regularItemCount = BottomNavigationTab.values().length - specialItemCount;
        float specialItemWidthRatio = BottomNavigationTab.SPECIAL_ITEM_WIDTH_RATIO;

        float cellWidth = parentWidth / itemCount;
        float specialItemsTotalWidth = cellWidth * specialItemCount * specialItemWidthRatio;
        float regularItemsTotalWidth = (parentWidth - specialItemsTotalWidth);

        regularItemWidth = (int) (regularItemsTotalWidth / regularItemCount);
        specialItemWidth = (int) (specialItemsTotalWidth / specialItemCount);
    }

    private int getItemWidth(int viewType) {
        boolean isRegularItem = viewType == REGULAR_ITEM_VIEW_TYPE;
        return isRegularItem ? regularItemWidth : specialItemWidth;
    }

    public void setSelectedItemPosition(int selectedPosition) {
        if (selectedPosition >= 0) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }
    }

    private void onItemSelected(int selectedPosition) {
        if (listener != null) {
            if (selectedPosition == SPECIAL_ITEM_POSITION) {
                listener.onSpecialTabSelected();
                return;
            }
        }

        boolean isConsumed = listener != null && listener.onTabSelected(selectedPosition > SPECIAL_ITEM_POSITION ?
                selectedPosition - 1 : selectedPosition);
        if (!isConsumed) {
            return;
        }

        int aux = this.selectedPosition;
        this.selectedPosition = selectedPosition;

        if (aux == this.selectedPosition) {
            return;
        }

        notifyItemChanged(aux);
        notifyItemChanged(this.selectedPosition);
    }

    class RegularTabViewHolder extends RecyclerView.ViewHolder {

        ImageView iconIV;
        TextView titleTV;

        public RegularTabViewHolder(View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.ibnrt_icon_iv);
            titleTV = itemView.findViewById(R.id.ibnrt_title_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelected(getAdapterPosition());
                }
            });
        }
    }

    class SpecialTabViewHolder extends RecyclerView.ViewHolder {

        ImageButton button;

        public SpecialTabViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.ibnst_special_btn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelected(getAdapterPosition());
                }
            });
        }
    }

    public interface BottomNavigationListener {
        boolean onTabSelected(int pos);

        void onSpecialTabSelected();
    }
}
