package com.dp.fflickr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dp.fflickr.R;
import com.googlecode.flickrjandroid.photos.comments.Comment;
import com.googlecode.flickrjandroid.util.UrlUtilities;
import com.makeramen.roundedimageview.RoundedImageView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DinhPhuc on 25/04/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> mComments;
    private Context mContext;
    private PrettyTime mPrettyTime;

    public CommentAdapter(Context context) {
        mContext = context;
        mComments = new ArrayList<Comment>();
        mPrettyTime = new PrettyTime();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.mUserName.setText(comment.getAuthorName());
        holder.mComment.setText(Html.fromHtml(comment.getText()));
        String time = mPrettyTime.format(comment.getDateCreate());
        holder.mTime.setText(time);
        String profileImageUrl = UrlUtilities.createBuddyIconUrl(comment.getIconFarm(), comment.getIconServer(), comment.getAuthor());
        Glide.with(mContext).load(profileImageUrl).into(holder.mProfileImage);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    public List<Comment> getCommentList() { return mComments;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView mProfileImage;
        private TextView mUserName;
        private TextView mComment;
        private TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mProfileImage = (RoundedImageView)itemView.findViewById(R.id.commentProfileImage);
            mUserName = (TextView)itemView.findViewById(R.id.commentUserName);
            mComment = (TextView)itemView.findViewById(R.id.commentContent);
            mTime = (TextView)itemView.findViewById(R.id.commentTime);
        }
    }
}
