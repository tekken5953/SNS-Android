package app.streamingproject.Message;

public class ChattingItem {
    private String name;
    private String time_stamp;
    private String content;
    private int viewType;

    public String getName() {
        return name;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public int getViewType() {
        return viewType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChattingItem(String name, String time_stamp, String content, int viewType) {
        this.name = name;
        this.time_stamp = time_stamp;
        this.content = content;
        this.viewType = viewType;
    }
}
