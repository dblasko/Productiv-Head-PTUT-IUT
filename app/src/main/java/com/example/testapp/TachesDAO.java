package com.example.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TachesDAO {

    private MySQLite mySQLite;
    private SQLiteDatabase db;

    public TachesDAO(Context context) {
        mySQLite = MySQLite.getInstance(context);
    }

    public void open() {
        db = mySQLite.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void addTache(Taches tache) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", tache.getId());
        contentValues.put("DATEDEB", tache.getDateDebut());
        contentValues.put("DATEFIN", tache.getDateFin());
        contentValues.put("HEURE", tache.getHeure());
        contentValues.put("NOM", tache.getNom());
        contentValues.put("RESUME", tache.getNom());
        db.insert("Taches",null, contentValues);
    }

    public Taches getTache(String nom){

        Taches tache = null;

        Cursor c = db.rawQuery("SELECT * FROM Taches WHERE nom = '" + nom + "'", null);
        if (c.moveToFirst()) {
            tache = new Taches(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),c.getString(5));
        }
        c.close();
        return tache;

    }


    public List<Taches> getAllTaches() {
        ArrayList<Taches> taches = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM Taches", null);
        while(c.moveToNext()) {
            Taches tache = new Taches(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),c.getString(5));
            taches.add(tache);
        }
        c.close();
        return taches;
    }

}
