package com.dp.fflickr.common;

import android.util.DisplayMetrics;
import android.view.Window;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by DinhPhuc on 28/04/2016.
 */
public class Utilities {
    public static Date getDateBefore(int days) {
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        date = cal.getTime();
        return date;
    }

    public static Date getDateAfter(int days) {
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        date = cal.getTime();
        return date;
    }

    public static boolean isDeviceBiggerThan(Window window, double inches) {
        DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);


        if(diagonalInches > inches)
            return true;
        else
            return false;
    }
}
