package perfect_apps.tutors.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.R;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 14/07/16.
 */
public class ContactUs extends Fragment implements View.OnClickListener {
    public static final String TAG = "ContactUs";

    private static String email;
    private static String name;
    private static String subject;
    private static String message;
    private static String phone;

    @Bind(R.id.linearSend)
    LinearLayout sendReport;
    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.text1)
    TextView textView1;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.editText3)
    EditText editText3;
    @Bind(R.id.editText4)
    EditText editText4;
    @Bind(R.id.editText5)
    EditText editText5;

    public ContactUs() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_us, container, false);
        ButterKnife.bind(this, view);
        changeFont();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionsOfToolBarIcons();
        button1.setOnClickListener(this);
        sendReport.setOnClickListener(this);
    }

    private void changeFont() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");

        textView1.setTypeface(fontBold);
        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);
        editText4.setTypeface(font);
        editText5.setTypeface(font);
        button1.setTypeface(font);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                sendReport();
                break;
            case R.id.linearSend:
                sendReport();
                break;
        }
    }

    private void setActionsOfToolBarIcons() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");

        ImageView searchIc = (ImageView) toolbar.findViewById(R.id.search);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView chatIc = (ImageView) toolbar.findViewById(R.id.chat);

        profileIc.setVisibility(View.GONE);
        chatIc.setVisibility(View.GONE);
        searchIc.setVisibility(View.GONE);


        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("اتصل بالأدارة");
        title.setTypeface(font);

    }

    private void sendReport() {
        if (Utils.isOnline(getActivity())) {

            if (checkValidation()) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("أنتظر...");
                pDialog.setCancelable(false);
                pDialog.show();

                // Tag used to cancel the request
                String url = "http://services-apps.net/tutors/api/contact_us";

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

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("عمل رأئع!")
                                .setContentText("تم ارسال رسالتك بنجاح")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        fm.popBackStack();

                                    }
                                })
                                .show();

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

                        params.put("full_name", name);
                        params.put("mobile_number", phone);
                        params.put("subject", subject);
                        params.put("message", message);
                        params.put("email", email);
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

    private boolean checkValidation() {
        try {
            name = URLEncoder.encode(editText1.getText().toString().trim(), "UTF-8");
            phone = URLEncoder.encode(editText3.getText().toString().trim(), "UTF-8");
            subject = URLEncoder.encode(editText4.getText().toString().trim(), "UTF-8");
            message = URLEncoder.encode(editText5.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        email = editText2.getText().toString().trim();


        // first check mail format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("البريد الالكترونى غير صالح")
                    .show();
            return false;
        }


        if (name != null && !name.trim().isEmpty()
                && subject != null && !subject.trim().isEmpty()
                && email != null && !email.trim().isEmpty()
                && phone != null && !phone.trim().isEmpty()
                && message != null && !message.trim().isEmpty()) {

            return true;

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("قم بإكمال تسجيل البيانات")
                    .show();
            return false;
        }
    }

}
