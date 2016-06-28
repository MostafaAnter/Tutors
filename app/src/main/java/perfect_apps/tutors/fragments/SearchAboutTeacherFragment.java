package perfect_apps.tutors.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import perfect_apps.tutors.R;

/**
 * Created by mostafa on 29/06/16.
 */
public class SearchAboutTeacherFragment extends Fragment {
    public SearchAboutTeacherFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_about_teacher_activity, container, false);
        return view;
    }
}
