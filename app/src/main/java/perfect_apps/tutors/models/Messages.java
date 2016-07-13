package perfect_apps.tutors.models;

/**
 * Created by mostafa on 13/07/16.
 */
public class Messages {
    public Messages(String userAvatar, String message, boolean show, String timestamp, int group_id, String messageOwnerEmail) {
        this.userAvatar = userAvatar;
        this.message = message;
        this.show = show;
        this.timestamp = timestamp;
        this.group_id = group_id;
        this.messageOwnerEmail = messageOwnerEmail;
    }

    private String userAvatar;
    private String message;
    private boolean show;
    private String timestamp;
    private int group_id;
    private String messageOwnerEmail;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getMessageOwnerEmail() {
        return messageOwnerEmail;
    }

    public void setMessageOwnerEmail(String messageOwnerEmail) {
        this.messageOwnerEmail = messageOwnerEmail;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
