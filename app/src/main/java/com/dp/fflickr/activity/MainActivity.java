package com.dp.fflickr.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.dp.fflickr.adapter.PhotoAdapter;
import com.dp.fflickr.adapter.PhotoViewPagerAdapter;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.FlickrHelper;
import com.dp.fflickr.common.Utils;
import com.dp.fflickr.task.LoadInterestingPhotosTask;
import com.dp.fflickr.R;
import com.googlecode.flickrjandroid.photos.Photo;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "fflickr/MainActivity";

    private static RecyclerView mRecyclerView;
    private static PhotoAdapter mPhotoAdapter;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    private AppBarLayout appBar;
    private static int mPage;
    private boolean loading = true;
    private int firstVisiblesItems, visibleItemCount, totalItemCount, previousTotal, visibleThreshold;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                startTask(getApplicationContext());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void startTask(final Context context) {

        new LoadInterestingPhotosTask(new Events.IPhotosReadyListener() {
            @Override
            public void onPhotosReady(List<Photo> photos, Exception e) {
                if (FlickrHelper.getInstance().handleFlickrUnavailable(context, e)) {
                    return;
                }

                mPhotoAdapter.addPhotos(photos);
                int curSize = mPhotoAdapter.getItemCount();
                mPhotoAdapter.notifyItemRangeInserted(curSize, photos.size() - 1);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, mPage++).execute();
    }
}
