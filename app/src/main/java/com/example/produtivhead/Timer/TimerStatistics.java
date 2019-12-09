package com.example.produtivhead.Timer;

public class TimerStatistics {
    /*
        Contient les statistiques du jour
     */


    /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
     */

    // TODO -> Dans le DAO : bien penser => si déjà une entrée pour le jour, augmenter avec les données de cet objet, sinon insert
    // TODO -> Temps moyen de la session de travail?
    // Classe métier correspondant à la table en BD avec constructeur/setter/getter

    private float tpsTravail; // J'ai mis le temps de travail en float au lieu d'int
    private float tpsPause;
    private int nbSessionsTravail;
    private String date; // J'ai changé les jour/mois/an en Date, plus simple à gérer, je dois faire pareil chez moi

    public TimerStatistics(float tpsTravail, float tpsPause, int nbSessionTvail, String date){
        this.tpsTravail=tpsTravail;
        this.tpsPause = tpsPause;
        this.nbSessionsTravail = nbSessionTvail;
        this.date = date;
    }

    /* J'ai adapté les getters / setters & le constructeur à ces modifs du coup */

    public float getTpsTravail() {return tpsTravail;}
    public float getTpsPause(){return tpsPause;}
    public int getNbSessionsTravail(){return nbSessionsTravail;}
    public String getDate(){return date;}

    public void setTpsTravail(float travail){this.tpsTravail=travail;}
    public void setTpsPause(float tpsPause){this.tpsPause=tpsPause;}
    public void setNbSessionsTravail(int nbSessionsTravail){this.nbSessionsTravail = nbSessionsTravail;}
    public void setDate(String date){this.date = date;}

}
