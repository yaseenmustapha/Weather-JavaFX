package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Alert {
    private String title;
    private String time;
    private String expires;
    private String description;
    private String uri;
    private Boolean notified;

    public Alert(String title, int time, int expires, String description, String uri) {
        this.title = title;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = dateFormat.format(new Date((long)time * 1000));
        this.expires = dateFormat.format(new Date((long)expires * 1000));
        this.description = description.replaceAll("\\*", "\n*");
        this.uri = uri;
        this.notified = false;
    }

    public Boolean getNotified() {
        return this.notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getExpires() {
        return expires;
    }

    public String getDescription() {
        return description;
    }

    public String getUri() {
        return uri;
    }
}
