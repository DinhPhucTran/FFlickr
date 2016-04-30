package com.dp.fflickr.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dp.fflickr.common.Constants;
import com.dp.fflickr.adapter.PhotoAdapter;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.dp.fflickr.activity.CommentsViewActivity;
import com.dp.fflickr.adapter.PhotoViewPagerAdapter;
import com.googlecode.flickrjandroid.photos.Photo;

import java.util.Date;
import java.util.List;

/**
 * Created by DinhPhuc on 24/04/2016.
 */
public class LoadInterestingPhotosTask extends AsyncTask<Void, Void, List<Photo>> {

    public static final String TAG = "fflickr/InterestingTask";
    private Events.IPhotosReadyListener mListener;
    private int mPage;
    private Exception mException;

    public LoadInterestingPhotosTask(Events.IPhotosReadyListener listener, int page) {
        mListener = listener;
        mPage = page;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Photo> doInBackground(Void... params) {
        Date day = null;
        try {
            //noinspection ConstantConditions
            return FlickrHelper.getInstance().getInterestingInterface()
                    .getList(day, Constants.EXTRAS, Constants.PHOTOS_PER_PAGE, mPage);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            mException = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(final List<Photo> result) {
        mListener.onPhotosReady(result, mException);
    }
}
