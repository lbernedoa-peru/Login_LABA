package com.example.login_laba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "abogados.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // CREAR TABLA
    @Override
    public void onCreate(SQLiteDatabase db) {

        String crearTabla = "CREATE TABLE abogados (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni TEXT UNIQUE," +
                "nombre TEXT," +
                "especialidad TEXT," +
                "telefono TEXT," +
                "correo TEXT," +
                "biografia TEXT)";

        db.execSQL(crearTabla);

        // INSERTAR DATOS DE PRUEBA
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('74839215','Carlos Ramirez','Derecho Penal','987654321','carlos.ramirez@gmail.com','Abogado con experiencia en casos penales')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('63928471','María Torres','Derecho Civil','912345678','maria.torres@gmail.com','Especialista en contratos')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('81273946','Luis Fernández','Derecho Laboral','923456781','luis.fernandez@gmail.com','Defensa de trabajadores')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('59384726','Ana Gómez','Derecho Familiar','934567812','ana.gomez@gmail.com','Casos de divorcio')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('72839461','Jorge Castillo','Derecho Penal','945678123','jorge.castillo@gmail.com','Litigación penal')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('68472915','Paola Vargas','Derecho Civil','956781234','paola.vargas@gmail.com','Consultoría civil')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('73918264','Ricardo Salas','Derecho Empresarial','967812345','ricardo.salas@gmail.com','Asesoría empresarial')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('84726193','Diana Flores','Derecho Tributario','978123456','diana.flores@gmail.com','Especialista en impuestos')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('91573826','Miguel Herrera','Derecho Penal','989234567','miguel.herrera@gmail.com','Defensa penal')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('76482915','Lucía Mendoza','Derecho Familiar','991345678','lucia.mendoza@gmail.com','Mediación familiar')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('83917462','Pedro Navarro','Derecho Civil','902456789','pedro.navarro@gmail.com','Contratos')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('67283914','Valeria Soto','Derecho Laboral','913567890','valeria.soto@gmail.com','Consultoría laboral')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('72819463','Fernando Ruiz','Derecho Penal','924678901','fernando.ruiz@gmail.com','Casos complejos')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('91827364','Andrea Silva','Derecho Civil','935789012','andrea.silva@gmail.com','Asesoría legal')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('62918374','José Paredes','Derecho Empresarial','946890123','jose.paredes@gmail.com','Consultor corporativo')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('78192364','Patricia León','Derecho Familiar','957901234','patricia.leon@gmail.com','Procesos familiares')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('84561239','Diego Rojas','Derecho Tributario','968012345','diego.rojas@gmail.com','Planificación tributaria')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('71283945','Carla Campos','Derecho Civil','979123456','carla.campos@gmail.com','Casos civiles')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('63472819','Eduardo Morales','Derecho Laboral','981234567','eduardo.morales@gmail.com','Relaciones laborales')");
        db.execSQL("INSERT INTO abogados (dni,nombre,especialidad,telefono,correo,biografia) VALUES ('91826473','Sofía Delgado','Derecho Penal','992345678','sofia.delgado@gmail.com','Defensa penal especializada')");
    }

    // ACTUALIZAR BASE DE DATOS
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS abogados");
        onCreate(db);
    }

    // INSERTAR ABOGADO
    public boolean insertarAbogado(String dni, String nombre, String especialidad,
                                   String telefono, String correo, String biografia) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("dni", dni);
        values.put("nombre", nombre);
        values.put("especialidad", especialidad);
        values.put("telefono", telefono);
        values.put("correo", correo);
        values.put("biografia", biografia);

        long resultado = db.insert("abogados", null, values);
        return resultado != -1;
    }

    // OBTENER TODOS
    public Cursor obtenerAbogados() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM abogados ORDER BY id DESC", null);
    }

    public Cursor obtenerAbogadoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM abogados WHERE id = ?",
                new String[]{String.valueOf(id)});
    }
    //para activity ver
    public Abogado obtenerAbogadoObjeto(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM abogados WHERE id = ?",
                new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Abogado abogado = new Abogado(
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("especialidad")),
                    cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    cursor.getString(cursor.getColumnIndexOrThrow("correo"))
            );
            cursor.close();
            return abogado;
        }

        if (cursor != null) cursor.close();
        return null;
    }

    // BUSCAR POR NOMBRE
    public Cursor buscarPorNombre(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM abogados WHERE nombre LIKE ?", new String[]{"%" + nombre + "%"});
    }

    // BUSCAR POR DNI
    public Cursor buscarPorDni(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM abogados WHERE dni LIKE ?", new String[]{"%" + dni + "%"});
    }

    // BUSCAR POR ID
    public Cursor buscarPorId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM abogados WHERE id = ?", new String[]{id});
    }

    // ELIMINAR
    public boolean eliminarAbogado(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultado = db.delete("abogados", "id=?", new String[]{String.valueOf(id)});
        return resultado > 0;
    }

    // ACTUALIZAR
    public boolean actualizarAbogado(int id, String dni, String nombre,
                                     String especializacion, String telefono,
                                     String correo, String biografia) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("dni", dni);
        values.put("nombre", nombre);
        values.put("especialidad", especializacion);
        values.put("telefono", telefono);
        values.put("correo", correo);
        values.put("biografia", biografia);

        int resultado = db.update("abogados", values, "id=?",
                new String[]{String.valueOf(id)});

        return resultado > 0;
    }
    // Verifica si el DNI ya existe en otro registro (excluyendo el actual)
    public boolean existeDni(String dni, int idActual) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM abogados WHERE dni = ? AND id != ?",
                new String[]{dni, String.valueOf(idActual)}
        );
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }
}