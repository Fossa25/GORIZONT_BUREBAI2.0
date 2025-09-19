package com.example.proburok;

import java.sql.Date;

//для создания внести и выдать данные нажми alt+insert
public class Baza {
    private String NOMER;
    private Date DATA;
    private String SEHEN;
    private String GORIZONT;
    private String NAME;
    private String KATEGORII;
    private String OPISANIE;
    private String FAKTOR;
    private String TIPPAS;
    private String PRIVIZKA;
    private String UGOL;
    private String DLINA;
    private String UHASTOK;
    private String PRIM;
    private String NAME_BD;
    private String IDI;

    public Baza(Date DATA, String NOMER, String SEHEN, String GORIZONT, String NAME) {
        this.DATA = DATA;
        this.NOMER = NOMER;
        this.SEHEN = SEHEN;
        this.GORIZONT = GORIZONT;
        this.NAME = NAME;
    }
    @Override
    public String toString() {
        return NAME; // Возвращаем название для отображения в ComboBox
    }
    public Baza() {}
    public String getNOMER() {
        return NOMER;
    }

    public void setNOMER(String NOMER) {
        this.NOMER = NOMER;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public String getSEHEN() {
        return SEHEN;
    }

    public void setSEHEN(String SEHEN) {
        this.SEHEN = SEHEN;
    }

    public String getGORIZONT() {
        return GORIZONT;
    }

    public void setGORIZONT(String GORIZONT) {
        this.GORIZONT = GORIZONT;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
    public String getKATEGORII() {
        return KATEGORII;
    }

    public void setKATEGORII(String KATEGORII) {
        this.KATEGORII = KATEGORII;
    }
    public String getOPISANIE() {
        return OPISANIE;
    }

    public void setOPISANIE(String OPISANIE) {
        this.OPISANIE = OPISANIE;
    }
    public String getFAKTOR() {
        return FAKTOR;
    }

    public void setFAKTOR(String FAKTOR) {
        this.FAKTOR = FAKTOR;
    }

    public void setTIPPAS(String TIPPAS) {
        this.TIPPAS = TIPPAS;
    }

    public String getTIPPAS() {
        return TIPPAS;
    }

    public void setPRIVIZKA(String  PRIVIZKA) {this. PRIVIZKA =  PRIVIZKA;}

    public String getPRIVIZKA() {
        return  PRIVIZKA;
    }

    public void setUGOL(String  UGOL) {this.UGOL =  UGOL;}

    public String getUGOL() {
        return  UGOL;
    }

    public void setDLINA(String  DLINA) {this.DLINA =  DLINA;}

    public String getDLINA() {
        return  DLINA;
    }

    public String getUHASTOK() {
        return UHASTOK;
    }

    public void setUHASTOK(String uhastok) {
        this.UHASTOK = uhastok;
    }

    public void setPRIM(String prim) {
        this.PRIM = prim;
    }

    public String getPRIM() {
        return PRIM;
    }

    public void setNAME_BD(String nameBd) {
        this.NAME_BD = nameBd;
    }

    public String getNAME_BD() {
        return NAME_BD;
    }
    public void setIDI(String  IDI) {this.IDI =  IDI;}

    public String getIDI() {
        return  IDI;
    }
}
