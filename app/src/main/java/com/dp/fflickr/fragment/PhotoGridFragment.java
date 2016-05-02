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
import com.googlecode.flickrjandroid.photos.Photo;

import java.util.List;

/**
 * Created by DinhPhuc on 02/05/2016.
 */
public class PhotoGridFragment extends Fragment {
    public static final String TAG = "ff/exploreFragment";

    protected RecyclerView mRecyclerView;
    protected PhotoAdapter mPhotoAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected int mPage;
    private boolean loading = true;
    private int firstVisiblesItems, visibleItemCount, totalItemCount, previousTotal, visibleThreshold;
    private GridLayoutManager mGridLayoutManager;
    private View mLayout;


    public PhotoGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_explore, container, false);
        int columnSpan;
        if (Utils.isDeviceBiggerThan(getActivity().getWindow(), 6)){
            columnSpan = 2;
        }else{
            columnSpan = 1;
        }
        mPage = 1;

        mGridLayoutManager = new GridLayoutManager(getContext(), columnSpan);

        mPhotoAdapter = new PhotoAdapter(getContext());
        mRecyclerView = (RecyclerView)mLayout.findViewById(R.id.exploreRecyclerView);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        loading = true;
        previousTotal = 0;
        visibleThreshold = 6;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();
                totalItemCount = mGridLayoutManager.getItemCount();
                visibleItemCount = mGridLayoutManager.getChildCount();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if(!loading && firstVisiblesItems + visibleItemCount + visibleThreshold >= totalItemCount){
                    startTask(getContext());
                    loading = true;
                    Toast.makeText(getContext(), "" + mPhotoAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout)mLayout.findViewById(R.id.exploreSwipeRefresh);

        //new LoadInterestingPhotosTask(this, mPage).execute();
        startTask(getContext());
        mRecyclerView.setAdapter(mPhotoAdapter);
        PhotoViewPagerAdapter.setPhotos(mPhotoAdapter.getDataSet());
        CommentsViewActivity.setPhotos(mPhotoAdapter.getDataSet());
        mSwipeRefreshLayout.setRefreshing(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //new LoadInterestingPhotosTask(MainActivity.this, mPage).execute();
                mPage = 1;
                startTask(getContext());
            }
        });

        return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPhotoAdapter != null) {
            PhotoViewPagerAdapter.setPhotos(mPhotoAdapter.getDataSet());
            CommentsViewActivity.setPhotos(mPhotoAdapter.getDataSet());
        }
    }

    public void startTask(final Context context) {

    }

}
