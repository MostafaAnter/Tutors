package perfect_apps.tutors.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.splunk.mint.Mint;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.tutors.R;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;

public class SplashActivity extends LocalizationActivity {
    @Bind(R.id.splash_image_logo)
    ImageView image0;
    private Animation fade0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default language of activity
        setDefaultLanguage("en");
        setLanguage("ar");


        setContentView(R.layout.content_splash);
        ButterKnife.bind(this);

        fade0 = AnimationUtils.loadAnimation(this, R.anim.fade_in_enter);

        image0.startAnimation(fade0);
        fade0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(new TutorsPrefStore(SplashActivity.this).getPreferenceValue(Constants.TEACHER_AUTHENTICATION_STATE)
                        .equalsIgnoreCase(Constants.TEACHER)){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra(Constants.COMMING_FROM, Constants.TEACHER_PAGE);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                    finish();


                }else if(new TutorsPrefStore(SplashActivity.this).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                        .equalsIgnoreCase(Constants.STUDENT)){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                    finish();

                }else {
                    startActivity(new Intent(SplashActivity.this, CategoryActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Mint.initAndStartSession(this.getApplication(), "74f29fe7");
    }
}
