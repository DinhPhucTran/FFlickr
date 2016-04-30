package com.dp.fflickr.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DinhPhuc on 28/04/2016.
 */
public class Utils {
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

        return (diagonalInches > inches);
    }

    public static Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
