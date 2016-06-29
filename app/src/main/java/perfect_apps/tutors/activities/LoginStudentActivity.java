package perfect_apps.tutors.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;

public class LoginStudentActivity extends LocalizationActivity {

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
                NavUtils.navigateUpFromSameTask(LoginStudentActivity.this);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
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


        new TutorsPrefStore(LoginStudentActivity.this).addPreference(Constants.AUTHENTICATION_STATE, Constants.STUDENT);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
        finish();
    }
}
