package com.example.inventurprogramm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventurprogramm.database.InventoryHelper;
import com.example.inventurprogramm.database.InventoryTbl;
import com.example.inventurprogramm.model.Eintrag;
import com.example.inventurprogramm.model.EintragAdapter;

import java.util.ArrayList;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {
    private SQLiteDatabase inventoryDB;
    private int current_page = 0;
    private int page_amount = 0;
    private int entry_amount = 10;

    private ListView entryListView;
    private List<Eintrag> entries = new ArrayList<>();
    private EintragAdapter entryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        setTitle(R.string.menuItemUebersicht);

        InventoryHelper inventoryHelper = new InventoryHelper(this);
        inventoryDB = inventoryHelper.getReadableDatabase();

        //Liste initalisieren;
        entryListView = findViewById(R.id.list_entry);
        entryAdapter = new EintragAdapter(this, R.layout.overview_list_layout, entries);
        entryListView.setAdapter(entryAdapter);

        //Pagination
        Button prev = findViewById(R.id.buttonPrev);
        Button next = findViewById(R.id.buttonNext);

        makePage();
        makeSuchfilter();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_page > 0){
                    current_page--;
                    updatePage();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_page < page_amount-1){
                    current_page++;
                    updatePage();
                }
            }
        });

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void makeSuchfilter() {
        EditText editSearch = findViewById(R.id.edtSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void updatePage(){
        EditText editSearch = findViewById(R.id.edtSearch);
        Cursor pageCursor;
        if(editSearch.getText().toString().equals("")){
            pageCursor = inventoryDB.rawQuery("SELECT * FROM " + InventoryTbl.TABLE_NAME + " limit " + current_page *entry_amount + ", " + entry_amount, null);
        }else{
            String text = editSearch.getText().toString();
            text = "%" + text + "%";
            pageCursor = inventoryDB.rawQuery("SELECT * FROM " + InventoryTbl.TABLE_NAME +
                            " WHERE (" + InventoryTbl.Bezeichnung + " LIKE ? ) OR (" + InventoryTbl.Lagerort + " LIKE ? ) OR (" + InventoryTbl.Menge + " LIKE ? ) OR (" + InventoryTbl.EAN + " LIKE ? ) " +
                            " limit " + current_page *entry_amount + ", " + entry_amount
                    , new String[]{text, text, text, text});
        }

        entries.clear();
        while(pageCursor.moveToNext()){
            entries.add(new Eintrag(
                    pageCursor.getString(0),
                    pageCursor.getString(1),
                    pageCursor.getString(2),
                    pageCursor.getString(3),
                    pageCursor.getString(4)
            ));
        }
        pageCursor.close();
        entryAdapter.notifyDataSetChanged();
    }

    private void makePage() {
        Cursor amountCursor = inventoryDB.rawQuery(InventoryTbl.STMT_COUNT, null);
        amountCursor.moveToNext();
        int amount = amountCursor.getInt(0);
        amountCursor.close();

        page_amount = (int) Math.ceil((double) amount/entry_amount);
        updatePage();
    }

    /*
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
        label_bezeichnung.setTextSize(15);
        label_bezeichnung.setText("Bezeichnung");
        label_bezeichnung.setTextColor(Color.WHITE);
        label_bezeichnung.setPadding(10, 5, 10, 5);
        tr_head.addView(label_bezeichnung);

        TextView label_menge = new TextView(this);
        label_menge.setId((Integer) 21);
        label_menge.setTextSize(15);
        label_menge.setText("Menge");
        label_menge.setTextColor(Color.WHITE);
        label_menge.setPadding(5, 5, 5, 5);
        tr_head.addView(label_menge);

        TextView label_lagerort = new TextView(this);
        label_lagerort.setId((Integer) 21);
        label_lagerort.setTextSize(15);
        label_lagerort.setText("Lagerort");
        label_lagerort.setTextColor(Color.WHITE);
        label_lagerort.setPadding(5, 5, 5, 5);
        tr_head.addView(label_lagerort);

        TextView label_ean = new TextView(this);
        label_ean.setId((Integer) 21);
        label_ean.setTextSize(15);
        label_ean.setText("EAN");
        label_ean.setTextColor(Color.WHITE);
        label_ean.setPadding(5, 5, 5, 5);
        tr_head.addView(label_ean);

        t_layout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,                    //part4
                TableLayout.LayoutParams.WRAP_CONTENT));

        ScrollView sv = new ScrollView(this);

        Cursor eintragCursor = inventoryDB.rawQuery(InventoryTbl.STMT_SELECT, null);
        int tr_id = 0;
        while(eintragCursor.moveToNext()){
            TableRow tr = new TableRow(this);
            tr.setId(tr_id);
            tr.setBackgroundColor(Color.GRAY);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Here we create the TextView dynamically
            Eintrag e = new Eintrag(
                    eintragCursor.getString(0),
                    eintragCursor.getString(1),
                    eintragCursor.getString(2),
                    eintragCursor.getString(3),
                    "" + eintragCursor.getInt(4));

            tr.addView(generateTableCell(e.getBezeichnung()));
            tr.addView(generateTableCell(e.getMenge()));
            tr.addView(generateTableCell(e.getLagerort()));
            tr.addView(generateTableCell(e.getEan()));

            t_layout.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            tr_id++;
        }

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
        t_label.setTextSize(15);
        t_label.setTextColor(Color.BLACK);
        t_label.setBackgroundColor(Color.WHITE);
        t_label.setBackground(this.getResources().getDrawable(R.drawable.border));
        t_label.setPadding(10, 5, 10, 5);

        return t_label;
    }
    */

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