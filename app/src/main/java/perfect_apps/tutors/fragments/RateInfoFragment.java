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


    @Bind(R.id.text1)TextView textView1;
    @Bind(R.id.text2)TextView textView2;
    @Bind(R.id.text3)TextView textView3;
    @Bind(R.id.text4)TextView textView4;
    @Bind(R.id.text5)TextView textView5;
    @Bind(R.id.text6)TextView textView6;


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
        textView1.setTypeface(fontBold);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);
        textView6.setTypeface(font);
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
                    String oneStar = jsonObject1.optString("1");
                    String twoStar = jsonObject1.optString("2");
                    String threeStar = jsonObject1.optString("3");
                    String fourStar = jsonObject1.optString("4");
                    String fiveStar = jsonObject1.optString("5");

                    textView2.setText(oneStar);
                    textView3.setText(twoStar);
                    textView4.setText(threeStar);
                    textView5.setText(fourStar);
                    textView6.setText(fiveStar);


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
                            String oneStar = jsonObject1.optString("1");
                            String twoStar = jsonObject1.optString("2");
                            String threeStar = jsonObject1.optString("3");
                            String fourStar = jsonObject1.optString("4");
                            String fiveStar = jsonObject1.optString("5");

                            textView2.setText(oneStar);
                            textView3.setText(twoStar);
                            textView4.setText(threeStar);
                            textView5.setText(fourStar);
                            textView6.setText(fiveStar);


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