package com.dblasko.productivhead.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dblasko.productivhead.Notifications.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private MySQLite mySQLite;
    private SQLiteDatabase db;

    public NotificationDAO(Context context) {
        mySQLite = MySQLite.getInstance(context);
    }

    public void open() {
        db = mySQLite.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void addNotification(Notification notification) {
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

        db.insert("Notifications",null, contentValues);
    }

    public void deleteNotification(int id) {
        String where = "id = ?";
        String[] whereArgs = {id + ""};
        db.delete("Notifications", where, whereArgs);
    }

    public void updateNotification(Notification notification) {
        ContentValues contentValues = new ContentValues();

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

        db.update("Notifications", contentValues, "id = " + notification.getId(), null);
    }

    public Notification getNotification(int id) {
        Notification notification = null;

        Cursor c = db.rawQuery("SELECT * FROM Notifications WHERE ID = " + id, null);
        if (c.moveToFirst()) {
            notification = new Notification(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getInt(9), c.getInt(10));
        }
        c.close();
        return notification;
    }

    public List<Notification> getAllNotifications() {
        ArrayList<Notification> notifications = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM Notifications", null);
        while(c.moveToNext()) {
            notifications.add(new Notification(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getInt(9), c.getInt(10)));
        }
        c.close();
        return notifications;
    }

}
