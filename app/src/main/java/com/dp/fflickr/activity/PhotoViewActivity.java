package com.dp.fflickr.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dp.fflickr.adapter.PhotoAdapter;
import com.dp.fflickr.adapter.PhotoViewPagerAdapter;
import com.dp.fflickr.R;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.common.Utils;
import com.dp.fflickr.task.DownloadPhotoTask;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.Size;
import com.googlecode.flickrjandroid.util.UrlUtilities;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity {

    private int mPosition;
    private ViewPager viewPager;
    private PhotoViewPagerAdapter adapter;
    private Toolbar toolbar;
    private static AppBarLayout appBarLayout;
    private static Window window;
    private SlidingUpPanelLayout mSlidingUpPanel;
    private TextView mUserName;
    private TextView mTitle;
    private ImageView mUserProfileImage;
    private ImageView mCommentsIcon;
    private TextView mFavsCount;
    private TextView mDescription;
    private String mPhotoUrl;
    private String mPhotoTitle;
    private Intent mSetWallpaperIntent;
    private List<Photo> mPhotos;
    private List<Target> targets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarLayout = (AppBarLayout)findViewById(R.id.photoViewAppBar);
        mSlidingUpPanel = (SlidingUpPanelLayout)findViewById(R.id.photoViewSlidingPanel);
        mUserName = (TextView)findViewById(R.id.photoViewUser);
        mTitle = (TextView)findViewById(R.id.photoViewTitle);
        mUserProfileImage = (ImageView)findViewById(R.id.photoViewUSerProfileImage);
        mCommentsIcon = (ImageView)findViewById(R.id.photoViewIcComments);
        mFavsCount = (TextView)findViewById(R.id.photoViewHeartCount);
        mDescription = (TextView)findViewById(R.id.photoViewDescription);

        mSetWallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
        targets = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        window = getWindow();

        //force expand to fit window
        showToolbar();

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(PhotoAdapter.PHOTO_POSITION, 0);
        mPhotoTitle = intent.getStringExtra(PhotoAdapter.PHOTO_TITLE);

        setTitle(mPhotoTitle);

        adapter = new PhotoViewPagerAdapter(getApplicationContext());
        viewPager = (ViewPager)findViewById(R.id.photoViewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPosition);

        mPhotos = PhotoViewPagerAdapter.getPhotos();

        //mFavsCount.setText(photos.get(position).getFavorites() + "");
        mDescription.setText(Html.fromHtml(mPhotos.get(mPosition).getDescription()));
        mPhotoUrl = getPhotoUrl(mPhotos.get(mPosition));

        mTitle.setText(mPhotoTitle);
        mUserName.setText("by " + mPhotos.get(mPosition).getOwner().getUsername());
        mCommentsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsViewActivity.class);
                intent.putExtra(PhotoViewPagerAdapter.PHOTO_ID, mPhotos.get(mPosition).getId());
                intent.putExtra(PhotoViewPagerAdapter.PHOTO_POSITION, mPosition);
                startActivity(intent);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                String title = mPhotos.get(position).getTitle();
                setTitle(title);
                mUserName.setText("by " + mPhotos.get(position).getOwner().getUsername());
                mTitle.setText(title);
                //mFavsCount.setText(photos.get(position).getViews());
                mDescription.setText(Html.fromHtml(mPhotos.get(position).getDescription()));
                mPhotoUrl = getPhotoUrl(mPhotos.get(position));
                mPhotoTitle = mPhotos.get(position).getTitle();
                Toast.makeText(PhotoViewActivity.this, mPhotoUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //force sliding panel draw above navigation bar
        mSlidingUpPanel.setPanelHeight(120);
        mSlidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        View decorView = getWindow().getDecorView();
        //if(View.SYSTEM_UI_FLAG_FULLSCREEN == 1)
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    mSlidingUpPanel.setPanelHeight(120);
                    mSlidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    Toast.makeText(PhotoViewActivity.this, "showed", Toast.LENGTH_SHORT).show();
                    PhotoViewPagerAdapter.setIsShowingUi(true);
                    //if(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 1)

                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    appBarLayout.animate().translationY(-100).setInterpolator(new AccelerateInterpolator(2)).start();
                    PhotoViewPagerAdapter.setIsShowingUi(false);
                    Toast.makeText(PhotoViewActivity.this, "hidden", Toast.LENGTH_SHORT).show();
                    mSlidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download_photo) {
            downloadPhoto();
            return true;
        }

        if(id == R.id.action_set_photo_as) {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mSetWallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    mSetWallpaperIntent.setDataAndType(Utils.getLocalBitmapUri(bitmap, getApplicationContext()), "image/jpeg");
                    Toast.makeText(PhotoViewActivity.this, Utils.getLocalBitmapUri(bitmap, getApplicationContext()).toString(), Toast.LENGTH_SHORT).show();
                    mSetWallpaperIntent.putExtra("mimeType", "image/jpeg");
                    startActivity(Intent.createChooser(mSetWallpaperIntent, "Set as"));
                    targets.remove(this);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Toast.makeText(PhotoViewActivity.this, "Set failed", Toast.LENGTH_SHORT).show();
                    targets.remove(this);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(getApplicationContext()).load(mPhotos.get(mPosition).getLarge1600Url()).into(target);
            targets.add(target);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhotoViewPagerAdapter.setIsShowingUi(true);
    }

    public static void showToolbar() {
        appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        if (Build.VERSION.SDK_INT < 16) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = window.getDecorView();
            // show the status bar.
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static void hideToolbar(){
        appBarLayout.animate().translationY(-500).setInterpolator(new AccelerateInterpolator(2)).start();
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = window.getDecorView();
            // Hide the status bar.
            if(Build.VERSION.SDK_INT < 19) {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            } else {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
        }
    }

    private String getPhotoUrl(Photo photo) {
        String url;
        try {
            url = photo.getOriginalUrl();
        } catch (FlickrException e) {
            e.printStackTrace();
            url = photo.getLarge1600Url();
        }
        return url;
    }

    private File createPhotoFile(Bitmap bitmap, String fileName) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, fileName);
        try {
            if(!path.exists())
                path.mkdir();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();

            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{file.toString()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {

                }
            });
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void downloadPhoto() {
        Toast.makeText(PhotoViewActivity.this, "Start downloading..." + mPhotoUrl, Toast.LENGTH_SHORT).show();
        new DownloadPhotoTask(getApplicationContext(), new Events.IPhotoDownloadCompleteListener() {
            @Override
            public void onPhotoDownloadComplete(Bitmap bitmap, Exception e) {
                String fileName = mPhotoTitle + ".jpg";
                if(e==null && createPhotoFile(bitmap, fileName) != null) {
                    Toast.makeText(PhotoViewActivity.this, "Saved photo to Pictures", Toast.LENGTH_SHORT).show();
                } else {
                    String ex = "---";
                    if(e!=null)
                        ex = e.toString();
                    Toast.makeText(PhotoViewActivity.this, "Failed: " + ex, Toast.LENGTH_SHORT).show();
                }
            }
        }, mPhotoUrl).execute();
    }

    public static String getUerProfileImageUrl(int position) {
        User user = PhotoViewPagerAdapter.getPhotos().get(position).getOwner();
        String profileImageUrl = UrlUtilities.createBuddyIconUrl(user.getIconFarm(),
                user.getIconServer(),
                user.getId());
        return profileImageUrl;
    }

}
