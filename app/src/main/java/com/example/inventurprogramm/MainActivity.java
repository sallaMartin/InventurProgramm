package com.example.inventurprogramm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    EditText plainTextEan;
    TextView textViewEanNichtGefunden;

    EditText plainTextMenge;
    EditText plainTextLagerort;

    Button buttonSpeichern;
    TextView textViewStamm;
    TextView textViewEingabe;

    Spinner spinnerMenue;
    Button buttonTastatur;
    Spinner spinnerDaten;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plainTextEan = (EditText) findViewById(R.id.plainTextEanView);
        textViewEanNichtGefunden = (TextView) findViewById(R.id.textViewEanNichtGefundenView);

        plainTextMenge = (EditText) findViewById(R.id.plainTextMengeView);
        plainTextLagerort = (EditText) findViewById(R.id.plainTextLagerortView);

        buttonSpeichern = (Button) findViewById(R.id.buttonSpeichernView);
        textViewStamm = (TextView) findViewById(R.id.textViewStammView);
        textViewEingabe = (TextView) findViewById(R.id.textViewEingabeView);

        spinnerMenue = (Spinner) findViewById(R.id.spinnerMenueView);
        buttonTastatur = (Button) findViewById(R.id.buttonTastaturView);
        spinnerDaten = (Spinner) findViewById(R.id.spinnerDatenView);

        fillSpinners();

        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity2.class);
                startActivity(intent);
            }
        });
    }


    public void fillSpinners() {
        ArrayList <String> spinnerMenueItems = new ArrayList<>();
        ArrayList <String> spinnerDatenItems = new ArrayList<>();

        spinnerMenueItems.add("Information");
        spinnerMenueItems.add("Beenden");

        spinnerDatenItems.add("Daten einlesen");
        spinnerDatenItems.add("Daten ausgeben");
        spinnerDatenItems.add("Pfade ändern");
        spinnerDatenItems.add("Übersicht");

        ArrayAdapter<String> arrayAdapterMenue = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerMenueItems);
        arrayAdapterMenue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenue.setAdapter(arrayAdapterMenue);

        ArrayAdapter<String> arrayAdapterDaten = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerDatenItems);
        arrayAdapterDaten.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaten.setAdapter(arrayAdapterDaten);
    }
}