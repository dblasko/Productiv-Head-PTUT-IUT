package com.blaskodaniel.charttest2;

// cf https://fr.jeffprod.com/blog/2015/utilisation-d-une-base-sqlite-sous-android/
public class Habit {
    /*
        On modélise la table en une classe + getters/setters/constructeurs
     */

    private String habit;
    private String year;
    private String month;
    private String day;
    private double advancement;

    public Habit(String habit, String year, String month, String day, double advancement){
        this.habit = habit;
        this.year = year;
        this.month = month;
        this.day = day;
        this.advancement = advancement;
    }

    public String getHabit() {
        return habit;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public double getAdvancement() {
        return advancement;
    }

    public void setHabit(String habit){
        this.habit = habit;
    }

    public void setYear(String year) { this.year = year; }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setAdvancement(double advancement) {
        this.advancement = advancement;
    }
}
