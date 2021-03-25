package com.example.inventurprogramm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.inventurprogramm.model.Eintrag;
import com.example.inventurprogramm.model.TempEintraegeFactory;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String DB_NAME = "databases";
    private static final int DB_VERSION = 1;
    EditText plainTextEan;
    TextView textViewEanNichtGefunden;

    EditText plainTextMenge;
    EditText plainTextLagerort;

    Button buttonSpeichern;
    TextView textViewStamm;
    TextView textViewEingabe;


    List<Eintrag> arry = new ArrayList<>();
    String ean;
    String bezeichnung = null;
    Boolean eanGefunden = false;

    MySQLiteHelper dbHelper = new MySQLiteHelper(this);//TODO

    SQLiteDatabase mydatabase;



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

        TempEintraegeFactory.getFilledList();
        vergleichEAN();


        mydatabase = openOrCreateDatabase("databases", MODE_PRIVATE, null);
        //mydatabase.execSQL("DROP TABLE Eintrag;");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Eintrag(id Integer, bezeichnung VARCHAR, menge VARCHAR, lagerort VARCHAR, ean VARCHAR)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Stammdaten(id Integer, ean VARCHAR, bezeichnung VARCHAR)");

        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewEintrag();

                /*try {
                    DB snappyDB =  DBFactory.open("/data/data/com.example.inventurprogramm/databases/DatabaseTest");//TODO pfad eingeben
                    //Schreibt Daten mittels EAN in die Datenbank
                    snappyDB.put(ean, new Eintrag(ean,plainTextMenge.getText().toString(),plainTextLagerort.getText().toString()));

                } catch (SnappydbException snappydbException) {
                    snappydbException.printStackTrace();
                }

                 */
                int id = 1;
                String ean = plainTextEan.getText().toString();
                String menge = plainTextMenge.getText().toString();
                String lagerort = plainTextLagerort.getText().toString();
                if (eanGefunden) {

                    mydatabase.execSQL("Insert INTO Eintrag (id, bezeichnung, menge, lagerort, ean) Values( ?, ?, ?, ?, ?)", new Object[]{id, bezeichnung, menge, lagerort, ean});//TODO Insert INTO Eintrag statt Stammdaten
                    // Für Stammdaten Insert INTO Stammdaten (id, ean, bezeichnung) Values( ?, ?, ?)", new Object[]{id, ean ,bezeichnung
                    plainTextEan.setText("");
                    plainTextLagerort.setText("");
                    plainTextMenge.setText("");
                }else{
                    Log.e("Wrong","Der Ean ist zukurz");
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Wollen Sie Speichern "+"\n" +"Ean ist zukurz");
                    alertDialogBuilder.setCancelable(false);

                    alertDialogBuilder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int id = 1;
                            String ean = plainTextEan.getText().toString();
                            String menge = plainTextMenge.getText().toString();
                            String lagerort = plainTextLagerort.getText().toString();
                            bezeichnung = null;
                            mydatabase.execSQL("Insert INTO Eintrag (id, bezeichnung, menge, lagerort, ean) Values( ?, ?, ?, ?, ?)", new Object[]{id, bezeichnung, menge, lagerort, ean});

                            plainTextEan.setText("");
                            plainTextLagerort.setText("");
                            plainTextMenge.setText("");
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });





