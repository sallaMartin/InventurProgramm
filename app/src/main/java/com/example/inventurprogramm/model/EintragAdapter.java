package com.example.inventurprogramm.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.inventurprogramm.R;
import com.example.inventurprogramm.database.InventoryHelper;
import com.example.inventurprogramm.database.InventoryTbl;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EintragAdapter extends BaseAdapter {
    private List<Eintrag> entries;
    private int layoutid;
    private LayoutInflater inflater;
    private Context context;

    public EintragAdapter(Context context, int layoutid, List<Eintrag> entries){
        this.entries = entries;
        this.layoutid = layoutid;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(entries.get(position).getId());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Eintrag entry = entries.get(position);
        final View listItem = (convertView == null) ? inflater.inflate(this.layoutid, null) : convertView;
        ((TextView) listItem.findViewById(R.id.txt_Bezeichnung)).setText(entry.getBezeichnung());
        ((TextView) listItem.findViewById(R.id.txt_Lagerort)).setText(entry.getLagerort());
        ((TextView) listItem.findViewById(R.id.txt_Menge)).setText(entry.getMenge());
        ((TextView) listItem.findViewById(R.id.txt_EAN)).setText(entry.getEan());

        return listItem;
    }


}
