# FormularioAlmacenamiento
## 📋 Requisitos Previos

Antes de ejecutar el proyecto, :

*   **Android Studio** (versión Jellyfish o superior recomendada).
*   **JDK 11** (configurado en el proyecto).
*   **Android SDK** (API 34 instalado).

## Configuración Inicial

### 1. Configurar el SDK de Android
Crea un archivo llamado `local.properties` en la raíz del proyecto y añade la siguiente línea (ajustando la ruta a tu usuario):

```properties
sdk.dir=/home/TU_USUARIO/Android/Sdk
```
## Cómo Ejecutar el Proyecto

### Desde Android Studio
1. Selecciona un dispositivo físico (con depuración USB activada) o un emulador.
2. Haz clic en el botón verde de **Run** (o presiona `Shift + F10`).

### Desde la Terminal (Linux/macOS)
Para instalar y ejecutar la versión de depuración directamente:

```bash
./gradlew installDebug
```

## Estructura del Proyecto
*   `/app/src/main/java`: Contiene la lógica en Kotlin (MainActivity).
*   `/app/src/main/res`: Contiene los recursos de interfaz (layouts, strings, iconos).
*   `build.gradle.kts`: Configuración de dependencias y compilación.
