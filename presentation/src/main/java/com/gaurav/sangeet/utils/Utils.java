package com.gaurav.sangeet.utils;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

public class Utils {
    @SuppressLint("DefaultLocale")
    public static String convertLongToDuration(long duration) {
        String res;
        if (duration < 60 * 60 * 1000) {
            res = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
        } else {
            res = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));

        }
        return res;
    }
}
