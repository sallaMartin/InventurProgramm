package com.example.inventurprogramm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class InventoryHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "inventory.db";
    private final static int DB_VERSION = 1;

    public InventoryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(InventoryTbl.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(InventoryTbl.SQL_DROP);
        db.execSQL(InventoryTbl.SQL_CREATE);
    }
}
