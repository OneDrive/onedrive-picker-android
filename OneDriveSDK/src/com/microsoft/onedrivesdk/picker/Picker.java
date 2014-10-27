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

import com.microsoft.onedrivesdk.common.Client;

import android.app.Activity;
import android.content.Intent;
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
     * The default pick file request code leveraged by onActivtyResult(...)
     * signature
     */
    private static final int PICK_FILE_REQUEST_CODE = 0xF0F0;

    /**
     * The application id registered with OneDrive that created this picker
     * {@see https://account.live.com/developers/applications/index}
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
        final Intent pickerIntent = Client.createOneDriveIntent(Client.ONEDRIVE_PICKER_ACTION, mAppId);
        final Intent androidMarketPlaceIntent = Client.createAndroidMarketPlaceIntent();
        final Intent amazonMarketPlaceIntent = Client.createAmazonMarketPlaceIntent();
        if (Client.isAvailable(activity, pickerIntent)) {
            pickerIntent.putExtra("linkType", linkType.toString());
            activity.startActivityForResult(pickerIntent, mRequestCode);
        } else if (Client.isAvailable(activity, androidMarketPlaceIntent)) {
            activity.startActivity(androidMarketPlaceIntent);
        } else if (Client.isAvailable(activity, amazonMarketPlaceIntent)) {
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
}
