package com.example.pablo.proyecto_keep.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pablo.proyecto_keep.Entidades.Nota;

import java.util.ArrayList;
import java.util.List;


public class GestorNota {
    private Ayudante abd;
    private SQLiteDatabase bd;
    public GestorNota(Context c){
        abd= new Ayudante(c);
        bd=abd.getWritableDatabase();
    }
    public void open() {
        bd = abd.getWritableDatabase();
    }
    public void openRead() {
        bd = abd.getReadableDatabase();
    }
    public void close() {
        abd.close();
    }

    public long insert(Nota n) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaNota.CONTENIDO,
                n.getContenido());
        valores.put(Tablas.TablaNota.USUARIO,
                n.getUsuario());
        valores.put(Tablas.TablaNota.SUBIDA,
                n.isSubida());
        long id= bd.insert(Tablas.TablaNota.TABLA,
                null, valores);
        return id;
    }

    public int delete(Nota n) {
        return deleteId(n.getId());
    }

    public int deleteId(long id) {
        String condicion = Tablas.TablaNota._ID + " = ?";
        String[] argumentos = { id + "" };
        int cuenta = bd.delete(
                Tablas.TablaNota.TABLA, condicion, argumentos);
        return cuenta;
    }


    public List<Nota> select() {
        return select(null);
    }

    public List<Nota> select(String condicion) {
        List<Nota> la = new ArrayList<>();
        Cursor cursor = bd.query(Tablas.TablaNota.TABLA, null,
                condicion, null, null, null, null);
        cursor.moveToFirst();
        Nota n;
        while (!cursor.isAfterLast()) {
            n = getRow(cursor);
            la.add(n);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }

    public Nota getRow(Cursor c) {

        Nota n = new Nota();
        if(c != null) {
            n.setId(c.getInt(0));
            n.setContenido(c.getString(1));
            if (c.getInt(2)==1){
                n.setSubida(true);
            }else{
                n.setSubida(false);
            }
        }else{

        }
        return n;
    }

    public void nuevoEstado(Nota n){
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaNota.CONTENIDO, n.getContenido());
        valores.put(Tablas.TablaNota.SUBIDA, 1);
        String condicion = Tablas.TablaNota._ID + " = ?";
        String[] argumentos = { n.getId() + "" };
        bd.update(Tablas.TablaNota.TABLA, valores,
                condicion, argumentos);
    }

    public void nuevoContenido(Nota n){
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaNota.CONTENIDO, n.getContenido());
        valores.put(Tablas.TablaNota.SUBIDA, 0);
        String condicion = Tablas.TablaNota._ID + " = ?";
        String[] argumentos = { n.getId() + "" };
        bd.update(Tablas.TablaNota.TABLA, valores,
                condicion, argumentos);
    }
}
