package com.example.inventurprogramm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StammdatenHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "stammdaten.db";
    private final static int DB_VERSION = 1;

    public StammdatenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StammdatenTbl.SQL_CREATE);
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"1111111111", "Michael Jacksons Playlist", "3", "Ried im Innkreis"});
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"2222222222", "Kamera", "2", "Ried im Innkreis"});
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"7979797979", "Jakob als Puppe", "100", "Eberschwang"});
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"6969696969", "Martins Geheimnis", "1", "Wels"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(StammdatenTbl.SQL_DROP);
        db.execSQL(StammdatenTbl.SQL_CREATE);
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"1111111111", "Michael Jacksons Playlist", "3", "Ried im Innkreis"});
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"2222222222", "Kamera", "2", "Ried im Innkreis"});
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"7979797979", "Jakob als Puppe", "100", "Eberschwang"});
        db.execSQL(StammdatenTbl.STMT_INSERT, new Object[]{"6969696969", "Martins Geheimnis", "1", "Wels"});
    }
}
