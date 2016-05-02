package com.dp.fflickr.task;

import android.os.AsyncTask;

import com.dp.fflickr.common.Constants;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by DinhPhuc on 29/04/2016.
 */
public class LoadRecentPublicPhotosTask extends AsyncTask<Void, Void, List<Photo>> {

    public static final String TAG = "ff/recentsTask";
    private Events.IPhotosReadyListener mListener;
    private int mPage;
    private Exception mException;

    public LoadRecentPublicPhotosTask(Events.IPhotosReadyListener listener, int page) {
        mListener = listener;
        mPage = page;
    }

    @Override
    protected List<Photo> doInBackground(Void... params) {
        try {
            return FlickrHelper.getInstance().getFlickr().getPhotosInterface().getRecent(Constants.EXTRAS, Constants.PHOTOS_PER_PAGE, mPage);
        } catch (IOException e) {
            e.printStackTrace();
            mException = e;
        } catch (FlickrException e) {
            e.printStackTrace();
            mException = e;
        } catch (JSONException e) {
            e.printStackTrace();
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(final List<Photo> result) {
        mListener.onPhotosReady(result, mException);
    }
}
