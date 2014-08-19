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

import java.util.Map;

/**
 * Interface for a class that represents the result of a file picked from
 * OneDrive for viewing or downloading.
 * 
 * @author pnied
 */
public interface IPickerResult {

    /**
     * Returns the file name of the file picked from OneDrive
     * 
     * @return the file name of the file picked from OneDrive
     */
    String getName();

    /**
     * Returns the size in bytes of the file picked from OneDrive
     * 
     * @return the size in bytes of the file picked from OneDrive
     */
    long getSize();

    /**
     * Returns the link type requested for the picked file
     * 
     * @return the link type requested for the picked file
     */
     LinkType getLinkType();

    /**
     * Returns a link to the requested file 
     * 
     * @return a link to the requested file
     */
    Uri getLink();

    /**
     * Returns the map of all available thumbnails with corresponding links from OneDrive.
     * Supported sizes are: Small, Medium, and Large
     * 
     * @return the map of all available thumbnails with corresponding links from OneDrive
     */
    Map<String, Uri> getThumbnailLinks();

}
