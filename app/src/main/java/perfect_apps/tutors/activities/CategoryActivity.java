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

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;

public class CategoryActivity extends AppCompatActivity {
    @Bind(R.id.text1) TextView textView1;
    @Bind(R.id.text2) TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        changeTextFont();
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
    }

    public void CateIsStudent(View view) {

    }

    public void cateIsTeacher(View view) {
        Intent intent = new Intent(this, RegisterTeacherMembershipActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);

    }
}
