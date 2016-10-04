package perfect_apps.tutors.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.fragments.TeacherDetails;
import perfect_apps.tutors.fragments.TeachersSearchResultList;
import perfect_apps.tutors.models.TeacherItem;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.OnLoadMoreListener;
import perfect_apps.tutors.utils.SquaredImageView;

/**
 * Created by mostafa on 24/06/16.
 */
public class TeachersSearchResultsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TeachersListAdapter";

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public OnLoadMoreListener mOnLoadMoreListener;
    public boolean isLoading;

    private List<TeacherItem> mDataSet;
    private static Context mContext;
    private String comingFrom;

    public TeachersSearchResultsListAdapter(Context mContext, List<TeacherItem> mDataSet, String comingFrom) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
        this.comingFrom = comingFrom;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.rate)
        TextView ratePerFive;
        // @Bind(R.id.rateStatic2)TextView rateStatic2;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.desc)
        TextView describtion;
        @Bind(R.id.costPerHour)
        TextView costPerHour;

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        @Bind(R.id.progressBar)
        ProgressBar progressBar;

        public TextView getHour() {
            return hour;
        }

        @Bind(R.id.hour)
        TextView hour;
        @Bind(R.id.avatar)
        SquaredImageView userAvatar;

//        public TextView getRateStatic2() {
//            return rateStatic2;
//        }

        public TextView getName() {
            return name;
        }

        public TextView getRatePerFive() {
            return ratePerFive;
        }

        public RatingBar getRatingBar() {
            return ratingBar;
        }

        public TextView getDescribtion() {
            return describtion;
        }

        public TextView getCostPerHour() {
            return costPerHour;
        }

        public SquaredImageView getUserAvatar() {
            return userAvatar;
        }

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Bundle arguments = new Bundle();
                    arguments.putString(Constants.DETAIL_USER_ID, mDataSet.get(getPosition()).getId());
                    arguments.putString(Constants.COMMING_FROM, comingFrom);
                    arguments.putString("rating_count", mDataSet.get(getPosition()).getRating_divide_count());
                    TeacherDetails fragment = new TeacherDetails();
                    fragment.setArguments(arguments);
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(TeacherDetails.TAG)
                            .commit();
                }
            });
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_forecast, viewGroup, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_loading_item, viewGroup, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");
            Typeface fontBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/bold.ttf");
            if (mDataSet.get(position).getName() != null &&
                    !mDataSet.get(position).getName().equalsIgnoreCase("null")) {

                viewHolder.getName().setText(mDataSet.get(position).getName());
                viewHolder.getName().setTypeface(font);
            } else {
                viewHolder.getName().setText("__");
            }

            // rate section
            // viewHolder.getRateStatic2().setText("التقييم");
            //viewHolder.getRateStatic2().setTypeface(font);

            if (mDataSet.get(position).getRating_divide_count() != null &&
                    !mDataSet.get(position).getRating_divide_count().equalsIgnoreCase("null")) {
                viewHolder.getRatePerFive().setText(String.valueOf(mDataSet.get(position).getRating_divide_count()));
                viewHolder.getRatePerFive().setTypeface(font);
            } else {
                viewHolder.getRatePerFive().setText(" _ ");
            }


            viewHolder.getRatingBar().setRating(mDataSet.get(position).getRating_per_5());

            if (mDataSet.get(position).getHour_price() != null &&
                    !mDataSet.get(position).getHour_price().trim().isEmpty() &&
                    !mDataSet.get(position).getHour_price().equalsIgnoreCase("null")) {
                viewHolder.getCostPerHour().setText(mDataSet.get(position).getHour_price());
            } else {
                viewHolder.getCostPerHour().setText("--");
            }
            viewHolder.getCostPerHour().setTypeface(fontBold);

            if (mDataSet.get(position).getDesc() != null &&
                    !mDataSet.get(position).getDesc().equalsIgnoreCase("null")) {
                viewHolder.getDescribtion().setText(mDataSet.get(position).getDesc());
                viewHolder.getDescribtion().setTypeface(font);
            } else {
                viewHolder.getDescribtion().setText("__");
            }
            viewHolder.getHour().setTypeface(font);

            // populate mainImage
            Glide.with(mContext)
                    .load(mDataSet.get(position).getImage_full_path())
                    .placeholder(R.drawable.rectangle)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            viewHolder.getProgressBar().setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(viewHolder.getUserAvatar());



        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

}
