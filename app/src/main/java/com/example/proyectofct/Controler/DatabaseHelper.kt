package com.example.proyectofct.Controler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "tokens.db", null, 1) {

    // Método para crear la base de datos
    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla tokens si no existe
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS tokens (
                id INTEGER PRIMARY KEY,
                access_token TEXT,
                refresh_token TEXT
            );
        """
        db.execSQL(createTableQuery)
    }

    // Si la base de datos necesita ser actualizada (cambio de versión, etc.)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tokens")
        onCreate(db)  // Vuelve a crear la tabla
    }
}

