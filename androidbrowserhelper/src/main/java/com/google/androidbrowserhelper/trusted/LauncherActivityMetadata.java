// Copyright 2019 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.androidbrowserhelper.trusted;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Parses and holds on to metadata parameters associated with {@link LauncherActivity}.
 */
public class LauncherActivityMetadata {

    /**
     * Url to launch in a Trusted Web Activity, unless other url provided in a VIEW intent.
     */
    private static final String METADATA_DEFAULT_URL =
            "android.support.customtabs.trusted.DEFAULT_URL";

    /**
     * Status bar color to use for Trusted Web Activity.
     */
    private static final String METADATA_STATUS_BAR_COLOR_ID =
            "android.support.customtabs.trusted.STATUS_BAR_COLOR";

    /**
     * Status bar color to use for Trusted Web Activity, when in Dark Mode.
     */
    private static final String METADATA_STATUS_BAR_COLOR_DARK_ID =
            "android.support.customtabs.trusted.STATUS_BAR_COLOR_DARK";

    /**
     * Navigation bar color to use for Trusted Web Activity (note: in Chrome this is supported
     * from version 76).
     */
    private static final String METADATA_NAVIGATION_BAR_COLOR_ID =
            "android.support.customtabs.trusted.NAVIGATION_BAR_COLOR";

    /**
     * Navigation bar color to use for Trusted Web Activity, when in Dark Mode (note: in Chrome this
     * is supported from version 76).
     */
    private static final String METADATA_NAVIGATION_BAR_COLOR_DARK_ID =
            "android.support.customtabs.trusted.NAVIGATION_BAR_COLOR_DARK";

    /**
     * Id of the Drawable to use as a splash screen.
     */
    private static final String METADATA_SPLASH_IMAGE_DRAWABLE_ID =
            "android.support.customtabs.trusted.SPLASH_IMAGE_DRAWABLE";

    /**
     * Background color of the splash screen (will be used only if
     * {@link #METADATA_SPLASH_IMAGE_DRAWABLE_ID} is provided).
     */
    private static final String METADATA_SPLASH_SCREEN_BACKGROUND_COLOR =
            "android.support.customtabs.trusted.SPLASH_SCREEN_BACKGROUND_COLOR";

    /**
     * The duration of fade out animation in milliseconds to be played when removing splash
     * screen.
     */
    private static final String METADATA_SPLASH_SCREEN_FADE_OUT_DURATION =
            "android.support.customtabs.trusted.SPLASH_SCREEN_FADE_OUT_DURATION";

    /**
     * Authority of FileProvider used to share files (e.g. splash image) with the browser
     */
    private static final String METADATA_FILE_PROVIDER_AUTHORITY =
            "android.support.customtabs.trusted.FILE_PROVIDER_AUTHORITY";

    /**
     * Reference to a string resource with the web share target JSON. See description of
     * {@link LauncherActivity} for more details.
     */
    private static final String METADATA_SHARE_TARGET =
            "android.support.customtabs.trusted.METADATA_SHARE_TARGET";

    /**
     * The domains to be validated, as part of the Digital Asset Links validation
     */
    private static final String METADATA_ADDITIONAL_TRUSTED_ORIGINS =
            "android.support.customtabs.trusted.ADDITIONAL_TRUSTED_ORIGINS";

    private final static int DEFAULT_COLOR_ID = android.R.color.white;

    @Nullable public final String defaultUrl;
    public final int statusBarColorId;
    public final int statusBarColorDarkId;
    public final int navigationBarColorId;
    public final int navigationBarColorDarkId;
    public final int splashImageDrawableId;
    public final int splashScreenBackgroundColorId;
    @Nullable public final String fileProviderAuthority;
    public final int splashScreenFadeOutDurationMillis;
    @Nullable public final List<String> additionalTrustedOrigins;
    @Nullable public final String shareTarget;

    private LauncherActivityMetadata(@NonNull Bundle metaData, @NonNull Resources resources) {
        defaultUrl = metaData.getString(METADATA_DEFAULT_URL);
        statusBarColorId = metaData.getInt(METADATA_STATUS_BAR_COLOR_ID, DEFAULT_COLOR_ID);
        statusBarColorDarkId = metaData.getInt(METADATA_STATUS_BAR_COLOR_DARK_ID, statusBarColorId);
        navigationBarColorId = metaData.getInt(METADATA_NAVIGATION_BAR_COLOR_ID, DEFAULT_COLOR_ID);
        navigationBarColorDarkId =
                metaData.getInt(METADATA_NAVIGATION_BAR_COLOR_DARK_ID, navigationBarColorId);
        splashImageDrawableId = metaData.getInt(METADATA_SPLASH_IMAGE_DRAWABLE_ID, 0);
        splashScreenBackgroundColorId = metaData.getInt(METADATA_SPLASH_SCREEN_BACKGROUND_COLOR,
                DEFAULT_COLOR_ID);
        fileProviderAuthority = metaData.getString(METADATA_FILE_PROVIDER_AUTHORITY);
        splashScreenFadeOutDurationMillis =
                metaData.getInt(METADATA_SPLASH_SCREEN_FADE_OUT_DURATION, 0);
        if (metaData.containsKey(METADATA_ADDITIONAL_TRUSTED_ORIGINS)) {
            int additionalTrustedOriginsResourceId
                    = metaData.getInt(METADATA_ADDITIONAL_TRUSTED_ORIGINS);
            additionalTrustedOrigins =
                    Arrays.asList(resources.getStringArray(additionalTrustedOriginsResourceId));
        } else {
            additionalTrustedOrigins = null;
        }
        int shareTargetId = metaData.getInt(METADATA_SHARE_TARGET, 0);
        shareTarget = shareTargetId == 0 ? null : resources.getString(shareTargetId);
    }

    /**
     * Creates LauncherActivityMetadata instance based on metadata of the passed Activity.
     */
    public static LauncherActivityMetadata parse(Activity activity) {
        Resources resources = activity.getResources();
        Bundle metaData = null;
        try {
            metaData = activity.getPackageManager().getActivityInfo(
                    new ComponentName(activity, activity.getClass()),
                    PackageManager.GET_META_DATA).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            // Will only happen if the package provided (the one we are running in) is not
            // installed - so should never happen.
        }
        return new LauncherActivityMetadata(metaData == null ? new Bundle() : metaData, resources);
    }
}
