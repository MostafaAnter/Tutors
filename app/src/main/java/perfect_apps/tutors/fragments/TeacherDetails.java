package perfect_apps.tutors.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.R;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 26/06/16.
 */
public class TeacherDetails extends Fragment implements View.OnClickListener {
    public static final String TAG = "TeacherDetails";
    private static int mStackLevel = 0;

    @Bind(R.id.text1)
    TextView textView1;
    @Bind(R.id.text2)
    TextView textView2;
    @Bind(R.id.text3)
    TextView textView3;
    @Bind(R.id.text4)
    TextView textView4;
    @Bind(R.id.text5)
    TextView textView5;
    @Bind(R.id.text6)
    TextView textView6;
    @Bind(R.id.text7)
    TextView textView7;
    @Bind(R.id.text8)
    TextView textView8;
    @Bind(R.id.text9)
    TextView textView9;
    @Bind(R.id.text10)
    TextView textView10;
    @Bind(R.id.text11)
    TextView textView11;
    @Bind(R.id.text12)
    TextView textView12;
    @Bind(R.id.text13)
    TextView textView13;
    @Bind(R.id.text14)
    TextView textView14;
    @Bind(R.id.text15)
    TextView textView15;
    @Bind(R.id.text16)
    TextView textView16;
    @Bind(R.id.text17)
    TextView textView17;
    @Bind(R.id.text18)
    TextView textView18;
    @Bind(R.id.text19)
    TextView textView19;
    @Bind(R.id.text20)
    Button textView20;
    @Bind(R.id.text21)
    TextView textView21;
    @Bind(R.id.name)
    TextView textView22;
    @Bind(R.id.rate)
    TextView textView23;
    @Bind(R.id.rateStatic1)
    TextView textView24;
    @Bind(R.id.rateStatic2)
    TextView textView25;
    @Bind(R.id.desc)
    TextView textView26;
    @Bind(R.id.hour)
    TextView textView27;
    @Bind(R.id.costPerHour)
    TextView textView28;

    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.button3)
    Button button3;

    @Bind(R.id.viewForStudent)
    LinearLayout viewThatShowForStudent;
    @Bind(R.id.viewForTeacher)
    LinearLayout viewThatShowForTeacher;

    @Bind(R.id.linearSentMessageToTeacher) LinearLayout sentMessageToTeacher;
    @Bind(R.id.linearRateTeacher) LinearLayout rateTeacher;


    @Bind(R.id.avatar)
    ImageView imageAvatar;

    @Bind(R.id.ratingBar) RatingBar ratingBar;

    public TeacherDetails() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_details, container, false);
        ButterKnife.bind(this, view);
        setTypeFace();


        // hide some view to suitable with context
        if (getArguments().getString(Constants.DETAIL_USER_ID)
                .equalsIgnoreCase(new TutorsPrefStore(getActivity())
                .getPreferenceValue(Constants.TEACHER_ID))){
            viewThatShowForStudent.setVisibility(View.GONE);
        }else if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_PAGE)) {
            viewThatShowForStudent.setVisibility(View.GONE);
        } else if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            viewThatShowForTeacher.setVisibility(View.GONE);
        }else {
            viewThatShowForTeacher.setVisibility(View.GONE);
            rateTeacher.setVisibility(View.GONE);
        }

        viewThatShowForTeacher.setOnClickListener(this);
        button3.setOnClickListener(this);

        sentMessageToTeacher.setOnClickListener(this);
        button1.setOnClickListener(this);

        rateTeacher.setOnClickListener(this);
        button2.setOnClickListener(this);
        textView20.setOnClickListener(this);


        setActionsOfToolBarIcons();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    private void setTypeFace() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");

        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);
        textView6.setTypeface(font);
        textView7.setTypeface(font);
        textView8.setTypeface(font);
        textView9.setTypeface(font);
        textView10.setTypeface(font);
        textView11.setTypeface(font);
        textView12.setTypeface(font);
        textView13.setTypeface(font);
        textView14.setTypeface(font);
        textView15.setTypeface(font);
        textView16.setTypeface(font);
        textView17.setTypeface(font);
        textView18.setTypeface(font);
        textView19.setTypeface(fontBold);
        textView20.setTypeface(font);
        textView21.setTypeface(font);
        textView22.setTypeface(font);
        textView23.setTypeface(font);
        textView24.setTypeface(font);
        textView25.setTypeface(font);
        textView26.setTypeface(font);
        textView27.setTypeface(font);
        textView28.setTypeface(fontBold);
        button1.setTypeface(fontBold);
        button2.setTypeface(fontBold);
        button3.setTypeface(fontBold);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewForTeacher:
                TeacherEditData teacherDetails =
                        new TeacherEditData();
                Bundle b = new Bundle();
                b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));

                teacherDetails.setArguments(b);

                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(TeacherDetails.TAG);
                transaction.commit();
                // to add to back stack
                getActivity().getSupportFragmentManager().executePendingTransactions();
                break;
            case R.id.button3:
                TeacherEditData teacherEditData =
                        new TeacherEditData();
                Bundle bb = new Bundle();
                bb.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));

                teacherEditData.setArguments(bb);

                FragmentTransaction transaction1 = getFragmentManager()
                        .beginTransaction();
                transaction1.replace(R.id.fragment_container, teacherEditData);
                transaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction1.addToBackStack(TeacherDetails.TAG);
                transaction1.commit();
                // to add to back stack
                getActivity().getSupportFragmentManager().executePendingTransactions();
                break;
            case R.id.linearSentMessageToTeacher:
                if (!getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_HOME_PAGE)
                        && !new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                        .equalsIgnoreCase(Constants.STUDENT)) {
                    mStackLevel++;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    // Create and show the dialog.
                    DialogFragment newFragment = MyDialogFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment.setArguments(bundle1);
                    newFragment.show(ft, "dialog");
                }else {
                    if (addConversetionToBackstack()) {
                        Conversation teacherDetail =
                                new Conversation();
                        Bundle b4 = new Bundle();
                        b4.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                        b4.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                        b4.putString("flag", "detail_page");
                        teacherDetail.setArguments(b4);
                        FragmentTransaction transaction11 = getFragmentManager()
                                .beginTransaction();
                        transaction11.replace(R.id.fragment_container, teacherDetail);
                        transaction11.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction11.addToBackStack(Conversation.TAG);
                        transaction11.commit();
                        // to add to back stack
                        getActivity().getSupportFragmentManager().executePendingTransactions();
                    }

                }
                break;
            case R.id.button1:
                if (!getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_HOME_PAGE)
                        &&!new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                        .equalsIgnoreCase(Constants.STUDENT)) {
                    mStackLevel++;
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    DialogFragment newFragment1 = MyDialogFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");
                }else {
                    if (addConversetionToBackstack()) {
                        Conversation teacherDetail =
                                new Conversation();
                        Bundle b4 = new Bundle();
                        b4.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                        b4.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                        b4.putString("flag", "detail_page");
                        teacherDetail.setArguments(b4);
                        FragmentTransaction transaction11 = getFragmentManager()
                                .beginTransaction();
                        transaction11.replace(R.id.fragment_container, teacherDetail);
                        transaction11.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction11.addToBackStack(Conversation.TAG);
                        transaction11.commit();
                        // to add to back stack
                        getActivity().getSupportFragmentManager().executePendingTransactions();
                    }

                }
                break;
            case R.id.linearRateTeacher:
                if (!getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_HOME_PAGE)
                        &&!new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                        .equalsIgnoreCase(Constants.STUDENT)) {
                    mStackLevel++;
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    DialogFragment newFragment1 = MyDialogFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");
                }else {
                    mStackLevel++;
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    RatingDialogFragment newFragment1 = RatingDialogFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");

                }
                break;
            case R.id.button2:
                if (!getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_HOME_PAGE)
                        &&!new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                        .equalsIgnoreCase(Constants.STUDENT)) {
                    mStackLevel++;
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    DialogFragment newFragment1 = MyDialogFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");
                }else {
                    mStackLevel++;
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    RatingDialogFragment newFragment1 = RatingDialogFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");

                }
                break;

            case R.id.text20:
                if (!getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_HOME_PAGE)
                        &&!new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                        .equalsIgnoreCase(Constants.STUDENT)) {
                    mStackLevel++;
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    DialogFragment newFragment1 = MyDialogFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");
                }else {
                    mStackLevel++;
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    RateInfoFragment newFragment1 = RateInfoFragment.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");

                }
                break;
        }

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

        ImageView searchIc = (ImageView) toolbar.findViewById(R.id.search);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView chatIc = (ImageView) toolbar.findViewById(R.id.chat);


        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_PAGE)) {
            profileIc.setVisibility(View.VISIBLE);
            chatIc.setVisibility(View.VISIBLE);
            searchIc.setVisibility(View.GONE);
            TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            title.setText("بياناتي الشخصية");
            title.setTypeface(font);
        }else if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_HOME_PAGE)){
            profileIc.setVisibility(View.VISIBLE);
            chatIc.setVisibility(View.VISIBLE);
            searchIc.setVisibility(View.GONE);
            TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            title.setText("بيانات المدرس");
            title.setTypeface(font);

        }else {
            searchIc.setVisibility(View.VISIBLE);
            TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            title.setText("بيانات المدرس");
            title.setTypeface(font);
        }


        profileIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
                    if (addStudentDetailToBackstack()) {
                        // clearBackStack();
                        StudentDetails teacherDetails =
                                new StudentDetails();
                        Bundle b = new Bundle();
                        b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                        b.putString(Constants.DETAIL_USER_ID, new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_ID));
                        teacherDetails.setArguments(b);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                .beginTransaction();
                        transaction.replace(R.id.fragment_container, teacherDetails);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.addToBackStack(StudentDetails.TAG);
                        transaction.commit();
                        // to add to back stack
                        getActivity().getSupportFragmentManager().executePendingTransactions();
                    }
                } else {
                    if (addTeacherDetailToBackstack()) {
                        TeacherDetails teacherDetails =
                                new TeacherDetails();
                        Bundle b = new Bundle();
                        b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
                        b.putString(Constants.DETAIL_USER_ID, new TutorsPrefStore(getActivity())
                                .getPreferenceValue(Constants.TEACHER_ID));

                        teacherDetails.setArguments(b);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                .beginTransaction();
                        transaction.replace(R.id.fragment_container, teacherDetails);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.addToBackStack(TeacherDetails.TAG);
                        transaction.commit();
                        // to add to back stack
                        getActivity().getSupportFragmentManager().executePendingTransactions();
                    }
                }

            }
        });

        searchIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        chatIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private boolean addStudentDetailToBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
            Log.i(TAG, "Found fragment: " + fm.getBackStackEntryAt(entry).getName());

            if (fm.getBackStackEntryAt(entry).getName().equalsIgnoreCase(StudentDetails.TAG)){
                fm.popBackStack(StudentDetails.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return true;

    }

    private boolean addTeacherDetailToBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
            Log.i(TAG, "Found fragment: " + fm.getBackStackEntryAt(entry).getName());

            if (fm.getBackStackEntryAt(entry).getName().equalsIgnoreCase(TeacherDetails.TAG)){
                fm.popBackStack(TeacherDetails.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return true;
    }


    private void fetchData() {
        if (Utils.isOnline(getActivity())) {
            // first fetch rate
            fetchRate();

            // Set up a progress dialog
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("جارى التحميل...");
            pDialog.setCancelable(false);
            pDialog.show();

            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = "http://services-apps.net/tutors/api/info/teacher";

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
                    parseFeed(response);

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
                    params.put("user_id", getArguments().getString(Constants.DETAIL_USER_ID));
                    return params;

                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("خطأ")
                    .setContentText("تحقق من الأتصال بألأنترنت")
                    .show();
        }

    }

    private void fetchRate() {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        String url = "http://services-apps.net/tutors/api/show/rates?teacher_id=" +
                getArguments().getString(Constants.DETAIL_USER_ID);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject  jsonRootObject = new JSONObject(response);
                    JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("data");
                    for (int i = 0; i < jsonMoviesArray.length(); i++) {
                        JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                        String rate = jsonObject.optString("rating");

                        float rating_per_5 = Float.valueOf(rate);
                        if (rating_per_5 != 0)
                            rating_per_5 = rating_per_5 / 5;

                        textView23.setText(String.valueOf(rating_per_5));
                        ratingBar.setRating(rating_per_5);




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void parseFeed(String response) {
        try {
            JSONObject jsonRootObject = new JSONObject(response);
            JSONObject itemObject = jsonRootObject.optJSONObject("item");
            textView22.setText(itemObject.optString("name"));
            textView26.setText(itemObject.optString("desc"));
            // populate mainImage
            Picasso.with(getActivity())
                    .load(itemObject.optString("image_full_path"))
                    .placeholder(R.drawable.login_user_ico)
                    .into(imageAvatar);

            JSONObject teacherInfoObject = itemObject.optJSONObject("teacher_info");
            if (teacherInfoObject.optString("hour_price") != null &&
                    !teacherInfoObject.optString("hour_price").trim().isEmpty() &&
                    !teacherInfoObject.optString("hour_price").equalsIgnoreCase("null")) {
                textView28.setText(teacherInfoObject.optString("hour_price"));
            } else {
                textView28.setText("--");
            }
            textView10.setText(teacherInfoObject.optString("subjects"));
            if (!teacherInfoObject.optString("qualification").equalsIgnoreCase("null"))
            textView16.setText(teacherInfoObject.optString("qualification"));
            if (!teacherInfoObject.optString("experience").equalsIgnoreCase("null"))
            textView18.setText(teacherInfoObject.optString("experience"));
            if (!teacherInfoObject.optString("who_am_i").equalsIgnoreCase("null"))
                textView21.setText(teacherInfoObject.optString("who_am_i"));


            JSONObject countryObject = teacherInfoObject.optJSONObject("country");
            if (countryObject != null) {
                if (countryObject.optString("name") != null)
                textView2.setText(countryObject.optString("name"));
            }

            JSONObject cityObject = teacherInfoObject.optJSONObject("city");
            if (cityObject != null) {
                if (cityObject.optString("name") != null)
                textView4.setText(cityObject.optString("name"));
            }

            JSONObject stageObject = teacherInfoObject.optJSONObject("stage");
            if (stageObject != null) {
                if (stageObject.optString("name") != null)
                textView6.setText(stageObject.optString("name"));
            }

            JSONObject majorObject = teacherInfoObject.optJSONObject("major");
            if (majorObject != null) {
                if (majorObject.optString("name") != null)
                textView8.setText(majorObject.optString("name"));
            }

            JSONObject applyServiceObject = teacherInfoObject.optJSONObject("apply_service");
            if (applyServiceObject != null) {
                if (applyServiceObject.optString("name") != null)
                textView12.setText(applyServiceObject.optString("name"));
            }

            JSONObject genderObject = teacherInfoObject.optJSONObject("gender");
            if (genderObject != null) {
                if (genderObject.optString("name") != null)
                textView14.setText(genderObject.optString("name"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
