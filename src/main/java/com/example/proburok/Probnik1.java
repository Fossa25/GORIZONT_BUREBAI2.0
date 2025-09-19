package com.example.proburok;

public class Probnik1 {
    private String virobotka1;
    private String nety;
    private int id;
    public Probnik1(String virobotka, String nety,int id) {
        this.virobotka1 = virobotka;
        this.nety = nety;
        this.id = id;
    }


    public String getVirobotka1() {
        return virobotka1;
    }

    public String getNety() {
        return nety;
    }
    public int getId() {
        return id;
    }
}
