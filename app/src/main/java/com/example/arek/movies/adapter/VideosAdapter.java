package com.example.arek.movies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arek.movies.R;
import com.example.arek.movies.model.Video;
import com.example.arek.movies.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 09.03.18.
 * VideosAdapter
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
    public static final String YOUTUBE_IMAGE_BASE_URL = "https://img.youtube.com/vi";
    public static final String YOUTUBE_IMAGE_VARIANT = "0.jpg";
    List<Video> mVideos = new ArrayList<>();
    VideosAdapterOnClickListener mVideosOnClickListener;
    private Context mContext;

    public VideosAdapter(VideosAdapterOnClickListener videosAdapterOnClickListener, Context context){
        mVideosOnClickListener = videosAdapterOnClickListener;
        mContext = context;
    }


    public interface VideosAdapterOnClickListener{
        void onVideoClick(String videoKey);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.videos_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mVideos.get(position);

        holder.videoTitle.setText(video.getName());

        Uri uri = buildVideoUri(video.getKey());

        GlideApp.with(mContext)
                .load(uri)
                .error(R.drawable.ic_broken_image_grey_24dp)
                .into(holder.videoImage);

    }


    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void addVideos(List<Video> videos){
        if ( videos == null || videos.isEmpty() ) return;
        mVideos.clear();
        mVideos.addAll(videos);
        notifyDataSetChanged();
    }

    public Uri buildVideoUri(String key) {
        return Uri.parse(YOUTUBE_IMAGE_BASE_URL)
                .buildUpon()
                .appendPath(key)
                .appendPath(YOUTUBE_IMAGE_VARIANT)
                .build();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView videoImage;
        TextView videoTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            videoTitle = (TextView) itemView.findViewById(R.id.item_title);
            videoImage = (ImageView)itemView.findViewById(R.id.item_video_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Video video = mVideos.get(position);

            mVideosOnClickListener.onVideoClick(video.getKey());
        }

    }
}
