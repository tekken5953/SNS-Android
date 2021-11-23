package app.streamingproject.Message;

import android.graphics.drawable.Drawable;

public class ChatNewItem {
    private Drawable profile;
    private String name;

    public Drawable getProfile() {
        return profile;
    }

    public void setProfile(Drawable profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatNewItem(Drawable profile, String name) {
        this.profile = profile;
        this.name = name;
    }
}
