package perfect_apps.tutors.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import perfect_apps.tutors.R;

/**
 * Created by mostafa on 28/06/16.
 */
public class TeacherEditData extends Fragment implements View.OnClickListener {

    @Bind(R.id.editText1) EditText editText1;
    @Bind(R.id.editText2) EditText editText2;
    @Bind(R.id.editText3) EditText editText3;
    @Bind(R.id.editText4) EditText editText4;
    @Bind(R.id.editText5) EditText editText5;
    @Bind(R.id.editText6) EditText editText6;
    @Bind(R.id.editText7) EditText editText7;
    @Bind(R.id.editText8) EditText editText8;
    @Bind(R.id.editText9) EditText editText9;
    @Bind(R.id.button1) Button button1;
    @Bind(R.id.button2) Button button2;
    @Bind(R.id.image1) ImageView imageView1;

    @Bind(R.id.pickPhoto) LinearLayout linearLayout;

    @Bind(R.id.spinner1) Spinner spinner1;
    @Bind(R.id.spinner2) Spinner spinner2;
    @Bind(R.id.spinner3) Spinner spinner3;
    @Bind(R.id.spinner4) Spinner spinner4;
    @Bind(R.id.spinner5) Spinner spinner5;
    @Bind(R.id.spinner6) Spinner spinner6;

    private static final int REQUEST_CODE = 1;

    public TeacherEditData(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_edit_data, container, false);
        ButterKnife.bind(this, view);
        changeTextFont();
        linearLayout.setOnClickListener(this);

        return view;
    }

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);
        editText4.setTypeface(font);
        editText5.setTypeface(font);
        editText6.setTypeface(font);
        editText7.setTypeface(font);
        editText8.setTypeface(font);
        editText9.setTypeface(font);
        button1.setTypeface(font);
        button2.setTypeface(fontBold);
    }

    public void pickPhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        intent.setShowGif(true);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Uri uri = Uri.fromFile(new File(photos.get(0)));
                setSelectedPhotoInsideCircleShap(uri);
            }
        }
    }

    private void setSelectedPhotoInsideCircleShap(Uri uri){
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .into(imageView1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pickPhoto:
                pickPhoto();
                break;
        }
    }
}
