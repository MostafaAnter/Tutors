package perfect_apps.tutors.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.splunk.mint.Mint;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;

public class CategoryActivity extends LocalizationActivity {
    @Bind(R.id.text1) TextView textView1;
    @Bind(R.id.text2) TextView textView2;
    @Bind(R.id.textTitle) TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        changeTextFont();

        Mint.initAndStartSession(this.getApplication(), "74f29fe7");
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        Typeface fontNormal = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView.setTypeface(fontNormal);
    }

    public void CateIsStudent(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
    }

    public void cateIsTeacher(View view) {
        if(new TutorsPrefStore(CategoryActivity.this).getPreferenceValue(Constants.TEACHER_AUTHENTICATION_STATE).equalsIgnoreCase(Constants.TEACHER)){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.COMMING_FROM, Constants.TEACHER_PAGE);
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
        }else {

            Intent intent = new Intent(this, RegisterTeacherMembershipActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
        }
    }
}
