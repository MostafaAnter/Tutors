package perfect_apps.tutors.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import perfect_apps.tutors.R;
import perfect_apps.tutors.models.MyChatsItem;

/**
 * Created by mostafa on 15/04/16.
 */
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder>{

    private static final String TAG = "CustomAdapter";
    private static Context mContext;
    private List<MyChatsItem> mDataSet;

    /**
     * Initialize the constructor of the Adapter.
     *
     * @param mDataSet String[] containing the data to populate views to be used by RecyclerView.
     * @param mContext Context hold context
     */
    public ChatsAdapter(Context mContext, List<MyChatsItem> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }

    /**
     * Provide a reference to the type of views (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name) TextView user_name;
        @Bind(R.id.message) TextView last_message;
        @Bind(R.id.timestamp) TextView timeStamp;
        @Bind(R.id.conversation_avatar)
        CircleImageView conversationAvatar;
        public ProgressBar getProgressBar() {
            return progressBar;
        }

        @Bind(R.id.progressBar)
        ProgressBar progressBar;

        public LinearLayout getContainer() {
            return container;
        }

        @Bind(R.id.container)
        LinearLayout container;

        public CircleImageView getConversationAvatar() {
            return conversationAvatar;
        }

        public TextView getUser_name() {
            return user_name;
        }

        public TextView getLast_message() {
            return last_message;
        }

        public TextView getTimeStamp() {
            return timeStamp;
        }

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ButterKnife.bind(this, v);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_message_list_row, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        // Get element from your dataset at this position and replace the contents of the view
        if (Integer.valueOf(mDataSet.get(position).getNew_count()) > 0){
            viewHolder.getContainer().setBackgroundResource(R.color.grey);
        }
        // with that element
        Typeface fontBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/bold.ttf");
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");

        // set user name
        viewHolder.getUser_name().setText(mDataSet.get(position).getChats_name());
        viewHolder.getUser_name().setTypeface(fontBold);


        // set last message
        viewHolder.getLast_message().setText(mDataSet.get(position).getChats_last_message());
        viewHolder.getLast_message().setTypeface(font);

        // set time stamp
        viewHolder.getTimeStamp().setText(mDataSet.get(position).getTime_stamp());
        viewHolder.getTimeStamp().setTypeface(font);

        // populate mainImage
        Glide.with(mContext)
                .load(mDataSet.get(position).getChats_avatar())
                .placeholder(R.drawable.avatr)
                .centerCrop()
                .crossFade()
                .thumbnail(0.1f)
                .into(viewHolder.getConversationAvatar());
        viewHolder.getProgressBar().setVisibility(View.GONE);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
