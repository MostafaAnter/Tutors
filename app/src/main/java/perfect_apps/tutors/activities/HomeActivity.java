package perfect_apps.tutors.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.localizationactivity.LocalizationActivity;

import perfect_apps.tutors.R;
import perfect_apps.tutors.fragments.MyChats;
import perfect_apps.tutors.fragments.SearchAboutTeacherFragment;
import perfect_apps.tutors.fragments.StudentDetails;
import perfect_apps.tutors.fragments.TeacherDetails;
import perfect_apps.tutors.fragments.TeachersHomeList;
import perfect_apps.tutors.store.TutorsPrefStore;
import perfect_apps.tutors.utils.Constants;
import perfect_apps.tutors.utils.CustomTypefaceSpan;

public class HomeActivity extends LocalizationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static NavigationView navigationView;

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
        }else if(new TutorsPrefStore(HomeActivity.this).getPreferenceValue(Constants.STUDENT_AUTHENTICATION_STATE)
                .equalsIgnoreCase(Constants.STUDENT) &&
                getIntent().getStringExtra(Constants.COMMING_FROM).equalsIgnoreCase(Constants.STUDENT_PAGE)){
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_home_drawer_authenticated_student);
            changeFontOfNavigation();
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


                if (getIntent().getExtras().containsKey("user_id")){
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
            startActivity(new Intent(HomeActivity.this, LoginStudentActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.register_teacher_membership) {
            startActivity(new Intent(HomeActivity.this, RegisterTeacherMembershipActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.register_student_membership) {
            startActivity(new Intent(HomeActivity.this, RegisterStudentMembershipActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.search_about_teacher) {

        } else if (id == R.id.about_app) {

        }else if (id == R.id.contact_us) {

        }else if (id == R.id.studentHome) {

        }else if (id == R.id.studentMessages) {

        }else if (id == R.id.studentMyData) {
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

        }else if (id == R.id.studentSearchAboutTeacher) {

        }else if (id == R.id.studentSignOut) {
            new TutorsPrefStore(HomeActivity.this).addPreference(Constants.STUDENT_AUTHENTICATION_STATE, "");
            startActivity(new Intent(HomeActivity.this, CategoryActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        }else if (id == R.id.teacherHome) {
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

        }else if (id == R.id.teacherMessages) {
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

        }else if (id == R.id.teacherMyData) {
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

        }else if (id == R.id.teacherSignOut) {
            new TutorsPrefStore(HomeActivity.this).addPreference(Constants.TEACHER_AUTHENTICATION_STATE, "");
            startActivity(new Intent(HomeActivity.this, CategoryActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean addStudentDetailToBackstack(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0){
            return true;
        }else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equalsIgnoreCase(StudentDetails.TAG)){
            return false;
        }
        return true;
    }

    private boolean addTeacherDetailToBackstack(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0){
            return true;
        }else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equalsIgnoreCase(TeacherDetails.TAG)){
            return false;
        }
        return true;
    }

    private boolean addTeacherMessageToBackstack(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0){
            return true;
        }else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equalsIgnoreCase(MyChats.TAG)){
            return false;
        }
        return true;
    }

    private boolean addTeacherHomeListToBackstack(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0){
            return true;
        }else if (fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equalsIgnoreCase(TeachersHomeList.TAG)){
            return false;
        }
        return true;
    }

    private void clearBackStack(){
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    //change font of drawer
    private void changeFontOfNavigation(){
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
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
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


}
