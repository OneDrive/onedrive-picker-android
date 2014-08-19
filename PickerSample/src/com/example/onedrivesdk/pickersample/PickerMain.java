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

package com.example.onedrivesdk.pickersample;

import com.microsoft.onedrivesdk.picker.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import java.io.InputStream;
import java.net.URL;

/**
 * Activity that shows how the OneDrive SDK can be used for file picking
 * 
 * @author pnied
 */
public class PickerMain extends Activity {

    /**
     * Registered Application id for OneDrive {@link http
     * ://go.microsoft.com/fwlink/p/?LinkId=193157}
     */
    private static final String ONEDRIVE_APP_ID = "48122D4E";

    /**
     * The onClickListener that will start the OneDrive Picker
     */
    private final OnClickListener mStartPickingListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            // Clear out any previous results
            clearResultTable();

            // Determine the link type that was selected
            LinkType linkType;
            if (((RadioButton)findViewById(R.id.radioWebViewLink)).isChecked()) {
                linkType = LinkType.WebViewLink;
            } else if (((RadioButton)findViewById(R.id.radioDownloadLink)).isChecked()) {
                linkType = LinkType.DownloadLink;
            } else {
                throw new RuntimeException("Invalid Radio Button Choosen.");
            }

            // Start the picker
            mPicker.startPicking((Activity)v.getContext(), linkType);
        }
    };

    /**
     * The OneDrive picker instance used by this activity
     */
    private IPicker mPicker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_main);

        // Create the picker instance
        mPicker = Picker.createPicker(ONEDRIVE_APP_ID);

        // Add the start picker listener
        ((Button)findViewById(R.id.startPickerButton)).setOnClickListener(mStartPickingListener);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // Get the results from the from the picker
        final IPickerResult result = mPicker.getPickerResult(requestCode, resultCode, data);

        // Handle the case if nothing was picked
        if (result == null) {
            Toast.makeText(this, "Did not get a file from the picker!", Toast.LENGTH_LONG).show();
            return;
        }

        // Update the UI with the picker results
        updateResultTable(result);
    }

    /**
     * Updates the results table with details from an {@link IPickerResult}
     * 
     * @param result The results of the picker
     */
    private void updateResultTable(final IPickerResult result) {
        ((TextView)findViewById(R.id.nameResult)).setText(result.getName());
        ((TextView)findViewById(R.id.linkTypeResult)).setText(result.getLinkType() + "");
        ((TextView)findViewById(R.id.linkResult)).setText(result.getLink() + "");
        ((TextView)findViewById(R.id.fileSizeResult)).setText(result.getSize() + "");

        final Uri thumbnailSmall = result.getThumbnailLinks().get("small");
        createUpdateThumbnail((ImageView)findViewById(R.id.thumbnail_small), thumbnailSmall).execute((Void)null);
        ((TextView)findViewById(R.id.thumbnail_small_uri)).setText(thumbnailSmall + "");

        final Uri thumbnailMedium = result.getThumbnailLinks().get("medium");
        createUpdateThumbnail((ImageView)findViewById(R.id.thumbnail_medium), thumbnailMedium).execute((Void)null);
        ((TextView)findViewById(R.id.thumbnail_medium_uri)).setText(thumbnailMedium + "");

        final Uri thumbnailLarge = result.getThumbnailLinks().get("large");
        createUpdateThumbnail((ImageView)findViewById(R.id.thumbnail_large), thumbnailLarge).execute((Void)null);
        ((TextView)findViewById(R.id.thumbnail_large_uri)).setText(thumbnailLarge + "");

        findViewById(R.id.thumbnails).setVisibility(View.VISIBLE);
    }

    /**
     * Clears out all picker results
     */
    private void clearResultTable() {
        ((TextView)findViewById(R.id.nameResult)).setText("");
        ((TextView)findViewById(R.id.linkTypeResult)).setText("");
        ((TextView)findViewById(R.id.linkResult)).setText("");
        ((TextView)findViewById(R.id.fileSizeResult)).setText("");
        findViewById(R.id.thumbnails).setVisibility(View.INVISIBLE);
        ((ImageView)findViewById(R.id.thumbnail_small)).setImageBitmap(null);
        ((TextView)findViewById(R.id.thumbnail_small_uri)).setText("");
        ((ImageView)findViewById(R.id.thumbnail_medium)).setImageBitmap(null);
        ((TextView)findViewById(R.id.thumbnail_medium_uri)).setText("");
        ((ImageView)findViewById(R.id.thumbnail_large)).setImageBitmap(null);
        ((TextView)findViewById(R.id.thumbnail_large_uri)).setText("");
    }

    /**
     * Download the thumbnails for display
     * 
     * @param uri The uri of the bitmap to retrieve from OneDrive
     * @return The image as a bitmap
     */
    private Bitmap getBitmap(final Uri uri) {
        try {
            if (uri == null) {
                return null;
            }

            final URL url = new URL(uri.toString());
            final InputStream inputStream = url.openConnection().getInputStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * Creates a task that will update a thumbnail
     * 
     * @param imageView The image view that should be updated
     * @param imageSource The uri of the image that should be put on the image
     *            view
     * @return The task that will perform this update
     */
    private AsyncTask<Void, Void, Bitmap> createUpdateThumbnail(final ImageView imageView, final Uri imageSource) {
        return new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(final Void... params) {
                return getBitmap(imageSource);
            }

            @Override
            protected void onPostExecute(final Bitmap result) {
                imageView.setImageBitmap(result);
            }
        };
    }
}
