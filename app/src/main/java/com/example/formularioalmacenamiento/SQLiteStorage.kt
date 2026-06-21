package com.example.formularioalmacenamiento

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteStorage(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PersonasDB"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "personas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_APELLIDO = "apellido"
        private const val COLUMN_EDAD = "edad"
        private const val COLUMN_CORREO = "correo"
        private const val COLUMN_TELEFONO = "telefono"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME(
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_APELLIDO TEXT,
                $COLUMN_EDAD INTEGER,
                $COLUMN_CORREO TEXT,
                $COLUMN_TELEFONO TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun guardarPersona(persona: Persona): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, persona.nombre)
            put(COLUMN_APELLIDO, persona.apellido)
            put(COLUMN_EDAD, persona.edad)
            put(COLUMN_CORREO, persona.correo)
            put(COLUMN_TELEFONO, persona.telefono)
        }

        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun leerTodosLosDatos(): String {
        val datos = StringBuilder()
        val db = readableDatabase

        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_ID DESC")

        if (cursor.moveToFirst()) {
            // Obtener índices de las columnas de forma segura
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE)
            val apellidoIndex = cursor.getColumnIndex(COLUMN_APELLIDO)
            val edadIndex = cursor.getColumnIndex(COLUMN_EDAD)
            val correoIndex = cursor.getColumnIndex(COLUMN_CORREO)
            val telefonoIndex = cursor.getColumnIndex(COLUMN_TELEFONO)

            // Verificar que todas las columnas existan
            if (idIndex >= 0 && nombreIndex >= 0 && apellidoIndex >= 0 &&
                edadIndex >= 0 && correoIndex >= 0 && telefonoIndex >= 0) {

                do {
                    val id = cursor.getInt(idIndex)
                    val nombre = cursor.getString(nombreIndex)
                    val apellido = cursor.getString(apellidoIndex)
                    val edad = cursor.getInt(edadIndex)
                    val correo = cursor.getString(correoIndex)
                    val telefono = cursor.getString(telefonoIndex)

                    datos.append("ID: $id\n")
                    datos.append("Nombre: $nombre\n")
                    datos.append("Apellido: $apellido\n")
                    datos.append("Edad: $edad\n")
                    datos.append("Correo: $correo\n")
                    datos.append("Teléfono: $telefono\n")
                    datos.append("------------------------\n\n")

                } while (cursor.moveToNext())
            } else {
                datos.append("Error: No se encontraron todas las columnas en la base de datos")
            }
        }

        cursor.close()
        db.close()

        return if (datos.isNotEmpty()) datos.toString() else "No hay datos guardados en SQLite"
    }
}