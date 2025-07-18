package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myapplication.models.Gasto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GastoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gastos.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_GASTOS = "gastos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_CANTIDAD = "cantidad";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_CATEGORIA = "categoria";

    public GastoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_GASTOS = "CREATE TABLE " + TABLE_GASTOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                COLUMN_CANTIDAD + " REAL, " +
                COLUMN_FECHA + " INTEGER, " +
                COLUMN_CATEGORIA + " TEXT)";
        db.execSQL(CREATE_TABLE_GASTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_GASTOS + " ADD COLUMN " + COLUMN_CATEGORIA + " TEXT");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean agregarGasto(Gasto gasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPCION, gasto.getDescripcion());
        values.put(COLUMN_CANTIDAD, gasto.getCantidad());
        values.put(COLUMN_FECHA, gasto.getFecha().getTime());
        values.put(COLUMN_CATEGORIA, gasto.getCategoria());
        long result = db.insert(TABLE_GASTOS, null, values);
        db.close();
        return result != -1;
    }

    public List<Gasto> obtenerGastos() {
        List<Gasto> listaGastos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GASTOS + " ORDER BY " + COLUMN_FECHA + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION));
                double cantidad = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD));
                long fechaMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
                String categoria = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORIA));
                Date fecha = new Date(fechaMillis);
                listaGastos.add(new Gasto(id, descripcion, cantidad, fecha, categoria));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaGastos;
    }

    public boolean eliminarGasto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_GASTOS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }
}
