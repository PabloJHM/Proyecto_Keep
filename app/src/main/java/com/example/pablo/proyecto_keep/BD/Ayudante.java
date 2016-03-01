package com.example.pablo.proyecto_keep.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Ayudante extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "notas.sqlite";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql = "create table " + Tablas.TablaNota.TABLA +
                " (" + Tablas.TablaNota._ID +
                " integer primary key autoincrement, " +
                Tablas.TablaNota.CONTENIDO + " text, " +
                Tablas.TablaNota.SUBIDA + " integer," +
                Tablas.TablaNota.USUARIO+" text );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists "
                + Tablas.TablaNota.TABLA;
        db.execSQL(sql);
        onCreate(db);
    }
}
