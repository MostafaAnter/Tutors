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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.Utils;

/**
 * Created by mostafa on 26/06/16.
 */
public class StudentDetails extends Fragment {
    public static final String TAG = "StudentDetails";
    private static int mStackLevel = 0;

    @Bind(R.id.text1) TextView textView1;

    @Bind(R.id.editText1) EditText editText1;
    @Bind(R.id.editText2) EditText editText2;


    @Bind(R.id.image1) ImageView imageView1;
    public StudentDetails() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_details, container, false);
        ButterKnife.bind(this, view);
        setTypeFace();

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
        editText1.setTypeface(font);
        editText2.setTypeface(font);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(true);
    }

    private void setActionsOfToolBarIcons() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");

        ImageView searchIc = (ImageView) toolbar.findViewById(R.id.search);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView chatIc = (ImageView) toolbar.findViewById(R.id.chat);

        searchIc.setVisibility(View.GONE);
        profileIc.setVisibility(View.GONE);
        chatIc.setVisibility(View.GONE);

        if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            title.setText("بياناتي الشخصية");
            title.setTypeface(font);
        }


//        profileIc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (addTeacherDetailToBackstack()) {
//                    StudentDetails teacherDetails =
//                            new StudentDetails();
//                    Bundle b = new Bundle();
//                    b.putString(Constants.COMMING_FROM, getArguments().getString(Constants.COMMING_FROM));
//
//                    teacherDetails.setArguments(b);
//
//                    FragmentTransaction transaction = getFragmentManager()
//                            .beginTransaction();
//                    transaction.replace(R.id.fragment_container, teacherDetails);
//                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    transaction.addToBackStack(StudentDetails.TAG);
//                    transaction.commit();
//                    // to add to back stack
//                    getActivity().getSupportFragmentManager().executePendingTransactions();
//                }
//
//            }
//        });
//
//        searchIc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        chatIc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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
            String url = "http://services-apps.net/tutors/api/info/student";

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

    private void parseFeed(String response) {
        try {
            JSONObject jsonRootObject = new JSONObject(response);
            JSONObject itemObject = jsonRootObject.optJSONObject("item");
            editText1.setText(itemObject.optString("name"));
            editText2.setText(itemObject.optString("email"));
            // populate mainImage
            Glide.with(getActivity())
                    .load(itemObject.optString("image_full_path"))
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.login_user_ico)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
