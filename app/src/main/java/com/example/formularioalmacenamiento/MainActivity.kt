package com.example.formularioalmacenamiento

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEdad: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardarArchivo: Button
    private lateinit var btnGuardarSQLite: Button
    private lateinit var btnVerDatos: Button

    private lateinit var archivoStorage: ArchivoStorage
    private lateinit var sqliteStorage: SQLiteStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etEdad = findViewById(R.id.etEdad)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)

        btnGuardarArchivo = findViewById(R.id.btnGuardarArchivo)
        btnGuardarSQLite = findViewById(R.id.btnGuardarSQLite)
        btnVerDatos = findViewById(R.id.btnVerDatos)

        // Inicializar almacenamientos
        archivoStorage = ArchivoStorage(this)
        sqliteStorage = SQLiteStorage(this)

        // Configurar listeners
        btnGuardarArchivo.setOnClickListener {
            guardarEnArchivo()
        }

        btnGuardarSQLite.setOnClickListener {
            guardarEnSQLite()
        }

        btnVerDatos.setOnClickListener {
            verDatosGuardados()
        }

        etNombre.postDelayed({
            etNombre.requestFocus()
            mostrarTeclado(etNombre)
        }, 300)

        etNombre.setOnClickListener {
            etNombre.requestFocus()
            mostrarTeclado(it as EditText)
        }
        etApellido.setOnClickListener {
            etApellido.requestFocus()
            mostrarTeclado(it as EditText)
        }
        etEdad.setOnClickListener {
            etEdad.requestFocus()
            mostrarTeclado(it as EditText)
        }
        etCorreo.setOnClickListener {
            etCorreo.requestFocus()
            mostrarTeclado(it as EditText)
        }
        etTelefono.setOnClickListener {
            etTelefono.requestFocus()
            mostrarTeclado(it as EditText)
        }
    }


    private fun mostrarTeclado(view: EditText) {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            // Si falla, intentar con otro método
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    private fun ocultarTeclado() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = currentFocus
            if (view != null) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
            // Ignorar
        }
    }

    private fun validarCampos(): Boolean {
        return when {
            etNombre.text.toString().trim().isEmpty() -> {
                Toast.makeText(this, "El campo Nombre es obligatorio", Toast.LENGTH_SHORT).show()
                etNombre.requestFocus()
                mostrarTeclado(etNombre)
                false
            }
            etApellido.text.toString().trim().isEmpty() -> {
                Toast.makeText(this, "El campo Apellido es obligatorio", Toast.LENGTH_SHORT).show()
                etApellido.requestFocus()
                mostrarTeclado(etApellido)
                false
            }
            etEdad.text.toString().trim().isEmpty() -> {
                Toast.makeText(this, "El campo Edad es obligatorio", Toast.LENGTH_SHORT).show()
                etEdad.requestFocus()
                mostrarTeclado(etEdad)
                false
            }
            etEdad.text.toString().trim().toIntOrNull() == null -> {
                Toast.makeText(this, "El campo Edad debe ser un número válido", Toast.LENGTH_SHORT).show()
                etEdad.requestFocus()
                mostrarTeclado(etEdad)
                false
            }
            etCorreo.text.toString().trim().isEmpty() -> {
                Toast.makeText(this, "El campo Correo es obligatorio", Toast.LENGTH_SHORT).show()
                etCorreo.requestFocus()
                mostrarTeclado(etCorreo)
                false
            }
            etTelefono.text.toString().trim().isEmpty() -> {
                Toast.makeText(this, "El campo Teléfono es obligatorio", Toast.LENGTH_SHORT).show()
                etTelefono.requestFocus()
                mostrarTeclado(etTelefono)
                false
            }
            else -> true
        }
    }

    private fun obtenerDatosFormulario(): Persona {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val edad = etEdad.text.toString().trim().toIntOrNull() ?: 0
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        return Persona(nombre, apellido, edad, correo, telefono)
    }

    private fun guardarEnArchivo() {
        if (validarCampos()) {
            val persona = obtenerDatosFormulario()
            val guardado = archivoStorage.guardarPersona(persona)

            if (guardado) {
                Toast.makeText(this, "Datos guardados en archivo", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error al guardar en archivo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarEnSQLite() {
        if (validarCampos()) {
            val persona = obtenerDatosFormulario()
            val id = sqliteStorage.guardarPersona(persona)

            if (id != -1L) {
                Toast.makeText(this, "Datos guardados en SQLite (ID: $id)", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error al guardar en SQLite", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verDatosGuardados() {
        ocultarTeclado()
        AlertDialog.Builder(this)
            .setTitle("Ver Datos Guardados")
            .setMessage("¿Qué datos deseas visualizar?")
            .setPositiveButton("Archivo") { _, _ ->
                val datos = archivoStorage.leerTodosLosDatos()
                mostrarDatosEnDialogo("Datos del Archivo", datos)
            }
            .setNegativeButton("SQLite") { _, _ ->
                val datos = sqliteStorage.leerTodosLosDatos()
                mostrarDatosEnDialogo("Datos de SQLite", datos)
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    private fun mostrarDatosEnDialogo(titulo: String, datos: String) {
        val mensaje = if (datos.isEmpty()) "No hay datos guardados" else datos

        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun limpiarCampos() {
        etNombre.text?.clear()
        etApellido.text?.clear()
        etEdad.text?.clear()
        etCorreo.text?.clear()
        etTelefono.text?.clear()
        etNombre.requestFocus()
        mostrarTeclado(etNombre)
    }
}