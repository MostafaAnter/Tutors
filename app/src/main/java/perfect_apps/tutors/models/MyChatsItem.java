package perfect_apps.tutors.models;

/**
 * Created by mostafa on 12/07/16.
 */
public class MyChatsItem {
    private String chats_name;

    private String chat_id;

    private String chats_last_message;
    private String time_stamp;
    private String chats_avatar;
    public MyChatsItem(){

    }

    public MyChatsItem(String chats_name, String chat_id, String chats_avatar
            , String chats_last_message, String time_stamp){
        this.chats_avatar = chats_avatar;
        this.chat_id = chat_id;
        this.chats_last_message = chats_last_message;
        this.chats_name = chats_name;
        this.time_stamp = time_stamp;
    }

    public String getChats_name() {
        return chats_name;
    }

    public void setChats_name(String chats_name) {
        this.chats_name = chats_name;
    }

    public String getChats_last_message() {
        return chats_last_message;
    }

    public void setChats_last_message(String chats_last_message) {
        this.chats_last_message = chats_last_message;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getChats_avatar() {
        return chats_avatar;
    }

    public void setChats_avatar(String chats_avatar) {
        this.chats_avatar = chats_avatar;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }
}
