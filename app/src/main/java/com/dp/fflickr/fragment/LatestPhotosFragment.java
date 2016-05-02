package com.dp.fflickr.fragment;

import android.content.Context;
import android.widget.Toast;

import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.dp.fflickr.task.LoadRecentPublicPhotosTask;
import com.googlecode.flickrjandroid.photos.Photo;

import java.util.List;

/**
 * Created by DinhPhuc on 02/05/2016.
 */
public class LatestPhotosFragment extends PhotoGridFragment {
    public void startTask(final Context context) {
        new LoadRecentPublicPhotosTask(new Events.IPhotosReadyListener() {
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
