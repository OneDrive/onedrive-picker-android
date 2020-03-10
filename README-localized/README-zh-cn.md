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
# 适用于 Android 的 OneDrive 选取器
[ ![下载](https://api.bintray.com/packages/onedrive/Maven/onedrive-picker-android/images/download.svg) ](https://bintray.com/onedrive/Maven/onedrive-picker-android/_latestVersion)
[![构建状态](https://travis-ci.org/OneDrive/onedrive-picker-android.svg)](https://travis-ci.org/OneDrive/onedrive-picker-android)

选取器是将 Android 应用与 OneDrive 和 OneDrive for Business 集成的最快捷方式。它提供了一组 Java API，应用可以使用这些 API 在用户的 OneDrive 中浏览、选取、打开和保存文件。打开 OneDrive 中的文件时，还可以获取可共享文件链接（非常适合发送给亲朋好友和同事）以及图像和视频文件的缩略图，免去了自行生成的麻烦。

本指南将介绍如何让应用在 OneDrive 上快速[打开](#opening-files)文件，并[保存文件](#saving-files)到 OneDrive。也可以沿用“[文件打开选取器示例应用](PickerSample)”和“[保存器示例应用](SaverSample)”。

![OneDrive 选取器操作预览](images/android-picker-saver.png)

[安装](#setup)

[打开文件](#opening-files)

[保存文件](#saving-files)

[支持的 Android 版本](#supported-android-versions)


## 安装

## 使用源代码进行编译

适用于 Android 的文件选取器已作为 GitHub 上的开放源代码公开，同时包含文件打开和保存功能。可以直接下载最新版本，也可以克隆存储库：

* [下载压缩文件](https://github.com/OneDrive/onedrive-picker-android/archive/master.zip)
* `git clone https://github.com/OneDrive/onedrive-picker-android.git`

若要了解如何将环境配置为支持示例应用或文件选取器 SDK，请参阅“[配置 Android 环境”](http://developer.android.com/sdk/index.html)。

## 通过 Gradle 安装 AAR

```gradle
repositories {
    jcenter()
}

dependencies {
    compile ('com.microsoft.onedrivesdk:onedrive-picker-android:v2.0')
}
```

### 获取应用 ID

注册应用 \[此处] (https://account.live.com/developers/applications) 以获取应用程序 ID（客户端 ID）以启动选取器。

### 生成库

#### Android Studio
1. 选择“**导入项目...**”或“**导入模块...**”，以导入到现有项目中。
2. 转到 SDK 的保存位置，然后选择根 *onedrive-picker-android*
3. 如果尚未安装，请按照提示操作或转到“*Android SDK 管理器*”，以安装 Android 4.4.2 (API 19) 及相关生成工具。

#### Eclipse
1. 在 Eclipse 中，依次转到“**文件**”→“**导入**”→“**常规**”→“**将现有项目导入工作区**”。
2. 单击“**浏览...**”，选择 SDK 保存位置“*onedrive-picker-android*”作为根目录。请确保至少已选中“*OneDriveSDK*”。
3. 如果尚未安装，请在“*Android SDK 管理器*”中安装 Android 4.4.2 (API 19)。
4. 右键单击项目并选择“**属性**”，然后转到左侧边栏中的“**Android**”。
5. 单击“*库*”中的“**添加...**”，然后选择“*OneDriveSDK*”，将其与项目相关联。

## 打开文件

用户需要能够使用应用开始打开 OneDrive 中的文件。以下示例设置了通过 `onClick()` 启动文件打开选取器的单击处理程序。在此示例中，应用使用 `LinkType.WebViewLink` 请求获取仅供查看共享链接类型。

**请务必将 APP\_ID 替换成应用标识符**

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

在 `onClick()` 方法得到调用后，文件选取器会针对用户请求获取的链接类型进行创建和配置。然后，调用 `startPicking()` 方法来启动文件选取器。如果在调用 `startPicking()` 时用户尚未安装 OneDrive 应用，则提示用户下载此应用，并将用户转到应用商店。

### 链接类型
文件打开选取器可以配置，
能够采用下列格式之一返回选定文件的 URL：
* `LinkType.DownloadLink` - 返回的 URL 能够提供直接访问文件内容 1 小时的权限。可以使用此 URL 将文件下载到应用中。
* `LinkType.WebViewLink` - 创建提供文件 Web 预览的共享链接。在用户通过 OneDrive 删除共享链接前，此链接一直有效。共享链接不适用于 OneDrive for Business 文件。

### 捕获结果
在用户完成文件打开操作或在文件打开选取器中取消操作后，将调用 `onActivityResult()` 方法来处理文件选取器结果。可以使用此方法捕获结果，并访问用户选取的文件。

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

如果用户已取消选取文件，result 对象将为 null。

### 文件选取器 result 对象
除了文件的文件名和链接以外，还可以访问 `IPickerResult` 对象中的其他几个属性，更详细地了解选定文件：

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

运行“[打开选取器示例应用](PickerSample)”，了解此任务的所有实际操作。

## 保存文件

与文件打开功能相似，用户应能够使用应用将文件保存到 OneDrive。在以下代码示例中，单击处理程序用于通过 `onClick()` 启动文件保存器。应用设备上必须有要传递给文件保存器的文件的文件名和 URI。以下示例代码在应用本地文件夹中创建占位符文件。例如，可将此文件命名为 file.txt。

**请务必将 APP\_ID 替换成应用标识符**

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
调用 `onClick()` 方法后，将创建保存器，然后调用 `startSaving()` 方法。这将启动 OneDrive 文件保存器，以便用户可以选取将文件上传到的文件夹。如果在调用 `startSaving()` 时用户尚未安装 OneDrive 应用，则提示用户从应用商店下载此应用。

目前，文件保存器支持 `content://` 和 `file://` 文件 URI 方案。如果使用其他 URI 方案，文件保存器会返回 `NoFileSpecified` 错误。请参阅下文，详细了解文件保存器响应。

### 文件保存器结果

在用户保存完文件或无法保存文件时，将调用 `onActivityResult()` 方法来处理文件保存器结果。可以使用此方法检查文件是否已保存。如果未保存，可以捕获异常并处理错误。

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
`getDebugErrorInfo()` 提供的错误消息主要用于开发和调试目的，可能会随时发生变更。处理错误时，可以使用 `getErrorType()` 确定导致错误出现的常见原因。


### 文件保存器错误类型

无法完成文件保存并引发异常时，文件保存器通过 `getErrorType()` 提供 `SaverError`，以从一组可能错误类型中指明一种错误。

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
运行“[文件保存器示例应用](SaverSample)”，了解此任务的所有实际操作。

## 支持的 Android 版本
[Android API 版本 14](http://source.android.com/source/build-numbers.html) 及更高版本的运行时支持 OneDrive 文件选取器库。必须安装 Android API 版本 19 或更高版本，才能生成文件选取器库。

必须安装 OneDrive 应用，才能运行文件选取器。如果未安装 OneDrive 应用，将在调用 `startPicking()` 或 `startSaving()` 方法时提示用户下载此应用。


## 参与

此项目已采用 [Microsoft 开放源代码行为准则](https://opensource.microsoft.com/codeofconduct/)。有关详细信息，请参阅[行为准则常见问题解答](https://opensource.microsoft.com/codeofconduct/faq/)。如有其他任何问题或意见，也可联系 [opencode@microsoft.com](mailto:opencode@microsoft.com)。
