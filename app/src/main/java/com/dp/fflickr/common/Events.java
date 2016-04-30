package com.dp.fflickr.common;

import android.graphics.Bitmap;

import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.comments.Comment;

import java.util.List;

/**
 * Created by DinhPhuc on 28/04/2016.
 */
public class Events {

    public interface IPhotosReadyListener {
        public void onPhotosReady(List<Photo> photos, Exception e);
    }

    public interface ICommentsReadyListener {
        public void onCommentsReady(List<Comment> comments, Exception e);
    }

    public interface IPhotoDownloadCompleteListener {
        public void onPhotoDownloadComplete(Bitmap bitmap, Exception e);
    }
}
