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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
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

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.messageInput) EditText messageInput;
    @Bind(R.id.send_button) ImageView sendButton;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_button:
                if (getArguments().getString("flag").equalsIgnoreCase("last_chat_page")){
                    doReplyMessage();
                }else {
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

    public Conversation(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize mDataSet
        mDataSet = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_refresh:
                if (getArguments().getString("flag").equalsIgnoreCase("last_chat_page")) {
                    getConversationMessagesWhenOpen();
                }
                return true;
            case R.id.about_sender:
                if (!getArguments().getString("flag").equalsIgnoreCase("last_chat_page")) {
                    TeacherDetails teacherDetails =
                            new TeacherDetails();
                    Bundle b = new Bundle();
                    b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                    b.putString(Constants.DETAIL_USER_ID, getArguments().getString("user_id"));

                    teacherDetails.setArguments(b);

                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.fragment_container, teacherDetails);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(TeacherDetails.TAG);
                    transaction.commit();
                    // to add to back stack
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                }else if (getArguments().getString("group_id").equalsIgnoreCase("3")){
                    StudentDetails teacherDetails =
                            new StudentDetails();
                    Bundle b = new Bundle();
                    b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                    b.putString(Constants.DETAIL_USER_ID, getArguments().getString("user_id"));
                    teacherDetails.setArguments(b);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.fragment_container, teacherDetails);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(StudentDetails.TAG);
                    transaction.commit();
                    // to add to back stack
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                }else if (getArguments().getString("group_id").equalsIgnoreCase("2")){
                    TeacherDetails teacherDetails =
                            new TeacherDetails();
                    Bundle b = new Bundle();
                    b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                    b.putString(Constants.DETAIL_USER_ID, getArguments().getString("user_id"));

                    teacherDetails.setArguments(b);

                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.fragment_container, teacherDetails);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(TeacherDetails.TAG);
                    transaction.commit();
                    // to add to back stack
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                }
                return true;
            case R.id.block:
                block();
                return true;
            case R.id.unblock:
                unblock();
                return true;

        }

        return super.onOptionsItemSelected(item);
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
        setActionsOfToolBarIcons();
        return view;
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
        LinearLayout messageCountView = (LinearLayout)toolbar.findViewById(R.id.messageCountView);

        searchIc.setVisibility(View.GONE);
        profileIc.setVisibility(View.GONE);
        chatIc.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
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

    private void getConversationMessagesWhenOpen(){
        String url = "";
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            url = "http://services-apps.net/tutors/api/message/show/message?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD)
                    + "&message_id=" + getArguments().getString("message_id");
        } else {
            url = "http://services-apps.net/tutors/api/message/show/message?email="
                    + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL)
                    + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD)
                    + "&message_id=" + getArguments().getString("message_id");
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
                // TODO: 13/07/16
                clearDataSet();
                for (Messages item :
                        JsonParser.parseConversation(data)) {
                    mDataSet.add(item);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

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
                    url1 = "http://services-apps.net/tutors/api/message/show/message?email="
                            + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL)
                            + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD)
                            + "&message_id=" + getArguments().getString("message_id");
                } else {
                    url1 = "http://services-apps.net/tutors/api/message/show/message?email="
                            + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL)
                            + "&password=" + new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD)
                            + "&message_id=" + getArguments().getString("message_id");
                }



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
                            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        }

    }

    // remove all item from RecyclerView
    private void clearDataSet() {
        if (mDataSet != null) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void doReplyMessage(){
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
                        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)){
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);

                        }else {
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL);

                        }

                        Messages item = new Messages(null, messageInput.getText().toString().trim(), false, null, -1, myEmail);
                        mDataSet.add(item);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

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

    private void doNewMessage(){

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
                        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)){
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);

                        }else {
                            myEmail = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL);

                        }

                        Messages item = new Messages(null, messageInput.getText().toString().trim(), false, null, -1, myEmail);
                        mDataSet.add(item);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

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

    private boolean checkDataForNewMessage(){
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)){
            email = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);
            password = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD);
        }else {
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
                && messageText != null && !messageText.trim().isEmpty()){

            return true;

        }else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("هذه طريقه غير صحيحه حاول مره اخرى")
                    .show();
            return false;
        }

    }

    private boolean checkDataForReplyMessage(){
        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)){
            email = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL);
            password = new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD);
        }else {
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
                && parentMessage != null && !parentMessage.trim().isEmpty()){

            return true;

        }else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("هذه طريقه غير صحيحه حاول مره اخرى")
                    .show();
            return false;
        }

    }

    private void block() {
        if (Utils.isOnline(getActivity())) {


            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = "http://services-apps.net/tutors/api/block/block";

            // Set up a progress dialog
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("أنتظر...");
            pDialog.setCancelable(false);
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("push_token_response", response);

                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("تم")
                            .setContentText("لقد قمت بحظر المستخدم")
                            .show();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("push_token_error", error.networkResponse.toString());
                    pDialog.dismissWithAnimation();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("blocked_id", getArguments().getString("message_id"));
                    if (new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                            .equalsIgnoreCase(Constants.STUDENT)) {
                        params.put("email", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL));
                        params.put("password", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD));
                    }else {
                        params.put("email", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL));
                        params.put("password", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD));
                    }
                    return params;

                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }

    private void unblock() {
        if (Utils.isOnline(getActivity())) {


            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = "http://services-apps.net/tutors/api/block/unblock";

            // Set up a progress dialog
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("أنتظر...");
            pDialog.setCancelable(false);
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("push_token_response", response);
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("تم")
                            .setContentText("لقد قمت بإلغاء الحظر")
                            .show();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("push_token_error", error.networkResponse.toString());
                    pDialog.dismissWithAnimation();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("blocked_id", getArguments().getString("message_id"));
                    if (new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                            .equalsIgnoreCase(Constants.STUDENT)) {
                        params.put("email", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL));
                        params.put("password", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD));
                    }else {
                        params.put("email", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_EMAIL));
                        params.put("password", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_PASSWORD));
                    }
                    return params;

                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }
    // to scroll to last item :)
//    LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//    llm.scrollToPositionWithOffset(displayedPosition, mDataSet.size());
}
