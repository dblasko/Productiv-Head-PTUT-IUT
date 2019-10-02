package com.example.timer;

public class TimerStatistique{

    //table modélisé en table avec constructeur/setter/getter

    private int travail;
    private String jour;
    private String mois;

    TimerStatistique(int travail, String jour,String mois){
        this.travail=travail;
        this.jour=jour;
        this.mois=mois;
    }

    public int getTravail() {return travail;}
    public String getJour(){return jour;}
    public String getMois(){return mois;}

    public void setTravail(int travail){this.travail=travail;}
    public void setJour(String jour){this.jour=jour;}
    public void setMois(String mois){this.mois=mois;}

}
