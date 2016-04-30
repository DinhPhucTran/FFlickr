package com.dp.fflickr.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.dp.fflickr.common.Events;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by DinhPhuc on 29/04/2016.
 */
public class DownloadPhotoTask extends AsyncTask<Void, Void, Bitmap> {

    public static final String TAG = "ff/downloadTask";

    private Events.IPhotoDownloadCompleteListener mListener;
    private Context mContext;
    private Exception mException;
    private String mUrl;

    public DownloadPhotoTask(Context context, Events.IPhotoDownloadCompleteListener listener, String url) {
        mContext = context;
        mListener = listener;
        mUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.with(mContext).load(mUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            mException = e;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(final Bitmap result) {
        mListener.onPhotoDownloadComplete(result, mException);
    }
}
