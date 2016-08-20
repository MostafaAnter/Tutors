package perfect_apps.tutors.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.activities.LoginStudentActivity;
import perfect_apps.tutors.activities.RegisterStudentMembershipActivity;

/**
 * Created by mostafa on 07/07/16.
 */
public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
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
    static MyDialogFragment newInstance(int num) {
        MyDialogFragment f = new MyDialogFragment();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        ButterKnife.bind(this, v);
        changeTextFont();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView6.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
        textView1.setTypeface(fontBold);
        textView2.setTypeface(fontBold);
//        textView3.setTypeface(font);
        textView4.setTypeface(font);
//        textView5.setTypeface(font);
        textView6.setTypeface(font);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.closeDialog:
                dismiss();
                break;
            case R.id.text4:
                Intent intent1 = new Intent(getActivity(), LoginStudentActivity.class);
                intent1.putExtra("user_id", getArguments().getString("user_id"));
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                getActivity().finish();
                break;
            case R.id.text6:
                Intent intent2 = new Intent(getActivity(), RegisterStudentMembershipActivity.class);
                intent2.putExtra("user_id", getArguments().getString("user_id"));
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                getActivity().finish();
                break;
        }
    }
}