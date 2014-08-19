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
import android.content.Intent;

/**
 * Interface for a class that allows for picking files from a user's OneDrive
 * and creating links for viewing or downloading a file.
 * 
 * <pre>
 * {@code
 *      // Create a picker instance
 *      private IPicker mPicker = Picker.createPicker(123456789); 
 *      
 *      // Trigger the opening of the picker
 *      private OnClickListener startPickerOnClick = new View.OnClickListener() {
 *          @Override
 *          public void onClick(View view) {
 *              mPicker.startPicking(getActivity(), LinkType.DownloadLink);
 *          }
 *      }
 *      
 *      // Handle the picker result
 *      @Override
 *      public void onActivityResult(int requestCode, int resultCode, Intent data) {
 *          IPickerResult result = mPicker.getPickerResult(requestCode, resultCode, data);
 *          if (null != result) {
 *              // Use the result in your application
 *          }
 *      }
 * }
 * </pre>
 * 
 * @author pnied
 */
public interface IPicker {

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
    IPickerResult getPickerResult(final int requestCode, final int resultCode, final Intent data);

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
    void startPicking(final Activity activity, final LinkType linkType);

    /**
     * Allows the request code used by this instance to be configured by the
     * source application.
     * 
     * @param requestCode The request code to be used by the
     *            startActivityForResult(Intent, int) call.
     */
    void setRequestCode(final int requestCode);

    /**
     * Gets the request code to be used by the startActivityForResult(Intent,
     * int) call during the startPicking(Activity, LinkType) call.
     * 
     * @return The request code
     */
    int getRequestCode();

}
