package perfect_apps.tutors.models;

/**
 * Created by mostafa on 24/06/16.
 */
public class TeacherItem {
    private String id;
    private String name;
    private String desc;
    private String image_full_path;
    private float rating_per_5;
    private String hour_price;
    private String rating_divide_count;

    public String getRating_divide_count() {
        return rating_divide_count;
    }

    public void setRating_divide_count(String rating_divide_count) {
        this.rating_divide_count = rating_divide_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_full_path() {
        return image_full_path;
    }

    public void setImage_full_path(String image_full_path) {
        this.image_full_path = image_full_path;
    }

    public float getRating_per_5() {
        return rating_per_5;
    }

    public void setRating_per_5(float rating_per_5) {
        this.rating_per_5 = rating_per_5;
    }

    public String getHour_price() {
        return hour_price;
    }

    public void setHour_price(String hour_price) {
        this.hour_price = hour_price;
    }

    public TeacherItem(String id, String name, String desc, String image_full_path, float rating_per_5, String hour_price,
                       String rating_divide_count) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image_full_path = image_full_path;
        this.rating_per_5 = rating_per_5;
        this.hour_price = hour_price;
        this.rating_divide_count = rating_divide_count;
    }
}
