package com.dp.fflickr.task;

import android.os.AsyncTask;

import com.dp.fflickr.common.Constants;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by DinhPhuc on 01/05/2016.
 */
public class SearchPhotoTask extends AsyncTask<Void, Void, List<Photo>> {

    private Events.IPhotosReadyListener mListener;
    private String mSearchQuery;
    private int mPage;
    private Exception mException;

    public SearchPhotoTask(Events.IPhotosReadyListener listener, String searchQuery, int page) {
        mListener = listener;
        mSearchQuery = searchQuery;
        mPage = page;
    }

    @Override
    protected List<Photo> doInBackground(Void... params) {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setExtras(Constants.EXTRAS);
        searchParameters.setText(mSearchQuery);
        searchParameters.setSort(SearchParameters.RELEVANCE);
        try {
            return FlickrHelper.getInstance().getFlickr().getPhotosInterface().search(searchParameters, Constants.PHOTOS_PER_PAGE, mPage);
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
    protected void onPostExecute(List<Photo> photos) {
        mListener.onPhotosReady(photos, mException);
    }
}
