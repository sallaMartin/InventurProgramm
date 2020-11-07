package com.example.inventurprogramm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.subitemInfo:
                //Intent intentPopAcivity = new Intent(getApplicationContext(), PopActivity.class);
                //startActivity(intentPopAcivity);
                return true;
            case R.id.subitemBeenden:
                finish();
                System.exit(0);
                return true;
            case R.id.subitemDatenEinlesen:
                //Code
                return true;
            case R.id.subitemDatenAusgeben:
                //Code
                return true;
            case R.id.subitemPfadeAendern:
                //Code
                return true;
            case R.id.subitemUebersicht:
                Intent intentActivity = new Intent(getBaseContext(), uebersichtActivity.class);
                startActivity(intentActivity);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
}