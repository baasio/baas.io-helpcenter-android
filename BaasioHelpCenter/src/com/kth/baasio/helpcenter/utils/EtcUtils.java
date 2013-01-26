/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kth.baasio.helpcenter.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * An assortment of UI helpers.
 */
public class EtcUtils {
    public static final String GOOGLE_PLUS_PACKAGE_NAME = "com.google.android.apps.plus";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setActivatedCompat(View view, boolean activated) {
        if (hasHoneycomb()) {
            view.setActivated(activated);
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static String getDateString(long millis) {
        String time = null;

        if (Locale.getDefault().equals(Locale.KOREA) || Locale.getDefault().equals(Locale.KOREAN)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd a hh:mm",
                    Locale.getDefault());
            time = formatter.format(new Date(millis));
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd a hh:mm",
                    Locale.getDefault());
            time = formatter.format(new Date(millis));
        }

        return time;
    }

    public static String getSimpleDateString(long millis) {
        String time = null;

        if (Locale.getDefault().equals(Locale.KOREA) || Locale.getDefault().equals(Locale.KOREAN)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 M월 d일 a h시 mm분",
                    Locale.getDefault());
            time = formatter.format(new Date(millis));
        } else {
            time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT,
                    Locale.getDefault()).format(new Date(millis));
        }

        return time;
    }

}
