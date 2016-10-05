package perfect_apps.tutors.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import de.hdodenhof.circleimageview.CircleImageView;
import perfect_apps.tutors.R;
import perfect_apps.tutors.models.Messages;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.OnLoadMoreListener;


/**
 * Created by mostafa on 15/04/16.
 */
public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public OnLoadMoreListener mOnLoadMoreListener;
    public boolean isLoading;


    private static final String TAG = "CustomAdapter";
    private static Context mContext;
    private List<Messages> mDataSet;
    private int SELF = 100;

    /**
     * Initialize the constructor of the Adapter.
     *
     * @param mDataSet String[] containing the data to populate views to be used by RecyclerView.
     * @param mContext Context hold context
     */
    public MessagesAdapter(Context mContext, List<Messages> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }

    /**
     * Provide a reference to the type of views (custom ViewHolder)
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message)
        TextView message;
        @Bind(R.id.conversation_avatar)
        CircleImageView avatarImage;
        @Bind(R.id.timestamp)
        TextView authorAndTimestamp;
        @Bind(R.id.showFlag)
        ImageView showFlag;

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        @Bind(R.id.progressBar)
        ProgressBar progressBar;

        public TextView getMessage() {
            return message;
        }

        public CircleImageView getAvatarImage() {
            return avatarImage;
        }

        public TextView getAuthorAndTimestamp() {
            return authorAndTimestamp;
        }

        public ImageView getShowFlag() {
            return showFlag;
        }

        public ItemViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            ButterKnife.bind(this, v);
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
        if (mDataSet.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else if (mDataSet.get(position)
                .getMessageOwnerEmail()
                .equalsIgnoreCase(new TutorsPrefStore(mContext)
                        .getPreferenceValue(Constants.STUDENT_EMAIL))
                || mDataSet.get(position).getMessageOwnerEmail()
                .equalsIgnoreCase(new TutorsPrefStore(mContext)
                        .getPreferenceValue(Constants.TEACHER_EMAIL))) {
            return SELF;
        } else {

            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_yourself_item, viewGroup, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_item, viewGroup, false);
            return new LoadingViewHolder(view);
        } else if (viewType == SELF) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_self_item, viewGroup, false);
            return new ItemViewHolder(view);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Log.d(TAG, "Element " + position + " set.");
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            Typeface makOnWayFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");
            // set message text and font
            viewHolder.getMessage().setText(mDataSet.get(position).getMessage());
            viewHolder.getMessage().setTypeface(makOnWayFont);

            if (mDataSet.get(position).isShow()) {
                viewHolder.getShowFlag().setImageResource(R.drawable.ic_action_show_ico);

            } else {
                viewHolder.getShowFlag().setImageResource(R.drawable.ic_action_unshow_ico);
            }

            viewHolder.getAuthorAndTimestamp().setText(mDataSet.get(position).getTimestamp());
            viewHolder.getAuthorAndTimestamp().setTypeface(makOnWayFont);


            // populate mainImage
            Glide.with(mContext)
                    .load(mDataSet.get(position).getUserAvatar())
                    .placeholder(R.drawable.avatr)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .dontAnimate()
                    .thumbnail(0.1f)
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
                    .into(viewHolder.getAvatarImage());

        }else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

}
