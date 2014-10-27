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

/**
 * All of the possible errors that could result in a {@link SaverException}
 * being thrown
 *
 * @author pnied
 */
public enum SaverError {
    /**
     * The error type was not known
     */
    Unknown,

    /**
     * The saver was cancelled by the user
     */
    Cancelled,

    /**
     * The OneDrive account did not have enough quota available to save the file
     */
    OutOfQuota,

    /**
     * The file could not be saved onto OneDrive because it contained
     * unsupported characters
     */
    InvalidFileName,

    /**
     * No network connectivity was available when attempting to save the file
     */
    NoNetworkConnectivity,

    /**
     * The Uri to the file could not be accessed
     */
    CouldNotAccessFile,

    /**
     * No file was specified to be saved, or the file URI scheme was not supported,
     * content:// and file:// are currently supported
     */
    NoFileSpecified
}
