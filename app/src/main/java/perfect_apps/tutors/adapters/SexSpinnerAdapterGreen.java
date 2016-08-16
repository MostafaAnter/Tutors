package perfect_apps.tutors.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import perfect_apps.tutors.R;
import perfect_apps.tutors.models.SpinnerItem;


/**
 * Created by mostafa on 20/06/16.
 */
public class SexSpinnerAdapterGreen extends ArrayAdapter {

    private Context mContext;
    private List<SpinnerItem> mDataset;
    LayoutInflater inflater;

    /*************  TeachersListAdapter Constructor *****************/
    public SexSpinnerAdapterGreen(
            Context mContext,
            int textViewResourceId,
            List<SpinnerItem> mDataset
    )
    {
        super(mContext, textViewResourceId, mDataset);

        /********** Take passed values **********/
        this.mContext = mContext;
        this.mDataset = mDataset;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_green_item, parent, false);

        TextView label = (TextView)row.findViewById(R.id.label);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");
        label.setTypeface(font);


        if(position==0){

            // Default selected Spinner item
            label.setText("الجنس");
        }
        else
        {
            label.setText(mDataset.get(position).getName());
            label.setTextColor(Color.BLACK);
        }

        return row;
    }
}