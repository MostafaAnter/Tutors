package perfect_apps.tutors.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.BuildConfig;
import perfect_apps.tutors.R;
import perfect_apps.tutors.adapters.CitiesSpinnerAdapter;
import perfect_apps.tutors.adapters.CitiesSpinnerAdapterGreen;
import perfect_apps.tutors.adapters.CountriesSpinnerAdapter;
import perfect_apps.tutors.adapters.CountriesSpinnerAdapterGreen;
import perfect_apps.tutors.adapters.MajorsSpinnerAdapter;
import perfect_apps.tutors.adapters.MajorsSpinnerAdapterGreen;
import perfect_apps.tutors.adapters.ServiceSpinnerAdapter;
import perfect_apps.tutors.adapters.ServiceSpinnerAdapterGreen;
import perfect_apps.tutors.adapters.SexSpinnerAdapter;
import perfect_apps.tutors.adapters.SexSpinnerAdapterGreen;
import perfect_apps.tutors.adapters.StageSpinnerAdapter;
import perfect_apps.tutors.adapters.StageSpinnerAdapterGreen;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.models.SpinnerItem;
import perfect_apps.tutors.parse.JsonParser;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 29/06/16.
 */
public class SearchAboutTeacherFragment extends Fragment {
    public static final String TAG = "SearchAboutTeacherFragment";
    private static String country_id = "";
    private static String city_id = "";
    private static String major_id = "";
    private static String stage_id = "";
    private static String apply_service_id = "";
    private static String gender_id = "";

    @Bind(R.id.text1) TextView textView1;
    @Bind(R.id.text2) TextView textView2;
    @Bind(R.id.button1) Button button1;

    @Bind(R.id.spinner1) Spinner spinner1;
    @Bind(R.id.spinner2) Spinner spinner2;
    @Bind(R.id.spinner3) Spinner spinner3;
    @Bind(R.id.spinner4) Spinner spinner4;
    @Bind(R.id.spinner5) Spinner spinner5;
    @Bind(R.id.spinner6) Spinner spinner6;

    @Bind(R.id.searchTeachers) LinearLayout searchTeachers;