alertDialogBuilder.show();
                }



            }
        });

    }


    private void saveNewEintrag() {
        Eintrag e = new Eintrag(plainTextEan.getText().toString(), "TestEintrag" + TempEintraegeFactory.eintraege.size(), plainTextMenge.getText().toString(), plainTextLagerort.getText().toString(), "" + TempEintraegeFactory.eintraege.size() );
        TempEintraegeFactory.eintraege.add(e);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.subitemInfo:
                String text = "Text1/nText2/ntext3";
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Information");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setMessage("Copyright 2020 by\n" + "kiwi.it\n" + "Günter Rienzner\n" + "office@mykiwi.at\n" + "Nummer...");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                builder.create();
                return true;
            case R.id.subitemBeenden:
                finish();
                System.exit(0);
                return true;
            case R.id.subitemDatenEinlesen:
                String filename = "pfade.txt";
                String pfadEinlesen = "";
                try {
                    FileInputStream fis = openFileInput(filename);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] s = line.split(";");
                        pfadEinlesen = s[0];
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileInputStream fis = openFileInput(pfadEinlesen);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] stammdatenArray = line.split(";");
                        int id = 1;
                        mydatabase.execSQL("Insert INTO Stammdaten (id, ean, bezeichnung) Values( ?, ?, ?)", new Object[]{id, stammdatenArray[0], stammdatenArray[1]});


                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return true;
            case R.id.subitemDatenAusgeben:
                //Code
                String filename2 = "test4.txt";
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename2);

                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
                    out.println("test");
                    out.flush();
                    out.close();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.subitemPfadeAendern:
                Intent intentpfadAendernAcitivity = new Intent(getBaseContext(), ChangePathActivity.class);
                startActivity(intentpfadAendernAcitivity);
                return true;
            case R.id.subitemUebersicht:
                Intent intentUebersichtActivity = new Intent(getBaseContext(), OverviewActivity.class);
                startActivity(intentUebersichtActivity);
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

    public void vergleichEAN(){

        plainTextEan.addTextChangedListener(new TextWatcher() {



            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


               TempEintraegeFactory tempEintraegeFactory = new TempEintraegeFactory();
               //arry = tempEintraegeFactory.getFilledList();




                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor result = db.rawQuery("Select ean from Stammdaten;", null);

               String eintragEan;
               String stammDatenEan;

                if (s.length() > 7 && s.length() < 14) {
                    ean = plainTextEan.getText().toString();

                    //Toast.makeText(MainActivity.this, ean+ " ", Toast.LENGTH_SHORT).show();
                    while (result != null && result.moveToNext()) {

                        stammDatenEan = result.getString(0); //Der hier muss auch die ganze list durch gehen

                        result = db.rawQuery("Select count(*) from Stammdaten;",null);
                        result.moveToFirst();
                        int length = result.getInt(0);

                        Cursor bitte = db.rawQuery("Select  ean from Stammdaten;",null);
                        Cursor stammBezeichnung = db.rawQuery("Select bezeichnung from Stammdaten;",null);
                        for (int i = 0; i < length; i++) {
                        int lauf = 0;
                            stammBezeichnung.moveToNext();
                            bitte.moveToNext();
                            stammDatenEan = bitte.getString(lauf);
                            if (stammDatenEan.equals(ean)) { //stammDatenEan.equals(ean)
                                textViewEanNichtGefunden.setText(" ");
                                plainTextLagerort.setText("");
                                plainTextMenge.setText("");
                                bezeichnung = stammBezeichnung.getString(lauf);
                                textViewEanNichtGefunden.setText("Der EAN wurde gefunden");
                                eanGefunden = true;
                                //plainTextMenge.setText(arry.get(i).getMenge());
                                //plainTextLagerort.setText(arry.get(i).getLagerort());


                                break;
                            } else {
                                textViewEanNichtGefunden.setText("Der EAN wurde nicht gefunden");
                            lauf++;

                            eanGefunden = false;
                            }
                        }

                    }
                    }else{
                        //Toast.makeText(MainActivity.this, "Ean hat nicht die richtige Länge", Toast.LENGTH_SHORT).show();
                        //  textViewEanNichtGefunden.setText("");
                        textViewEanNichtGefunden.setText("Ean hat nicht die richtige Länge");
                        plainTextLagerort.setText("");
                        plainTextMenge.setText("");
                    }


/*
                rows.close();
                db.close();

 */

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper(@Nullable Context context){

            super(context, DB_NAME, null, DB_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
