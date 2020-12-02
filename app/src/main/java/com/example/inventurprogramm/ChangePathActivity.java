package com.example.inventurprogramm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ChangePathActivity extends AppCompatActivity {


    EditText plainTextLesepfad;
    EditText plainTextSpeicherPfad;
    EditText plainTextGeraetename;
    Button buttonSpeichern, buttonEinstellungenZuruecksetzen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_path);

        plainTextLesepfad = (EditText) findViewById(R.id.editTextLesepfadView);
        plainTextSpeicherPfad = (EditText) findViewById(R.id.editTextSpeicherPfadView);
        plainTextGeraetename = (EditText) findViewById(R.id.editTextGeraetenameView);

        buttonSpeichern = (Button) findViewById(R.id.buttonSpeichernPfad);
        buttonEinstellungenZuruecksetzen = (Button) findViewById(R.id.buttonEinstellungenZurzecksetzenView);

        buttonSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "pfade.txt";
                String input = plainTextLesepfad.getText().toString() + ";" + plainTextSpeicherPfad.getText().toString() + ";" + plainTextGeraetename.getText().toString();

                try {
                    FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE | MODE_APPEND);
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
                    out.println(input);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                plainTextLesepfad.setText("");
                plainTextSpeicherPfad.setText("");
                plainTextGeraetename.setText("");
            }

        });

        //back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}