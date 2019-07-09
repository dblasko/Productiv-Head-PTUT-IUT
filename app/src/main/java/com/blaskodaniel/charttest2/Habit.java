package com.blaskodaniel.charttest2;

// cf https://fr.jeffprod.com/blog/2015/utilisation-d-une-base-sqlite-sous-android/
public class Habit {
    /*
        On modélise la table en une classe + getters/setters/constructeurs
     */

    private String habit;
    private String month;
    private int day;
    private double advancement;

    public Habit(String habit, String month, int day, double advancement){
        this.habit = habit;
        this.month = month;
        this.day = day;
        this.advancement = advancement;
    }

    public String getHabit() {
        return habit;
    }

    public String getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public double getAdvancement() {
        return advancement;
    }

    public void setHabit(String habit){
        this.habit = habit;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setAdvancement(double advancement) {
        this.advancement = advancement;
    }
}
