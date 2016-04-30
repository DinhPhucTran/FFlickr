package com.dp.fflickr.common;

/**
 * Created by DinhPhuc on 23/04/2016.
 */
public class FlickrItem {
    private String mUserName;
    private String mCaption;
    private String mImageUrl;
    private String mThumbnailUrl;

    public FlickrItem(String mUserName, String mCaption, String thumbnailUrl, String mImageUrl) {
        this.mUserName = mUserName;
        this.mCaption = mCaption;
        this.mImageUrl = mImageUrl;
        this.mThumbnailUrl = thumbnailUrl;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getCaption() {
        return mCaption;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
