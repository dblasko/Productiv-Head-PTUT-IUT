package fr.anerdunicorn.notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NotificationDatabaseManager {

    private MySQLite mySQLite;
    private SQLiteDatabase db;

    public NotificationDatabaseManager(Context context) {
        mySQLite = MySQLite.getInstance(context);
    }

    public void open() {
        db = mySQLite.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public long addNotification(Notification notification) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", notification.getId());
        contentValues.put("title", notification.getTitle());
        contentValues.put("content", notification.getContent());
        contentValues.put("repeatable", notification.getRepeatable());
        contentValues.put("hour", notification.getHour());
        contentValues.put("minute", notification.getMinute());
        contentValues.put("days", notification.getDays());
        contentValues.put("year", notification.getYear());
        contentValues.put("month", notification.getMonth());
        contentValues.put("day", notification.getDay());
        contentValues.put("active", notification.getActive());

        return db.insert("Notifications",null, contentValues);
    }

    public int deleteNotification(int id) {
        String where = "id" + " = ?";
        String[] whereArgs = {id + ""};
        return db.delete("Notifications", where, whereArgs);
    }

    public Notification getNotification(int id) {
        Notification notification = new Notification();

        Cursor c = db.rawQuery("SELECT * FROM Notifications WHERE ID = " + id, null);
        if (c.moveToFirst()) {
            notification.setId(c.getInt(0));
            notification.setTitle(c.getString(1));
            notification.setContent(c.getString(2));
            notification.setRepeatable(c.getInt(3));
            notification.setHour(c.getInt(4));
            notification.setMinute(c.getInt(5));
            notification.setDays(c.getInt(6));
            notification.setYear(c.getInt(7));
            notification.setMonth(c.getInt(8));
            notification.setDay(c.getInt(9));
            notification.setActive(c.getInt(10));
        }
        c.close();

        return notification;
    }

}
