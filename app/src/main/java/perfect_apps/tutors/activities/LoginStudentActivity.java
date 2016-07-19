package perfect_apps.tutors.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.BuildConfig;
import perfect_apps.tutors.R;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.Utils;

public class LoginStudentActivity extends LocalizationActivity {

    private static String email;
    private static String password;

    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.editText2) EditText editText2;
    @Bind(R.id.button1)
    Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);
        ButterKnife.bind(this);
        setToolbar();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("email")){
                editText1.setText(getIntent().getStringExtra("email"));
                editText2.setText(getIntent().getStringExtra("password"));
                requestData();
            }
        }
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
                //NavUtils.navigateUpFromSameTask(LoginStudentActivity.this);
                Intent intent = new Intent(LoginStudentActivity.this, HomeActivity.class);
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
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");

        editText1.setTypeface(font);
        editText2.setTypeface(font);
        button1.setTypeface(fontBold);
    }

    public void loginTeacher(View view) {
        requestData();
    }

    private void requestData() {
        if (Utils.isOnline(LoginStudentActivity.this)) {
            if (attempData()) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("جارى تسجيل الدخول...");
                pDialog.setCancelable(false);
                pDialog.show();

                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url = BuildConfig.API_BASE_URL + "/api/login/student";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();
                        parseFeed(response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        // show error message
                        new SweetAlertDialog(LoginStudentActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("خطأ")
                                .setContentText("تأكد من البريد الايلكترونى والرقم السرى")
                                .show();
                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;

                    }
                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        } else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }

    private boolean attempData(){
        email = editText1.getText().toString().trim();
        password = editText2.getText().toString().trim();

        // first check mail format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("البريد الالكترونى غير صالح")
                    .show();
            return false;
        }


        if (email != null && !email.trim().isEmpty()
                && password != null && !password.trim().isEmpty()){

            return true;

        }else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("قم بإكمال تسجيل البيانات")
                    .show();
            return false;
        }


    }

    private void parseFeed(String strJson) {

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonRootObject.optInt("status_code") == 200){
            try {
                JSONObject itemObject = jsonRootObject.getJSONObject("item");
                String id = itemObject.optString("id");
                String email = itemObject.optString("email");
                String password = itemObject.optString("user_password");
                String image_full_path = itemObject.optString("image_full_path");

                new TutorsPrefStore(LoginStudentActivity.this).addPreference(Constants.STUDENT_ID, id);
                new TutorsPrefStore(LoginStudentActivity.this).addPreference(Constants.STUDENT_EMAIL, email);
                new TutorsPrefStore(LoginStudentActivity.this).addPreference(Constants.STUDENT_PASSWORD, password);
                new TutorsPrefStore(LoginStudentActivity.this).addPreference(Constants.STUDENT_IMAGE_FULL_PATH, image_full_path);

                new TutorsPrefStore(LoginStudentActivity.this).addPreference(Constants.STUDENT_AUTHENTICATION_STATE, Constants.STUDENT);
                if (!getIntent().getExtras().containsKey("user_id")) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                    finish();
                } else {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
                    intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                    finish();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{

        }
    }

}
