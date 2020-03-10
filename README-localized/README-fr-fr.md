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
# Sélecteur OneDrive pour Android
[ ![Télécharger](https://api.bintray.com/packages/onedrive/Maven/onedrive-picker-android/images/download.svg) ](https://bintray.com/onedrive/Maven/onedrive-picker-android/_latestVersion)
[ ![État de création](https://travis-ci.org/OneDrive/onedrive-picker-android.svg)](https://travis-ci.org/OneDrive/onedrive-picker-android)

Le sélecteur est le moyen le plus rapide pour intégrer votre application Android à OneDrive et OneDrive Entreprise. Il fournit un ensemble d’API Java que votre application peut utiliser pour parcourir, sélectionner, ouvrir et enregistrer des fichiers dans le OneDrive de votre utilisateur. Lorsque vous ouvrez des fichiers dans OneDrive, vous pouvez aussi obtenir des liens partageables vers des fichiers, ce qui est idéal pour l’envoi aux amis, collègues et parents, et des miniatures de fichiers image et vidéo, ce qui vous évite de les créer vous-même.

Dans ce guide, nous allons vous présenter comment utiliser votre application pour [Ouvrir](#opening-files) et [enregistrer des fichiers](#saving-files) rapidement sur OneDrive. Vous pouvez aussi suivre avec notre [exemple d’application du sélecteur d’ouverture](PickerSample) et l'[application d’exemple de l’économiseur](SaverSample).

![Aperçu du sélecteur OneDrive en action](images/android-picker-saver.png)

[Installation](#setup)

[Ouverture des fichiers](#opening-files)

[Enregistrement des fichiers](#saving-files)

[Versions Android prises en charge](#supported-android-versions)


## Installation

## Compiler avec le code source

Le sélecteur pour Android est disponible comme source ouverte sur GitHub et inclut les fonctionnalités d’ouverture et d’enregistrement. Vous pouvez télécharger la dernière version directement ou dupliquer le référentiel :

* [Télécharger le fichier ZIP](https://github.com/OneDrive/onedrive-picker-android/archive/master.zip)
* `Obtenez un clone sur https://github.com/OneDrive/onedrive-picker-android.git`

Pour plus d’informations sur la configuration de votre environnement pour qu’il fonctionne avec les applications d'exemple ou le Kit de développement logiciel (SDK) du sélecteur, voir [Configuration de votre environnement Android](http://developer.android.com/sdk/index.html).

## Installer AAR via Gradle

```gradle
repositories {
    jcenter()
}

dependencies {
    compile ('com.microsoft.onedrivesdk:onedrive-picker-android:v2.0')
}
```

### Obtenir un ID d’application

Enregistrez votre application \[ici] (https://account.live.com/developers/applications) pour obtenir un ID d’application (ID client) afin de lancer le sélecteur.

### Création de la bibliothèque

#### Android Studio
1. Choisissez **Importer un projet...** ou **Importer un module...**, pour l’importer dans un projet existant.
2. Naviguez jusqu'à l’emplacement où le Kit de développement logiciel (SDK) est enregistré, puis sélectionnez la racine *onedrive-picker-android*
3. S’il n’est pas encore installé, suivez les invites ou accédez au *Gestionnaire du Kit de développement logiciel (SDK) Android* pour installer Android 4.4.2 (API 19) et les outils de création associés.

#### Eclipse
1. Dans Eclipse, choisissez **Fichier** → **Importer** → **Général** → **Projects existants dans l'espace de travail**.
2. Cliquez sur **Parcourir...** pour sélectionner *onedrive-picker-android*, où vous avez enregistré le Kit de développement logiciel (SDK), comme votre répertoire racine. Assurez-vous qu’au moins *OneDriveSDK* est activé.
3. S’il n’est pas installé, à partir du *Gestionnaire de Kit de développement logiciel (SDK) Android*, installez Android 4.4.2 (API 19).
4. Cliquez avec le bouton droit sur votre projet, puis sélectionnez **Propriétés** et accédez à **Android** dans la barre située à gauche.
5. Cliquez sur **Ajouter...** dans la *Bibliothèque* et sélectionnez *OneDriveSDK* pour le lier à votre projet.

## Ouverture des fichiers

Votre application doit permettre à l’utilisateur de commencer à ouvrir des fichiers à partir de OneDrive. Cet exemple configure un gestionnaire de clics qui lance le sélecteur d’ouverture à partir de `SurClic`. Dans ce cas, l’application demande un type de lien de partage en lecture seule en utilisant `LinkType.WebViewLink`.

**Veillez à remplacer APP\_ID par l’identificateur de votre application**

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

Lorsque la méthode `SurClic` est appelée, le sélecteur est créé et configuré pour le type de lien demandé par l’utilisateur. La méthode `startPicking()` est ensuite appelée pour lancer l’expérience de sélection. Si l’application OneDrive n’est pas installée lorsque la méthode `startPicking()` est appelée, l’utilisateur est invité à télécharger l’application et redirigé vers le magasin d'applications.

### Types de liens
Le sélecteur d’ouverture peut être configuré pour renvoyer l’URL du fichier sélectionné dans l’un des formats
suivants:
* `LinkType.DownloadLink` – une URL est renvoyée qui fournit un accès direct d'une heure au contenu du fichier. Vous pouvez utiliser cette URL pour télécharger le fichier dans votre application.
* `LinkType.WebViewLink` – un lien de partage qui fournit un aperçu web du fichier est créé. Le lien est valide jusqu'à ce que l’utilisateur supprime le lien partagé via OneDrive. Les liens de partage ne sont pas disponibles pour les fichiers OneDrive Entreprise.

### Interception des résultats
Lorsque l’utilisateur a terminé l'ouverture d'un fichier ou a annulé l’action dans le sélecteur d’ouverture, la méthode `onActivityResult()` est appelée pour gérer les résultats du sélecteur. Dans cette méthode, vous pouvez intercepter les résultats et accéder au fichier sélectionné par l’utilisateur.

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

Si l’utilisateur annule la sélection d’un fichier, l’objet de résultat est null.

### Objet Picker Result
Outre le nom de fichier et le lien pour le fichier, vous pouvez accéder à d'autres propriétés sur l’objet `IPickerResult` qui fournissent des détails supplémentaires sur le fichier sélectionné :

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

Exécutez l’[exemple d’application du sélecteur d’ouverture](PickerSample) pour voir tout ceci en action.

## Enregistrement des fichiers

Tout comme pour l’ouverture des fichiers, votre application doit permettre à l’utilisateur d’enregistrer un fichier dans OneDrive. Dans cet exemple de code, un gestionnaire de clics est utilisé pour lancer l’économiseur à partir de `SurClic()`. Votre application doit avoir un nom de fichier et un URI vers le fichier sur l'appareil pour passer à l'économiseur. Cet exemple de code crée un fichier d’espace réservé dans le dossier local de l’application nommé fichier.txt par exemple.

**Veillez à remplacer APP\_ID par l’identificateur de votre application**

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
Lorsque la méthode `SurClic()` est appelée, l’économiseur est créé puis la méthode `startSaving()` est appelée. Cette action démarre l’expérience d’économiseur OneDrive, qui permet aux utilisateurs de sélectionner un dossier pour télécharger le fichier. Si l’application OneDrive n’est pas installée lorsque la méthode `startSaving()` est appelée, l’utilisateur est invité à télécharger l’application du marketplace.

L’économiseur prend actuellement en charge le modèle d’URI de fichier `contenu://` et `fichier://`. Si un autre modèle d’URI est utilisé, l’économiseur renvoie une erreur `NoFileSpecified`. Pour plus d’informations sur la réponse de l’économiseur, voir ci-dessous.

### Résultats de l’économiseur

Lorsque l’utilisateur a terminé l'enregistrement d'un fichier, ou s’il y a eu un problème d’enregistrement, la méthode `onActivityResult()` est appelée pour gérer le résultat de l’économiseur. À l’aide de cette méthode, vous pouvez vérifier si le fichier a été enregistré et, si ce n’est pas le cas, vous pouvez intercepter l’exception et gérer l’erreur.

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
Le message d’erreur fourni par `getDebutErrorInfo()` est principalement pour le développement et le débogage et peut changer à tout moment. Lors de la gestion d'erreurs, vous pouvez utiliser `getErrorType()` pour déterminer la cause générale de l’erreur.


### Types d’erreur de l’économiseur

Lorsque l’économiseur ne peut pas terminer l’enregistrement d’un fichier et qu’il renvoie une exception, il fournit `SaverError` via `getErrorType()` qui indique un ensemble de types d’erreurs possibles.

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
Exécutez l’[exemple d’application de l’économiseur](SaverSample) pour voir ceci en action.

## Versions Android prises en charge
La bibliothèque du sélecteur OneDrive est prise en charge lors de l’exécution de l’[API Android version 14](http://source.android.com/source/build-numbers.html) et versions ultérieures. Pour créer la bibliothèque du sélecteur, vous devez installer l’API Android API version 19 ou ultérieure.

Pour que le sélecteur fonctionne, l’application OneDrive doit être installée. Si l’application OneDrive n’est pas installée, l’utilisateur est invité à télécharger l’application lorsque la méthode `startPicking` ou `startSaving` est appelée.


## Contribution

Ce projet a adopté le [Code de conduite Open Source de Microsoft](https://opensource.microsoft.com/codeofconduct/). Pour en savoir plus, reportez-vous à la [FAQ relative au code de conduite](https://opensource.microsoft.com/codeofconduct/faq/) ou contactez [opencode@microsoft.com](mailto:opencode@microsoft.com) pour toute question ou tout commentaire.
