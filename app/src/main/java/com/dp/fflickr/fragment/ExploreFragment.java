package com.dp.fflickr.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.fflickr.R;
import com.dp.fflickr.activity.CommentsViewActivity;
import com.dp.fflickr.adapter.PhotoAdapter;
import com.dp.fflickr.adapter.PhotoViewPagerAdapter;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.dp.fflickr.common.Utils;
import com.dp.fflickr.task.LoadInterestingPhotosTask;
import com.googlecode.flickrjandroid.photos.Photo;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends PhotoGridFragment {

    public static final String TAG = "ff/exploreFragment";

    public void startTask(final Context context) {

        new LoadInterestingPhotosTask(new Events.IPhotosReadyListener() {
            @Override
            public void onPhotosReady(List<Photo> photos, Exception e) {
                if (FlickrHelper.getInstance().handleFlickrUnavailable(context, e)) {
                    return;
                }

                if (photos != null) {
                    mPhotoAdapter.addPhotos(photos);
                    int curSize = mPhotoAdapter.getItemCount();
                    mPhotoAdapter.notifyItemRangeInserted(curSize, photos.size() - 1);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, mPage++).execute();
    }

}
