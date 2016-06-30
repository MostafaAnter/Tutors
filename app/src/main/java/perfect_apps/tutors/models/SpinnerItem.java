package perfect_apps.tutors.models;

/**
 * Created by mostafa on 30/06/16.
 */
public class SpinnerItem {
    private String id;
    private String name;

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

    public SpinnerItem(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
