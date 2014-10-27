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
 * Exceptions raised by the saver while attempting to put a file onto OneDrive
 *
 * @author pnied
 */
public class SaverException extends Exception {

    /**
     * The serialized version for this exception
     */
    private static final long serialVersionUID = -2097471120614542027L;

    /**
     * The {@link SaverError} for this exception
     */
    private final SaverError mErrorType;

    /**
     * The debug error message used for development (can change at any time)
     */
    private final String mDebugErrorInfo;

    /**
     * Default Constructor
     *
     * @param errorType The errorType for this exception
     * @param debugErrorInfo The debug error message used for development (can change at any time)
     */
    SaverException(final String errorType, final String debugErrorInfo) {
        SaverError error;
        try {
            if (errorType != null) {
                error = Enum.valueOf(SaverError.class, errorType);
            } else {
                error = SaverError.Unknown;
            }
        } catch (final IllegalArgumentException e) {
            error = SaverError.Unknown;
        }
        mErrorType = error;
        mDebugErrorInfo = debugErrorInfo;
    }

    /**
     * Gets the specific error type for this exception
     *
     * @return the {@link SaverError} for this exception
     */
    public SaverError getErrorType() {
        return mErrorType;
    }

    /**
     * Gets the specific debug error message used for development (can change at any time)
     *
     * @return the string that represents the internal error from the OneDrive Application for debugging
     */
    public String getDebugErrorInfo() {
        return mDebugErrorInfo;
    }
}
