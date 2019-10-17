package com.example.timer;

import android.app.Service;

public class TimerStatistique{

    //table modélisé en table avec constructeur/setter/getter

    private int tpsTravail;
    private int nbSessionTvail;
    private String jour;
    private String mois;
    private String annee;

    TimerStatistique(int tpsTravail, int nbSessionTvail, String jour,String mois, String annnee){
        this.tpsTravail=tpsTravail;
        this.nbSessionTvail=nbSessionTvail;
        this.jour=jour;
        this.mois=mois;
        this.annee=annnee;
    }

    public int getTpsTravail() {return tpsTravail;}
    public int getNbSessionTvail(){return nbSessionTvail;}
    public String getJour(){return jour;}
    public String getMois(){return mois;}
    public String getAnnee(){return annee;}

    public void setTpsTravail(int travail){this.tpsTravail=travail;}
    public void setNbSessionTvail(int nbSessionTvail){this.nbSessionTvail=nbSessionTvail;}
    public void setJour(String jour){this.jour=jour;}
    public void setMois(String mois){this.mois=mois;}
    public void setAnnee(String annee){this.annee=annee;}

}
