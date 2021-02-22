package com.example.cineflix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class SQLite extends SQLiteOpenHelper implements Serializable {
    public SQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE canciones(codigo integer primary key, foto blob, nombre text)");
        db.execSQL("CREATE TABLE salas(codigo integer primary key, nombre text, pelicula integer)");
        db.execSQL("CREATE TABLE horarios(codigo integer primary key, horario text, sala integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
