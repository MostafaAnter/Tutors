package perfect_apps.tutors.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.splunk.mint.Mint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import perfect_apps.tutors.BuildConfig;
import perfect_apps.tutors.Manifest;
import perfect_apps.tutors.R;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.Utils;
import perfect_apps.tutors.utils.VolleyMultipartRequest;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class RegisterStudentMembershipActivity extends LocalizationActivity {
    private String name;
    private String email;
    private String password;
    private String password_confirmation;
    private Uri image;



    @Bind(R.id.text1) TextView textView1;
    @Bind(R.id.text2) TextView textView2;
    @Bind(R.id.text3) TextView textView3;
    @Bind(R.id.text4) TextView textView4;

    @Bind(R.id.editText1) EditText editText1;
    @Bind(R.id.editText2) EditText editText2;
    @Bind(R.id.editText3) EditText editText3;

    @Bind(R.id.button1) Button button1;
    @Bind(R.id.button2) Button button2;
    @Bind(R.id.image1) ImageView imageView1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student_membership);
        ButterKnife.bind(this);
        setToolbar();

        Mint.initAndStartSession(this.getApplication(), "74f29fe7");
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeTextFont();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrowleft_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavUtils.navigateUpFromSameTask(RegisterStudentMembershipActivity.this);
                Intent intent = new Intent(RegisterStudentMembershipActivity.this, HomeActivity.class);
                intent.putExtra(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                finish();
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
        Intent intent = new Intent(RegisterStudentMembershipActivity.this, HomeActivity.class);
        intent.putExtra(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
        finish();
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView4.setTypeface(font);
        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);
        button1.setTypeface(font);
        button2.setTypeface(fontBold);
        textView3.setTypeface(fontBold);
    }

    public void goToLogin(View view) {
        if (!getIntent().getExtras().containsKey("user_id")) {
            Intent intent = new Intent(this, LoginStudentActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
            finish();
        } else {
            Intent intent = new Intent(this, LoginStudentActivity.class);
            intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
            finish();
        }

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void pickPhoto(View view) {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
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
        registerStudent();
    }

    private void registerStudent(){
        // check on required data
        if (attempRegister()) {
            if (Utils.isOnline(RegisterStudentMembershipActivity.this)) {

                // make request
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("جارى انشاء الحساب...");
                pDialog.setCancelable(false);
                pDialog.show();
                String tag_string_req = "string_req";
                String url = BuildConfig.API_BASE_URL + "/api/register/student";
                // begin of request
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pDialog.dismissWithAnimation();
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject result = new JSONObject(resultResponse);
                            Log.d("response", resultResponse);
                            if (getIntent().getExtras() != null) {
                                if (!getIntent().getExtras().containsKey("user_id")) {
                                    Intent intent = new Intent(RegisterStudentMembershipActivity.this, LoginStudentActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
                                    finish();
                                } else {
                                    Intent intent = new Intent(RegisterStudentMembershipActivity.this, LoginStudentActivity.class);
                                    intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                                    intent.putExtra("email", email);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
                                    finish();
                                }
                            } else {
                                Intent intent = new Intent(RegisterStudentMembershipActivity.this, LoginStudentActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
                                finish();
                            }

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
                                errorServerMessage = errorMessageObject.optString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // show error message
                        new SweetAlertDialog(RegisterStudentMembershipActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("نأسف!")
                                .setContentText("البريد مستخدم من قبل اعد المحاوله")
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
                        if(name != null)
                            params.put("name", name);
                        if(email != null)
                            params.put("email", email);
                        if(password != null)
                            params.put("password", password);
                        if(password_confirmation != null)
                            params.put("password_confirmation", password_confirmation);
                        params.put("desc", null);
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView

                        if (image != null)
                            params.put("image", new DataPart("file_avatar.jpg", Utils.getFileDataFromDrawable(RegisterStudentMembershipActivity.this,
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

    private boolean attempRegister(){
        try {
            name = URLEncoder.encode(editText1.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        email = editText2.getText().toString().trim();
        password = editText3.getText().toString().trim();
        password_confirmation = editText3.getText().toString().trim();

        if (name == null || name.trim().isEmpty()) {
            Utils.showErrorMessage(this, "الرجاء أدخال الاسم كامل");
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

        if (editText3.getText().toString().trim().length() < 6){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("لابد ان يكون الرقم السرى اكثر من 6 خانات")
                    .show();
            return false;

        }

        return true;


    }
}
