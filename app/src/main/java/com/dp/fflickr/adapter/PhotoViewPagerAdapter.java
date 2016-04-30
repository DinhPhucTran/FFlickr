package com.dp.fflickr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.fflickr.R;
import com.dp.fflickr.activity.CommentsViewActivity;
import com.dp.fflickr.activity.PhotoViewActivity;
import com.googlecode.flickrjandroid.photos.Photo;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by DinhPhuc on 24/04/2016.
 */
public class PhotoViewPagerAdapter extends PagerAdapter {

    public static String PHOTO_ID = "photoViewPagerAdapter.photoId";
    public static String PHOTO_POSITION = "photoViewPagerAdapter.photoPosition";

    private static List<Photo> mPhotos;
    private LayoutInflater mInflater;
    private Context mContext;
    private PhotoViewAttacher mAttacher;
    private static boolean mIsShowingUi = true;
    private static int mPosition = 0;

    public PhotoViewPagerAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final View photoViewLayout = mInflater.inflate(R.layout.layout_photo_view, null);
        final ImageView imageView = (ImageView)photoViewLayout.findViewById(R.id.photoViewImage);
        final ProgressBar progress = (ProgressBar)photoViewLayout.findViewById(R.id.photoViewProgress);
        mPosition = position;

        Picasso.with(mContext).load(mPhotos.get(position).getLargeUrl()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                mAttacher = new PhotoViewAttacher(imageView);
                mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float v, float v1) {

                        if(mIsShowingUi) {
                            PhotoViewActivity.hideToolbar();
                            mIsShowingUi = false;
                        } else {
                            PhotoViewActivity.showToolbar();
                            mIsShowingUi = true;
                        }

                        if(PhotoViewActivity.getIsSlideshow()) {
                            PhotoViewActivity.stopSlideshow();
                            PhotoViewActivity.setIsSlideshow(false);
                            Toast.makeText(mContext, "Stop slideshow", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onOutsidePhotoTap() {
                        if(mIsShowingUi) {
                            PhotoViewActivity.hideToolbar();
                            mIsShowingUi = false;
                        } else {
                            PhotoViewActivity.showToolbar();
                            mIsShowingUi = true;
                        }

                        if(PhotoViewActivity.getIsSlideshow()) {
                            PhotoViewActivity.stopSlideshow();
                            PhotoViewActivity.setIsSlideshow(false);
                            Toast.makeText(mContext, "Stop slideshow", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });

        view.addView(photoViewLayout, 0);
        return photoViewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public static void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public static void addPhotos(List<Photo> photos) {
        mPhotos.addAll(photos);
    }

    public static List<Photo> getPhotos() {return mPhotos; }

    public static void setIsShowingUi(boolean isShowingUi) {
        mIsShowingUi = isShowingUi;
    }

    public static int getCurrentPosition() {
        if(mPosition > 0)
            return mPosition - 1;
        return 0;
    }
}
