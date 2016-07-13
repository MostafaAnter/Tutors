package perfect_apps.tutors.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import perfect_apps.tutors.R;
import perfect_apps.tutors.models.Messages;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;


/**
 * Created by mostafa on 15/04/16.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.message) TextView message;
        @Bind(R.id.conversation_avatar)
        CircleImageView avatarImage;
        @Bind(R.id.timestamp) TextView authorAndTimestamp;
        @Bind(R.id.showFlag) ImageView showFlag;

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

        public ViewHolder(View v) {
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

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v;
        if (viewType != SELF) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_yourself_item, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chat_self_item, viewGroup, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {

        if (new TutorsPrefStore(mContext).getPreferenceValue(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)){
            if (mDataSet.get(position).getMessageOwnerEmail().equalsIgnoreCase(new TutorsPrefStore(mContext).getPreferenceValue(Constants.STUDENT_EMAIL))){
                return SELF;
            }

        }else if (new TutorsPrefStore(mContext).getPreferenceValue(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_PAGE)){
            if (mDataSet.get(position).getMessageOwnerEmail().equalsIgnoreCase(new TutorsPrefStore(mContext).getPreferenceValue(Constants.TEACHER_EMAIL))){
                return SELF;
            }
        }


        return position;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        Typeface makOnWayFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");
        // set message text and font
        viewHolder.getMessage().setText(mDataSet.get(position).getMessage());
        viewHolder.getMessage().setTypeface(makOnWayFont);

        if (mDataSet.get(position).isShow()){
            viewHolder.getShowFlag().setImageResource(R.drawable.ic_action_show_ico);

        }else {
            viewHolder.getShowFlag().setImageResource(R.drawable.ic_action_unshow_ico);
        }

        viewHolder.getAuthorAndTimestamp().setText(mDataSet.get(position).getTimestamp());
        viewHolder.getAuthorAndTimestamp().setTypeface(makOnWayFont);


        // populate mainImage
        Picasso.with(mContext)
                .load(mDataSet.get(position).getUserAvatar())
                .placeholder(R.drawable.login_user_ico)
                .into(viewHolder.getAvatarImage());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
