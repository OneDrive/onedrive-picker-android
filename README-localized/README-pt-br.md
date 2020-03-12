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
# Seletor do OneDrive para Android
[ ![Baixar](https://api.bintray.com/packages/onedrive/Maven/onedrive-picker-android/images/download.svg) ](https://bintray.com/onedrive/Maven/onedrive-picker-android/_latestVersion)
[![Status do Build](https://travis-ci.org/OneDrive/onedrive-picker-android.svg)](https://travis-ci.org/OneDrive/onedrive-picker-android)

O seletor é a forma mais rápida de integrar seu aplicativo Android ao OneDrive e ao OneDrive for Business. Ele fornece um conjunto de APIs Java que seu aplicativo pode usar para navegar, selecionar, abrir e salvar arquivos no OneDrive do seu usuário. Ao abrir arquivos do OneDrive, você também pode obter links compartilháveis para arquivos, que são ótimos para enviar para amigos, colegas de trabalho e parentes, além de miniaturas de arquivos de imagem e vídeo para que você não tenha que criá-los por conta própria.

Neste guia, mostraremos como fazer seu aplicativo [abrir](#opening-files) e [salvar arquivos](#saving-files) rapidamente no OneDrive. Você também pode fazer o acompanhamento com o [aplicativo de exemplo do seletor de abertura](PickerSample) e o [aplicativo de exemplo de armazenamento](SaverSample).

![Visualização do seletor do OneDrive em ação](images/android-picker-saver.png)

[Instalação](#setup)

[Abrindo arquivos](#opening-files)

[Salvando arquivos](#saving-files)

[Versões do Android Compatíveis](#supported-android-versions)


## Instalação

## Compilar com o código-fonte

O seletor para Android está disponível como fonte aberta no GitHub e inclui recursos para abrir e salvar. Você pode baixar a versão mais recente diretamente ou clonar o repositório:

* [Baixar zip](https://github.com/OneDrive/onedrive-picker-android/archive/master.zip)
* `git clone https://github.com/OneDrive/onedrive-picker-android.git`

Para saber mais sobre como configurar seu ambiente para trabalhar com os aplicativos de exemplo ou o seletor de SDK, confira [Como configurar o ambiente Android](http://developer.android.com/sdk/index.html).

## Instalar o AAR via Gradle

```gradle
repositories {
    jcenter()
}

dependencies {
    compile ('com.microsoft.onedrivesdk:onedrive-picker-android:v2.0')
}
```

### Obter uma ID do aplicativo

Registre seu aplicativo \[aqui] (https://account.live.com/developers/applications) para obter uma ID de aplicativo (ID de cliente) para iniciar o seletor.

### Criar a biblioteca

#### Android Studio
1. Escolha **Importar projeto...** ou **Importar módulo...**, para importar para um projeto existente.
2. Navegue até o local onde o SDK está salvo e selecione a raiz *onedrive-picker-android*
3. Se ainda não estiver instalado, siga as instruções na tela ou vá para o *Gerenciador de SDK para Android* para instalar o Android 4.4.2 (API 19) e as ferramentas de build associadas.

#### Eclipse
1. No Eclipse, acesse **Arquivo** → **Importar** → **Geral** → **Projetos existentes no espaço de trabalho**.
2. Clique em **Procurar...**para selecionar *onedrive-picker-android*, onde você salvou o SDK, como seu diretório raiz. Certifique-se de que no mínimo a opção *OneDriveSDK* esteja marcada.
3. Se ainda não estiver instalado, no *Gerenciador de SDK para Android*, instale o Android 4.4.2 (API 19).
4. Clique com botão direito do mouse no seu projeto, selecione **Propriedades** e acesse **Android** na barra lateral esquerda.
5. Clique em **Adicionar**.. na *Biblioteca* e selecione *OneDriveSDK* para vinculá-lo ao seu projeto.

## Abrindo arquivos

Seu aplicativo precisa dar ao usuário uma forma de abrir arquivos no OneDrive. Este exemplo configura um manipulador de cliques que lança o seletor de abertura do `onClick()`. Nesse caso, o aplicativo está solicitando um tipo de link de compartilhamento somente para exibição usando o `LinkType.WebViewLink`.

**Não se esqueça de substituir a APP\_ID pela identificação do aplicativo**

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

Quando o método `onClick()` é chamado, o seletor é criado e configurado para o tipo de link solicitado pelo usuário. Em seguida, o método `startPicking()` é chamado para iniciar a experiência de seleção. Se o usuário não tiver o aplicativo do OneDrive instalado quando `startPicking()` for chamado, ele será solicitado a baixar o aplicativo e levado para a loja de aplicativos.

### Tipos de link
O seletor de abertura pode ser configurado para retornar uma URL para o arquivo selecionado em um destes
formatos:
* `LinkType.DownloadLink` - É retornada uma URL que fornece acesso a uma hora diretamente para o conteúdo do arquivo. Você pode usar essa URL para baixar o arquivo no aplicativo.
*`LinkType.WebViewLink` -um link de compartilhamento que fornece uma visualização da Web do arquivo será criado. O link é válido até que o usuário exclua o link compartilhado por meio do OneDrive. O compartilhamento de links não está disponível para os arquivos do OneDrive for Business.

### Obtendo resultados
Quando o usuário abrir um arquivo ou cancelar por meio do seletor de abertura, o método `onActivityResult()` será chamado para gerenciar os resultados do seletor. Neste método você pode obter os resultados e acessar o arquivo selecionado pelo usuário.

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

Se o usuário cancelar a escolha de um arquivo, o objeto de resultado será nulo.

### Objeto de resultado do seletor
Além do nome do arquivo e um link para o arquivo, você pode acessar várias outras propriedades no objeto `IPickerResult` que fornecem mais detalhes sobre o arquivo selecionado:

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

Execute o [aplicativo de exemplo do seletor de abertura](PickerSample) para ver tudo isso em ação.

## Salvando arquivos

Assim como na abertura de arquivos, seu aplicativo deve fornecer uma forma de o usuário salvar um arquivo no OneDrive. Neste exemplo de código, um manipulador de cliques é usado para iniciar o armazenamento do `onClick()`. Seu aplicativo precisa ter um nome de arquivo e URI para o arquivo no dispositivo para passar para o armazenamento. Este código de exemplo cria um arquivo de espaço reservado na pasta local de aplicativos denominada file.txt como exemplo.

**Não se esqueça de substituir a APP\_ID pela identificação do aplicativo**

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
Quando o método `onClick()` é chamado, o armazenamento é criado e depois o método `startSaving()` é chamado. Isso lança a experiência de armazenamento do OneDrive, permitindo que seus usuários escolham uma pasta para carregar o arquivo. Se o usuário não tiver o aplicativo do OneDrive instalado quando `startSaving()` é chamado, ele será solicitado a baixar o aplicativo no marketplace.

O armazenamento atualmente oferece suporte para o esquema de URI do arquivo `content://` and `file://`. Se for usado um esquema de URI diferente, o armazenamento retornará um erro `NoFileSpecified`. Confira abaixo para obter detalhes sobre a resposta de armazenamento.

### Resultado de armazenamento

Quando o usuário tiver terminado de salvar um arquivo, ou se houver um problema ao salvar, o método `onActivityResult()` será chamado para gerenciar o resultado de armazenamento. Ao usar esse método, você pode verificar se o arquivo foi salvo e, caso contrário, você pode capturar a exceção e lidar com o erro.

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
A mensagem de erro fornecida pelo`getDebugErrorInfo()` é principalmente para desenvolvimento e depuração, e pode mudar a qualquer momento. Ao gerenciar erros, você pode usar `getErrorType()` para determinar a causa geral do erro.


### Tipos de erro de armazenamento

Quando o armazenamento falha e gera uma exceção, ele fornece `SaverError`através de`getErrorType()`, que indica um erro dentro de um conjunto de tipos de erro possíveis.

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
Execute o [aplicativo de exemplo de armazenamento](SaverSample) para observar tudo isso em ação.

## Versões do Android Compatíveis
A biblioteca do seletor do OneDrive oferece suporte em tempo de execução para a [API Android revisão 14](http://source.android.com/source/build-numbers.html) e superior. Para criar a biblioteca de seletor você precisa instalar a API Android revisão 19 ou superior.

O seletor exige que o aplicativo OneDrive seja instalado, para funcionar. Se o aplicativo OneDrive não estiver instalado, o usuário será solicitado a baixar o aplicativo quando o método `startPicking()` ou `startSaving()` for chamado.


## Colaboração

Este projeto adotou o [Código de Conduta de Código Aberto da Microsoft](https://opensource.microsoft.com/codeofconduct/).  Para saber mais, confira as [Perguntas frequentes sobre o Código de Conduta](https://opensource.microsoft.com/codeofconduct/faq/) ou entre em contato pelo [opencode@microsoft.com](mailto:opencode@microsoft.com) se tiver outras dúvidas ou comentários.
