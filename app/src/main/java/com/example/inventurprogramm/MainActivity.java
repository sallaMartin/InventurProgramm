package com.example.inventurprogramm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText plainTextEan = (EditText) findViewById(R.id.plainTextEanView);
        Button buttonSpeichern = (Button) findViewById(R.id.buttonSpeichern);

        buttonSpeichern.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                plainTextEan.setText("Hello");
            }
        });


    }
}