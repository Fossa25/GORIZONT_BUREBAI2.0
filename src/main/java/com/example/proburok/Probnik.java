package com.example.proburok;

public class Probnik {
    private String virobotka;
    private Double plohad;
    private int id;
    public Probnik(String virobotka, Double plohad,int id) {
        this.virobotka = virobotka;
        this.plohad = plohad;
        this.id = id;
    }


    public String getVirobotka() {
        return virobotka;
    }

    public Double getPlohad() {
        return plohad;
    }
    public int getId() {
        return id;
    }
}
