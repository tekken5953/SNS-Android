package app.streamingproject.Home;


import android.graphics.drawable.Drawable;

public class HomePostRecyclerItem {

    private String name;
    private String date;
    private Drawable photo;
    private String post_content;
    private Integer heart_count;
    private Integer comment_count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public Integer getHeart_count() {
        return heart_count;
    }

    public void setHeart_count(Integer heart_count) {
        this.heart_count = heart_count;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public HomePostRecyclerItem(String name, String date, String post_content, Integer heart_count, Integer comment_count) {
        this.name = name;
        this.date = date;
        this.post_content = post_content;
        this.heart_count = heart_count;
        this.comment_count = comment_count;
    }
}
