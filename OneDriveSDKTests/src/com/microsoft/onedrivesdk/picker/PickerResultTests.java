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

import android.net.Uri;
import android.os.Bundle;
import android.test.AndroidTestCase;

import java.util.Map;

/**
 * All test cases for the {@link PickerResult}
 * 
 * @author pnied
 */
public class PickerResultTests extends AndroidTestCase {

    /**
     * Default bundle for use in the test cases
     */
    private final Bundle mBundle = new Bundle();

    /**
     * Configures the default bundle for all test cases
     */
    @Override
    protected void setUp() throws Exception {
        mBundle.putParcelable("link", Uri.parse("file://C:/valid.link"));
        mBundle.putString("linkType", LinkType.DownloadLink.toString());
    }

    /**
     * Makes sure that the {@link PickerResult#getName()} method retrieves the
     * name from the response bundle.
     */
    public void testName() {
        // Setup
        final String name = "this is a great file name";
        final String extension = ".txt";
        final String expected = name + extension;
        mBundle.putString("name", name);
        mBundle.putString("extension", extension);
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final String actual = result.getName();

        // Verify
        assertEquals(expected, actual);
    }

    /**
     * Makes sure that the {@link PickerResult#getName()} method retrieves the
     * name from the response and makes sure it does not include a null extension.
     */
    public void testNameWithNullExtension() {
        // Setup
        final String name = "this is a great file name";
        final String extension = null;
        final String expected = name;
        mBundle.putString("name", name);
        mBundle.putString("extension", extension);
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final String actual = result.getName();

        // Verify
        assertEquals(expected, actual);
    }

    /**
     * Makes sure that the {@link PickerResult#getLink()} method retrieves the
     * link from the response bundle.
     */
    public void testLink() {
        // Setup
        final Uri expected = Uri.parse("file://C:/foo.bar");
        mBundle.putParcelable("link", expected);
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final Uri actual = result.getLink();

        // Verify
        assertEquals(expected, actual);
    }

    /**
     * Makes sure that the {@link PickerResult#getLinkType()} method retrieves
     * the link type {@link LinkType#DownloadLink} from the response bundle.
     */
    public void testLinkTypeDownload() {
        // Setup
        final LinkType expected = LinkType.DownloadLink;
        mBundle.putString("linkType", expected.toString());
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final LinkType actual = result.getLinkType();

        // Verify
        assertEquals(expected, actual);
    }

    /**
     * Makes sure that the {@link PickerResult#getLinkType()} method retrieves
     * the link type {@link LinkType#WebViewLink} from the response bundle.
     */
    public void testLinkTypeWebView() {
        // Setup
        final LinkType expected = LinkType.WebViewLink;
        mBundle.putString("linkType", expected.toString());
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final LinkType actual = result.getLinkType();

        // Verify
        assertEquals(expected, actual);
    }

    /**
     * Makes sure that the {@link PickerResult#getLinkType()} method handles an
     * invalid link type from the response bundle.
     */
    public void testInvalidLinkTypeFound() {
        // Setup
        mBundle.putString("linkType", "none");

        // Act
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Verify
        assertNull(result);
    }

    /**
     * Makes sure that the {@link PickerResult#getSize()} method retrieves the
     * size from the response bundle.
     */
    public void testSize() {
        // Setup
        final long expected = 4321L;
        mBundle.putLong("size", expected);
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final long actual = result.getSize();

        // Verify
        assertEquals(expected, actual);
    }

    /**
     * Makes sure that the {@link PickerResult#getThumbnailLinks()} method
     * retrieves the no thumbnails from the response bundle when none were
     * provided.
     */
    public void testNoThumbnailsReturned() {
        // Setup
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final Map<String, Uri> thumbnails = result.getThumbnailLinks();

        // Verify
        assertTrue(thumbnails.isEmpty());
    }

    /**
     * Makes sure that the {@link PickerResult#getThumbnailLinks()} method
     * retrieves the thumbnails from the response bundle.
     */
    public void testThumbnails() {
        // Setup
        final Bundle thumbnailsBundle = new Bundle();
        final String thumbnail1Name = "small";
        final Uri thumbnail1Uri = Uri.parse("http://foo.bar.jpg");
        thumbnailsBundle.putParcelable(thumbnail1Name, thumbnail1Uri);
        mBundle.putBundle("thumbnails", thumbnailsBundle);
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Act
        final Map<String, Uri> thumbnails = result.getThumbnailLinks();

        // Verify
        assertEquals(1, thumbnails.size());
        assertEquals(thumbnail1Uri, thumbnails.get(thumbnail1Name));
    }

    /**
     * Makes sure that the {@link PickerResult#fromBundle(Bundle)} method
     * returns a null picker result if the link was not found.
     */
    public void testNoLinkFound() {
        // Setup
        mBundle.remove("link");

        // Act
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Verify
        assertNull(result);
    }

    /**
     * Makes sure that the {@link PickerResult#fromBundle(Bundle)} method
     * returns a null picker result if the link type was not found.
     */
    public void testNoLinkTypeFound() {
        // Setup
        mBundle.remove("linkType");

        // Act
        final IPickerResult result = PickerResult.fromBundle(mBundle);

        // Verify
        assertNull(result);
    }
}
