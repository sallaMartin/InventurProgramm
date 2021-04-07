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
import com.example.inventurprogramm.database.StammdatenHelper;
import com.example.inventurprogramm.database.StammdatenTbl;
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
    private EditText plainTextEan;
    private TextView textViewEanVergleich;

    private EditText plainTextMenge;
    private EditText plainTextLagerort;
    private EditText alertBezeichnung;

    private Button buttonSpeichern;
    private TextView textViewStamm;
    private TextView textViewEingabe;


    private Boolean eanGefunden = false;

    private SQLiteDatabase inventoryDB;
    private SQLiteDatabase stammdatenDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI-Komponenten initalisieren
        plainTextEan = findViewById(R.id.plainTextEanView);
        textViewEanVergleich = findViewById(R.id.textViewEanVergleich);

        plainTextMenge = findViewById(R.id.plainTextMengeView);
        plainTextLagerort = findViewById(R.id.plainTextLagerortView);


        buttonSpeichern = findViewById(R.id.buttonSpeichernView);
        textViewStamm = findViewById(R.id.textViewStammView);
        textViewEingabe = findViewById(R.id.textViewEingabeView);

        //fortlaufenden EAN-Vergleich initalisieren
        vergleichEAN();

        //Databases initalisieren
        InventoryHelper inventoryHelper = new InventoryHelper(this);
        inventoryDB = inventoryHelper.getReadableDatabase();
        StammdatenHelper stammdatenHelper = new StammdatenHelper(this);
        stammdatenDB = stammdatenHelper.getReadableDatabase();


        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eanGefunden) {
                    String tempEAN = plainTextEan.getText().toString();
                    String tempMenge = plainTextMenge.getText().toString();
                    String tempLagerort = plainTextLagerort.getText().toString();

                    Cursor eintragCursor = stammdatenDB.rawQuery(StammdatenTbl.STMT_SELECT_WHERE_EAN, new String[]{tempEAN});
                    eintragCursor.moveToNext();
                    String tempBezeichnung = eintragCursor.getString(1);
                    eintragCursor.close();

                    inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{tempEAN, tempBezeichnung, tempMenge, tempLagerort});
                    plainTextEan.setText("");
                    plainTextLagerort.setText("");
                    plainTextMenge.setText("");

                    eanGefunden = false;
                } else {

                    Log.e("Wrong", "Der Ean ist zukurz");

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    final View customLayout = getLayoutInflater().inflate(R.layout.custom_alertdialog,null);
                    alertDialogBuilder.setView(customLayout);
                    alertDialogBuilder.setTitle("Speichern obwohl EAN falsch ist ");
                    alertDialogBuilder.setCancelable(false);




                    alertDialogBuilder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText alertDialog = customLayout.findViewById(R.id.alertDialog);
                            String tempEAN = plainTextEan.getText().toString();
                            String tempMenge = plainTextMenge.getText().toString();
                            String tempLagerort = plainTextLagerort.getText().toString();
                            String tempBezeichnung= alertDialog.getText().toString();

                            inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{tempEAN, tempBezeichnung, tempMenge, tempLagerort});
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


                    AlertDialog dialog = alertDialogBuilder.create();
                    alertDialogBuilder.show();

                }
            }
        });

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 7 && s.length() < 14) {
                    String tempEAN = plainTextEan.getText().toString();
                    Cursor eanCursor = stammdatenDB.rawQuery(StammdatenTbl.STMT_SELECT_EAN, null);

                    while (eanCursor != null && eanCursor.moveToNext()) {
                        String stammdatenEAN = eanCursor.getString(0);

                        if (stammdatenEAN.equals(tempEAN)) { //stammDatenEan.equals(ean)
                            /* weitere Werte aus Stammdaten einsetzen
                            Cursor eintragCursor = stammdatenDB.rawQuery(StammdatenTbl.STMT_SELECT_WHERE_EAN, new String[]{stammdatenEAN});
                            eintragCursor.moveToNext();
                            plainTextMenge.setText("" + eintragCursor.getString(2));
                            plainTextLagerort.setText("" + eintragCursor.getString(3));
                            eintragCursor.close();
                             */
                            textViewEanVergleich.setText("Der EAN wurde gefunden");

                            eanGefunden = true;
                            break;
                        } else {
                            textViewEanVergleich.setText("Der EAN wurde nicht gefunden");
                            plainTextLagerort.setText("");
                            plainTextMenge.setText("");

                            eanGefunden = false;
                        }
                    }

                    eanCursor.close();
                } else {
                    textViewEanVergleich.setText("Ean hat nicht die richtige Länge");
                    plainTextLagerort.setText("");
                    plainTextMenge.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}
