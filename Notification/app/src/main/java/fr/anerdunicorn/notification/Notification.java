package fr.anerdunicorn.notification;

class Notification {

    private int id;
    private String title;
    private String content;
    private int repeatable;
    private int hour;
    private int minute;
    private int days;
    private int year;
    private int month;
    private int day;
    private int active;

    public Notification() {
        this.id = -1;
        this.title = "";
        this.content = "";
        this.repeatable = -1;
        this.hour = -1;
        this.minute = -1;
        this.day = -1;
        this.year = -1;
        this.month = -1;
        this.day = -1;
        this.active = -1;
    }

    public Notification(int id, String title, String content, int repeatable, int hour, int minute, int days, int year, int month, int day, int active) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.repeatable = repeatable;
        this.hour = hour;
        this.minute = minute;
        this.days = days;
        this.year = year;
        this.month = month;
        this.day = day;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(int repeatable) {
        this.repeatable = repeatable;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static boolean[] intToDays(int days) {
        boolean[] result = new boolean[7];
        int value = 64;
        for(int i = 0; i < 7; i++) {
            result[i] = (days - value >= 0);
            if(result[i]) {
                days -= value;
            }
            value /= 2;
        }
        return result;
    }

    public static int daysToInt(boolean[] days) {
        int result = 0;
        for(int i = 0; i < 7; i++) {
            if(days[i]) {
                result += Math.pow(2, i + 1);
            }
        }
        return result;
    }

    public static boolean isDayChosen(Notification notification, int day) {
        return notification.getDays() - Math.pow(2, day - 1) >= 0;
    }
}
