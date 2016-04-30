package com.dp.fflickr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dp.fflickr.R;
import com.dp.fflickr.activity.PhotoViewActivity;
import com.googlecode.flickrjandroid.photos.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DinhPhuc on 23/04/2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<Photo> mDataSet;
    private Context mContext;

    public static String PHOTO_POSITION = "flickrAdapter.photoPosition";
    public static String PHOTO_TITLE = "flickrAdapter.phototitle";

    public class ViewHolder extends RecyclerView.ViewHolder {
        //private View mView;
        private ImageView mImageView;
        private TextView mUserName;
        private TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            //mView = itemView;
            mImageView = (ImageView)itemView.findViewById(R.id.itemImage);
            mUserName = (TextView)itemView.findViewById(R.id.itemUserName);
            mTitle = (TextView)itemView.findViewById(R.id.itemTitle);
        }
    }

    public PhotoAdapter(Context context) {
        mDataSet = new ArrayList<Photo>();
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.flickr_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Photo photo = mDataSet.get(position);
        holder.mUserName.setText("by " + photo.getOwner().getUsername());
        holder.mTitle.setText(photo.getTitle());
        Glide.with(mContext).load(photo.getSmallUrl()).placeholder(R.drawable.loading).into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoViewActivity.class);
                intent.putExtra(PHOTO_POSITION, position);
                intent.putExtra(PHOTO_TITLE, holder.mTitle.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setDataSet(List<Photo> dataSet) {
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    public List<Photo> getDataSet() { return mDataSet; }

    public void add(Photo item) {
        mDataSet.add(item);
    }

    public void addPhotos(List<Photo> photos) {
        mDataSet.addAll(photos);
    }
}
