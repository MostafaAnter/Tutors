package perfect_apps.tutors.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import perfect_apps.tutors.models.Messages;
import perfect_apps.tutors.models.MyChatsItem;
import perfect_apps.tutors.models.SpinnerItem;
import perfect_apps.tutors.models.TeacherItem;
import perfect_apps.tutors.utils.Utils;

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
                rating_per_5 = (float) Math.round(rating_per_5 * 10) /10;
                String hour_price = jsonObject.optString("hour_price");
                String rating_count = jsonObject.optString("rating_count");
                String rating_divid_count = rating_per_5 + " / " + /*rating_count +*/ "5  ";
                teacherItems.add(new TeacherItem(id, name, desc, image_full_path, rating_per_5, hour_price, rating_divid_count));
            }
            return teacherItems;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<SpinnerItem> parseCountriesFeed(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("countries");
            List<SpinnerItem> brandList = new ArrayList<>();
            brandList.add(null);
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new SpinnerItem(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<SpinnerItem> parseCitiesFeed(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("cities");
            List<SpinnerItem> brandList = new ArrayList<>();
            brandList.add(null);
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new SpinnerItem(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<SpinnerItem> parseStageFeed(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("stages");
            List<SpinnerItem> brandList = new ArrayList<>();
            brandList.add(null);
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new SpinnerItem(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<SpinnerItem> parseMajorsFeed(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("majors");
            List<SpinnerItem> brandList = new ArrayList<>();
            brandList.add(null);
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new SpinnerItem(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<SpinnerItem> parseApplyServicesFeed(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("apply_services");
            List<SpinnerItem> brandList = new ArrayList<>();
            brandList.add(null);
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new SpinnerItem(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<SpinnerItem> parseSexFeed(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("genders");
            List<SpinnerItem> brandList = new ArrayList<>();
            brandList.add(null);
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new SpinnerItem(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<MyChatsItem> parseMyMessages(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("data");
            List<MyChatsItem> teacherItems = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String message = jsonObject.optString("message");
                String created_at = jsonObject.optString("created_at");
                String new_count = jsonObject.optString("new_count");
                JSONObject sender = jsonObject.optJSONObject("sender");
                String name = sender.optString("name");
                String group_id  = sender.optString("group_id");
                String user_id = sender.optString("id");
                String image_full_path = sender.optString("image_full_path");
                teacherItems.add(new MyChatsItem(name, id, user_id, message,
                        Utils.manipulateDateFormat(created_at),
                        image_full_path , group_id, new_count));
            }
            return teacherItems;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<Messages> parseConversation(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("data");
            List<Messages> teacherItems = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String message = jsonObject.optString("message");
                String created_at = Utils.manipulateDateFormat(jsonObject.optString("created_at"));

                JSONObject sender = jsonObject.optJSONObject("sender");
                String email = sender.optString("email");
                String image_full_path = sender.optString("image_full_path");
                int group_id = sender.optInt("group_id");

                JSONArray message_users  = jsonObject.optJSONArray("message_users");
                JSONObject message_user = message_users.getJSONObject(0);
                String user_id = message_user.optString("user_id");
                String user_to_id = message_user.optString("user_to_id");
                String is_seen = message_user.optString("is_seen");

                boolean show =false;
                if (is_seen.equalsIgnoreCase("1")){
                    show = true;
                }

                teacherItems.add(new Messages(image_full_path, message, show, created_at, group_id, email, user_id, user_to_id));
            }
            return teacherItems;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

}
