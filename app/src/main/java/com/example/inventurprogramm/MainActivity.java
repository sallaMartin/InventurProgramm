package com.example.inventurprogramm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.inventurprogramm.database.InventoryHelper;
import com.example.inventurprogramm.database.InventoryTbl;
import com.example.inventurprogramm.model.Eintrag;
import com.example.inventurprogramm.model.TempEintraegeFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String DB_NAME = "databases";
    private static final int DB_VERSION = 1;

    //UI-Komponenten
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

    SQLiteDatabase inventoryDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI-Komponenten initalisieren
        plainTextEan = findViewById(R.id.plainTextEanView);
        textViewEanNichtGefunden = findViewById(R.id.textViewEanNichtGefundenView);

        plainTextMenge = findViewById(R.id.plainTextMengeView);
        plainTextLagerort = findViewById(R.id.plainTextLagerortView);

        buttonSpeichern = findViewById(R.id.buttonSpeichernView);
        textViewStamm = findViewById(R.id.textViewStammView);
        textViewEingabe = findViewById(R.id.textViewEingabeView);

        //fortlaufenden EAN-Vergleich initalisieren
        vergleichEAN();

        InventoryHelper dbHelper = new InventoryHelper(this);
        inventoryDB = dbHelper.getReadableDatabase();

        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                saveNewEintrag();

                 */

                /*try {
                    DB snappyDB =  DBFactory.open("/data/data/com.example.inventurprogramm/databases/DatabaseTest");//TODO pfad eingeben
                    //Schreibt Daten mittels EAN in die Datenbank
                    snappyDB.put(ean, new Eintrag(ean,plainTextMenge.getText().toString(),plainTextLagerort.getText().toString()));

                } catch (SnappydbException snappydbException) {
                    snappydbException.printStackTrace();
                }

                 */
                String ean = plainTextEan.getText().toString();
                String menge = plainTextMenge.getText().toString();
                String lagerort = plainTextLagerort.getText().toString();
                if (eanGefunden) {
                    inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{bezeichnung, menge, lagerort, ean});
                    // Für Stammdaten Insert INTO Stammdaten (id, ean, bezeichnung) Values( ?, ?, ?)", new Object[]{id, ean ,bezeichnung
                    plainTextEan.setText("");
                    plainTextLagerort.setText("");
                    plainTextMenge.setText("");
                } else {
                    Log.e("Wrong", "Der Ean ist zukurz");
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Wollen Sie Speichern " + "\n" + "Ean ist zukurz");
                    alertDialogBuilder.setCancelable(false);

                    alertDialogBuilder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String ean = plainTextEan.getText().toString();
                            String menge = plainTextMenge.getText().toString();
                            String lagerort = plainTextLagerort.getText().toString();
                            bezeichnung = null;
                            inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{ean, bezeichnung, menge, lagerort});
                            /*
                            mydatabase.execSQL("Insert INTO Eintrag (id, bezeichnung, menge, lagerort, ean) Values( ?, ?, ?, ?, ?)", new Object[]{id, bezeichnung, menge, lagerort, ean});
                             */
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

                inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{bezeichnung, "", "", "6969669696"});

                Cursor rows = inventoryDB.rawQuery(InventoryTbl.STMT_SELECT, null);
                rows.moveToNext();
                Eintrag e = new Eintrag(
                        rows.getString(0),
                        rows.getString(2),
                        rows.getString(1),
                        rows.getString(3),
                        rows.getString(4));
                Log.i("info", e.toString());



            }
        });

    }


    private void saveNewEintrag() {
        Eintrag e = new Eintrag(plainTextEan.getText().toString(), "TestEintrag" + TempEintraegeFactory.eintraege.size(), plainTextMenge.getText().toString(), plainTextLagerort.getText().toString(), "" + TempEintraegeFactory.eintraege.size());
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
                        //mydatabase.execSQL("Insert INTO Stammdaten (id, ean, bezeichnung) Values( ?, ?, ?)", new Object[]{id, stammdatenArray[0], stammdatenArray[1]});


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


    public void vergleichEAN() {
        plainTextEan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TempEintraegeFactory tempEintraegeFactory = new TempEintraegeFactory();
                //arry = tempEintraegeFactory.getFilledList();


/*
                Cursor result = inventoryDB.rawQuery("Select ean from Stammdaten;", null);
    */

                String eintragEan;
                String stammDatenEan;

                if (s.length() > 7 && s.length() < 14) {
                    ean = plainTextEan.getText().toString();

                    /*
                    //Toast.makeText(MainActivity.this, ean+ " ", Toast.LENGTH_SHORT).show();
                    while (result != null && result.moveToNext()) {

                        stammDatenEan = result.getString(0); //Der hier muss auch die ganze list durch gehen

                        result = inventoryDB.rawQuery("Select count(*) from Stammdaten;", null);
                        result.moveToFirst();
                        int length = result.getInt(0);

                        Cursor bitte = inventoryDB.rawQuery("Select  ean from Stammdaten;", null);
                        Cursor stammBezeichnung = inventoryDB.rawQuery("Select bezeichnung from Stammdaten;", null);
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
                    */
                } else {
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


}
