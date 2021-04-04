package com.example.inventurprogramm.database;

public class InventoryTbl {
    public static final String TABLE_NAME = "inventory";

    public static final String Inventory_ID = "inventory_id";
    public static final String Bezeichnung = "bezeichnung";
    public static final String Menge = "menge";
    public static final String Lagerort = "lagerort";
    public static final String EAN = "ean";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME +
                    " (" +
                    Inventory_ID + " INTEGER IDENTITY(1,1) PRIMARY KEY,"+
                    Bezeichnung + " VARCHAR," +
                    Menge + " VARCHAR," +
                    Lagerort + " VARCHAR," +
                    EAN + " VARCHAR" +
                    ")";

    public static final String STMT_INSERT =
            "INSERT INTO " + TABLE_NAME +
                    " (" + Bezeichnung + ", " + Menge + ", " + Lagerort + ", " + EAN + ")" +
                    " VALUES (?,?,?,?)";

    public static final String STMT_UPDATE =
            "UPDATE " + TABLE_NAME + " SET " +
                    Bezeichnung + " = ?, " +
                    Menge + " = ?, " +
                    Lagerort + " = ?, " +
                    EAN + " = ? " +
                    "WHERE "+ Inventory_ID +" = ?";


    public static final String STMT_SELECT =
            "SELECT * FROM " + TABLE_NAME;

    public static final String PSTMT_COUNT =
            "SELECT COUNT(*) FROM " + TABLE_NAME;

    public static final String PSTMT_SELECT_ID =
            "SELECT * FROM " + TABLE_NAME + " WHERE " + Inventory_ID + " = ?";

}
