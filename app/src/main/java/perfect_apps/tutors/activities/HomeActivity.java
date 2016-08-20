package perfect_apps.tutors.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.tutors.R;
import perfect_apps.tutors.app.AppController;
import perfect_apps.tutors.fragments.AboutFragment;
import perfect_apps.tutors.fragments.ContactUs;
import perfect_apps.tutors.fragments.MyChats;
import perfect_apps.tutors.fragments.SearchAboutTeacherFragment;
import perfect_apps.tutors.fragments.StudentDetails;
import perfect_apps.tutors.fragments.TeacherDetails;
import perfect_apps.tutors.fragments.TeachersHomeList;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.CustomTypefaceSpan;
import perfect_apps.tutors.utils.Utils;

public class HomeActivity extends LocalizationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static NavigationView navigationView;
    TextView studentMessageCount, teacherMessageCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        ImageView searchIc = (ImageView) toolbar.findViewById(R.id.search);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView chatIc = (ImageView) toolbar.findViewById(R.id.chat);
        ImageView back = (ImageView) toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() == 1){
                    finish();
                }else {
                    clearBackStack();
                }

            }
        });

        searchIc.setVisibility(View.GONE);
        profileIc.setVisibility(View.GONE);
        chatIc.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeFontOfNavigation();

        // if user is authenticated as teacher or student change menu :)
        if (new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.TEACHER_AUTHENTICATION_STATE)
                .equalsIgnoreCase(Constants.TEACHER) &&
                getIntent().getStringExtra(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_PAGE)) {
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_home_drawer_authenticated_teacher);
            changeFontOfNavigation();
            // get teacher message count
            teacherMessageCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.teacherMessages));
            teacherMessageCount.setGravity(Gravity.CENTER_VERTICAL);
            teacherMessageCount.setTypeface(null, Typeface.BOLD);
            teacherMessageCount.setTextColor(getResources().getColor(R.color.colorAccent));
            teacherMessageCount.setText("0");
            getMessageCount(new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.TEACHER_EMAIL),
                    new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.TEACHER_PASSWORD), teacherMessageCount);

        } else if (new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                .equalsIgnoreCase(Constants.STUDENT) &&
                getIntent().getStringExtra(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)) {
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_home_drawer_authenticated_student);
            changeFontOfNavigation();
            // get student message count
            studentMessageCount = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.studentMessages));
            studentMessageCount.setGravity(Gravity.CENTER_VERTICAL);
            studentMessageCount.setTypeface(null, Typeface.BOLD);
            studentMessageCount.setTextColor(getResources().getColor(R.color.colorAccent));
            studentMessageCount.setText("0");
            getMessageCount(new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.STUDENT_EMAIL),
                    new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.STUDENT_PASSWORD), studentMessageCount);
        }


        if (savedInstanceState == null) {
            if (getIntent().getStringExtra(Constants.COMMING_FROM).equalsIgnoreCase(Constants.TEACHER_PAGE)) {
                TeachersHomeList teachersListFragment = new TeachersHomeList();
                teachersListFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, teachersListFragment).addToBackStack(TeachersHomeList.TAG).commit();
            } else {
                SearchAboutTeacherFragment teachersListFragment =
                        new SearchAboutTeacherFragment();
                teachersListFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, teachersListFragment).addToBackStack(SearchAboutTeacherFragment.TAG).commit();


                if (getIntent().getExtras().containsKey("user_id")) {
                    Bundle arguments = new Bundle();
                    arguments.putString(Constants.DETAIL_USER_ID, getIntent().getStringExtra("user_id"));
                    arguments.putString(Constants.COMMING_FROM, Constants.STUDENT_PAGE);
                    TeacherDetails fragment = new TeacherDetails();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(TeacherDetails.TAG)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.login) {
            // login student page
            Intent intent = new Intent(HomeActivity.this, LoginStudentActivity.class);
            intent.putExtra("", "");
            startActivity(intent
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.register_teacher_membership) {
            startActivity(new Intent(HomeActivity.this, RegisterTeacherMembershipActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.register_student_membership) {
            startActivity(new Intent(HomeActivity.this, RegisterStudentMembershipActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.search_about_teacher) {
            if (addStudentHomeListToBackstack()) {
                clearBackStack();
                SearchAboutTeacherFragment teacherDetails =
                        new SearchAboutTeacherFragment();
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(SearchAboutTeacherFragment.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }
        } else if (id == R.id.about_app) {
            if (addAboutToBackstack()) {
                // clearBackStack();
                AboutFragment teacherDetails =
                        new AboutFragment();

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(AboutFragment.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }

        } else if (id == R.id.contact_us) {
            if (addContactToBackstack()) {
                // clearBackStack();
                ContactUs teacherDetails =
                        new ContactUs();

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(ContactUs.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }

        } else if (id == R.id.studentHome) {
            if (addStudentHomeListToBackstack()) {
                clearBackStack();
                SearchAboutTeacherFragment teacherDetails =
                        new SearchAboutTeacherFragment();
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(SearchAboutTeacherFragment.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }
        } else if (id == R.id.studentMessages) {
            if (addTeacherMessageToBackstack()) {
                // clearBackStack();
                MyChats teacherDetails =
                        new MyChats();
                Bundle b = new Bundle();
                b.putString(Constants.COMMING_FROM, getIntent().getStringExtra(Constants.COMMING_FROM));
                teacherDetails.setArguments(b);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(MyChats.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }
        } else if (id == R.id.studentMyData) {
            if (addStudentDetailToBackstack()) {
                // clearBackStack();
                StudentDetails teacherDetails =
                        new StudentDetails();
                Bundle b = new Bundle();
                b.putString(Constants.COMMING_FROM, getIntent().getStringExtra(Constants.COMMING_FROM));
                b.putString(Constants.DETAIL_USER_ID, new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.STUDENT_ID));
                teacherDetails.setArguments(b);

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(StudentDetails.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }

        } else if (id == R.id.studentSignOut) {
            new TutorsPrefStore(HomeActivity.this).addPreference(Constants.STUDENT_AUTHENTICATION_STATE, "");
            startActivity(new Intent(HomeActivity.this, CategoryActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.teacherHome) {
            if (addTeacherHomeListToBackstack()) {
                clearBackStack();
                TeachersHomeList teacherDetails =
                        new TeachersHomeList();
                Bundle b = new Bundle();
                b.putString(Constants.COMMING_FROM, getIntent().getStringExtra(Constants.COMMING_FROM));

                teacherDetails.setArguments(b);

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(TeachersHomeList.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }

        } else if (id == R.id.teacherMessages) {
            if (addTeacherMessageToBackstack()) {
                // clearBackStack();
                MyChats teacherDetails =
                        new MyChats();
                Bundle b = new Bundle();
                b.putString(Constants.COMMING_FROM, getIntent().getStringExtra(Constants.COMMING_FROM));
                teacherDetails.setArguments(b);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(MyChats.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }

        } else if (id == R.id.teacherMyData) {
            if (addTeacherDetailToBackstack()) {
                // clearBackStack();
                TeacherDetails teacherDetails =
                        new TeacherDetails();
                Bundle b = new Bundle();
                b.putString(Constants.COMMING_FROM, getIntent().getStringExtra(Constants.COMMING_FROM));
                b.putString(Constants.DETAIL_USER_ID, new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.TEACHER_ID));

                teacherDetails.setArguments(b);

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, teacherDetails);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(TeacherDetails.TAG);
                transaction.commit();
                // to add to back stack
                getSupportFragmentManager().executePendingTransactions();
            }

        } else if (id == R.id.teacherSignOut) {
            new TutorsPrefStore(HomeActivity.this).addPreference(Constants.TEACHER_AUTHENTICATION_STATE, "");
            startActivity(new Intent(HomeActivity.this, CategoryActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean addStudentDetailToBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return true;
        } else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(StudentDetails.TAG)) {
            return false;
        }
        return true;
    }

    private boolean addTeacherDetailToBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return true;
        } else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(TeacherDetails.TAG)) {
            return false;
        }
        return true;
    }

    private boolean addContactToBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return true;
        } else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(ContactUs.TAG)) {
            return false;
        }
        return true;
    }

    private boolean addAboutToBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return true;
        } else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(AboutFragment.TAG)) {
            return false;
        }
        return true;
    }


    private boolean addTeacherMessageToBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return true;
        } else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(MyChats.TAG)) {
            return false;
        }
        return true;
    }

    private boolean addTeacherHomeListToBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return true;
        } else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(TeachersHomeList.TAG)) {
            return false;
        }
        return true;
    }

    private boolean addStudentHomeListToBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return true;
        } else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase(SearchAboutTeacherFragment.TAG)) {
            return false;
        }
        fm.popBackStackImmediate(SearchAboutTeacherFragment.TAG, 0);
        return true;
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    //change font of drawer
    private void changeFontOfNavigation() {
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void getMessageCount(String email, String password , final TextView t){
        String url = "http://services-apps.net/tutors/api/message/show/count?email=" + email + "&password=" + password;
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null && !Utils.isOnline(this)) {
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
                    String count = jsonObject.optString("count");
                    t.setText(count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (Utils.isOnline(this)) {
                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url1 = "http://services-apps.net/tutors/api/message/show/count?email=" + email + "&password=" + password;

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url1, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            response = URLDecoder.decode(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // do some thing here
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String count = jsonObject.optString("count");
                            t.setText(count);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("teeest", response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        }


    }

}
