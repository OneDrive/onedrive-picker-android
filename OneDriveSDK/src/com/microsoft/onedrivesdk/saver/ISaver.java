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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Interface for a class that allows for saving files onto a user's OneDrive.
 *
 * <pre>
 * {@code
 *      // Create a saver instance
 *      private ISaver mSaver = Saver.createSaver(123456789);
 *
 *      // Trigger the opening of the saver
 *      private OnClickListener startSaverOnClick = new View.OnClickListener() {
 *          @Override
 *          public void onClick(View view) {
 *              mSaver.startSaving(getActivity(), "MyFile.txt", Uri.Parse("file://sdcard/Download/MyFile.txt"));
 *          }
 *      }
 *
 *      // Handle the saver result
 *      @Override
 *      public void onActivityResult(int requestCode, int resultCode, Intent data) {
 *          try {
 *              if (mSaver.handleSave(requestCode, resultCode, data)) {
 *                  // Handle the succesful save flow
 *              }
 *          } catch (SaverException e) {
 *              // Handle the user error flow
 *          }
 *      }
 * }
 * </pre>
 *
 * @author pnied
 */
public interface ISaver {

    /**
     * Helper for onActivityResult(int, int, Intent), validates request and
     * result codes.
     *
     * @param requestCode The request code from the onActivityResult call
     * @param resultCode The result code from the onActivityResult call
     * @param data The data from the onActivityResult call
     * @return If the saving process was successful <b>true</b>. If the the
     *         request code did not match the saver, then false.
     * @exception SaverException If there was any problem saving the file onto
     *                OneDrive. See {@link SaverException} for more details.
     */
    boolean handleSave(final int requestCode, final int resultCode, final Intent data) throws SaverException;

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
    void startSaving(final Activity activity, final String filename, final Uri file);

    /**
     * Allows the request code used by this instance to be configured by the
     * source application
     * 
     * @param requestCode The request code to be used by the
     *            startActivityForResult(Intent, int) call.
     */
    void setRequestCode(final int requestCode);

    /**
     * Gets the request code to be used by the startActivityForResult(Intent,
     * int) call during startSaving(Activity)
     * 
     * @return The request code
     */
    int getRequestCode();
}
