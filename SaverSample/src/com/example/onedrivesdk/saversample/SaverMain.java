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

package com.example.onedrivesdk.saversample;

import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.microsoft.onedrivesdk.saver.*;

/**
 * Activity that shows how the OneDrive SDK can be used for file saving
 * 
 * @author pnied
 */
public class SaverMain extends Activity {

    /**
     * The default file size
     */
    private static final int DEFAULT_FILE_SIZE_KB = 100;

    /**
     * Registered Application id for OneDrive {@see http://go.microsoft.com/fwlink/p/?LinkId=193157}
     */
    private static final String ONEDRIVE_APP_ID = "48122D4E";

    /**
     * The onClickListener that will start the OneDrive Picker
     */
    private final OnClickListener mStartPickingListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            final Activity activity = (Activity) v.getContext();
            activity.findViewById(R.id.result_table).setVisibility(View.INVISIBLE);

            final String filename = ((EditText)activity.findViewById(R.id.file_name_edit_text))
                    .getText().toString();
            final String fileSizeString = ((EditText)activity.findViewById(R.id.file_size_edit_text))
                    .getText().toString();
            int size;
            try {
                size = Integer.parseInt(fileSizeString);
            } catch (final NumberFormatException nfe) {
                size = DEFAULT_FILE_SIZE_KB;
            }

            // Create a file
            final File f = createExternalSdCardFile(filename, size);

            // Start the saver
            mSaver.startSaving(activity, filename, Uri.parse("file://" + f.getAbsolutePath()));
        }
    };

    /**
     * The OneDrive saver instance used by this activity
     */
    private ISaver mSaver;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saver_main);

        // Create the picker instance
        mSaver = Saver.createSaver(ONEDRIVE_APP_ID);

        // Add the start saving listener
        findViewById(R.id.startSaverButton).setOnClickListener(mStartPickingListener);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // Check that we were able to save the file on OneDrive
        final TextView overallResult = (TextView) findViewById(R.id.overall_result);
        final TextView errorResult = (TextView) findViewById(R.id.error_type_result);
        final TextView debugErrorResult = (TextView) findViewById(R.id.debug_error_result);

        try {
            mSaver.handleSave(requestCode, resultCode, data);
            overallResult.setText(getString(R.string.overall_result_success));
            errorResult.setText(getString(R.string.error_message_none));
            debugErrorResult.setText(getString(R.string.error_message_none));
        } catch (final SaverException e) {
            overallResult.setText(getString(R.string.overall_result_failure));
            errorResult.setText(e.getErrorType().toString());
            debugErrorResult.setText(e.getDebugErrorInfo());
        }
        findViewById(R.id.result_table).setVisibility(View.VISIBLE);
    }

    /**
     * Creates an file on the SDCard
     * @param filename The name of the file to create
     * @param size The size in KB to make the file
     * @return The {@link File} object that was created
     */
    private File createExternalSdCardFile(final String filename, final int size) {
        final int bufferSize = 1024;
        final int alphabetRange = 'z' - 'a';
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory(), filename);
            final FileOutputStream fos = new FileOutputStream(file);

            // Create a 1 kb size buffer to use in writing the temp file
            byte[] buffer = new byte[bufferSize];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte)('a' + i % alphabetRange);
            }

            // Write out the file, 1 kb at a time
            for (int i = 0; i < size; i++) {
                fos.write(buffer, 0, buffer.length);
            }

            fos.close();
        } catch (final IOException e) {
            Toast.makeText(this, "Error when creating the file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return file;
    }
}
