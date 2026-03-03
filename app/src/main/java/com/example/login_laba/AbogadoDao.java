package com.example.login_laba;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AbogadoDao {

    DBHelper dbHelper;

    public AbogadoDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insertar(Abogado abogado) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nombre", abogado.nombre);
        valores.put("especialidad", abogado.especialidad);
        valores.put("telefono", abogado.telefono);
        valores.put("correo", abogado.correo);

        db.insert("abogados", null, valores);
    }
}