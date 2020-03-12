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
# Android 用の OneDrive ピッカー
[ ![ダウンロード](https://api.bintray.com/packages/onedrive/Maven/onedrive-picker-android/images/download.svg) ](https://bintray.com/onedrive/Maven/onedrive-picker-android/_latestVersion)
[![ビルドの状態](https://travis-ci.org/OneDrive/onedrive-picker-android.svg)](https://travis-ci.org/OneDrive/onedrive-picker-android)

このピッカーは、OneDrive および OneDrive for Business に Android アプリを統合するための最も簡単な方法です。これにより、ユーザーの OneDrive 内のファイルをアプリで参照、選択、開く、および保存するために使用できる Java API のセットが提供されます。OneDrive からファイルを開くときは、友達、同僚、親戚などに送信するのに便利な、ファイルへの共有可能なリンクも取得できます。また、画像ファイルとビデオ ファイルのサムネイルも取得できるため、自分でサムネイルを作成する必要がなくなります。

このガイドでは、アプリでファイルを素早く[開いて](#opening-files) OneDrive に[保存](#saving-files)する方法について説明します。[オープン ピッカー サンプル アプリ](PickerSample)と[セーバー サンプル アプリ](SaverSample)をご自分で実行することもできます。

![動作中の OneDrive ピッカーのプレビュー](images/android-picker-saver.png)

[セットアップ](#setup)

[ファイルを開く](#opening-files)

[ファイルを保存する](#saving-files)

[サポートされる Android のバージョン](#supported-android-versions)


## セットアップ

## ソース コードのコンパイル

Android 対応ピッカーは、オープン ソースとして GitHub で入手できます。このピッカーは、ファイルを開く機能と保存する機能を備えています。最新バージョンを直接ダウンロードすることも、リポジトリを複製することもできます。

* [Zip のダウンロード](https://github.com/OneDrive/onedrive-picker-android/archive/master.zip)
* `git clone https://github.com/OneDrive/onedrive-picker-android.git`

サンプル アプリケーションまたはピッカー SDK を使用するための環境の構成方法の詳細については、「[Configuring your Android Environment (Android 環境を構成する)](http://developer.android.com/sdk/index.html)」を参照してください。

## Gradle 経由で AAR をインストールする

```gradle
repositories {
    jcenter()
}

dependencies {
    compile ('com.microsoft.onedrivesdk:onedrive-picker-android:v2.0')
}
```

### アプリ ID を取得する

アプリをこちら (https://account.live.com/developers/applications) で登録し、ピッカーを起動するためのアプリ ID (クライアント ID) を取得します。

### ライブラリをビルドする

#### Android Studio
1. \[**Import Project...**] または \[**Import Module...**] を選択して、既存のプロジェクトにインポートします。
2. SDK が保存されている場所を参照して、ルートの *onedrive-picker-android* を選択します。
3. まだインストールしていない場合は、ダイアログの指示に従うか、*Android SDK マネージャー*に移動して、Android 4.4.2 (API 19) と関連するビルド ツールをインストールします。

#### Eclipse
1. Eclipse で、\[**File**]、\[**Import**]、\[**General**]、\[**Existing Projects into Workspace**] の順に移動します。
2. \[**Browse..**] をクリックして、ルート ディレクトリとして SDK を保存した場所である *onedrive-picker-android* を選択します。少なくとも \[*OneDriveSDK*] がオンになっていることを確認します。
3. まだインストールしていない場合は、*Android SDK マネージャー*から Android 4.4.2 (API 19) をインストールします。
4. プロジェクトを右クリックし、\[**Properties**] を選択して、左側のバーで \[**Android**] に移動します。
5. \[*Library*] で \[**Add..**] をクリックし、\[*OneDriveSDK*] を選択してプロジェクトにリンクさせます。

## ファイルを開く

アプリでは、ユーザーに OneDrive からファイルを開く操作を開始するための手段を提供する必要があります。この例では、`onClick()` からオープン ピッカーを起動するクリック ハンドラーをセットアップします。この場合、アプリは `LinkType.WebViewLink` を使用して表示専用の共有リンクの種類を要求します。

**APP\_ID は、必ず自分のアプリケーションの識別子に置き換えます**

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

`onClick()` メソッドが呼び出されると、ピッカーが作成され、ユーザーが要求したリンクの種類に応じて構成されます。その後で、`startPicking()` メソッドが呼び出され、ピッキング エクスペリエンスが起動されます。`startPicking()` が呼び出されたときに、ユーザーが OneDrive アプリをインストールしていなかった場合は、アプリのダウンロードを求めるダイアログが表示され、アプリ ストアに移動されます。

### リンクの種類
オープン ピッカーは、選択されているファイルの URL
を次のフォーマットのいずれかで返すように構成できます。
* `LinkType.DownloadLink` \- ファイルの内容への直接のアクセスを 1 時間提供する URL が返されます。この URL を使用してファイルをアプリケーションにダウンロードできます。
* `LinkType.WebViewLink` \- ファイルの Web プレビューを提供する共有リンクが作成されます。このリンクは、OneDrive を通じて共有リンクをユーザーが削除するまで有効です。共有リンクは、OneDrive for Business のファイルには使用できません。

### 結果を取得する
ユーザーがファイルを開く操作を完了するか、オープン ピッカーから操作を取り消した場合、`onActivityResult()` メソッドが呼び出されてピッカーの結果が処理されます。このメソッドでは、結果を取得して、ユーザーが選択したファイルにアクセスできます。

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

ユーザーがファイルのピッキングを取り消した場合、結果オブジェクトは Null になります。

### ピッカーの結果オブジェクト
ファイルのファイル名とリンクに加えて、`IPickerResult` オブジェクトのその他のプロパティにもアクセスできます。これらのプロパティでは、選択したファイルに関する追加の詳細情報が提供されます。

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

[オープン ピッカー サンプル アプリ](PickerSample)を実行して、全体がどのように動作するかを確認してください。

## ファイルを保存する

ファイルを開く場合と同様に、アプリでは、ファイルを OneDrive に保存する方法をユーザーに提供する必要があります。このコード サンプルでは、クリック ハンドラーを使用して `onClick()` からセーバーを起動します。アプリは、セーバーに渡すために、デバイス上のファイル名とファイルへのリンクが必要になります。このコード サンプルでは、アプリのローカル フォルダーに file.txt という名前のプレースホルダー ファイルを作成して例を示します。

**APP\_ID は、必ず自分のアプリケーションの識別子に置き換えます**

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
`onClick ()` メソッドを呼び出すとセーバーが作成され、次に `startSaving()` が呼び出されます。これにより OneDrive セーバー エクスペリエンスが起動し、ユーザーがファイルをアップロードするフォルダーを選択できるようになります。`startSaving()` が呼び出されたときに OneDrive アプリがインストールされていない場合は、マーケットプレースからアプリをダウンロードするように求めるダイアログがユーザーに表示されます。

現在、セーバーでは `content://` と `file://` ファイル URI スキーマがサポートされています。これと異なる URI スキーマを使用すると、セーバーから `NoFileSpecified` エラーが返されます。セーバーの応答に関する詳細については、後述します。

### セーバーの結果

ユーザーがファイルの保存を完了したとき、または保存に問題が発生した場合は、セーバーの結果を処理するために `onActivityResult` メソッドが呼び出されます。このメソッドを使用すると、ファイルが保存されたかどうかを確認でき、ファイルが保存されなかった場合は、例外をキャッチしてエラーを処理できます。

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
`getDebugErrorInfo()` により提供されるエラー メッセージは、主に開発およびデバッグ向けものであり、常に変更される可能性があります。エラーを処理するときは、`getErrorType()` を使用してエラーの一般的な原因を特定できます。


### セーバーのエラーの種類

セーバーがファイルの保存を完了できずに例外をスローした場合は、`getErrorType()` を通じて `SaverError` が提示されます。これにより、可能性のあるエラーのセットのいずれかが示されます。

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
[セーバー サンプル アプリ](SaverSample)を実行して、全体がどのように動作するかを確認してください。

## サポートされる Android のバージョン
OneDrive ピッカー ライブラリは、[Android API リビジョン 14](http://source.android.com/source/build-numbers.html)以降のラインタイムでサポートされます。ピッカー ライブラリをビルドするには、Android API リビジョン 19 以降をインストールする必要があります。

ピッカーが機能するには、OneDrive がインストールされている必要があります。OneDrive アプリがインストールされていない場合は、`startPicking()` メソッドまたは `startSaving()` メソッドが呼び出されたときに、アプリをダウンロードするようにユーザーに求めるダイアログが表示されます。


## 投稿

このプロジェクトでは、[Microsoft Open Source Code of Conduct (Microsoft オープン ソース倫理規定)](https://opensource.microsoft.com/codeofconduct/) が採用されています。詳細については、「[Code of Conduct の FAQ (倫理規定の FAQ)](https://opensource.microsoft.com/codeofconduct/faq/)」を参照してください。また、その他の質問やコメントがあれば、[opencode@microsoft.com](mailto:opencode@microsoft.com) までお問い合わせください。
