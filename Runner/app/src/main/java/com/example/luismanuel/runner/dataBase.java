package com.example.luismanuel.runner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LuisManuel on 08/11/2015.
 */
public class dataBase extends SQLiteOpenHelper {

    private String usuarios="CREATE TABLE IF NOT EXISTS usuarios (usuario TEXT PRIMARY KEY, password TEXT)";
    private String sesiones="CREATE TABLE IF NOT EXISTS sesiones (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, tiempo TEXT )";
    private String puntos="CREATE TABLE IF NOT EXISTS puntos (id INTEGER PRIMARY KEY AUTOINCREMENT, sesion_id INTEGER, latitud TEXT, longitud TEXT, fecha DATE)";

    public dataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(usuarios);
        db.execSQL(sesiones);
        db.execSQL(puntos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS sesiones");
        db.execSQL("DROP TABLE IF EXISTS puntos");
        db.execSQL(usuarios);
        db.execSQL(sesiones);
        db.execSQL(puntos);
    }
}
