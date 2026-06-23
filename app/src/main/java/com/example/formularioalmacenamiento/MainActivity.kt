package com.example.formularioalmacenamiento

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEdad: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardar: AppCompatButton
    private lateinit var btnVerDatos: AppCompatButton

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

        btnGuardar = findViewById(R.id.btnGuardar)
        btnVerDatos = findViewById(R.id.btnVerDatos)

        // Inicializar almacenamiento SQLite
        sqliteStorage = SQLiteStorage(this)

        // Configurar listeners
        btnGuardar.setOnClickListener {
            guardarEnSQLite()
        }

        btnVerDatos.setOnClickListener {
            verDatosGuardados()
        }

        // Foco inicial y teclado
        etNombre.postDelayed({
            etNombre.requestFocus()
            mostrarTeclado(etNombre)
        }, 300)

        // Listeners para asegurar que el teclado se muestre al tocar los campos
        val campos = listOf(etNombre, etApellido, etEdad, etCorreo, etTelefono)
        campos.forEach { field ->
            field.setOnClickListener {
                field.requestFocus()
                mostrarTeclado(field)
            }
        }

        // Configurar validación en tiempo real
        configurarValidacionEnTiempoReal()
    }

    private fun configurarValidacionEnTiempoReal() {
        etNombre.doAfterTextChanged { s ->
            val texto = s.toString().trim()
            if (texto.isEmpty()) {
                etNombre.error = "El nombre es obligatorio"
            } else if (!texto.all { it.isLetter() || it.isWhitespace() }) {
                etNombre.error = "El nombre solo debe contener letras"
            } else {
                etNombre.error = null
            }
        }

        etApellido.doAfterTextChanged { s ->
            val texto = s.toString().trim()
            if (texto.isEmpty()) {
                etApellido.error = "El apellido es obligatorio"
            } else if (!texto.all { it.isLetter() || it.isWhitespace() }) {
                etApellido.error = "El apellido solo debe contener letras"
            } else {
                etApellido.error = null
            }
        }

        etEdad.doAfterTextChanged { s ->
            val texto = s.toString().trim()
            val edad = texto.toIntOrNull()
            if (texto.isEmpty()) {
                etEdad.error = "La edad es obligatoria"
            } else if (edad == null) {
                etEdad.error = "La edad debe ser un número válido"
            } else if (edad !in 1..120) {
                etEdad.error = "Ingresa una edad válida (1 a 120)"
            } else {
                etEdad.error = null
            }
        }

        etCorreo.doAfterTextChanged { s ->
            val texto = s.toString().trim()
            if (texto.isEmpty()) {
                etCorreo.error = "El correo es obligatorio"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(texto).matches()) {
                etCorreo.error = "El formato del correo es inválido"
            } else {
                etCorreo.error = null
            }
        }

        etTelefono.doAfterTextChanged { s ->
            val texto = s.toString().trim()
            if (texto.isEmpty()) {
                etTelefono.error = "El teléfono es obligatorio"
            } else if (!texto.all { it.isDigit() }) {
                etTelefono.error = "El teléfono solo debe contener números"
            } else if (texto.length < 8) {
                etTelefono.error = "El teléfono debe tener al menos 8 dígitos"
            } else {
                etTelefono.error = null
            }
        }
    }

    private fun mostrarTeclado(view: EditText) {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
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
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val edadStr = etEdad.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        return when {
            nombre.isEmpty() -> {
                mostrarError(etNombre, "El nombre es obligatorio")
                false
            }
            !nombre.all { it.isLetter() || it.isWhitespace() } -> {
                mostrarError(etNombre, "El nombre solo debe contener letras")
                false
            }
            apellido.isEmpty() -> {
                mostrarError(etApellido, "El apellido es obligatorio")
                false
            }
            !apellido.all { it.isLetter() || it.isWhitespace() } -> {
                mostrarError(etApellido, "El apellido solo debe contener letras")
                false
            }
            edadStr.isEmpty() -> {
                mostrarError(etEdad, "La edad es obligatoria")
                false
            }
            edadStr.toIntOrNull() == null -> {
                mostrarError(etEdad, "La edad debe ser un número válido")
                false
            }
            edadStr.toInt() !in 1..120 -> {
                mostrarError(etEdad, "Ingresa una edad válida (1 a 120)")
                false
            }
            correo.isEmpty() -> {
                mostrarError(etCorreo, "El correo es obligatorio")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                mostrarError(etCorreo, "El formato del correo es inválido")
                false
            }
            telefono.isEmpty() -> {
                mostrarError(etTelefono, "El teléfono es obligatorio")
                false
            }
            !telefono.all { it.isDigit() } -> {
                mostrarError(etTelefono, "El teléfono solo debe contener números")
                false
            }
            telefono.length < 8 -> {
                mostrarError(etTelefono, "El teléfono debe tener al menos 8 dígitos")
                false
            }
            else -> true
        }
    }

    private fun mostrarError(editText: EditText, mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        editText.error = mensaje
        editText.requestFocus()
        mostrarTeclado(editText)
    }

    private fun obtenerDatosFormulario(): Persona {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val edad = etEdad.text.toString().trim().toIntOrNull() ?: 0
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        return Persona(nombre, apellido, edad, correo, telefono)
    }

    private fun guardarEnSQLite() {
        if (validarCampos()) {
            val persona = obtenerDatosFormulario()
            val id = sqliteStorage.guardarPersona(persona)

            if (id != -1L) {
                Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                ocultarTeclado()
            } else {
                Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verDatosGuardados() {
        ocultarTeclado()
        val datos = try {
            sqliteStorage.leerTodosLosDatos()
        } catch (e: Exception) {
            "Error al leer la base de datos"
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Registros en el sistema")
            .setMessage(datos)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarCampos() {
        etNombre.text?.clear()
        etApellido.text?.clear()
        etEdad.text?.clear()
        etCorreo.text?.clear()
        etTelefono.text?.clear()
        
        etNombre.error = null
        etApellido.error = null
        etEdad.error = null
        etCorreo.error = null
        etTelefono.error = null

        etNombre.requestFocus()
    }
}