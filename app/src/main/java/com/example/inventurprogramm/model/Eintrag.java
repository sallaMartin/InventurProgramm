package com.example.inventurprogramm.model;

public class Eintrag {
    private String ean;
    private String bezeichnung;
    private String menge;
    private String lagerort;
    private String id;

    public Eintrag(String ean, String menge, String lagerort) {
        this.ean = ean;
        this.menge = menge;
        this.lagerort = lagerort;
    }

    public Eintrag(String ean, String bezeichnung, String menge, String lagerort, String id) {
        this.ean = ean;
        this.bezeichnung = bezeichnung;
        this.menge = menge;
        this.lagerort = lagerort;
        this.id = id;
    }

    public Eintrag() {

    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getMenge() {
        return menge;
    }

    public void setMenge(String menge) {
        this.menge = menge;
    }

    public String getLagerort() {
        return lagerort;
    }

    public void setLagerort(String lagerort) {
        this.lagerort = lagerort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
