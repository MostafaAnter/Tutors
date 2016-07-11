package perfect_apps.tutors.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;

/**
 * Created by mostafa on 07/07/16.
 */
public class RatingDialogFragment extends DialogFragment implements View.OnClickListener {
    int mNum;


    @Bind(R.id.text1)TextView textView1;
    @Bind(R.id.closeDialog) ImageView close;
    @Bind(R.id.ratingBar) RatingBar ratingBar;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static RatingDialogFragment newInstance(int num) {
        RatingDialogFragment f = new RatingDialogFragment();

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
        View v = inflater.inflate(R.layout.fragment_dialog_rate, container, false);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.closeDialog:
                dismiss();
                break;
        }
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
}