package com.example.timer;

import java.util.Date;

public class TimerStatistics {
    /*
        Contient les statistiques du jour
     */

    // TODO -> Dans le DAO : bien penser => si déjà une entrée pour le jour, augmenter avec les données de cet objet, sinon insert
    // Classe métier correspondant à la table en BD avec constructeur/setter/getter

    private float tpsTravail; // J'ai mis le temps de travail en float au lieu d'int
    private int nbSessionsTravail;
    private Date date; // J'ai changé les jour/mois/an en Date, plus simple à gérer, je dois faire pareil chez moi

    TimerStatistics(int tpsTravail, int nbSessionTvail, Date date){
        this.tpsTravail=tpsTravail;
        this.nbSessionsTravail =nbSessionTvail;
        this.date = date;
    }

    /* J'ai adapté les getters / setters & le constructeur à ces modifs du coup */

    public float getTpsTravail() {return tpsTravail;}
    public int getNbSessionsTravail(){return nbSessionsTravail;}
    public Date getDate(){return date;}

    public void setTpsTravail(int travail){this.tpsTravail=travail;}
    public void setNbSessionsTravail(int nbSessionsTravail){this.nbSessionsTravail = nbSessionsTravail;}
    public void setDate(Date date){this.date = date;}

}
