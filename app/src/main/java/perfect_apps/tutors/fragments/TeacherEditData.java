package perfect_apps.tutors.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import perfect_apps.tutors.BuildConfig;
import perfect_apps.tutors.R;
import perfect_apps.tutors.adapters.CitiesSpinnerAdapter;
import perfect_apps.tutors.adapters.CountriesSpinnerAdapter;
import perfect_apps.tutors.adapters.MajorsSpinnerAdapter;
import perfect_apps.tutors.adapters.ServiceSpinnerAdapter;
import perfect_apps.tutors.adapters.SexSpinnerAdapter;
import perfect_apps.tutors.adapters.StageSpinnerAdapter;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.models.SpinnerItem;
import perfect_apps.tutors.parse.JsonParser;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 28/06/16.
 */
public class TeacherEditData extends Fragment implements View.OnClickListener {
    private static String name;
    private static String email;
    private static String password;
    private static String country_id;
    private static String city_id ;
    private static String major_id ;
    private static String stage_id ;
    private static String subjects ;
    private static String hour_price ;
    private static String apply_service_id;
    private static String gender_id;
    private static Uri image;
    private static String desc;
    private static String current_email;
    private static String current_password;
    private static String who_am_i;
    private static String qualification;
    private static String experience;




    @Bind(R.id.editText1) EditText editText1;
    @Bind(R.id.editText2) EditText editText2;
    @Bind(R.id.editText3) EditText editText3;
    @Bind(R.id.editText4) EditText editText4;
    @Bind(R.id.editText5) EditText editText5;
    @Bind(R.id.editText6) EditText editText6;
    @Bind(R.id.editText7) EditText editText7;
    @Bind(R.id.editText8) EditText editText8;
    @Bind(R.id.editText9) EditText editText9;
    @Bind(R.id.button1) Button button1;
    @Bind(R.id.button2) Button button2;
    @Bind(R.id.button3) Button button3;
    @Bind(R.id.image1) ImageView imageView1;

    @Bind(R.id.pickPhoto) LinearLayout linearLayout;

    @Bind(R.id.spinner1) Spinner spinner1;
    @Bind(R.id.spinner2) Spinner spinner2;
    @Bind(R.id.spinner3) Spinner spinner3;
    @Bind(R.id.spinner4) Spinner spinner4;
    @Bind(R.id.spinner5) Spinner spinner5;
    @Bind(R.id.spinner6) Spinner spinner6;

    private static final int REQUEST_CODE = 1;

    public TeacherEditData(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_edit_data, container, false);
        ButterKnife.bind(this, view);
        changeTextFont();
        linearLayout.setOnClickListener(this);

