package perfect_apps.tutors.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.models.TeacherItem;

/**
 * Created by mostafa on 24/06/16.
 */
public class TeachersListAdapter extends RecyclerView.Adapter<TeachersListAdapter.ViewHolder> {
    private static final String TAG = "TeachersListAdapter";

    private List<TeacherItem> mDataSet;
    private static Context mContext;

    public TeachersListAdapter(Context mContext, List<TeacherItem> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)TextView name;
        @Bind(R.id.rate)TextView ratePerFive;
        @Bind(R.id.rateStatic1)TextView rateStatic1;
        @Bind(R.id.rateStatic2)TextView rateStatic2;
        @Bind(R.id.ratingBar)RatingBar ratingBar;
        @Bind(R.id.desc) TextView describtion;
        @Bind(R.id.costPerHour) TextView costPerHour;

        public TextView getHour() {
            return hour;
        }

        @Bind(R.id.hour) TextView hour;
        @Bind(R.id.avatar) ImageView userAvatar;

        public TextView getRateStatic1() {
            return rateStatic1;
        }

        public TextView getRateStatic2() {
            return rateStatic2;
        }

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

        public ImageView getUserAvatar() {
            return userAvatar;
        }

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_forecast, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/bold.ttf");
        viewHolder.getName().setText(mDataSet.get(position).getName());
        viewHolder.getName().setTypeface(font);

        // rate section
        viewHolder.getRateStatic2().setText("التقييم");
        viewHolder.getRateStatic2().setTypeface(font);
        viewHolder.getRateStatic1().setText("/");
        viewHolder.getRateStatic1().setTypeface(font);

        viewHolder.getRatePerFive().setText(String.valueOf(mDataSet.get(position).getRating_per_5()));
        viewHolder.getRatePerFive().setTypeface(font);
        viewHolder.getRatingBar().setRating(mDataSet.get(position).getRating_per_5());

        if (mDataSet.get(position).getHour_price() != null &&
                !mDataSet.get(position).getHour_price().trim().isEmpty()) {
            viewHolder.getCostPerHour().setText(mDataSet.get(position).getHour_price());
        } else {
            viewHolder.getCostPerHour().setText("-");
        }
        viewHolder.getCostPerHour().setTypeface(fontBold);

        viewHolder.getDescribtion().setText(mDataSet.get(position).getDesc());
        viewHolder.getDescribtion().setTypeface(font);
        viewHolder.getHour().setTypeface(font);

        // populate mainImage
        Picasso.with(mContext)
                .load(mDataSet.get(position).getImage_full_path())
                .placeholder(R.drawable.login_user_ico)
                .into(viewHolder.getUserAvatar());



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
