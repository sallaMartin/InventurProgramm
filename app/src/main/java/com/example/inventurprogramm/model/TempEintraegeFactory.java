package com.example.inventurprogramm.model;

import com.example.inventurprogramm.model.Eintrag;

import java.util.ArrayList;

public class TempEintraegeFactory {
    ArrayList<Eintrag> eintraege;

    public TempEintraegeFactory() {
        eintraege = new ArrayList<>();
    }

    public ArrayList<Eintrag> getFilledList(){
        Eintrag e = new Eintrag("1234 12341234 1234", "Nividea G11", "3", "Wien", "1" );
        eintraege.add(e);
        e = new Eintrag("4321 12344532 5132", "AMD 8", "120", "Ried im Innkreis", "2" );
        eintraege.add(e);
        e = new Eintrag("1334 98321234 1564", "IMAC 10", "5", "Marchtrenk", "3" );
        eintraege.add(e);
        e = new Eintrag("9878 19923234 1244", "IPhone 11x", "10", "Ried im Innkreis", "4" );
        eintraege.add(e);
        e = new Eintrag("2934 12341234 1234", "LAPTOP", "23", "Gmunde", "5" );
        eintraege.add(e);
        e = new Eintrag("1234 55541234 5543", "Windows Explorer", "1", "Frankfurt am Main", "6" );
        eintraege.add(e);
        e = new Eintrag("7234 76341234 3114", "Wonderwall MP3", "200", "Main", "7" );

        return eintraege;
    }
}
