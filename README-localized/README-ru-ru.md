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
# Средство выбора файлов в OneDrive для Android
[ ![Скачать](https://api.bintray.com/packages/onedrive/Maven/onedrive-picker-android/images/download.svg) ](https://bintray.com/onedrive/Maven/onedrive-picker-android/_latestVersion) 
[![Состояние сборки](https://travis-ci.org/OneDrive/onedrive-picker-android.svg)](https://travis-ci.org/OneDrive/onedrive-picker-android)

Средство выбора — самый быстрый способ интегрировать ваше приложение Android с OneDrive и OneDrive для бизнеса. Оно предоставляет набор API Java, которые ваше приложение может использовать для поиска, выбора, открытия и сохранения файлов в OneDrive пользователя. При открытии файлов в OneDrive вы также можете получить ссылки для общего доступа к файлам (это отличное средство для отправки файлов друзьям, коллегам и родственникам), а также эскизы изображений и видеофайлов, поэтому вам не придется создавать их самостоятельно.

В этом руководстве показано, как быстро приступить к работе с приложением [открытии](#opening-files) и [сохранения файлов](#saving-files) в OneDrive. Кроме того, вы можете использовать для этого [открыть образец приложения](PickerSample) и учебное приложение ["Заставка".](SaverSample).

![Средство выбора файлов OneDrive в действии](images/android-picker-saver.png)

[Установка](#setup)

[Открытие файлов](#opening-files)

[Сохранение файлов](#saving-files)

[Поддерживаемые версии Android](#supported-android-versions)


## Установка

## Компиляция исходного кода

Средство выбора для Android доступно в виде проекта с открытым исходным кодом на GitHub и включает функции открытия и сохранения файлов. Вы можете либо скачать последнюю версию, либо клонировать репозиторий.

* [Скачать ZIP-файл](https://github.com/OneDrive/onedrive-picker-android/archive/master.zip)
* `git clone https://github.com/OneDrive/onedrive-picker-android.git`

Сведения о настройке среды для работы с примерами приложений или пакетом SDK средства выбора см. в статье, посвященной [настройке среды Android](http://developer.android.com/sdk/index.html).

## Установка AAR с помощью Gradle

```gradle
repositories {
    jcenter()
}

dependencies {
    compile ('com.microsoft.onedrivesdk:onedrive-picker-android:v2.0')
}
```

### Получение идентификатора приложения

Зарегистрируйте свое приложение (https://account.live.com/developers/applications), чтобы получить идентификатор приложения (идентификатор клиента), чтобы запустить средство выбора.

### Создание библиотеки

#### Android Studio
1. Нажмите кнопку **Импорт проекта...** или **импорт модуля...**, чтобы импортировать в существующий проект.
2. Перейдите в папку, в которой хранится пакет SDK, и выберите корневой *onedrive — выбор — Android*
3. Если она еще не установлена, выполните действия, указанные в разделе "пошаговые инструкции", или откройте *Manager SDK для Android*, чтобы установить Android 4.4.2 (API 19) и соответствующие средства построения.

#### Eclipse
1. В Eclipse выберите **файл** → **импорт** → **общие** → **существующие проекты в рабочую область**.
2. Нажмите кнопку **обзор...**, чтобы выбрать *onedrive — выбор*, где вы сохранили пакет SDK в корневом каталоге. Убедитесь, что, как минимум, установлен флажок *OneDriveSDK*.
3. Если пакет еще не установлен, в *Android SDK Manager* установите Android 4.4.2 (API 19).
4. Щелкните проект правой кнопкой мыши, выберите **Properties** (Свойства) и перейдите в раздел **Android** на расположенной слева панели.
5. Нажмите кнопку **добавить...** в *библиотеке* и выберите *Онедривесдк*, чтобы связать его с проектом.

## Открытие файлов

Ваше приложение должно предоставлять пользователю возможность открывать файлы из OneDrive. В этом примере устанавливается обработчик нажатия кнопки `"Открыть" в `. В этом случае приложение запрашивает тип ссылки для общего доступа только для просмотра с помощью `LinkType.WebViewLink`.

**Не забудьте заменить выражение APP\_ID идентификатором вашего приложения.**

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

При вызове метода `нажатия кнопки ()` создается и настраивается тип ссылки, запрошенной пользователем. После этого для запуска процесса выбора вызывается `startPicking()`. Если для пользователя не установлено приложение OneDrive, когда выдается `startPicking()`, ему будет предлагаться скачать приложение и переходить в App Store.

### Типы ссылок
Средство выбора открытых можно настроить так, чтобы оно возвращало URL для выбранного файла в одном из следующих
форматов:
* `LinkType.DownloadLink` - возвращается URL-адрес, который обеспечивает доступ на 1 час непосредственно к содержимому файла. Вы можете использовать этот URL для загрузки файла в ваше приложение.
* `LinkType.WebViewLink` - создается ссылка для обмена, которая обеспечивает предварительный просмотр файла в Интернете. Ссылка будет действовать, пока пользователь не удалит ее в OneDrive. Ссылки для общего доступа недоступны для файлов в OneDrive для бизнеса.

### Получение результатов
Когда пользователь завершил открытие файла или отменил его из средства выбора открытых, для обработки результатов средства выбора будет вызван метод `onActivityResult ()`. В этом методе вы можете перехватить результаты и получить доступ к файлу, выбранному пользователем.

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

Если пользователь отменит выбор файла, результирующий объект будет иметь значение null.

### Объект результата средства выбора
Помимо имени файла и ссылки на файл, вы можете получить доступ к нескольким другим свойствам объекта `IPickerResult`, которые предоставляют более подробную информацию о выбранном файле:

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

Запустите [пример приложения Open Picker](PickerSample), чтобы увидеть все это в действии.

## Сохранение файлов

Помимо возможности открытия файлов ваше приложение должно предоставлять пользователю возможность сохранения файлов в OneDrive. В этом примере кода обработчик щелчка используется для запуска заставки из `onClick ()`. У вашего приложения должны быть имя и URI файла на устройстве, которые можно передать в средство сохранения. В этом примере кода показано, как создать файл-заполнитель с именем file.txt в локальной папке приложений.

**Не забудьте заменить выражение APP\_ID идентификатором вашего приложения.**

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
Когда вызывается метод `onClick()`, создается заставка, а затем вызывается метод `startSaving()`. В результате будет запущен интерфейс средства сохранения в OneDrive, и пользователи смогут выбрать папку для отправки файла. Если пользователь не установил приложение OneDrive при вызове `startSaving()`, ему будет предложено загрузить приложение из магазина.

В настоящий момент эта экономия поддерживает `content://` и `file://` файловых URI. Если используется другая схема URI, заставка возвратит ошибку `NoFileSpecified`. Дополнительные сведения об ответе средства сохранения см. ниже.

### Результат, возвращенный средством сохранения

Когда пользователь завершил сохранение файла или возникла проблема с сохранением, будет вызван метод `onActivityResult ()` для обработки сохраненного результата. Используя этот метод, вы можете проверить, был ли сохранен файл. Если файл не сохранен, вы можете перехватить исключение и обработать ошибку.

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
Сообщение об ошибке, предоставляемое `getDebugErrorInfo` (), предназначено главным образом для разработки и отладки и может измениться в любое время. При обработке ошибок вы можете использовать `getErrorType ()`, чтобы определить общую причину ошибки.


### Типы ошибок средства сохранения

Когда заставка не может завершить сохранение файла и выдает исключение, она предоставляет `SaverError`через `getErrorType()`, который указывает один из набора возможных типов ошибок.

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
Запустите [пример приложения-заставки](SaverSample), чтобы увидеть все это в действии.

## Поддерживаемые версии Android
Библиотека средства выбора OneDrive поддерживается во время выполнения для [Android API версии 14](http://source.android.com/source/build-numbers.html) и выше. Чтобы создать библиотеку средства выбора, необходимо установить API Android 19 или более поздней редакции.

Для работы средства выбора необходимо, чтобы было установлено приложение OneDrive. Если приложение OneDrive не установлено, пользователю будет предложено загрузить приложение при вызове метода `startPicking()` () или `startSaving()`.


## Участие

Этот проект соответствует [Правилам поведения разработчиков открытого кода Майкрософт](https://opensource.microsoft.com/codeofconduct/). Дополнительные сведения см. в разделе [часто задаваемых вопросов о правилах поведения](https://opensource.microsoft.com/codeofconduct/faq/). Если у вас возникли вопросы или замечания, напишите нам по адресу [opencode@microsoft.com](mailto:opencode@microsoft.com).
