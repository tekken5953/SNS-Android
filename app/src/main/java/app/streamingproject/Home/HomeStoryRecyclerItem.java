package app.streamingproject.Home;

import android.graphics.drawable.Drawable;

public class HomeStoryRecyclerItem {
    private Drawable profile;
    private String user_name;

    public Drawable getProfile() {
        return profile;
    }

    public void setProfile(Drawable profile) {
        this.profile = profile;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String user_name) {
        this.user_name = user_name;
    }

    public HomeStoryRecyclerItem(Drawable profile, String user_name) {
        this.profile = profile;
        this.user_name = user_name;
    }
}
