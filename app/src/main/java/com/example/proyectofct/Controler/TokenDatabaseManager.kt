package com.example.proyectofct.Controler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class TokenDatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    // Metodo para insertar los tokens
    fun guardarTokens(accessToken: String, refreshToken: String) {
        // Asegúrate de que la base de datos esté disponible
        val db = dbHelper.writableDatabase

        // Verifica si la base de datos está abierta correctamente
        if (db.isOpen) {
            val contentValues = ContentValues().apply {
                put("id", 1) // Usamos id 1 para el único registro
                put("access_token", accessToken)
                put("refresh_token", refreshToken)
            }

            // Inserta o reemplaza el token en la tabla
            db.replace("tokens", null, contentValues)
            db.close()
        } else {
            Log.e("TokenDatabaseManager", "Database not open!")
        }
    }



    fun getAccessToken(): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tokens", arrayOf("access_token"),
            "id = ?", arrayOf("1"), null, null, null
        )

        var accessToken: String? = null
        if (cursor.moveToFirst()) {
            accessToken = cursor.getString(cursor.getColumnIndexOrThrow("access_token"))
        }

        cursor.close()
        db.close()
        return accessToken
    }

    fun getRefreshToken(): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tokens", arrayOf("refresh_token"),
            "id = ?", arrayOf("1"), null, null, null
        )

        var refreshToken: String? = null
        if (cursor.moveToFirst()) {
            refreshToken = cursor.getString(cursor.getColumnIndexOrThrow("refresh_token"))
        }

        cursor.close()
        db.close()
        return refreshToken
    }
}
