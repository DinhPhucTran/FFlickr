package com.dp.fflickr.task;

import android.os.AsyncTask;
import android.util.Log;

import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.dp.fflickr.common.Utils;
import com.googlecode.flickrjandroid.photos.comments.Comment;

import java.util.List;

/**
 * Created by DinhPhuc on 25/04/2016.
 */
public class LoadCommentsTask extends AsyncTask<Void, Void, List<Comment>>{

    public static final String TAG = "fflickr/LoadCmtTask";
    private String mPhotoId;
    private Events.ICommentsReadyListener mListener;
    private Exception mException;

    public LoadCommentsTask(Events.ICommentsReadyListener listener, String photoId) {
        mListener = listener;
        mPhotoId = photoId;
    }

    @Override
    protected List<Comment> doInBackground(Void... params) {
        try {
            return FlickrHelper.getInstance().getFlickr().getCommentsInterface()
                    .getList(mPhotoId, Utils.getDateBefore(2), null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(final List<Comment> result) {
        mListener.onCommentsReady(result, mException);
    }
}
