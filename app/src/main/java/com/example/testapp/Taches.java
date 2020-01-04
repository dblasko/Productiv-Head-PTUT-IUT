package com.example.testapp;

public class Taches {
    private int id;
    private String dateDebut;
    private String dateFin;
    private String heure;
    private String nom;
    private String resume;
    Taches(int idp,String dateD,String dateF,String heurep,String nomp,String resumep){
        id=idp;
        dateDebut=dateD;
        dateFin=dateF;
        heure=heurep;
        nom=nomp;
        resume=resumep;
    }
    public int getId(){
        return id;
    }
    public String getDateDebut(){
        return dateDebut;
    }
    public String getDateFin(){
        return dateFin;
    }
    public String getHeure(){
            return heure;
    }
    public String getNom(){
        return nom;
    }
    public String getResume(){
        return resume;
    }


}
