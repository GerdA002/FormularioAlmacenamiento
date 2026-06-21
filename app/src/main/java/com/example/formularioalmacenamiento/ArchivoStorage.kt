package com.example.formularioalmacenamiento

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class ArchivoStorage(private val context: Context) {
    companion object{
        private const val NOMBRE_ARCHIVO = "datos_personas.txt"
    }

    fun guardarPersona(persona: Persona): Boolean {
        return try {
            val fos = context.openFileOutput(NOMBRE_ARCHIVO, Context.MODE_APPEND)
            val osw = OutputStreamWriter(fos)

            val datos = "${persona.nombre}|${persona.apellido}|${persona.edad}|${persona.correo}|${persona.telefono}\n"

            osw.write(datos)
            osw.close()
            fos.close()
            true
        } catch (e: Exception) {
            Log.e("ArchivoStorage", "Error al guardar: ${e.message}")
            false
        }
    }

    fun leerTodosLosDatos(): String{
        val datos = StringBuilder()
        try{
            val fis = context.openFileInput(NOMBRE_ARCHIVO)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)

            var linea: String?
            while (br.readLine().also { linea = it } != null) {
                val partes = linea?.split("|") ?: emptyList()
                if (partes.size == 5) {
                    datos.append("Nombre: ${partes[0]}\n")
                    datos.append("Apellido: ${partes[1]}\n")
                    datos.append("Edad: ${partes[2]}\n")
                    datos.append("Correo: ${partes[3]}\n")
                    datos.append("Teléfono: ${partes[4]}\n")
                    datos.append("------------------------\n\n")
                }
            }
            br.close()
            isr.close()
            fis.close()
        }catch (e: Exception){
            Log.e("ArchivoStorage", "Error al leer: ${e.message}")
            return "No hay datos guardados o error al leer"
        }
        return  if (datos.isNotEmpty()) datos.toString() else "No hay datos guardados"
    }
}