package perfect_apps.tutors.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.R;
import perfect_apps.tutors.adapters.MessagesAdapter;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.models.Messages;
import perfect_apps.tutors.parse.JsonParser;
import perfect_apps.tutors.services.NotificationEvent;
import perfect_apps.tutors.services.UpdateMessageCountEvent;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.OnLoadMoreListener;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 13/07/16.
 */
public class Conversation extends Fragment implements View.OnClickListener {
    private static String email;
    private static String password;
    private static String to;
    private static String messageText;
    private static String parentMessage;


    public static final String TAG = "Conversation";

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.messageInput)
    EditText messageInput;
    @Bind(R.id.send_button)
    ImageView sendButton;


    private int pageCount = 1;
    // add listener for loading more view
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                if (getArguments().getString("flag").equalsIgnoreCase("last_chat_page")) {
                    doReplyMessage();
                } else {
                    doNewMessage();
                }
                break;
        }
    }

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER;
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected MessagesAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<Messages> mDataSet;

    public Conversation() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize mDataSet
        mDataSet = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversation_activity, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new MessagesAdapter(getActivity(), mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (lastVisibleItem == 0
                        && !mAdapter.isLoading
                        && totalItemCount > visibleThreshold) {
                    if (mAdapter.mOnLoadMoreListener != null) {
                        mAdapter.mOnLoadMoreListener.onLoadMore();
                    }
                    mAdapter.isLoading = true;
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "Load More");
                pageCount++;
                mDataSet.add(0, null);
                mAdapter.notifyItemInserted(0);

                // loadMoreData
                getConversationMessagesWhenOpenPages();
            }
        });
        setActionsOfToolBarIcons();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setActionsOfToolBarIcons() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("رسائلي");
        title.setTypeface(font);


        ImageView searchIc = (ImageView) toolbar.findViewById(R.id.search);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView chatIc = (ImageView) toolbar.findViewById(R.id.chat);
        ImageView back = (ImageView) toolbar.findViewById(R.id.back);
        LinearLayout messageCountView = (LinearLayout) toolbar.findViewById(R.id.messageCountView);
        messageCountView.setVisibility(View.GONE);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // getConversationMessages if coming from last chat messages
        if (getArguments().getString("flag").equalsIgnoreCase("last_chat_page")) {
            getConversationMessagesWhenOpen();
        }

        sendButton.setOnClickListener(this);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findLastCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setStackFromEnd(true);
                mLayoutManager = linearLayoutManager;
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void getConversationMessagesWhenOpen() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        LinearLayout messageCountView = (LinearLayout) toolbar.findViewById(R.id.messageCountView);
        messageCountView.setVisibility(View.GONE);


        String url = "";
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            url = "http://services-apps.net/tutors/api/message/show/message?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD)
                    + "&message_id=" + getArguments().getString("message_id") + "&page=" + 1;
        } else {
            url = "http://services-apps.net/tutors/api/message/show/message?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD)
                    + "&message_id=" + getArguments().getString("message_id") + "&page=" + 1;
        }

        if (Utils.isOnline(getActivity())) {
            // Tag used to cancel the request
            String tag_string_req = "string_req";

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        response = URLDecoder.decode(response, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    // TODO: 13/07/16
                    clearDataSet();
                    for (Messages item :
                            JsonParser.parseConversation(response)) {
                        mDataSet.add(item);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);

                    }

                    EventBus.getDefault().post(new UpdateMessageCountEvent("message"));

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            strReq.setShouldCache(false);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private void getConversationMessagesWhenOpenPages() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        LinearLayout messageCountView = (LinearLayout) toolbar.findViewById(R.id.messageCountView);
        messageCountView.setVisibility(View.GONE);


        String url = "";
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            url = "http://services-apps.net/tutors/api/message/show/message?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD)
                    + "&message_id=" + getArguments().getString("message_id") + "&page=" + pageCount;
        } else {
            url = "http://services-apps.net/tutors/api/message/show/message?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD)
                    + "&message_id=" + getArguments().getString("message_id") + "&page=" + pageCount;
        }

        if (Utils.isOnline(getActivity())) {
            // Tag used to cancel the request
            String tag_string_req = "string_req";

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        response = URLDecoder.decode(response, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(pageCount > 1 && mDataSet.size() != 0){
                        mDataSet.remove(0);
                        mAdapter.isLoading = false;
                        mAdapter.notifyItemRemoved(0);
                    }
                    List<Messages> messagesList = JsonParser.parseConversation(response);
                    if (messagesList != null) {
                        for (Messages item : messagesList) {
                            mDataSet.add(0, item);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.smoothScrollToPosition(12);

                        }
                    }

                    EventBus.getDefault().post(new UpdateMessageCountEvent("message"));

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            strReq.setShouldCache(false);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }


    // remove all item from RecyclerView
    private void clearDataSet() {
        if (mDataSet != null) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void doReplyMessage() {
        if (Utils.isOnline(getActivity())) {
            if (checkDataForReplyMessage()) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("أنتظر...");
                pDialog.setCancelable(false);
                pDialog.show();


                String url = "http://services-apps.net/tutors/api/message/add/reply";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();
                        try {
                            response = URLDecoder.decode(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        // add item to list here
                        String myEmail = "";
                        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);

                        } else {
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL);

                        }

                        String imageUrl;
                        if (!new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_IMAGE_FULL_PATH).isEmpty()) {
                            imageUrl = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_IMAGE_FULL_PATH);
                        } else {
                            imageUrl = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_IMAGE_FULL_PATH);
                        }

                        Messages item = new Messages(imageUrl, messageInput.getText().toString().trim(), false, null, -1, myEmail, null, null);
                        mDataSet.add(item);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                        //getConversationMessagesWhenOpen();
                        messageInput.setText("");


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        // show error message
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("خطأ")
                                .setContentText("حاول مره أخري")
                                .show();
                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        params.put("to[]", to);
                        params.put("message", messageText);
                        params.put("parent_id", parentMessage);
                        return params;

                    }
                };
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq);

            }

        } else {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("خطأ")
                    .setContentText("تحقق من الأتصال بألأنترنت")
                    .show();
        }
    }

    private void doNewMessage() {

        if (Utils.isOnline(getActivity())) {
            if (checkDataForNewMessage()) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("أنتظر...");
                pDialog.setCancelable(false);
                pDialog.show();

                // Tag used to cancel the request
                String url = "http://services-apps.net/tutors/api/message/add/new";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();
                        try {
                            response = URLDecoder.decode(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        // add item to list here
                        String myEmail = "";
                        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);

                        } else {
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL);

                        }

                        String imageUrl;
                        if (!new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_IMAGE_FULL_PATH).isEmpty()) {
                            imageUrl = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_IMAGE_FULL_PATH);
                        } else {
                            imageUrl = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_IMAGE_FULL_PATH);
                        }


                        Messages item = new Messages(imageUrl, messageInput.getText().toString().trim(), false, null, -1, myEmail, null, null);
                        mDataSet.add(item);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                        //getConversationMessagesWhenOpen();
                        messageInput.setText("");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        // show error message
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("خطأ")
                                .setContentText("حاول مره أخري")
                                .show();
                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        params.put("to[]", to);
                        params.put("message", messageText);
                        return params;

                    }
                };
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq);
            }

        } else {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("خطأ")
                    .setContentText("تحقق من الأتصال بألأنترنت")
                    .show();
        }
    }

    private boolean checkDataForNewMessage() {
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            email = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);
            password = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD);
        } else {
            email = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL);
            password = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD);
        }

        to = getArguments().getString("user_id");
        try {
            messageText = URLEncoder.encode(messageInput.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if (email != null && !email.trim().isEmpty()
                && password != null && !password.trim().isEmpty()
                && to != null && !to.trim().isEmpty()
                && messageText != null && !messageText.trim().isEmpty()) {

            return true;

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("هذه طريقه غير صحيحه حاول مره اخرى")
                    .show();
            return false;
        }

    }

    private boolean checkDataForReplyMessage() {
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            email = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);
            password = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD);
        } else {
            email = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL);
            password = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD);
        }

        to = getArguments().getString("user_id");
        try {
            messageText = URLEncoder.encode(messageInput.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        parentMessage = getArguments().getString("message_id");

        if (email != null && !email.trim().isEmpty()
                && password != null && !password.trim().isEmpty()
                && to != null && !to.trim().isEmpty()
                && messageText != null && !messageText.trim().isEmpty()
                && parentMessage != null && !parentMessage.trim().isEmpty()) {

            return true;

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("هذه طريقه غير صحيحه حاول مره اخرى")
                    .show();
            return false;
        }

    }

    @Subscribe
    public void onMessageEvent(NotificationEvent event) {
        getConversationMessagesWhenOpen();
    }

    // to scroll to last item :)
//    LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//    llm.scrollToPositionWithOffset(displayedPosition, mDataSet.size());
}
