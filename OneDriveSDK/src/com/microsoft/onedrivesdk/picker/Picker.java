// ------------------------------------------------------------------------------
// Copyright (c) 2014 Microsoft Corporation
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
// ------------------------------------------------------------------------------

package com.microsoft.onedrivesdk.picker;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Picker provides hooks into the OneDrive application for users to find files
 * on OneDrive and hand back references to the current application.
 * 
 * @see IPicker for detailed usage information
 * @author pnied
 */
public final class Picker implements IPicker {

    /**
     * The intent that triggers the OneDrive picking experience
     */
    private static final String ONEDRIVE_PICKER_ACTION = "onedrive.intent.action.PICKER";

    /**
     * The specific picker handler in the OneDrive application
     */
    private static final String ONEDRIVE_PICKER_HANDLER = "com.microsoft.skydrive.getcontent.RecieveSdkPickerActivity";

    /**
     * The default picker file request code leveraged by onActivtyResult(...)
     * signature
     */
    private static final int PICK_FILE_REQUEST_CODE = 0xF0F0;

    /**
     * The OneDrive application package name
     */
    private static final String ONEDRIVE_PACKAGE_NAME = "com.microsoft.skydrive";

    /**
     * The application ID registered with OneDrive that created this picker
     * {@link https://account.live.com/developers/applications/index}
     */
    private String mAppId;

    /**
     * The request code leveraged by onActivityResult(int, int, Intent)
     * signature
     */
    private int mRequestCode;

    /**
     * Default Constructor
     * 
     * @param appId The application ID registered with OneDrive that created
     *            this picker.
     */
    private Picker(final String appId) {
        this.mAppId = appId;
        this.mRequestCode = PICK_FILE_REQUEST_CODE;
    }

    /**
     * Creates an instance of the OneDrive picker
     * 
     * @param appId The application ID registered with OneDrive that created
     *            this picker.
     * @return The new picker instance ready to use
     */
    public static IPicker createPicker(final String appId) {
        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("appId");
        }
        return new Picker(appId);
    }

    /**
     * Helper for onActivityResult(int, int, Intent). Validates the request and
     * result codes and returns a type-safe {@link IPickerResult}.
     * 
     * @param requestCode The request code from the onActivityResult call
     * @param resultCode The result code from the onActivityResult call
     * @param data The data from the onActivityResult call
     * @return If the picking process was successful, returns an instance of the
     *         {@link IPickerResult}; if it was unsuccessful because of a user
     *         cancellation or error, returns <b>null</b>.
     */
    public IPickerResult getPickerResult(final int requestCode, final int resultCode, final Intent data) {
        if (mRequestCode == requestCode && Activity.RESULT_OK == resultCode) {
            return PickerResult.fromBundle(data.getExtras());
        }
        return null;
    }

    /**
     * Starts a new activity that allows users to pick a file from OneDrive,
     * create links for viewing or downloading, and then to be returned to the
     * starting application. Note, to get the picking results, you must
     * implement onActivityResult(int, int, Intent) to get the picking results.
     * If the OneDrive application is not available, starts a marketplace so
     * that users can install the OneDrive application
     * 
     * @param activity The activity that will start the picker experience
     * @param linkType The type of link that should be returned to this activity
     *            from the picking flow
     */
    public void startPicking(final Activity activity, final LinkType linkType) {
        final Intent pickerIntent = createOneDriveIntent();
        final Intent androidMarketPlaceIntent = createAndroidMarketPlaceIntent();
        final Intent amazonMarketPlaceIntent = createAmazonMarketPlaceIntent();
        if (isAvailable(activity, pickerIntent)) {
            pickerIntent.putExtra("linkType", linkType.toString());
            activity.startActivityForResult(pickerIntent, mRequestCode);
        } else if (isAvailable(activity, androidMarketPlaceIntent)) {
            activity.startActivity(androidMarketPlaceIntent);
        } else if (isAvailable(activity, amazonMarketPlaceIntent)) {
            activity.startActivity(amazonMarketPlaceIntent);
        } else {
            Toast.makeText(activity, "Unable to start the OneDrive picker or device market place", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Allows the request code used by this instance to be configured by the
     * source application
     * 
     * @param requestCode The request code to be used by the
     *            startActivityForResult(Intent, int) call.
     */
    public void setRequestCode(final int requestCode) {
        mRequestCode = requestCode;
    }

    /**
     * Gets the request code to be used by the startActivityForResult(Intent,
     * int) method during the startPicking(Activity, LinkType) call.
     * 
     * @return The request code
     */
    public int getRequestCode() {
        return mRequestCode;
    }

    /**
     * Determines if the given intent can be resolved to an activity.
     * 
     * @param activity The activity that would start the picker experience
     * @param intent The intent to check
     * @return <b>true</b> if the OneDrive application can start and execute the
     *         file picking flow. Return <b>false</b> if the OneDrive
     *         application does not support this call, or if the application is
     *         not installed.
     */
    private boolean isAvailable(final Activity activity, final Intent intent) {
        final PackageManager pm = activity.getPackageManager();
        return pm.queryIntentActivities(intent, 0).size() != 0;
    }

    /**
     * Creates the intent to launch the OneDrive picker
     * 
     * @return The intent instance
     */
    private Intent createOneDriveIntent() {
        final Intent intent = new Intent();
        intent.setAction(ONEDRIVE_PICKER_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setComponent(new ComponentName(ONEDRIVE_PACKAGE_NAME, ONEDRIVE_PICKER_HANDLER));
        intent.putExtra("appId", mAppId);
        return intent;
    }

    /**
     * Creates the intent to launch the android marketplace for the OneDrive
     * application
     * 
     * @return the intent instance
     */
    private Intent createAndroidMarketPlaceIntent() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format("market://details?id=%s", ONEDRIVE_PACKAGE_NAME)));
        return intent;
    }

    /**
     * Creates the intent to launch the amazon marketplace for the OneDrive
     * application
     * 
     * @return the intent instance
     */
    private Intent createAmazonMarketPlaceIntent() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format("amzn://apps/android?p=%s", ONEDRIVE_PACKAGE_NAME)));
        return intent;
    }
}
