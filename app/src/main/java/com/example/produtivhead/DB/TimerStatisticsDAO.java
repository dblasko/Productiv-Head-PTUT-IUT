package com.example.produtivhead.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.produtivhead.Timer.TimerStatistics;

public class TimerStatisticsDAO {
      /*
        Lien entre classe modèle & base de données - on définit la table, récupère base et méthodes accès
        CLASSE DAO EN SOMME
     */

    public static final String TABLE_NAME = "timerStatistics";
    public static final String KEY_TPS_TRAVAIL = "tpsTravail";
    public static final String KEY_TPS_PAUSE = "tpsPause";
    public static final String KEY_NB_SESSION_TVAIL = "nbSessionsTravail";
    public static final String KEY_DATE = "statDate";
    public static final String CREATE_TABLE_TIMERSTATISTIQUE = "CREATE TABLE " + TABLE_NAME
            + " (" +
            " " + KEY_TPS_TRAVAIL + " REAL," +
            " " + KEY_TPS_PAUSE + " REAL," +
            " " + KEY_NB_SESSION_TVAIL + " INTEGER," +
            " " + KEY_DATE + " TEXT," +
            " " + " CONSTRAINT pk_timerStatistique PRIMARY KEY(statDate)" // !! attention la clé primaire
            + ");";
    private MySQLite mySQLiteBase; // Notre gestionnaire de bd
    private SQLiteDatabase db; // Notre instance de bd

    public TimerStatisticsDAO(Context context){
        // On récupère le manager SQLite
        mySQLiteBase = MySQLite.getInstance(context);
    }

    public void open(){
        // Ouverture de la base en lecture/écriture - instance de la bd
        db = mySQLiteBase.getWritableDatabase();
    }

    public void close() {
        // Fermeture de l'accès à la BD
        db.close();
    }

    // TODO : enr||maj stat jour -> expliquer pour récupérer sysdate à laulau
    public void saveSessionStatistics(TimerStatistics stats) {
        /* During a session, the information is saved to a timerStatistics object -> this method
            saves its information into the dabatase to the corresponding date
         */
        Cursor c = db.rawQuery("SELECT tpsTravail, tpsPause, nbSessionsTravail FROM timerStatistics WHERE statDate = '" + stats.getDate() + "'", null);
        // If cursor fetches a line : there is an entry to update. Else, we insert
        if (c.moveToFirst()) {
            // UPDATE LES ATTR
            db.execSQL("UPDATE timerStatistics SET tpsTravail = tpsTravail + " + stats.getTpsTravail() + " WHERE statDate = '" + stats.getDate() + "'");
            db.execSQL("UPDATE timerStatistics SET tpsPause = tpsPause + " + stats.getTpsPause() + " WHERE statDate = '" + stats.getDate() + "'");
            db.execSQL("UPDATE timerStatistics SET nbSessionsTravail = nbSessionsTravail + " + stats.getNbSessionsTravail() + " WHERE statDate = '" + stats.getDate() + "'");
            // TODO IMPORTANT : penser à bien recréer un stats neuf après ajout -> DANS LE METIER BIEN LE REMETTRE À LA DATE ETC OU IDEALEMENT RECREER A CHAQUE APPEL
            stats = new TimerStatistics(0, 0, 0, "");
        } else {
                db.execSQL("INSERT INTO timerStatistics VALUES (" + Float.toString(stats.getTpsTravail()) + ", " + Float.toString(stats.getTpsPause()) + ", " + Integer.toString(stats.getNbSessionsTravail()) + ", '" + stats.getDate() + "')");
        }
        c.close();
    }

    public TimerStatistics getTimerStatistics(String date) {
        // RETOURNE LES STATISTIQUES D'UN JOUR DONNE OU NULL SI PAS D'ENTREE
        Cursor c = db.rawQuery("SELECT tpsTravail, tpsPause, nbSessionsTravail, statDate FROM timerStatistics WHERE statDate = '" + date + "'", null);
        if (c.moveToFirst()) { // on l'a mis en primary key, sûr qu'une ou 0 lignes
            float tpsTravail = c.getFloat(c.getColumnIndex(KEY_TPS_TRAVAIL));
            float tpsPause = c.getFloat(c.getColumnIndex(KEY_TPS_PAUSE));
            int nbSessionsTravail = c.getInt(c.getColumnIndex(KEY_NB_SESSION_TVAIL));
            c.close();
            return new TimerStatistics(tpsTravail, tpsPause, nbSessionsTravail, date);
        } else {
            c.close();
            return null;
        }
    }
    // TODO : laisser laulau faire fonctions dérivées de getStat pour avoir ce qu'elle veut


