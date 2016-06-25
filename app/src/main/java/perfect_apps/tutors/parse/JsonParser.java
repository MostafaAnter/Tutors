package perfect_apps.tutors.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import perfect_apps.tutors.models.TeacherItem;

/**
 * Created by mostafa on 06/06/16.
 */
public class JsonParser {

    public static List<TeacherItem> parseTeachers(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("data");
            List<TeacherItem> teacherItems = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                String desc = jsonObject.optString("desc");
                String image_full_path = jsonObject.optString("image_full_path");
                float rating_per_5 = Float.valueOf(jsonObject.optString("rating_per_5"));
                String hour_price = jsonObject.optString("hour_price");
                teacherItems.add(new TeacherItem(id, name, desc, image_full_path, rating_per_5, hour_price));
            }
            return teacherItems;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