    public SearchAboutTeacherFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_about_teacher_activity, container, false);
        ButterKnife.bind(this, view);
        setActionsOfToolBarIcons();
        changeTextFont();



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        country_id = "";
        city_id = "";
        major_id = "";
        stage_id = "";
        apply_service_id = "";
        gender_id = "";

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfFieldIsEmpty()) {
                    TeachersSearchResultList teacherDetails =
                            new TeachersSearchResultList();
                    Bundle b = new Bundle();

                    b.putString(Constants.COUNTRY_ID, country_id);
                    b.putString(Constants.CITY_ID, city_id);
                    b.putString(Constants.MAJOR_ID, major_id);
                    b.putString(Constants.STAGE_ID, stage_id);
                    b.putString(Constants.APPLY_SERVICE_ID, apply_service_id);
                    b.putString(Constants.GENDER_ID, gender_id);

                    teacherDetails.setArguments(b);

                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.fragment_container, teacherDetails);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(TeachersSearchResultList.TAG);
                    transaction.commit();
                    // to add to back stack
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfFieldIsEmpty()) {
                    TeachersSearchResultList teacherDetails =
                            new TeachersSearchResultList();
                    Bundle b = new Bundle();

                    b.putString(Constants.COUNTRY_ID, country_id);
                    b.putString(Constants.CITY_ID, city_id);
                    b.putString(Constants.MAJOR_ID, major_id);
                    b.putString(Constants.STAGE_ID, stage_id);
                    b.putString(Constants.APPLY_SERVICE_ID, apply_service_id);
                    b.putString(Constants.GENDER_ID, gender_id);

                    teacherDetails.setArguments(b);

                    resetSpinner(spinner1);
                    resetSpinner(spinner2);
                    resetSpinner(spinner3);
                    resetSpinner(spinner4);
                    resetSpinner(spinner5);
                    resetSpinner(spinner6);

                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.fragment_container, teacherDetails);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(TeachersSearchResultList.TAG);
                    transaction.commit();
                    // to add to back stack
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                }
            }
        });


        List<SpinnerItem> spinnerItemList = new ArrayList<>();
        spinnerItemList.add(null);
        populateSpinner2(spinnerItemList);
        populateSpinner4(spinnerItemList);

        if(new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                .equalsIgnoreCase(Constants.STUDENT)){
            pushToken();
        }
    }

    private boolean checkIfFieldIsEmpty(){
        if (country_id.trim().isEmpty()){
            Utils.showErrorMessage(getActivity(), "الرجاء قم بأختيار الدولة");
            return false;
        }
//        if (city_id.trim().isEmpty()){
//            Utils.showErrorMessage(getActivity(), "الرجاء قم بأختيار المدينة");
//            return false;
//        }
        if (stage_id.trim().isEmpty()){
            Utils.showErrorMessage(getActivity(), "الرجاء قم بأختيار المرحلة الدراسية");
            return false;
        }
        if (major_id.trim().isEmpty()){
            Utils.showErrorMessage(getActivity(), "الرجاء قم بأختيار التخصص");
            return false;
        }
//        if (apply_service_id.trim().isEmpty()){
//            Utils.showErrorMessage(getActivity(), "الرجاء اختيار نوع تقديم الخدمة");
//            return false;
//        }
//        if (gender_id.trim().isEmpty()){
//            Utils.showErrorMessage(getActivity(), "الرجاء اختيار نوع المعلم");
//            return false;
//        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchData();
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
        textView1.setTypeface(fontBold);
        textView2.setTypeface(font);
        button1.setTypeface(fontBold);
    }

    private void setActionsOfToolBarIcons() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("بحث عن مدرس");
        title.setTypeface(font);

        ImageView searchIc = (ImageView) toolbar.findViewById(R.id.search);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView chatIc = (ImageView) toolbar.findViewById(R.id.chat);

        searchIc.setVisibility(View.VISIBLE);
        profileIc.setVisibility(View.GONE);
        chatIc.setVisibility(View.GONE);

        if (new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                .equalsIgnoreCase(Constants.STUDENT)){
            profileIc.setVisibility(View.VISIBLE);
            chatIc.setVisibility(View.VISIBLE);
        }

        profileIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addStudentDetailToBackstack()) {
                    // clearBackStack();
                    StudentDetails teacherDetails =
                            new StudentDetails();
                    Bundle b = new Bundle();
                    b.putString(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
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

            }
        });

        searchIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addStudentHomeListToBackstack()) {
                    //clearBackStack();
                    SearchAboutTeacherFragment teacherDetails =
                            new SearchAboutTeacherFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.fragment_container, teacherDetails);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(SearchAboutTeacherFragment.TAG);
                    transaction.commit();
                    // to add to back stack
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                }
            }
        });

        chatIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addTeacherMessageToBackstack()) {
                    // clearBackStack();
                    MyChats teacherDetails =
                            new MyChats();
                    Bundle b = new Bundle();
                    b.putString(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
                    teacherDetails.setArguments(b);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.fragment_container, teacherDetails);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(MyChats.TAG);
                    transaction.commit();
                    // to add to back stack
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                }

            }
        });
    }

    private boolean addTeacherMessageToBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
            Log.i("tag", "Found fragment: " + fm.getBackStackEntryAt(entry).getName());

            if (fm.getBackStackEntryAt(entry).getName().equalsIgnoreCase(MyChats.TAG)){
                fm.popBackStack(MyChats.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return true;
    }

    private boolean addStudentHomeListToBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
            if (fm.getBackStackEntryAt(entry).getName().equalsIgnoreCase(SearchAboutTeacherFragment.TAG)){
                fm.popBackStack(SearchAboutTeacherFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return true;
    }


    private boolean addStudentDetailToBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
            Log.i("tag", "Found fragment: " + fm.getBackStackEntryAt(entry).getName());

            if (fm.getBackStackEntryAt(entry).getName().equalsIgnoreCase(StudentDetails.TAG)){
                fm.popBackStack(StudentDetails.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return true;
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(true);
    }

    private void populateSpinner1(List<SpinnerItem> mlist) {

        CountriesSpinnerAdapterGreen spinnerArrayAdapter = new CountriesSpinnerAdapterGreen(getActivity(), R.layout.spinner_green_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_green_item);
        spinner1.setAdapter(spinnerArrayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    country_id = selectedItem.getId();
                    city_id = "";
                    String urlCities = BuildConfig.API_CITIES + selectedItem.getId();
                    fetchCitiesData(urlCities);
                }else{
                    country_id = "";
                    city_id = "";

                    List<SpinnerItem> spinnerItemList = new ArrayList<>();
                    spinnerItemList.add(null);
                    populateSpinner2(spinnerItemList);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner2(List<SpinnerItem> mlist) {

        CitiesSpinnerAdapterGreen spinnerArrayAdapter = new CitiesSpinnerAdapterGreen(getActivity(), R.layout.spinner_green_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_green_item);
        spinner2.setAdapter(spinnerArrayAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    city_id = selectedItem.getId();
                }else{
                    city_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner3(List<SpinnerItem> mlist) {

        StageSpinnerAdapterGreen spinnerArrayAdapter = new StageSpinnerAdapterGreen(getActivity(), R.layout.spinner_green_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_green_item);
        spinner3.setAdapter(spinnerArrayAdapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    stage_id = selectedItem.getId();
                    major_id = "";
                    getMagor(stage_id);
                }else{
                    stage_id = "";
                    major_id = "";

                    List<SpinnerItem> spinnerItemList = new ArrayList<>();
                    spinnerItemList.add(null);
                    populateSpinner4(spinnerItemList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner4(List<SpinnerItem> mlist) {

        MajorsSpinnerAdapterGreen spinnerArrayAdapter = new MajorsSpinnerAdapterGreen(getActivity(), R.layout.spinner_green_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_green_item);
        spinner4.setAdapter(spinnerArrayAdapter);

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    major_id = selectedItem.getId();
                }else {
                    major_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner5(List<SpinnerItem> mlist) {

        ServiceSpinnerAdapterGreen spinnerArrayAdapter = new ServiceSpinnerAdapterGreen(getActivity(), R.layout.spinner_green_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_green_item);
        spinner5.setAdapter(spinnerArrayAdapter);

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    apply_service_id = selectedItem.getId();
                }else {
                    apply_service_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner6(List<SpinnerItem> mlist) {

        SexSpinnerAdapterGreen spinnerArrayAdapter = new SexSpinnerAdapterGreen(getActivity(), R.layout.spinner_green_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_green_item);
        spinner6.setAdapter(spinnerArrayAdapter);

        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    gender_id = selectedItem.getId();
                }else {
                    gender_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fetchData(){
        getCountries();
        getStage();
        getSex();
        getApplyService();

    }


    private void getSex() {
        /**
         * this section for fetch stage
         */
        String urlstage = BuildConfig.API_GENDERS;
        // We first check for cached request
        Cache cache1 = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry1 = cache1.get(urlstage);
        if (entry1 != null) {
            // fetch the data from cache
            try {
                String data = new String(entry1.data, "UTF-8");
                // do some thing
                List<SpinnerItem> spinnerItemList = JsonParser.parseSexFeed(data);
                if (spinnerItemList != null) {
                    populateSpinner6(spinnerItemList);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<SpinnerItem> spinnerItemList = JsonParser.parseSexFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner6(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void getMagor(String stage_id) {
        /**
         * this section for fetch stage
         */
        String urlstage = BuildConfig.API_MAJORS + "?stage=" + stage_id;
        // We first check for cached request
        Cache cache1 = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry1 = cache1.get(urlstage);
        if (entry1 != null) {
            // fetch the data from cache
            try {
                String data = new String(entry1.data, "UTF-8");
                // do some thing
                List<SpinnerItem> spinnerItemList = JsonParser.parseMajorsFeed(data);

                if (spinnerItemList != null) {
                    populateSpinner4(spinnerItemList);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<SpinnerItem> spinnerItemList = JsonParser.parseMajorsFeed(response);

                    if (spinnerItemList != null) {
                        populateSpinner4(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void getApplyService() {
        /**
         * this section for fetch stage
         */
        String urlstage = BuildConfig.API_APPLY_SERVICES;
        // We first check for cached request
        Cache cache1 = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry1 = cache1.get(urlstage);
        if (entry1 != null) {
            // fetch the data from cache
            try {
                String data = new String(entry1.data, "UTF-8");
                // do some thing
                List<SpinnerItem> spinnerItemList = JsonParser.parseApplyServicesFeed(data);
                if (spinnerItemList != null) {
                    populateSpinner5(spinnerItemList);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<SpinnerItem> spinnerItemList = JsonParser.parseApplyServicesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner5(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void getStage() {
        /**
         * this section for fetch stage
         */
        String urlstage = BuildConfig.API_STAGES;
        // We first check for cached request
        Cache cache1 = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry1 = cache1.get(urlstage);
        if (entry1 != null) {
            // fetch the data from cache
            try {
                String data = new String(entry1.data, "UTF-8");
                // do some thing
                List<SpinnerItem> spinnerItemList = JsonParser.parseStageFeed(data);
                if (spinnerItemList != null) {
                    populateSpinner3(spinnerItemList);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<SpinnerItem> spinnerItemList = JsonParser.parseStageFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner3(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void getCountries() {
        /**
         * this section for fetch country
         */
        String urlBrands = BuildConfig.API_COUNTRIES;
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(urlBrands);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                // do some thing
                List<SpinnerItem> spinnerItemList = JsonParser.parseCountriesFeed(data);
                if (spinnerItemList != null) {
                    populateSpinner1(spinnerItemList);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlBrands, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<SpinnerItem> spinnerItemList = JsonParser.parseCountriesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner1(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }

    private void fetchCitiesData(String urlCities){

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(urlCities);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                // do some thing
                List<SpinnerItem> spinnerItemList = JsonParser.parseCitiesFeed(data);
                if (spinnerItemList != null) {
                    populateSpinner2(spinnerItemList);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlCities, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<SpinnerItem> spinnerItemList = JsonParser.parseCitiesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner2(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void pushToken() {
        if (Utils.isOnline(getActivity())) {


            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = "http://services-apps.net/tutors/api/token/add";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("push_token_response", response);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token_id", FirebaseInstanceId.getInstance().getToken());
                    params.put("email", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_EMAIL));
                    params.put("password", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.STUDENT_PASSWORD));
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

    private void resetSpinner(Spinner spinner) {
        spinner.setSelection(0);
    }
}
