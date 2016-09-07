package perfect_apps.tutors.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import perfect_apps.tutors.utils.Utils;
import perfect_apps.tutors.utils.VolleyMultipartRequest;

public class RegisterTeacherMembershipActivity extends LocalizationActivity {
    // vars that i will post to service

    private String name;
    private String email;
    private String password;
    private String password_confirmation;
    private String country_id;
    private String city_id ;
    private String major_id ;
    private String stage_id ;
    private String subjects ;
    private String hour_price ;
    private String apply_service_id;
    private String gender_id;
    private Uri image;
    private String desc;
    private String who_am_i;
    private String qualification;
    private String experience;




    @Bind(R.id.text1) TextView textView1;
    @Bind(R.id.text2) TextView textView2;
    @Bind(R.id.text3) TextView textView3;
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
    @Bind(R.id.image1) ImageView imageView1;


    @Bind(R.id.spinner1) Spinner spinner1;
    @Bind(R.id.spinner2) Spinner spinner2;
    @Bind(R.id.spinner3) Spinner spinner3;
    @Bind(R.id.spinner4) Spinner spinner4;
    @Bind(R.id.spinner5) Spinner spinner5;
    @Bind(R.id.spinner6) Spinner spinner6;

    private static final int REQUEST_CODE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher_membership);
        ButterKnife.bind(this);
        setToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeTextFont();
        fetchData();
        List<SpinnerItem> spinnerItemList = new ArrayList<>();
        spinnerItemList.add(null);
        populateSpinner2(spinnerItemList);
        populateSpinner4(spinnerItemList);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrowleft_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(RegisterTeacherMembershipActivity.this);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
            }
        });

        /*
        * hide title
        * */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);

        /*
        * change font of title
        * */
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        mTitle.setTypeface(sharknyFont);

    }

    // set on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
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
        textView3.setTypeface(fontBold);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginTeacherActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);

    }

    public void pickPhoto(View view) {
        PhotoPickerIntent intent = new PhotoPickerIntent(RegisterTeacherMembershipActivity.this);
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        intent.setShowGif(true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Uri uri = Uri.fromFile(new File(photos.get(0)));
                image = uri;
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

    public void registerNewUser(View view) {
        registerTeacher();

    }

    private void populateSpinner1(List<SpinnerItem> mlist) {

        CountriesSpinnerAdapter spinnerArrayAdapter = new CountriesSpinnerAdapter(RegisterTeacherMembershipActivity.this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(spinnerArrayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    country_id = selectedItem.getId();
                    city_id = null;
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

    CitiesSpinnerAdapter spinnerArrayAdapter = new CitiesSpinnerAdapter(RegisterTeacherMembershipActivity.this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(spinnerArrayAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        StageSpinnerAdapter spinnerArrayAdapter = new StageSpinnerAdapter(RegisterTeacherMembershipActivity.this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner3.setAdapter(spinnerArrayAdapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    stage_id = selectedItem.getId();
                    major_id = null;
                    getMagor(stage_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner4(List<SpinnerItem> mlist) {

        MajorsSpinnerAdapter spinnerArrayAdapter = new MajorsSpinnerAdapter(RegisterTeacherMembershipActivity.this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner4.setAdapter(spinnerArrayAdapter);

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        ServiceSpinnerAdapter spinnerArrayAdapter = new ServiceSpinnerAdapter(RegisterTeacherMembershipActivity.this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner5.setAdapter(spinnerArrayAdapter);

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        SexSpinnerAdapter spinnerArrayAdapter = new SexSpinnerAdapter(RegisterTeacherMembershipActivity.this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner6.setAdapter(spinnerArrayAdapter);

        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                    List<SpinnerItem> spinnerItemList = JsonParser.parseSexFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner6(spinnerItemList);
                    }
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
                    List<SpinnerItem> spinnerItemList = JsonParser.parseMajorsFeed(response);

                    if (spinnerItemList != null) {
                        populateSpinner4(spinnerItemList);
                    }
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
                    List<SpinnerItem> spinnerItemList = JsonParser.parseApplyServicesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner5(spinnerItemList);
                    }
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
                    List<SpinnerItem> spinnerItemList = JsonParser.parseStageFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner3(spinnerItemList);
                    }
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
                    List<SpinnerItem> spinnerItemList = JsonParser.parseCountriesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner1(spinnerItemList);
                    }
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
                    List<SpinnerItem> spinnerItemList = JsonParser.parseCitiesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner2(spinnerItemList);
                    }
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

    private void registerTeacher(){
        // check on required data
        if (attempRegister()) {
            if (Utils.isOnline(RegisterTeacherMembershipActivity.this)) {

                // make request
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("جارى انشاء الحساب...");
                pDialog.setCancelable(false);
                pDialog.show();
                String tag_string_req = "string_req";
                String url = BuildConfig.API_BASE_URL + "/api/register/teacher";
                // begin of request
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pDialog.dismissWithAnimation();
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject result = new JSONObject(resultResponse);
                            Log.d("response", resultResponse);
                            Intent intent = new Intent(RegisterTeacherMembershipActivity.this, LoginTeacherActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        String errorServerMessage = "";
                        if (error.networkResponse.data != null) {
                            errorServerMessage = new String(error.networkResponse.data);
                            try {
                                JSONObject errorMessageObject = new JSONObject(errorServerMessage);
                                Log.e("server error", errorMessageObject.toString());
                                JSONObject jsonObjectError = errorMessageObject.optJSONObject("errors");
                                errorServerMessage = jsonObjectError.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // show error message
                        new SweetAlertDialog(RegisterTeacherMembershipActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("خطأ!")
                                .setContentText(errorServerMessage)
                                .show();

                        NetworkResponse networkResponse = error.networkResponse;
                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";
                            } else if (error.getClass().equals(NoConnectionError.class)) {
                                errorMessage = "Failed to connect server";
                            }
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                JSONObject response = new JSONObject(result);
                                String status = response.getString("status");
                                String message = response.getString("message");

                                Log.e("Error Status", status);
                                Log.e("Error Message", message);

                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        if(name != null && !name.trim().isEmpty())
                        params.put("name", name);
                        if(email != null && !email.trim().isEmpty())
                        params.put("email", email);
                        if(password != null&& !password.trim().isEmpty())
                        params.put("password", password);
                        if(password_confirmation != null && !password_confirmation.trim().isEmpty())
                        params.put("password_confirmation", password_confirmation);
                        if(country_id != null && !country_id.trim().isEmpty())
                        params.put("country_id", country_id);
                        if(city_id != null && !city_id.trim().isEmpty())
                        params.put("city_id", city_id);
                        if(major_id != null && !major_id.trim().isEmpty())
                        params.put("major_id", major_id);
                        if(stage_id != null && !stage_id.trim().isEmpty())
                        params.put("stage_id", stage_id);
                        if(subjects != null && !subjects.trim().isEmpty())
                        params.put("subjects", subjects);
                        if(hour_price != null && !hour_price.trim().isEmpty())
                        params.put("hour_price", hour_price);
                        if(apply_service_id != null && !apply_service_id.trim().isEmpty())
                        params.put("apply_service_id", apply_service_id);
                        if(gender_id != null && !gender_id.trim().isEmpty())
                        params.put("gender_id", gender_id);
                        if(desc != null && !desc.trim().isEmpty())
                        params.put("desc", desc);
                        if(who_am_i != null && !who_am_i.trim().isEmpty())
                        params.put("who_am_i", who_am_i);
                        if(experience != null && !experience.trim().isEmpty())
                        params.put("experience", experience);
                        if(qualification != null && !qualification.trim().isEmpty())
                        params.put("qualification", qualification);
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView

                        if (image != null)
                        params.put("image", new DataPart("file_avatar.jpg", Utils.getFileDataFromDrawable(RegisterTeacherMembershipActivity.this,
                                image), "image/jpeg"));

                        return params;
                    }
                };

                int socketTimeout = 30000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                multipartRequest.setRetryPolicy(policy);
                
                AppController.getInstance().addToRequestQueue(multipartRequest);
                // last of request



            }else {
                // show error message
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ناسف...")
                        .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                        .show();
            }
        }
    }

    private boolean attempRegister() {
        try {
            desc = URLEncoder.encode(editText1.getText().toString().trim(), "UTF-8");
            name = URLEncoder.encode(editText2.getText().toString().trim(), "UTF-8");
            subjects = URLEncoder.encode(editText3.getText().toString().trim(), "UTF-8");
            who_am_i = URLEncoder.encode(editText9.getText().toString().trim(), "UTF-8");
            qualification = URLEncoder.encode(editText7.getText().toString().trim(), "UTF-8");
            experience = URLEncoder.encode(editText8.getText().toString().trim(), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hour_price = editText4.getText().toString().trim();
        email = editText5.getText().toString().trim();
        password = editText6.getText().toString().trim();
        password_confirmation = editText6.getText().toString().trim();


//        if (desc == null || desc.trim().isEmpty()) {
//            Utils.showErrorMessage(this, "الرجاء كتابة نبذة مختصرة عنك");
//            return false;
//        }

        if (name == null || name.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء أدخال الاسم كامل");
            return false;
        }

        if (country_id == null || country_id.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء قم بأختيار الدولة");
            return false;
        }
        if (city_id == null || city_id.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء قم بأختيار المدينة");
            return false;
        }
        if (stage_id== null || stage_id.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء قم بأختيار المرحلة الدراسية");
            return false;
        }
        if (major_id == null || major_id.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء قم بأختيار التخصص");
            return false;
        }
        if (apply_service_id == null || apply_service_id.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء اختيار نوع تقديم الخدمة");
            return false;
        }
        if (gender_id == null || gender_id.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء اختيار نوع المعلم");
            return false;
        }
        if (email == null || email.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء ادخل البريد الاليكترونى");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء ادخل الرقم السري");
            return false;
        }


        // first check mail format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("البريد الالكترونى غير صالح")
                    .show();
            return false;
        }

        return true;

    }

}

