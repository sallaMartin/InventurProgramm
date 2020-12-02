package com.example.inventurprogramm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.inventurprogramm.model.Eintrag;
import com.example.inventurprogramm.model.TempEintraegeFactory;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        setTitle(R.string.menuItemUebersicht);



        //make table
        makeTable();

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void makeTable(){
        TableLayout t_layout = (TableLayout) findViewById(R.id.main_table);

        //make table header
        TableRow tr_head = new TableRow(this);
        tr_head.setId((Integer) 10);
        tr_head.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView label_bezeichnung = new TextView(this);
        label_bezeichnung.setId((Integer) 20);
        label_bezeichnung.setText("Bezeichnung");
        label_bezeichnung.setTextColor(Color.WHITE);
        label_bezeichnung.setPadding(10, 5, 10, 5);
        tr_head.addView(label_bezeichnung);

        TextView label_menge = new TextView(this);
        label_menge.setId((Integer) 21);
        label_menge.setText("Menge");
        label_menge.setTextColor(Color.WHITE);
        label_menge.setPadding(5, 5, 5, 5);
        tr_head.addView(label_menge);

        TextView label_lagerort = new TextView(this);
        label_lagerort.setId((Integer) 21);
        label_lagerort.setText("Lagerort");
        label_lagerort.setTextColor(Color.WHITE);
        label_lagerort.setPadding(5, 5, 5, 5);
        tr_head.addView(label_lagerort);

        TextView label_ean = new TextView(this);
        label_ean.setId((Integer) 21);
        label_ean.setText("EAN");
        label_ean.setTextColor(Color.WHITE);
        label_ean.setPadding(5, 5, 5, 5);
        tr_head.addView(label_ean);

        t_layout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,                    //part4
                TableLayout.LayoutParams.WRAP_CONTENT));

        ScrollView sv = new ScrollView(this);
        //später per Datenbank
        for(int i = 0; i < TempEintraegeFactory.eintraege.size(); i++){
            TableRow tr = new TableRow(this);
            tr = new TableRow(this);
            tr.setId(i+1);
            tr.setBackgroundColor(Color.GRAY);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Here we create the TextView dynamically
            tr.addView(generateTableCell(TempEintraegeFactory.eintraege.get(i).getBezeichnung()));
            tr.addView(generateTableCell(TempEintraegeFactory.eintraege.get(i).getMenge()));
            tr.addView(generateTableCell(TempEintraegeFactory.eintraege.get(i).getLagerort()));
            tr.addView(generateTableCell(TempEintraegeFactory.eintraege.get(i).getEan()));

            t_layout.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        } // end of for loop
    }

    private TextView generateTableCell(String text){
        TextView t_label = new TextView(this);
        t_label.setText(text);
        t_label.setTextSize(13);
        t_label.setTextColor(Color.BLACK);
        t_label.setBackgroundColor(Color.WHITE);
        t_label.setBackground(this.getResources().getDrawable(R.drawable.border));
        t_label.setPadding(10, 5, 10, 5);

        return t_label;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}