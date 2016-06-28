package perfect_apps.tutors.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.utils.Constants;

/**
 * Created by mostafa on 26/06/16.
 */
public class TeacherDetails extends Fragment {
    public static final String TAG = "TeacherDetails";

    @Bind(R.id.text1) TextView textView1;
    @Bind(R.id.text2) TextView textView2;
    @Bind(R.id.text3) TextView textView3;
    @Bind(R.id.text4) TextView textView4;
    @Bind(R.id.text5) TextView textView5;
    @Bind(R.id.text6) TextView textView6;
    @Bind(R.id.text7) TextView textView7;
    @Bind(R.id.text8) TextView textView8;
    @Bind(R.id.text9) TextView textView9;
    @Bind(R.id.text10) TextView textView10;
    @Bind(R.id.text11) TextView textView11;
    @Bind(R.id.text12) TextView textView12;
    @Bind(R.id.text13) TextView textView13;
    @Bind(R.id.text14) TextView textView14;
    @Bind(R.id.text15) TextView textView15;
    @Bind(R.id.text16) TextView textView16;
    @Bind(R.id.text17) TextView textView17;
    @Bind(R.id.text18) TextView textView18;
    @Bind(R.id.text19) TextView textView19;
    @Bind(R.id.text20) TextView textView20;
    @Bind(R.id.text21) TextView textView21;
    @Bind(R.id.name) TextView textView22;
    @Bind(R.id.rate) TextView textView23;
    @Bind(R.id.rateStatic1) TextView textView24;
    @Bind(R.id.rateStatic2) TextView textView25;
    @Bind(R.id.desc) TextView textView26;
    @Bind(R.id.hour) TextView textView27;

    @Bind(R.id.button1)Button button1;
    @Bind(R.id.button2)Button button2;
    @Bind(R.id.button3)Button button3;

    @Bind(R.id.viewForStudent) LinearLayout viewThatShowForStudent;
    @Bind(R.id.viewForTeacher) LinearLayout viewThatShowForTeacher;

    public TeacherDetails(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_details, container, false);
        ButterKnife.bind(this, view);
        setTypeFace();


        // hide some view to suitable with context
        if (getArguments().containsKey(Constants.COMMING_FROM)) {
            if (getArguments().getString(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_PAGE)){
                viewThatShowForStudent.setVisibility(View.GONE);
            }
        }
        return view;
    }

    private void setTypeFace(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");

        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);
        textView6.setTypeface(font);
        textView7.setTypeface(font);
        textView8.setTypeface(font);
        textView9.setTypeface(font);
        textView10.setTypeface(font);
        textView11.setTypeface(font);
        textView12.setTypeface(font);
        textView13.setTypeface(font);
        textView14.setTypeface(font);
        textView15.setTypeface(font);
        textView16.setTypeface(font);
        textView17.setTypeface(font);
        textView18.setTypeface(font);
        textView19.setTypeface(fontBold);
        textView20.setTypeface(font);
        textView21.setTypeface(font);
        textView22.setTypeface(font);
        textView23.setTypeface(font);
        textView24.setTypeface(font);
        textView25.setTypeface(font);
        textView26.setTypeface(font);
        textView27.setTypeface(font);
        button1.setTypeface(fontBold);
        button2.setTypeface(fontBold);
        button3.setTypeface(fontBold);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(true);
    }
}
