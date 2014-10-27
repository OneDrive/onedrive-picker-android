package com.microsoft.onedrivesdk.common;

import android.content.Intent;
import android.test.AndroidTestCase;

/**
 * All test cases for the clients
 *
 * @author pnied
 */
public class ClientTests extends AndroidTestCase {

    /**
     * Validate that intent creation
     */
    public void testOneDriveIntentCreation() {
        // Setup
        final String expectedAction = "a.b.c.d.e";
        final String expectedAppId = "12341234";

        // Action
        final Intent result = Client.createOneDriveIntent(expectedAction, expectedAppId);

        // Verify
        assertEquals(expectedAction, result.getAction());
        assertEquals(expectedAppId, result.getStringExtra("appId"));
        assertEquals(2, result.getIntExtra("version", -1));
        assertEquals(1, result.getCategories().size());
        assertEquals(Intent.CATEGORY_DEFAULT, result.getCategories().iterator().next());
    }

    /**
     * Validate that android marketplace intent creation
     */
    public void testAndroidMarketPlaceIntent() {
        // Setup
        final String expectedAction = Intent.ACTION_VIEW;
        final String expectedData = "market://details?id=com.microsoft.skydrive";

        // Action
        final Intent result = Client.createAndroidMarketPlaceIntent();

        // Verify
        assertEquals(expectedAction, result.getAction());
        assertEquals(expectedData, result.getData().toString());
    }

    /**
     * Validate that amazon marketplace intent creation
     */
    public void testAmazonMarketPlaceIntent() {
        // Setup
        final String expectedAction = Intent.ACTION_VIEW;
        final String expectedData = "amzn://apps/android?p=com.microsoft.skydrive";

        // Action
        final Intent result = Client.createAmazonMarketPlaceIntent();

        // Verify
        assertEquals(expectedAction, result.getAction());
        assertEquals(expectedData, result.getData().toString());
    }
}
