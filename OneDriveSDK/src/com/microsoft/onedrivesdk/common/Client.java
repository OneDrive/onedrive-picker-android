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

package com.microsoft.onedrivesdk.common;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * All OneDrive Client specific functionality
 * 
 * @author pnied
 *
 */
public final class Client {

    /**
     * Hidden constructor for utility classes
     */
    private Client() {
    }

    /**
     * The OneDrive application package name
     */
    public static final String ONEDRIVE_PACKAGE_NAME = "com.microsoft.skydrive";

    /**
     * The intent that triggers the OneDrive picking experience
     */
    public static final String ONEDRIVE_PICKER_ACTION = "onedrive.intent.action.PICKER";

    /**
     * The intent that triggers the OneDrive saving experience
     */
    public static final String ONEDRIVE_SAVER_ACTION = "onedrive.intent.action.SAVER";

    /**
     * The version number for the SDK
     */
    private static final int SDK_VERSION = 2;

    /**
     * Creates the intent to launch the OneDrive picker
     * 
     * @param action The intent action
     * @param appId The appId for this request
     * @return The intent instance
     */
    public static Intent createOneDriveIntent(final String action, final String appId) {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("appId", appId);
        intent.putExtra("version", SDK_VERSION);
        return intent;
    }

    /**
     * Creates the intent to launch the marketplace for the OneDrive application
     * 
     * @return the intent instance
     */
    public static Intent createAndroidMarketPlaceIntent() {
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
    public static Intent createAmazonMarketPlaceIntent() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format("amzn://apps/android?p=%s", ONEDRIVE_PACKAGE_NAME)));
        return intent;
    }

    /**
     * Determines if the OneDrive application is installed and able to process
     * the specified {@link Intent}.
     * 
     * @param activity The activity that would start the picker experience
     * @param intent The intent to check availability
     * @return <b>true</b> if the OneDrive application can start and execute the
     *         file picking flow. <b>false</b> if the OneDrive application does
     *         not support this call, or if the application is not installed.
     */
    public static boolean isAvailable(final Activity activity, final Intent intent) {
    	final PackageManager pm = activity.getPackageManager();
        return pm.queryIntentActivities(intent, 0).size() != 0;
    }
}
