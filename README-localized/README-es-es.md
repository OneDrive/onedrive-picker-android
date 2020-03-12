---
page_type: sample
products:
- office-onedrive
- ms-graph
languages:
- java
extensions:
  contentType: samples
  technologies:
  - Microsoft Graph
  services:
  - OneDrive
  platforms:
  - Android
  createdDate: 8/6/2014 3:51:12 PM
  scenarios:
  - Mobile
---
# Selector de OneDrive para Android
[ ![Descargar](https://api.bintray.com/packages/onedrive/Maven/onedrive-picker-android/images/download.svg) ](https://bintray.com/onedrive/Maven/onedrive-picker-android/_latestVersion)
[![Estado de la compilación](https://travis-ci.org/OneDrive/onedrive-picker-android.svg)](https://travis-ci.org/OneDrive/onedrive-picker-android)

El selector es la forma más rápida de integrar la aplicación de Android con OneDrive y OneDrive para la Empresa. Proporciona un conjunto de API de Java que la aplicación puede usar para examinar, seleccionar, abrir y guardar archivos en el OneDrive del usuario. Al abrir archivos desde OneDrive, también puede obtener vínculos que se pueden compartir a los archivos (ideal para enviar a amigos, compañeros de trabajo y familiares) y miniaturas de archivos de imagen y vídeo, de manera que no tenga que crearlas.

En esta guía, le mostraremos cómo hacer que la aplicación [abra](#opening-files) y [guarde archivos](#saving-files) rápidamente en OneDrive. También puede seguir esta información con nuestra [aplicación de ejemplo del selector de apertura](PickerSample) y la [aplicación de ejemplo del protector](SaverSample).

![Vista previa del selector de OneDrive en funcionamiento](images/android-picker-saver.png)

[Instalación](#setup)

[Abrir archivos](#opening-files)

[Guardar archivos](#saving-files)

[Versiones de Android admitidas](#supported-android-versions)


## Instalación

## Compilar con el código fuente

El selector para Android está disponible como código fuente en GitHub e incluye funciones de abrir y guardar. Puede descargar la última versión directamente o clonar el repositorio:

* [Descargar ZIP](https://github.com/OneDrive/onedrive-picker-android/archive/master.zip)
* `git clone https://github.com/OneDrive/onedrive-picker-android.git`

Para obtener información sobre cómo configurar el entorno para trabajar con las aplicaciones de ejemplo o con el SDK del selector, vea [Configurar el entorno de Android](http://developer.android.com/sdk/index.html).

## Instalar AAR mediante Gradle

```gradle
repositories {
    jcenter()
}

dependencies {
    compile ('com.microsoft.onedrivesdk:onedrive-picker-android:v2.0')
}
```

### Obtener un id. de la aplicación

Registre su aplicación en (https://account.live.com/developers/applications) para obtener un id. de la aplicación (ID de cliente) para lanzar el selector.

### Crear la biblioteca

#### Android Studio
1. Elija **Importar proyecto...** o **Importar módulo...** para importar a un proyecto existente.
2. Vaya a la ubicación donde está guardado el SDK y seleccione la raíz*onedrive-picker-android*.
3. Si todavía no está instalado, siga las indicaciones o vaya a *Android SDK Manager* para instalar Android 4.4.2 (API 19) y las herramientas de compilación asociadas.

#### Eclipse
1. En Eclipse, vaya a **Archivo** → **Importar** → **General** → **Proyectos existentes en el área de trabajo**.
2. Haga clic en **Examinar...** para seleccionar *onedrive-picker-android*, donde ha guardado el SDK, como su directorio raíz. Asegúrese de que al menos *OneDriveSDK* está activado.
3. Si todavía no está instalado, desde *Android SDK Manager*, instale Android 4.4.2 (API 19).
4. Haga clic con el botón derecho en su proyecto, seleccione **Propiedades** y vaya a **Android** en la barra lateral izquierda.
5. Haga clic en **Agregar...** en *Biblioteca* y seleccione *OneDriveSDK* para vincularlo a su proyecto.

## Abrir archivos

Su aplicación necesita proporcionar al usuario una manera para empezar a abrir archivos desde OneDrive. En este ejemplo se configura un controlador de clic que inicia el selector de apertura desde `onClick()`. En este caso, la aplicación está solicitando un tipo de vínculo de uso compartido de solo vista con `LinkType.WebViewLink`.

**Asegúrese de reemplazar APP\_ID por el identificador de su aplicación**

```java
import android.view.View.OnClickListener;
import com.microsoft.onedrivesdk.picker.*;

// Within the activity's class definition

private IPicker mPicker;
private String ONEDRIVE_APP_ID = "APP_ID"; // Get app id here: https://account.live.com/developers/applications

// The onClickListener that will start the OneDrive picker
private final OnClickListener mStartPickingListener = new OnClickListener() {
    @Override
    public void onClick(final View v) {
        mPicker = Picker.createPicker(ONEDRIVE_APP_ID);
        mPicker.startPicking((Activity)v.getContext(), LinkType.WebViewLink);
    }
};
```

Cuando se invoque al método `onClick()`, el selector se crea y se configura para el tipo de vínculo que ha solicitado el usuario. Después, el método `startPicking()` se invoca para iniciar la experiencia de selección. Si el usuario no tiene la aplicación de OneDrive instalada cuando se invoque a `startPicking()`, se le pedirá que descargue la aplicación y se le dirigirá a la tienda de aplicaciones.

### Tipos de vínculos
El selector de apertura puede configurarse para devolver una dirección URL para el archivo seleccionado en uno de estos
formatos:
* `LinkType. DownloadLink`: se devolverá una dirección URL que proporciona acceso directo al contenido del archivo durante una hora. Puede usar esta dirección URL para descargar el archivo en la aplicación.
* `LinkType. WebViewLink`: se crea un vínculo para compartir que proporciona una vista previa web del archivo. El vínculo es válido hasta que el usuario elimine el vínculo de uso compartido a través de OneDrive. Los vínculos de uso compartido no están disponibles para los archivos de OneDrive para la Empresa.

### Capturar los resultados
Cuando el usuario haya completado la apertura de un archivo o haya cancelado desde el selector de apertura, se llamará al método `onActivityResult()` para controlar los resultados del selector. En este método, puede capturar los resultados y obtener acceso al archivo seleccionado por el usuario.

```java
protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    // Get the results from the picker
    IPickerResult result = mPicker.getPickerResult(requestCode, resultCode, data);
    // Handle the case if nothing was picked
    if (result != null) {
        // Do something with the picked file
        Log.d("main", "Link to file '" + result.getName() + ": " + result.getLink());
        return;
    }

    // Handle non-OneDrive picker request
    super.onActivityResult(requestCode, resultCode, data);
}
```

Si el usuario cancela la selección de un archivo, el objeto de resultado será nulo.

### Objeto de resultado del selector
Además de al nombre y al vínculo del archivo, puede tener acceso a otras propiedades del objeto `IPickerResult` que proporcionen más detalles sobre el archivo seleccionado:

```java
public static class IPickerResult {
	// Name of the file with extension
	public String getName();

	// Type of link generated
	public LinkType getLinkType();

	// URI for the file, which varies based on the value of getLinkType()
	public Uri getLink();

	// Size of the file, in bytes
	public int getSize();

	// Set of thumbnail links for various sizes: "small", "medium", and "large"
	public Map<String, Uri> getThumnailLinks();
}
```

Ejecute la [aplicación de ejemplo del selector de apertura](PickerSample) para ver todo esto en funcionamiento.

## Guardar archivos

De manera similar a la apertura de archivo, la aplicación debe proporcionar una manera para que el usuario guarde un archivo en OneDrive. En este ejemplo de código, se usa un controlador de clic para iniciar el protector desde `onClick()`. Su aplicación necesita tener un nombre de archivo y un URI para el archivo en el dispositivo para pasarlo al protector. En este código de ejemplo se crea un archivo de marcador de posición en la carpeta local de las aplicaciones denominado file.txt como ejemplo.

**Asegúrese de reemplazar APP\_ID por el identificador de su aplicación**

```java
import android.view.View.OnClickListener;
import com.microsoft.onedrivesdk.saver.*

// Within the activity's class definition

private ISaver mSaver;
private String ONEDRIVE_APP_ID = "APP_ID"; // Get app id here: https://account.live.com/developers/applications

// The onClickListener that will start the OneDrive picker
private final OnClickListener mStartPickingListener = new OnClickListener() {
    @Override
    public void onClick(final View v) {
        // create example file to save to OneDrive
        final String filename = "file.txt";
        final File f = new File(context.getFilesDir(), filename);

        // create and launch the saver
        mSaver = Saver.createSaver(ONEDRIVE_APP_ID);
        mSaver.startSaving((Activity)v.getContext(), filename, Uri.fromFile(f));
    }
};

```
Cuando se invoca al método `onClick()`, se crea el protector y, después, se invoca al método `startSaving()`. Esto inicia la experiencia del protector de OneDrive, lo que permite a sus usuarios elegir una carpeta para cargar el archivo. Si el usuario no tiene la aplicación de OneDrive instalada cuando se invoque a `startSaving()`, se le pedirá que descargue la aplicación desde Marketplace.

Actualmente, el protector admite el esquema URI de archivos `content://` y `file://`. Si se usa un esquema URI diferente, el protector devolverá un error `NoFileSpecified`. Vea lo que se muestra a continuación para obtener detalles sobre la respuesta del protector.

### Resultado del protector

Cuando el usuario haya terminado de guardar un archivo, o si se ha producido algún problema al guardar, se llamará al método `onActivityResult()` para controlar el resultado del protector. Con este método, puede comprobar si el archivo se ha guardado y, si no lo ha hecho, puede filtrar la excepción y controlar el error.

```java
protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    // check that the file was successfully saved to OneDrive
    try {
        mSaver.handleSave(requestCode, resultCode, data);
    } catch (final SaverException e) {
        // Log error information
        Log.e("OneDriveSaver". e.getErrorType().toString()); // Provides one of the SaverError enum
        Log.d("OneDriveSaver", e.getDebugErrorInfo()); // Detailed debug error message
    }
}

```
El mensaje de error que proporciona `getDebugErrorInfo()` es principalmente por motivos de desarrollo y depuración, y puede cambiar en cualquier momento. Al controlar los errores, puede usar `getErrorType()` para determinar la causa general del error.


### Tipos de error del protector

Cuando el protector no puede terminar de guardar un archivo y genera una excepción, proporciona `SaverError` mediante `getErrorType()`, que indica uno de un conjunto de posibles tipos de error.

```java
public enum SaverError {

    // The error type was not known
    Unknown,

    // The saver was cancelled by the user
    Cancelled,

    // The OneDrive account did not have enough quota available to save the file
    OutOfQuota,

    // The file could not be saved onto OneDrive because it contained
    // unsupported characters
    InvalidFileName,

    // No network connectivity was available when attempting to save the file
    NoNetworkConnectivity,

    // The Uri to the file could not be accessed
    CouldNotAccessFile,

    // No file was specified to be saved, or the file URI scheme was not supported,
    // content:// and file:// are currently supported
    NoFileSpecified

}
```
Ejecute la [aplicación de ejemplo del protector](SaverSample) para ver todo esto en funcionamiento.

## Versiones de Android admitidas
La biblioteca del selector de OneDrive se admite en tiempo de ejecución para la [revisión de la API de Android 14](http://source.android.com/source/build-numbers.html) y versiones posteriores. Para crear la biblioteca del selector, necesita instalar la revisión de la API de Android 19 o posterior.

El selector necesita que la aplicación de OneDrive esté instalada para funcionar. Si la aplicación de OneDrive no está instalada, al usuario se le pedirá que descargue la aplicación cuando se invoque al método `startPicking()` o `startSaving()`.


## Colaboradores

Este proyecto ha adoptado el [Código de conducta de código abierto de Microsoft](https://opensource.microsoft.com/codeofconduct/). Para obtener más información, vea [Preguntas frecuentes sobre el código de conducta](https://opensource.microsoft.com/codeofconduct/faq/) o póngase en contacto con [opencode@microsoft.com](mailto:opencode@microsoft.com) si tiene otras preguntas o comentarios.
