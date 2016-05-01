package com.dp.fflickr.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.fflickr.R;
import com.dp.fflickr.adapter.PhotoAdapter;
import com.dp.fflickr.adapter.PhotoViewPagerAdapter;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.dp.fflickr.common.Utils;
import com.dp.fflickr.task.LoadInterestingPhotosTask;
import com.dp.fflickr.task.SearchPhotoTask;
import com.googlecode.flickrjandroid.photos.Photo;

import java.util.List;

public class SearchActivity extends PhotoGridActivity {

    public static final String SEARCH = "fflickr.Search";

    private static RecyclerView mRecyclerView;
    private static PhotoAdapter mPhotoAdapter;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    private AppBarLayout appBar;
    private static int mPage;
    private boolean loading = true;
    private int firstVisiblesItems, visibleItemCount, totalItemCount, previousTotal, visibleThreshold;
    private GridLayoutManager mGridLayoutManager;
    private String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleIntent(getIntent());
        mPage = 1;

        appBar = (AppBarLayout)findViewById(R.id.mainActivityAppBar);
        //if(Build.VERSION.SDK_INT >= 19) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)toolbar.getLayoutParams();
        if(params != null)
            params.topMargin = 24;
        appBar.getLayoutParams().height += 24;
        //}

        //check if device is phone or tablet
        int columnSpan;
        if (Utils.isDeviceBiggerThan(getWindow(), 6)){
            columnSpan = 2;
        }else{
            columnSpan = 1;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mGridLayoutManager = new GridLayoutManager(this, columnSpan);

        mPhotoAdapter = new PhotoAdapter(getApplicationContext());
        mRecyclerView = (RecyclerView)findViewById(R.id.mainRecyclerView);
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
                    startTask(getApplicationContext());
                    loading = true;
                    Toast.makeText(getApplicationContext(), "" + mPhotoAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.mainSwipeRefresh);

        //new LoadInterestingPhotosTask(this, mPage).execute();
        startTask(this);
        mRecyclerView.setAdapter(mPhotoAdapter);
        PhotoViewPagerAdapter.setPhotos(mPhotoAdapter.getDataSet());
        CommentsViewActivity.setPhotos(mPhotoAdapter.getDataSet());
        mSwipeRefreshLayout.setRefreshing(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //new LoadInterestingPhotosTask(MainActivity.this, mPage).execute();
                mPage = 1;
                startTask(getApplicationContext());
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
        //Toast.makeText(SearchActivity.this, "New search", Toast.LENGTH_SHORT).show();
        startNewSearchTask(getApplicationContext());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSearchQuery = query;
            setTitle(query);
        }
    }


    public void startTask(final Context context) {
        new SearchPhotoTask(new Events.IPhotosReadyListener() {
            @Override
            public void onPhotosReady(List<Photo> photos, Exception e) {
                if (FlickrHelper.getInstance().handleFlickrUnavailable(context, e)) {
                    return;
                }

                if(photos != null) {
                    mPhotoAdapter.addPhotos(photos);
                    int curSize = mPhotoAdapter.getItemCount();
                    mPhotoAdapter.notifyItemRangeInserted(curSize, photos.size() - 1);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        },mSearchQuery, mPage++).execute();
    }

    public void startNewSearchTask(final Context context) {
        mPage = 1;
        new SearchPhotoTask(new Events.IPhotosReadyListener() {
            @Override
            public void onPhotosReady(List<Photo> photos, Exception e) {
                if (FlickrHelper.getInstance().handleFlickrUnavailable(context, e)) {
                    return;
                }

                if(photos != null) {
                    mPhotoAdapter.setDataSet(photos);
                    mPhotoAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                Toast.makeText(SearchActivity.this, "Search for \"" + mSearchQuery + "\"", Toast.LENGTH_SHORT).show();
                PhotoViewPagerAdapter.setPhotos(photos);
                CommentsViewActivity.setPhotos(photos);
            }
        },mSearchQuery, mPage).execute();
    }

}
