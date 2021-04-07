package com.example.inventurprogramm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventurprogramm.database.InventoryHelper;
import com.example.inventurprogramm.database.InventoryTbl;
import com.example.inventurprogramm.database.StammdatenHelper;
import com.example.inventurprogramm.database.StammdatenTbl;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private final static String DB_NAME = "databases";
    private static final int DB_VERSION = 1;

    //UI-Komponenten
    private TextInputLayout plainTextEan;
    private TextView textViewEanVergleich;

    private TextInputLayout  plainTextMenge;
    private TextInputLayout  plainTextLagerort;
    private EditText alertBezeichnung;

    private Button buttonSpeichern;
    private TextView textViewStamm;
    private TextView textViewEingabe;


    private LinearLayout mainLayout;

    private Intent myFileIntent;
    private String stammdatenPfad = "";

    private int CODE_READ_EXTERNAL_FILE = 901;
    private int CODE_WRITE_EXTERNAL_FILE = 902;


    private Boolean eanGefunden = false;

    private SQLiteDatabase inventoryDB;
    private SQLiteDatabase stammdatenDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI-Komponenten initalisieren
        mainLayout = findViewById(R.id.mainLayout);

        plainTextEan = findViewById(R.id.plainTextEanView);
        textViewEanVergleich = findViewById(R.id.textViewEanVergleich);

        plainTextMenge = findViewById(R.id.plainTextMengeView);
        plainTextLagerort = findViewById(R.id.plainTextLagerortView);


        buttonSpeichern = findViewById(R.id.buttonSpeichernView);
        textViewStamm = findViewById(R.id.txtStamm);
        textViewEingabe = findViewById(R.id.txtInventory);


        //fortlaufenden EAN-Vergleich initalisieren
        vergleichEAN();

        //Databases initalisieren
        InventoryHelper inventoryHelper = new InventoryHelper(this);
        inventoryDB = inventoryHelper.getReadableDatabase();
        StammdatenHelper stammdatenHelper = new StammdatenHelper(this);
        stammdatenDB = stammdatenHelper.getReadableDatabase();

        //Stamm und Eingabae einholen
        anzahlQuery();

        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eanGefunden) {
                    String tempEAN = plainTextEan.getEditText().getText().toString();
                    String tempMenge = plainTextMenge.getEditText().getText().toString();
                    String tempLagerort = plainTextLagerort.getEditText().getText().toString();

                    Cursor eintragCursor = stammdatenDB.rawQuery(StammdatenTbl.STMT_SELECT_WHERE_EAN, new String[]{tempEAN});
                    eintragCursor.moveToNext();
                    String tempBezeichnung = eintragCursor.getString(1);
                    eintragCursor.close();

                    inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{tempEAN, tempBezeichnung, tempMenge, tempLagerort});
                    plainTextEan.getEditText().setText("");
                    plainTextLagerort.getEditText().setText("");
                    plainTextMenge.getEditText().setText("");
                    anzahlQuery();

                    Snackbar snackbar = Snackbar.make(mainLayout, "Gespeichert!", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    int myColor = getResources().getColor(R.color.colorPrimary);
                    sbView.setBackgroundColor(myColor);
                    snackbar.show();


                    eanGefunden = false;
                } else {

                    Log.e("Wrong", "Der Ean ist zukurz");

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    final View customLayout = getLayoutInflater().inflate(R.layout.custom_alertdialog,null);
                    alertDialogBuilder.setView(customLayout);
                    alertDialogBuilder.setCancelable(false);




                    alertDialogBuilder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            TextInputLayout alertDialog = customLayout.findViewById(R.id.alertDialog);
                            String tempEAN = plainTextEan.getEditText().getText().toString();
                            String tempMenge = plainTextMenge.getEditText().getText().toString();
                            String tempLagerort = plainTextLagerort.getEditText().getText().toString();
                            String tempBezeichnung= alertDialog.getEditText().getText().toString();

                            if(tempBezeichnung.equals("")) {
                                Snackbar snackbar = Snackbar.make(mainLayout, "Bitte Bezeichnung eingeben!", Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                int myColor = getResources().getColor(R.color.colorPrimary);
                                sbView.setBackgroundColor(myColor);
                                snackbar.show();
                            } else {
                                inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{tempEAN, tempBezeichnung, tempMenge, tempLagerort});
                                plainTextEan.getEditText().setText("");
                                plainTextLagerort.getEditText().setText("");
                                plainTextMenge.getEditText().setText("");


                                Snackbar snackbar = Snackbar.make(mainLayout, "Gespeichert!", Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                int myColor = getResources().getColor(R.color.colorPrimary);
                                sbView.setBackgroundColor(myColor);
                                snackbar.show();
                            }



                            inventoryDB.execSQL(InventoryTbl.STMT_INSERT, new Object[]{tempEAN, tempBezeichnung, tempMenge, tempLagerort});
                            plainTextEan.getEditText().setText("");
                            plainTextLagerort.getEditText().setText("");
                            plainTextMenge.getEditText().setText("");
                            anzahlQuery();

                        }
                    });

                    alertDialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            Snackbar snackbar = Snackbar.make(mainLayout, "Abgebrochen!", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            int myColor = getResources().getColor(R.color.colorAccent);
                            sbView.setBackgroundColor(myColor);
                            snackbar.show();



                        }
                    });


                    AlertDialog dialog = alertDialogBuilder.create();
                    alertDialogBuilder.show();

                }
            }
        });



    }

    private void anzahlQuery() {
        Cursor countCursor = inventoryDB.rawQuery(InventoryTbl.STMT_COUNT, null);
        countCursor.moveToNext();
        int count = countCursor.getInt(0);
        countCursor.close();
        textViewEingabe.setText(""+count);

        countCursor = stammdatenDB.rawQuery(StammdatenTbl.STMT_COUNT, null);
        countCursor.moveToNext();
        count = countCursor.getInt(0);
        countCursor.close();
        textViewStamm.setText(""+count);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_FILE);
                } else {
                    stammdatenEinlesen();
                }
                return true;
            case R.id.subitemDatenAusgeben:

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_FILE);
                } else {
                    inventoryAuslesen();
                }
                return true;
                /*
            case R.id.subitemPfadeAendern:
                Intent intentpfadAendernAcitivity = new Intent(getBaseContext(), ChangePathActivity.class);
                startActivity(intentpfadAendernAcitivity);
                return true;
                 */
            case R.id.subitemUebersicht:
                Intent intentUebersichtActivity = new Intent(getBaseContext(), OverviewActivity.class);
                startActivity(intentUebersichtActivity);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void inventoryAuslesen() {
        String filename2 = "invCeDaten.txt";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename2);


        Cursor c = inventoryDB.rawQuery(InventoryTbl.STMT_SELECT, null);
        if (c.getCount() == 0) {
            Snackbar snackbar = Snackbar.make(mainLayout, "Keine Daten in der Datenbank gefunden!", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            int myColor = getResources().getColor(R.color.colorAccent);
            sbView.setBackgroundColor(myColor);
            snackbar.show();

        } else {
            StringBuffer buffer = new StringBuffer();
            while(c.moveToNext()) {
                buffer.append(c.getString(0) + ";" + c.getString(1) + ";" + c.getString(2) + ";" + c.getString(3));
                buffer.append("\n");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
                    out.println(buffer.toString());
                    out.flush();
                    out.close();
                    fos.close();
                    Snackbar snackbar = Snackbar.make(mainLayout, "Daten gespeichert!", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    int myColor = getResources().getColor(R.color.colorPrimary);
                    sbView.setBackgroundColor(myColor);
                    snackbar.show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mainLayout, "Es ist ein Fehler aufgetreten! Fehlercode: " + e.toString() , Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    int myColor = getResources().getColor(R.color.colorAccent);
                    sbView.setBackgroundColor(myColor);
                    snackbar.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mainLayout, "Es ist ein Fehler aufgetreten! Fehlercode: " + e.toString() , Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    int myColor = getResources().getColor(R.color.colorAccent);
                    sbView.setBackgroundColor(myColor);
                    snackbar.show();
                }
            }
        }

    }

    private void stammdatenEinlesen() {
        myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        myFileIntent.setType("*/*");
        startActivityForResult(myFileIntent, 10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPath(); //pfad
                    stammdatenPfad = path;
                    String tempPath = (stammdatenPfad.split(":"))[1];
                    try {

                        File file = new File(tempPath);
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        String line;

                        while ((line = br.readLine()) != null) {
                            String[] stammdatenArray = line.split(";");
                            stammdatenDB.execSQL(StammdatenTbl.STMT_INSERT_STAMM, new Object[]{stammdatenArray[0], stammdatenArray[1]});
                            Snackbar snackbar = Snackbar.make(mainLayout, "Daten in der Datenbank gespeichert!" , Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            int myColor = getResources().getColor(R.color.colorPrimary);
                            sbView.setBackgroundColor(myColor);
                            snackbar.show();
                        }

                        br.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(mainLayout, "Es ist ein Fehler aufgetreten! Fehlercode: " + e.toString() , Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        int myColor = getResources().getColor(R.color.colorAccent);
                        sbView.setBackgroundColor(myColor);
                        snackbar.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(mainLayout, "Es ist ein Fehler aufgetreten! Fehlercode: " + e.toString() , Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        int myColor = getResources().getColor(R.color.colorAccent);
                        sbView.setBackgroundColor(myColor);
                        snackbar.show();

                    }
                    anzahlQuery();
                }
                break;
        }
    }


    public void vergleichEAN() {
        plainTextEan.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 7 && s.length() < 14) {
                    String tempEAN = plainTextEan.getEditText().getText().toString();
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
                            plainTextLagerort.getEditText().setText("");
                            plainTextMenge.getEditText().setText("");

                            eanGefunden = false;
                        }
                    }

                    eanCursor.close();
                } else {
                    textViewEanVergleich.setText("Ean hat nicht die richtige Länge");
                    plainTextLagerort.getEditText().setText("");
                    plainTextMenge.getEditText().setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_READ_EXTERNAL_FILE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            } else {
                stammdatenEinlesen();
            }
        }
        if(requestCode == CODE_WRITE_EXTERNAL_FILE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            } else {
                inventoryAuslesen();
            }
        }
    }
}
