package woopy.com.juanmckenzie.caymanall.home.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import woopy.com.juanmckenzie.caymanall.Objects.Categories;
import woopy.com.juanmckenzie.caymanall.TinderType.TinderHome;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.resturents.ResturentsactivityActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.wizard.WizardActivity;

/**
 * @author cubycode
 * @since 05/08/2018
 * All rights reserved
 */
public class BrowseCategoriesAdapter extends RecyclerView.Adapter<BrowseCategoriesAdapter.BrowseCategoryVH> {

    List<Categories> categories;
    Activity activity;
    private OnCategorySelectedListener onCategorySelectedListener;
    Configs configs;


    public BrowseCategoriesAdapter(List<Categories> categorieslist, Activity activity) {
        this.categories = categorieslist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BrowseCategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_browse_category, parent, false);
        return new BrowseCategoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BrowseCategoryVH holder, final int position) {
        final Categories cObj = categories.get(position);

        holder.titleTV.setTypeface(Configs.qsBold);
        holder.titleTV.setText(cObj.getCategory().toUpperCase());


        if (categories.get(position).getCategory().contains("Find Partner")) {
            holder.titleTV.setBackgroundColor(activity.getResources().getColor(R.color.findpartner));


            holder.loadingPB.setVisibility(View.VISIBLE);

            Categories categoryObj = categories.get(position);
            Glide.with(activity)
                    .asBitmap()
                    .load(categoryObj.getImageUrl())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            holder.loadingPB.setVisibility(View.GONE);
                            holder.imageIV.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            holder.loadingPB.setVisibility(View.GONE);
                            holder.imageIV.setImageBitmap(null);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        } else {

            holder.titleTV.setBackgroundColor(activity.getResources().getColor(R.color.darkOverlay));
            holder.loadingPB.setVisibility(View.VISIBLE);

            Categories categoryObj = categories.get(position);
            Glide.with(activity)
                    .asBitmap()
                    .load(categoryObj.getImageUrl())
                    .into(holder.imageIV);
            holder.loadingPB.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configs = (Configs) activity.getApplicationContext();
                if (categories.get(position).getCategory().contains("Find Partner")) {


                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                        if (configs.getCurrentUser().getVisibility() == null) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                            alert.setMessage(activity.getString(R.string.by_entering_this_category_you_agree))
                                    .setTitle(R.string.app_name)
                                    .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Configs.showPD(activity.getString(R.string.please_wait), activity);
                                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("visibility").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Configs.hidePD();
                                                    String selectedCategoryName = cObj.getCategory();
                                                    Intent adsListIntent = new Intent(activity, TinderHome.class);
                                                    adsListIntent.putExtra(AdsListActivity.CATEGORY_NAME_KEY, selectedCategoryName);
                                                    activity.startActivity(adsListIntent);
                                                }
                                            });

                                        }
                                    })
                                    .setIcon(R.drawable.logo);
                            alert.create().show();

                        } else {
                            String selectedCategoryName = cObj.getCategory();
                            Intent adsListIntent = new Intent(activity, TinderHome.class);
                            adsListIntent.putExtra(AdsListActivity.CATEGORY_NAME_KEY, selectedCategoryName);
                            activity.startActivity(adsListIntent);
                        }

                    } else {
                        activity.startActivity(new Intent(activity, WizardActivity.class));

                    }

                } else if (categories.get(position).getCategory().contains("Restaurants")) {
                    String selectedCategoryName = cObj.getCategory();
                    Intent adsListIntent = new Intent(activity, ResturentsactivityActivity.class);
                    adsListIntent.putExtra(AdsListActivity.CATEGORY_NAME_KEY, selectedCategoryName);
                    activity.startActivity(adsListIntent);
                } else {
                    String selectedCategoryName = cObj.getCategory();
                    Intent adsListIntent = new Intent(activity, AdsListActivity.class);
                    adsListIntent.putExtra(AdsListActivity.CATEGORY_NAME_KEY, selectedCategoryName);
                    activity.startActivity(adsListIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class BrowseCategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTV;
        private ImageView imageIV;
        private ProgressBar loadingPB;

        public BrowseCategoryVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            titleTV = itemView.findViewById(R.id.ibc_title_tv);
            imageIV = itemView.findViewById(R.id.ibc_image_iv);
            loadingPB = itemView.findViewById(R.id.ibc_loading_pb);
        }

        @Override
        public void onClick(View v) {
            if (onCategorySelectedListener != null) {
                Categories cat = categories.get(getAdapterPosition());
                onCategorySelectedListener.onCategorySelected(cat);
            }
        }
    }

    public interface OnCategorySelectedListener {

        void onCategorySelected(Categories categoryObj);
    }
}
