package perfect_apps.tutors.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.adapters.ChatsAdapter;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.models.MyChatsItem;
import perfect_apps.tutors.parse.JsonParser;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.DividerItemDecoration;
import perfect_apps.tutors.utils.RecyclerItemClickListener;
import perfect_apps.tutors.utils.Utils;


/**
 * Created by mostafa on 02/04/16.
 */
public class MyChats extends Fragment {
    public static final String TAG = "MyChats";
    @Bind(R.id.noData)
    LinearLayout noDataView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected ChatsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<MyChatsItem> mDataset;

    public MyChats() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset = new ArrayList<>();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("swipe", "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
            }
        });

        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        // Start our refresh background task
        initiateRefresh();

        // save coming from state to use it in conversation adapter
        new TutorsPrefStore(getActivity()).addPreference(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));


        mMenuItems.add(new DialogMenuItem("  عن الراسل", R.drawable.ic_info_outline_black_24dp));
        if (new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_AUTHENTICATION_STATE)
                .equalsIgnoreCase(Constants.TEACHER)) {
            mMenuItems.add(new DialogMenuItem("  حظر", R.drawable.block));
            mMenuItems.add(new DialogMenuItem("  إلغاء الحظر", R.drawable.unblock));
        }
        mMenuItems.add(new DialogMenuItem(" مسح", R.drawable.ic_delete_black_24dp));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_chats, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        //noinspection ResourceAsColor
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        getResources().getDisplayMetrics()));

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new ChatsAdapter(getActivity(), mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        // set item click listener
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        if (addConversetionToBackstack()) {
                            Conversation teacherDetails =
                                    new Conversation();
                            Bundle b = new Bundle();
                            b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                            b.putString("message_id", mDataset.get(position).getChat_id());
                            b.putString("user_id", mDataset.get(position).getUser_id());
                            b.putString("group_id", mDataset.get(position).getGroup_id());
                            b.putString("flag", "last_chat_page");
                            teacherDetails.setArguments(b);
                            FragmentTransaction transaction = getFragmentManager()
                                    .beginTransaction();
                            transaction.replace(R.id.fragment_container, teacherDetails);
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.addToBackStack(Conversation.TAG);
                            transaction.commit();
                            // to add to back stack
                            getActivity().getSupportFragmentManager().executePendingTransactions();
                        }


                    }
                })
        );

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    childView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            // show dialog :)
                            normalListDialogCustomAttr();

                            return false;
                        }
                    });
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        setActionsOfToolBarIcons();
        return view;
    }

    private boolean addConversetionToBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
            Log.i(TAG, "Found fragment: " + fm.getBackStackEntryAt(entry).getName());

            if (fm.getBackStackEntryAt(entry).getName().equalsIgnoreCase(Conversation.TAG)){
                fm.popBackStack(Conversation.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return true;
    }

    private void setActionsOfToolBarIcons() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("رسائلي");
        title.setTypeface(font);

    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // that view you want to save
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * When the AsyncTask finishes, it calls onRefreshComplete(), which updates the data in the
     * ListAdapter and turns off the progress bar.
     */
    private void onRefreshComplete() {

        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initiateRefresh() {
        requestMessages();
    }

    // remove all item from RecyclerView
    private void clearDataSet() {
        if (mDataset != null) {
            mDataset.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void requestMessages() {

        String url = "";
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            url = "http://services-apps.net/tutors/api/message/show?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD);
        } else {
            url = "http://services-apps.net/tutors/api/message/show?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD);
        }

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null && !Utils.isOnline(getActivity())) {
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    data = URLDecoder.decode(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                clearDataSet();
                for (MyChatsItem item :
                        JsonParser.parseMyMessages(data)) {
                    mDataset.add(item);
                    mAdapter.notifyDataSetChanged();
                    onRefreshComplete();

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (Utils.isOnline(getActivity())) {
                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url1 = "";
                if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
                    url1 = "http://services-apps.net/tutors/api/message/show?email="
                            + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL)
                            + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD);
                } else {
                    url1 = "http://services-apps.net/tutors/api/message/show?email="
                            + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL)
                            + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD);
                }

                // Cached response doesn't exists. Make network call here
                if (!mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(true);

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            response = URLDecoder.decode(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        clearDataSet();
                        for (MyChatsItem item :
                                JsonParser.parseMyMessages(response)) {
                            mDataset.add(item);
                            mAdapter.notifyDataSetChanged();

                        }
                        onRefreshComplete();
                        if (mDataset.size() > 0){
                            noDataView.setVisibility(View.GONE);
                        }else {
                            noDataView.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onRefreshComplete();

                        if (mDataset.size() > 0){
                            noDataView.setVisibility(View.GONE);
                        }else {
                            noDataView.setVisibility(View.VISIBLE);
                        }

                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        }


    }

    private void normalListDialogCustomAttr() {
        final NormalListDialog dialog = new NormalListDialog(getActivity(), mMenuItems);
        dialog.title("خيارات أضافية")//
                .titleTextSize_SP(18)//
                .titleBgColor(Color.parseColor("#409ED7"))//
                .itemPressColor(Color.parseColor("#85D3EF"))//
                .itemTextColor(Color.parseColor("#303030"))//
                .itemTextSize(14)//
                .cornerRadius(0)//
                .widthScale(0.8f)//
                .show(R.style.myDialogAnim);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                // mMenuItems.get(position).mOperName
                Toast.makeText(getActivity(), mMenuItems.get(position).mOperName + position, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
