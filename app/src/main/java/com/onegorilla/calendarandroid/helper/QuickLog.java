package com.onegorilla.calendarandroid.helper;

import android.util.Log;

public class QuickLog {

    private static final String tag = "TAG";

    public static void qLog(Object o) {
        if (o == null) {
            return;
        }
        Log.d(tag, o.toString());
    }

    public static void qLog(String tag, Object o) {
        if (o == null) {
            return;
        }
        Log.d(tag, o.toString());
    }
}
