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
import android.text.TextUtils;

import java.util.*;

/**
 * A collection of information about an object that was picked with an
 * {@link IPicker}
 * 
 * @author pnied
 */
public final class PickerResult implements IPickerResult {

    /**
     * The raw results information
     */
    private Bundle mData;

    /**
     * Default Constructor
     * 
     * @param data The backing data store for this picker result
     */
    private PickerResult(final Bundle data) {
        mData = data;
    }

    /**
     * Creates a picker result from a {@link Bundle}
     * 
     * @param data The response from {@link IPicker}
     * @return An instance of {@link IPickerResult}
     */
    public static IPickerResult fromBundle(final Bundle data) {
        final PickerResult result = new PickerResult(data);
        if (result == null || result.getLinkType() == null || result.getLink() == null) {
            return null;
        }
        return result;
    }

    /*
     * Returns the name of the file picked from OneDrive
     * 
     * @return the name of the file picked from OneDrive
     */
    @Override
    public String getName() {
        final String extension = mData.getString("extension");
        return mData.getString("name") + (TextUtils.isEmpty(extension) ? "" : extension);
    }

    /**
     * Returns the size in bytes of the file picked from OneDrive
     * 
     * @return the size in bytes of the file picked from OneDrive
     */
    @Override
    public long getSize() {
        return mData.getLong("size", -1);
    }

    /**
     * Returns the link type {@link LinkType} that was requested for the picked file
     * 
     * @return the link type {@link LinkType} that was requested for the picked file
     */
    @Override
    public LinkType getLinkType() {
        final String linkType = mData.getString("linkType", "none");
        try {
            return Enum.valueOf(LinkType.class, linkType);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns a link to the requested file, based on the request {@link LinkType}.
     * 
     * @return a link to the requested file
     */
    @Override
    public Uri getLink() {
        return mData.getParcelable("link");
    }

    /**
     * Returns the map of all available thumbnails with corresponding links from OneDrive. 
     * Will be empty if no thumbnails are available. Supported sizes are: Small, Medium,and Large
     * 
     * @return The map of all available thumbnails with corresponding links from OneDrive
     */
    @Override
    public Map<String, Uri> getThumbnailLinks() {
        final Bundle thumbnails = mData.getParcelable("thumbnails");
        final Map<String, Uri> map = new HashMap<String, Uri>();

        if (thumbnails != null) {
            for (final String key : thumbnails.keySet()) {
                final Uri uri = thumbnails.getParcelable(key);
                if (uri != null) {
                    map.put(key, uri);
                }
            }
        }

        return map;
    }
}
