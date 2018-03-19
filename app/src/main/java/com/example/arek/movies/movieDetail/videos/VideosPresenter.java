package com.example.arek.movies.movieDetail.videos;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.arek.movies.model.Video;
import com.example.arek.movies.repository.VideosRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Arkadiusz Wilczek on 09.03.18.
 */

public class VideosPresenter implements VideosContract.Presenter {
    public static final String YOUTUBE_BASE_URL_WITH_PATH = "http://www.youtube.com/watch?v=";
    public static final String YOUTUBE_APP_URI = "vnd.youtube:";
    private VideosContract.View mView;
    private VideosRepository mRepository;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    public VideosPresenter(@NonNull VideosRepository videoRepository){
        mRepository = videoRepository;
    }

    @Override
    public void takeView(VideosContract.View view) { mView = view; }

    @Override
    public void dropView() {
        if ( !mCompositeDisposable.isDisposed() )
            mCompositeDisposable.dispose();
        mView=null; }

    @Override
    public void loadVideos(long movieId) {
        DisposableObserver disposable = getDisposableObserver();
        mView.showProgressBar();
        mRepository.getVideos(movieId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(disposable);
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void openVideo(Context context, String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_URI + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_BASE_URL_WITH_PATH + key));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private DisposableObserver<List<Video>> getDisposableObserver(){
        return new DisposableObserver<List<Video>>() {
            @Override
            public void onNext(List<Video> videos) {
                if ( videos.isEmpty() ){
                    mView.hideProgressBar();
                    mView.showNoVideosImage();
                } else {
                    mView.hideNoVideosImage();
                    mView.hideProgressBar();
                    mView.showVideos(videos);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
