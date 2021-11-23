package app.streamingproject.Home;

public class HomeCommentItem {
    private String name;
    private String profile;
    private String content;
    private String timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public HomeCommentItem(String name, String profile, String content, String timestamp) {
        this.name = name;
        this.profile = profile;
        this.content = content;
        this.timestamp = timestamp;
    }
}
