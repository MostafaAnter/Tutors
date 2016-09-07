package perfect_apps.tutors.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.R;
import perfect_apps.tutors.activities.LoginStudentActivity;
import perfect_apps.tutors.activities.RegisterStudentMembershipActivity;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.models.MyChatsItem;
import perfect_apps.tutors.parse.JsonParser;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 07/07/16.
 */
public class RateInfoFragment extends DialogFragment implements View.OnClickListener {
    int mNum;


    @Bind(R.id.pb1)ProgressBar pb1;
    @Bind(R.id.pb2)ProgressBar pb2;
    @Bind(R.id.pb3)ProgressBar pb3;
    @Bind(R.id.pb4)ProgressBar pb4;
    @Bind(R.id.pb5)ProgressBar pb5;
    @Bind(R.id.ratingValue) TextView ratingValue;
    @Bind(R.id.ratingValue1) TextView ratingValue1;
    @Bind(R.id.ratingBar) RatingBar rb;



    @Bind(R.id.closeDialog) ImageView close;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static RateInfoFragment newInstance(int num) {
        RateInfoFragment f = new RateInfoFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_TITLE, theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        getRateInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rate_info, container, false);
        ButterKnife.bind(this, v);
        changeTextFont();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close.setOnClickListener(this);

    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.closeDialog:
                dismiss();
                break;
        }
    }

    private void getRateInfo(){
        String url = "http://services-apps.net/tutors/api/show/rates/grouping?teacher_id="
                 + getArguments().getString("user_id");
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
                // to do some thing
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("rates");
                    int rate1 = jsonObject1.optInt("1");
                    int rate2 = jsonObject1.optInt("2");
                    int rate3 = jsonObject1.optInt("3");
                    int rate4 = jsonObject1.optInt("4");
                    int rate5 = jsonObject1.optInt("5");



                    if ((rate1 + rate2 + rate3 + rate4 + rate5) != 0) {
                        float rate = (1*rate1 + 2*rate2 + 3*rate3 + 4*rate4 + 5*rate5)/(rate1 + rate2 + rate3 + rate4 + rate5);
                        rb.setRating(rate);
                        ratingValue.setText(String.valueOf(rate));
                        ratingValue1.setText((rate1 + rate2 + rate3 + rate4 + rate5)+" " + "تقييمات");
                        pb1.setMax((int) rate);
                        pb1.setProgress((int)((1*rate1)/(rate2 + rate3 + rate4 + rate5)));
                        pb2.setMax((int) rate);
                        pb2.setProgress((int)((2*rate2)/(rate1 + rate3 + rate4 + rate5)));
                        pb3.setMax((int) rate);
                        pb3.setProgress((int)((3*rate3)/(rate1 + rate2 + rate4 + rate5)));
                        pb4.setMax((int) rate);
                        pb4.setProgress((int)((4*rate4)/(rate1 + rate2 + rate3 + rate5)));
                        pb5.setMax((int) rate);
                        pb5.setProgress((int)((5*rate5)/(rate1 + rate2 + rate3 + rate4)));
                    }else {
                        rb.setRating(0);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (Utils.isOnline(getActivity())) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("جارى التحميل...");
                pDialog.setCancelable(false);
                pDialog.show();



                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url1 = "http://services-apps.net/tutors/api/show/rates/grouping?teacher_id="
                        + getArguments().getString("user_id");

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        try {
                            response = URLDecoder.decode(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // do some thing here

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.optJSONObject("rates");
                            int rate1 = jsonObject1.optInt("1");
                            int rate2 = jsonObject1.optInt("2");
                            int rate3 = jsonObject1.optInt("3");
                            int rate4 = jsonObject1.optInt("4");
                            int rate5 = jsonObject1.optInt("5");

                            if ((rate1 + rate2 + rate3 + rate4 + rate5) != 0) {
                                float rate = (1*rate1 + 2*rate2 + 3*rate3 + 4*rate4 + 5*rate5)/(rate1 + rate2 + rate3 + rate4 + rate5);
                                rb.setRating(rate);
                                ratingValue.setText(String.valueOf(rate));
                                ratingValue1.setText((rate1 + rate2 + rate3 + rate4 + rate5) +" "+ "تقييمات");
                                pb1.setMax((int) rate);
                                pb1.setProgress((int)((1*rate1)/(rate1 + rate2 + rate3 + rate4 + rate5)));
                                pb2.setMax((int) rate);
                                pb2.setProgress((int)((2*rate2)/(rate1 + rate2 + rate3 + rate4 + rate5)));
                                pb3.setMax((int) rate);
                                pb3.setProgress((int)((3*rate3)/(rate1 + rate2 + rate3 + rate4 + rate5)));
                                pb4.setMax((int) rate);
                                pb4.setProgress((int)((4*rate4)/(rate1 + rate2 + rate3 + rate4 + rate5)));
                                pb5.setMax((int) rate);
                                pb5.setProgress((int)((5*rate5)/(rate1 + rate2 + rate3 + rate4 + rate5)));
                            }else {
                                rb.setRating(0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    pDialog.dismissWithAnimation();
                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        }


    }
}