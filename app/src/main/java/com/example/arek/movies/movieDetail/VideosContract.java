package com.example.arek.movies.movieDetail;

import android.content.Context;
import android.net.Uri;

import com.example.arek.movies.BasePresenter;
import com.example.arek.movies.model.Video;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 09.03.18.
 */

public interface VideosContract {

    interface View {
        void showVideos(List<Video> videos);

        void showProgressBar();

        void hideProgressBar();

        void showNoVideosImage();

        void hideNoVideosImage();
    }

    interface Presenter extends BasePresenter<VideosContract.View> {
        void loadVideos(long movieId);

        void openVideo(Context context, String key);
    }

}
