package com.example.formularioalmacenamiento

data class Persona(
    var nombre: String = "",
    var apellido: String = "",
    var edad: Int = 0,
    var correo : String = "",
    var telefono: String = ""
){
    override fun toString(): String {
        return """
            Nombre: $nombre
            Apellido: $apellido
            Edad: $edad
            Correo: $correo
            Telefono: $telefono
            -------------------------
        """.trimIndent()
    }
}