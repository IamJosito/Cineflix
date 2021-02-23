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
        db.execSQL("CREATE TABLE peliculas(codigo integer primary key, foto blob, nombre text)");
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

    public void insertFilm(String nomPeli, String nomSesion, String hora, byte[] foto){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("PRAGMA foreign_keys = OFF");

        ContentValues contenidoPelis = new ContentValues();
        contenidoPelis.put("nombre", nomPeli);
        contenidoPelis.put("foto", foto);
        db.insert("peliculas", null, contenidoPelis);

        String selectSalas = "SELECT * FROM salas WHERE nombre = '" + nomSesion + "'";
        Cursor cursorSalas = db.rawQuery(selectSalas, null);
        cursorSalas.moveToLast();


        String selectPeliculas = "SELECT * FROM peliculas";
        Cursor cursorPelis = db.rawQuery(selectPeliculas, null);
        cursorPelis.moveToLast();
        String idSalas = String.valueOf(cursorSalas.getInt(0));

        String []args ={idSalas,hora};

        ContentValues contenidoHoras = new ContentValues();
        contenidoHoras.put("cancion", cursorPelis.getInt(0));
        db.update("horarios", contenidoHoras, "sala = ? AND horario = ?",args);

        db.execSQL("PRAGMA foreign_keys = ON");
    }

    public void updateFilm(String nuevoNom, byte[] nuevaFoto, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("PRAGMA foreign_keys = OFF");

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nuevoNom);
        contentValues.put("foto", nuevaFoto);
        db.update("peliculas", contentValues, "codigo = "+id, null);

        db.execSQL("PRAGMA foreign_keys = ON");
    }

    public void deleteFilm(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = OFF");

        db.delete("peliculas", "codigo = " + id, null);
        db.delete("horarios","cancion = " + id, null);


        db.execSQL("PRAGMA foreign_keys = ON");
    }

    public void deleteSession(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = OFF");

        db.delete("salas", "codigo = " + id, null);
        db.delete("peliculas","codigo = (SELECT cancion FROM horarios WHERE sala = "+id+")", null);
        db.delete("horarios","sala = " + id, null);



        db.execSQL("PRAGMA foreign_keys = ON");
    }

    public void updateSession(int id, String nombre, ArrayList<String> horas){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = OFF");

        ContentValues contenidoSala = new ContentValues();
        contenidoSala.put("nombre",nombre);
        db.update("salas",contenidoSala,"codigo = "+id, null);
        db.delete("horarios","sala = "+id,null);

        ContentValues contenidoHorario = new ContentValues();
        Cursor filasSalas = db.rawQuery("SELECT * FROM salas WHERE codigo = "+ id, null);
        filasSalas.moveToLast();
        for(String hora:horas){
            contenidoHorario.put("horario",hora);
            contenidoHorario.put("sala",filasSalas.getInt(0));
            db.insert("horarios",null,contenidoHorario);
        }

        db.execSQL("PRAGMA foreign_keys = ON");
    }
}
