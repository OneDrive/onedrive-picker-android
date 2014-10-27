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

package com.microsoft.onedrivesdk.common;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.*;
import android.content.res.Resources;
import android.test.mock.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test activity that allows for checking of {@link Activity#startActivity} and {@link Activity#startActivityForResult}
 * 
 * @author pnied
 */
public class TestActivity extends Activity {

    /**
     * Test action for QueryIntentActivities
     * @author pnied
     *
     */
    public interface QueryIntentActivitiesAction {
        List<ResolveInfo> action(final Intent intent, final int flags);
    }

    /**
     * Test action for StartActivityForResult
     * 
     * @author pnied
     */
    public interface StartActivityForResultAction {
        void action(final Intent intent, final int requestCode);
    }

    /**
     * Test action for StartActivity
     * 
     * @author pnied
     */
    public interface StartActivityAction {
        void action(final Intent intent);
    }

    /**
     * Action to be invoked when {@link MockPackageManager} returned by
     * {@link Activity#getPackageManager()} has its
     * {@link PackageManager#queryIntentActivities(Intent, int)} method is
     * called.
     */
    public QueryIntentActivitiesAction mQueryIntentActivitiesAction = null;

    /**
     * Action to be invoked when
     * {@link Activity#startActivityForResult(Intent, int)} is called
     */
    public StartActivityForResultAction mStartActivityForResultAction = null;

    /**
     * Action to be invoked when {@link Activity#startActivity(Intent)} is
     * called
     */
    public StartActivityAction mStartActivityAction = null;

    /**
     * The number of times the
     * {@link TestActivity#startActivityForResult(Intent, int) method
     * was called.
     */
    public final AtomicInteger mStartActivityForResultCallCount = new AtomicInteger(0);

    /**
     * The number of times the
     * {@link TestActivity#startActivity(Intent) method was called.
     */
    public final AtomicInteger mStartActivityCallCount = new AtomicInteger(0);

    @Override
    public PackageManager getPackageManager() {
        return new MockPackageManager() {
            @Override
            public List<ResolveInfo> queryIntentActivities(final Intent intent, final int flags) {
                return mQueryIntentActivitiesAction.action(intent, flags);
            }
        };
    }

    @Override
    public void startActivityForResult(final Intent intent, final int requestCode) {
        mStartActivityForResultAction.action(intent, requestCode);
        mStartActivityForResultCallCount.incrementAndGet();
    }

    @Override
    public void startActivity(final Intent intent) {
        mStartActivityAction.action(intent);
        mStartActivityCallCount.incrementAndGet();
    }

    @Override
    public Resources getResources() {
        return new MockResources();
    }
}
