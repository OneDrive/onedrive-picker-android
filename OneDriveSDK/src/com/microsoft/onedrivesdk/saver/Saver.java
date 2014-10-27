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

package com.microsoft.onedrivesdk.saver;

import com.microsoft.onedrivesdk.common.Client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Saver provides hooks into the OneDrive application for users to upload files
 * on OneDrive.
 * 
 * @see {@link ISaver} for detailed usage information
 * @author pnied
 */
public final class Saver implements ISaver {

    /**
     * The default saver file request code leveraged by onActivtyResult(...)
     * signature
     */
    private static final int SAVE_FILE_REQUEST_CODE = 0xF1F1;

    /**
     * The application id registered with OneDrive that created this saver
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
     * @param appId The application id registered with OneDrive that created
     *            this saver.
     */
    private Saver(final String appId) {
        mAppId = appId;
        mRequestCode = SAVE_FILE_REQUEST_CODE;
    }

    /**
     * Creates an instance of the OneDrive Saver
     *
     * @param appId The application id registered with OneDrive that created
     *            this saver.
     * @return The new saver instance ready to use
     */
    public static ISaver createSaver(final String appId) {
        return new Saver(appId);
    }

    /**
     * Helper for onActivityResult(int, int, Intent), validates request and
     * result codes.
     *
     * @param requestCode The request code from the onActivityResult call
     * @param resultCode The result code from the onActivityResult call
     * @param data The data from the onActivityResult call
     * @return If the saving process was successful <b>true</b>. If the the
     *         request code did not match the saver, then <b>false</b>.
     * @exception SaverException If there was any problem saving the file onto
     *                OneDrive. See {@link SaverException} for more details.
     */
    public boolean handleSave(final int requestCode, final int resultCode, final Intent data)
            throws SaverException {
        if (mRequestCode == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                return true;
            }

            // Pull the error type from the response
            final String errorType;
            final String debugErrorType;
            if (data != null) {
                errorType = data.getStringExtra("error");
                debugErrorType = data.getStringExtra("debugError");
            } else {
                errorType = null;
                debugErrorType = null;
            }
            throw new SaverException(errorType, debugErrorType);
        }
        return false;
    }

    /**
     * Starts a new activity that allows the user to pick a location on their
     * OneDrive to save a file onto. Note, implementing onActivityResult(int,
     * int, Intent) is required to get the saver results. If the OneDrive
     * application is not available it will start a marketplace so users can
     * install the OneDrive application.
     *
     * @param activity The activity that will start the saver experience
     * @param filename The file name to save the file as
     * @param file A readable file stream to upload onto OneDrive
     */
    public void startSaving(final Activity activity, final String filename, final Uri file) {
        final Intent saverIntent = Client.createOneDriveIntent(Client.ONEDRIVE_SAVER_ACTION, mAppId);
        saverIntent.putExtra("filename", filename);
        saverIntent.putExtra("data", file);
        final Intent androidMarketPlaceIntent = Client.createAndroidMarketPlaceIntent();
        final Intent amazonMarketPlaceIntent = Client.createAmazonMarketPlaceIntent();
        if (Client.isAvailable(activity, saverIntent)) {
            activity.startActivityForResult(saverIntent, mRequestCode);
        } else if (Client.isAvailable(activity, androidMarketPlaceIntent)) {
            activity.startActivity(androidMarketPlaceIntent);
        } else if (Client.isAvailable(activity, amazonMarketPlaceIntent)) {
            activity.startActivity(amazonMarketPlaceIntent);
        } else {
            Toast.makeText(activity, "Unable to start the OneDrive saver or device market place", Toast.LENGTH_LONG)
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
     * int) call during startSaving(Activity)
     *
     * @return The request code
     */
    public int getRequestCode() {
        return mRequestCode;
    }
}
