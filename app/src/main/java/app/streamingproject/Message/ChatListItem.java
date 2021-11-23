package app.streamingproject.Message;

import android.graphics.drawable.Drawable;

public class ChatListItem {
    private Drawable profile;
    private String user_name;
    private String chat_content;
    private String time_stamp;

    public Drawable getProfile() {
        return profile;
    }

    public void setProfile(Drawable profile) {
        this.profile = profile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getChat_content() {
        return chat_content;
    }

    public void setChat_content(String chat_content) {
        this.chat_content = chat_content;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public ChatListItem(Drawable profile, String user_name, String chat_content, String time_stamp) {
        this.profile = profile;
        this.user_name = user_name;
        this.chat_content = chat_content;
        this.time_stamp = time_stamp;
    }
}
