package com.example.cineflix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class SQLite extends SQLiteOpenHelper implements Serializable {
    public SQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE canciones(codigo integer primary key, foto blob, nombre text)");
        db.execSQL("CREATE TABLE salas(codigo integer primary key, nombre text, pelicula integer, FOREIGN KEY(pelicula) REFERENCES canciones(codigo))");
        db.execSQL("CREATE TABLE horarios(codigo integer primary key, horario text, sala integer, cancion integer, FOREIGN KEY(sala) REFERENCES salas(codigo), FOREIGN KEY(cancion) REFERENCES canciones(codigo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertSession(String nomSesion, ArrayList<String> horas){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("PRAGMA foreign_keys = OFF");

        ContentValues contenidoSalas = new ContentValues();
        contenidoSalas.put("nombre",nomSesion);
        db.insert("salas",null,contenidoSalas);

        ContentValues contenidoHorario = new ContentValues();
        String selectQuery = "SELECT * FROM salas";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();

        for(String hora:horas){
            contenidoHorario.put("horario",hora);
            contenidoHorario.put("sala",cursor.getInt(0));
            db.insert("horarios",null,contenidoHorario);
        }

        db.execSQL("PRAGMA foreign_keys = ON");
    }
}
