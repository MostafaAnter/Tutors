package perfect_apps.tutors.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.R;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 14/07/16.
 */
public class AboutFragment extends Fragment {
    public static final String TAG = "AboutFragment";

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    public AboutFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.bind(this, view);
        setActionsOfToolBarIcons();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);

        if (Utils.isOnline(getActivity())) {
            String tag_string_req = "string_req";

            String url = "http://services-apps.net/tutors/api/pages/1";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    progressBar.setVisibility(View.GONE);
                    parseResponse(response);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            // show error message
            progressBar.setVisibility(View.GONE);
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
        }
    }

    private void parseResponse(String feed) {

        try {
            JSONObject rootObject = new JSONObject(feed);
            JSONObject dataObject = rootObject.optJSONObject("data");

            title.setText(dataObject.optString("title"));
            content.setText(Html.fromHtml(dataObject.optString("page_description")).toString().replaceAll("</p>|</br>", ""));


        } catch (JSONException e) {
            e.printStackTrace();
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
        title.setText("عن التطبيق");
        title.setTypeface(font);

    }
}
