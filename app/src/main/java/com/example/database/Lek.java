package com.example.database;

import java.util.ArrayList;

public class Lek {
    private String nazwa;
    private String jednostka;
    private String dawkowanie;
    private String zapas;
    private String powiadomienia;
    private Powiadomienie powiadomienie;
    private String kiedyPowiadomienie;

    public Lek() {
    }

    public String getKiedyPowiadomienie() {
        return kiedyPowiadomienie;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getPowiadomienia(){
        return powiadomienia;
    }

    public String getJednostka() {
        return jednostka;
    }

    public String getDawkowanie() {
        return dawkowanie;
    }

    public String getZapas() {
        return zapas;
    }

    public Powiadomienie getPowiadomienie() {
        return powiadomienie;
    }

    public void setPowiadomienie(Powiadomienie powiadomienie) {
        this.powiadomienie = powiadomienie;
    }

    public void setKiedyPowiadomienie(String kiedyPowiadomienie) {
        this.kiedyPowiadomienie = kiedyPowiadomienie;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public void setJednostka(String jednostka) {
        this.jednostka = jednostka;
    }

    public void setDawkowanie(String dawkowanie) {
        this.dawkowanie = dawkowanie;
    }

    public void setZapas(String zapas) {
        this.zapas = zapas;
    }

    public void setPowiadomienia(String powiadomienia){
        this.powiadomienia = powiadomienia;
    }
}
