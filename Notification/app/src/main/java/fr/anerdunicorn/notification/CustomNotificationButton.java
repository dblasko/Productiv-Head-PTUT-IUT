package fr.anerdunicorn.notification;

public class CustomNotificationButton {

    private int id;
    private String content;

    public CustomNotificationButton(int id, String content){
        this.id = id;
        this.content = content;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