    public float getNbSession(String date) {
        // RETOURNE LES STATISTIQUES D'UN JOUR DONNE OU NULL SI PAS D'ENTREE
        Cursor c = db.rawQuery("SELECT nbSessionsTravail, statDate FROM timerStatistics WHERE statDate = '" + date + "'", null);
        if (c.moveToFirst()) { // on l'a mis en primary key, sûr qu'une ou 0 lignes
            int nbSessionsTravail = c.getInt(c.getColumnIndex(KEY_NB_SESSION_TVAIL));
            c.close();
            return (nbSessionsTravail);
        } else {
            c.close();
            return 0;
        }
    }

    public float getTpsTravail(String date) {
        // RETOURNE LES STATISTIQUES D'UN JOUR DONNE OU NULL SI PAS D'ENTREE
        Cursor c = db.rawQuery("SELECT tpsTravail,statDate FROM timerStatistics WHERE statDate = '" + date + "'", null);
        if (c.moveToFirst()) { // on l'a mis en primary key, sûr qu'une ou 0 lignes
            float tpsTravail = c.getFloat(c.getColumnIndex(KEY_TPS_TRAVAIL));
            c.close();
            return (tpsTravail);
        } else {
            c.close();
            return 0;
        }
    }

    public float getTpsPause(String date) {
        // RETOURNE LES STATISTIQUES D'UN JOUR DONNE OU NULL SI PAS D'ENTREE
        Cursor c = db.rawQuery("SELECT tpsPause, statDate FROM timerStatistics WHERE statDate = '" + date + "'", null);
        if (c.moveToFirst()) { // on l'a mis en primary key, sûr qu'une ou 0 lignes
            float tpsPause = c.getFloat(c.getColumnIndex(KEY_TPS_PAUSE));
            c.close();
            return (tpsPause);
        } else {
            c.close();
            return 0;
        }
    }

   //**********************************************************************************************************************************************************************************


    public int getNbSessionsMoisAnnee(String date) {
        String dateRech = date.substring(0, 7); // 7 exclus
        Cursor c = db.rawQuery("SELECT nbSessionsTravail, statDate FROM timerStatistics WHERE statDate LIKE '" + dateRech + "%'", null);
        int nb = 0;
        while(c.moveToNext()) {
            int nbSessionsTravail = c.getInt(c.getColumnIndex(KEY_NB_SESSION_TVAIL));
            nb = nb + nbSessionsTravail; // du jour
        } // si pas de tuple, vaudra 0
        c.close();
        return nb;
    }

    public float getTpsTravailMoisAnnee(String date) {
        String dateRech = date.substring(0, 7); // 7 exclus
        Cursor c = db.rawQuery("SELECT tpsTravail, statDate FROM timerStatistics WHERE statDate LIKE '" + dateRech + "%'", null);
        int tps = 0;
        while(c.moveToNext()) {
            int tpsTravail= c.getInt(c.getColumnIndex(KEY_TPS_TRAVAIL));
            tps = tps + tpsTravail; // du jour
        } // si pas de tuple, vaudra 0
        c.close();
        return tps;
    }

    public float getTpsPauseMoisAnnee(String date) {
        String dateRech = date.substring(0, 7); // 7 exclus
        Cursor c = db.rawQuery("SELECT tpsPause, statDate FROM timerStatistics WHERE statDate LIKE '" + dateRech + "%'", null);
        int tps = 0;
        while(c.moveToNext()) {
            int tpsPause= c.getInt(c.getColumnIndex(KEY_TPS_PAUSE));
            tps = tps + tpsPause; // du jour
        } // si pas de tuple, vaudra 0
        c.close();
        return tps;
    }




}
