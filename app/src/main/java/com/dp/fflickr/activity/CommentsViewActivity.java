package com.dp.fflickr.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.dp.fflickr.adapter.CommentAdapter;
import com.dp.fflickr.common.Events;
import com.dp.fflickr.task.LoadCommentsTask;
import com.dp.fflickr.adapter.PhotoViewPagerAdapter;
import com.dp.fflickr.R;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.comments.Comment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommentsViewActivity extends AppCompatActivity implements Events.ICommentsReadyListener{

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private CommentAdapter mAdapter;
    private String photoId;
    private static List<Photo> mPhotos;

    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImage = (ImageView)findViewById(R.id.commentViewImage);
        mProgressBar = (ProgressBar)findViewById(R.id.commentViewProgress);
        mRecyclerView = (RecyclerView)findViewById(R.id.commentViewRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentAdapter(this);

        Intent intent = getIntent();
        photoId = intent.getStringExtra(PhotoViewPagerAdapter.PHOTO_ID);
        int pos = intent.getIntExtra(PhotoViewPagerAdapter.PHOTO_POSITION, 0);
        Glide.with(this).load(mPhotos.get(pos).getMediumUrl()).into(mImage);

        new LoadCommentsTask(this, photoId).execute();
    }

    public static void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public static void addPhotos(List<Photo> photos) { mPhotos.addAll(photos); }

    @Override
    public void onCommentsReady(List<Comment> comments, Exception e) {
        Collections.sort(comments, new Comparator<Comment>(){
            public int compare(Comment cmt1, Comment cmt2) {
                return cmt2.getDateCreate().compareTo(cmt1.getDateCreate());
            }
        });
        mAdapter.setComments(comments);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