        setActionsOfToolBarIcons();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchSpinners();
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);
        editText4.setTypeface(font);
        editText5.setTypeface(font);
        editText6.setTypeface(font);
        editText7.setTypeface(font);
        editText8.setTypeface(font);
        editText9.setTypeface(font);
        button1.setTypeface(font);
        button2.setTypeface(fontBold);
        button3.setTypeface(fontBold);
    }

    public void pickPhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        intent.setShowGif(true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Uri uri = Uri.fromFile(new File(photos.get(0)));
                setSelectedPhotoInsideCircleShap(uri);
            }
        }
    }

    private void setSelectedPhotoInsideCircleShap(Uri uri){
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .into(imageView1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pickPhoto:
              //  pickPhoto();
                break;
        }
    }

    private void setActionsOfToolBarIcons() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);



        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");

        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("تعديل بياناتي");
        title.setTypeface(font);

        ImageView searchIc = (ImageView) toolbar.findViewById(R.id.search);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView chatIc = (ImageView) toolbar.findViewById(R.id.chat);

        profileIc.setVisibility(View.VISIBLE);
        chatIc.setVisibility(View.VISIBLE);

        profileIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addTeacherDetailToBackstack()) {
                    TeacherDetails teacherDetails =
                            new TeacherDetails();
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

    private boolean addTeacherDetailToBackstack(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0){
            return true;
        }else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equalsIgnoreCase(TeacherDetails.TAG)){
            return false;
        }
        return true;
    }

    private void populateSpinner1(List<SpinnerItem> mlist) {

        CountriesSpinnerAdapter spinnerArrayAdapter = new CountriesSpinnerAdapter(getActivity(), R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(spinnerArrayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    country_id = selectedItem.getId();
                    String urlCities = BuildConfig.API_CITIES + selectedItem.getId();
                    fetchCitiesData(urlCities);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner2(List<SpinnerItem> mlist) {

        CitiesSpinnerAdapter spinnerArrayAdapter = new CitiesSpinnerAdapter(getActivity(), R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(spinnerArrayAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    city_id = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner3(List<SpinnerItem> mlist) {

        StageSpinnerAdapter spinnerArrayAdapter = new StageSpinnerAdapter(getActivity(), R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner3.setAdapter(spinnerArrayAdapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    stage_id = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner4(List<SpinnerItem> mlist) {

        MajorsSpinnerAdapter spinnerArrayAdapter = new MajorsSpinnerAdapter(getActivity(), R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner4.setAdapter(spinnerArrayAdapter);

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    major_id = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner5(List<SpinnerItem> mlist) {

        ServiceSpinnerAdapter spinnerArrayAdapter = new ServiceSpinnerAdapter(getActivity(), R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner5.setAdapter(spinnerArrayAdapter);

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    apply_service_id = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner6(List<SpinnerItem> mlist) {

        SexSpinnerAdapter spinnerArrayAdapter = new SexSpinnerAdapter(getActivity(), R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner6.setAdapter(spinnerArrayAdapter);

        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    gender_id = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }





    private void fetchSpinners(){
        getCountries();
        getStage();
        getMagor();
        getSex();
        getApplyService();

        fetchData();

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
                populateSpinner6(JsonParser.parseSexFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    populateSpinner6(JsonParser.parseSexFeed(response));
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
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

    private void getMagor() {
        /**
         * this section for fetch stage
         */
        String urlstage = BuildConfig.API_MAJORS;
        // We first check for cached request
        Cache cache1 = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry1 = cache1.get(urlstage);
        if (entry1 != null) {
            // fetch the data from cache
            try {
                String data = new String(entry1.data, "UTF-8");
                // do some thing
                populateSpinner4(JsonParser.parseMajorsFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    populateSpinner4(JsonParser.parseMajorsFeed(response));
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
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
                populateSpinner5(JsonParser.parseApplyServicesFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    populateSpinner5(JsonParser.parseApplyServicesFeed(response));
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
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
                populateSpinner3(JsonParser.parseStageFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlstage, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    populateSpinner3(JsonParser.parseStageFeed(response));
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
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
                populateSpinner1(JsonParser.parseCountriesFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlBrands, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    populateSpinner1(JsonParser.parseCountriesFeed(response));
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
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
                populateSpinner2(JsonParser.parseCitiesFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlCities, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    populateSpinner2(JsonParser.parseCitiesFeed(response));
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
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

    private void fetchData() {
        if (Utils.isOnline(getActivity())) {

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
                    params.put("user_id", new TutorsPrefStore(getActivity()).getPreferenceValue(Constants.TEACHER_ID));
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

    private void parseFeed(String response) {
        try {
            JSONObject jsonRootObject = new JSONObject(response);
            JSONObject itemObject = jsonRootObject.optJSONObject("item");
            editText3.setText(itemObject.optString("name"));
            name = itemObject.optString("name");
            editText1.setText(itemObject.optString("desc"));
            desc = itemObject.optString("desc");
            // populate mainImage
            Glide.with(getActivity())
                    .load(itemObject.optString("image_full_path"))
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.login_user_ico)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView1);

            JSONObject teacherInfoObject = itemObject.optJSONObject("teacher_info");
            editText5.setText(teacherInfoObject.optString("hour_price"));
            hour_price = teacherInfoObject.optString("hour_price");
            editText4.setText(teacherInfoObject.optString("subjects"));
            subjects = teacherInfoObject.optString("subjects");
            if (!teacherInfoObject.optString("qualification").equalsIgnoreCase("null")){
                editText6.setText(teacherInfoObject.optString("qualification"));
                qualification = teacherInfoObject.optString("qualification");

            }
            if (!teacherInfoObject.optString("experience").equalsIgnoreCase("null")){
                editText7.setText(teacherInfoObject.optString("experience"));
                experience = teacherInfoObject.optString("experience");
            }


            JSONObject countryObject = teacherInfoObject.optJSONObject("country");
            selectValue(spinner1, countryObject.optString("name"));

            JSONObject cityObject = teacherInfoObject.optJSONObject("city");
            selectValue(spinner2, cityObject.optString("name"));

            JSONObject stageObject = teacherInfoObject.optJSONObject("stage");
            selectValue(spinner3, stageObject.optString("name"));

            JSONObject majorObject = teacherInfoObject.optJSONObject("major");
            selectValue(spinner4, majorObject.optString("name"));

            JSONObject applyServiceObject = teacherInfoObject.optJSONObject("apply_service");
            selectValue(spinner5, applyServiceObject.optString("name"));

            JSONObject genderObject = teacherInfoObject.optJSONObject("gender");
            selectValue(spinner6, genderObject.optString("name"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void selectValue(Spinner spinner, String value) {
        for (int i = 1; i < spinner.getCount(); i++) {
            if (((SpinnerItem) spinner.getItemAtPosition(i)).getName().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
