package com.example.arek.movies.movieDetail;

import android.support.annotation.NonNull;

import com.example.arek.movies.model.Review;
import com.example.arek.movies.repository.ReviewsRepository;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 10.03.18.
 */

public class ReviewsPresenter implements ReviewsContract.Presenter {
    private ReviewsRepository mReviewsRepository;
    private ReviewsContract.View mView;
    private long mMovieId;

    public ReviewsPresenter(@NonNull ReviewsRepository reviewsRepository) {
        mReviewsRepository = reviewsRepository;
    }

    @Override
    public void takeView(ReviewsContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void loadReviews(long movieId) {
        mMovieId = movieId;
        mView.showProgressBar();
        mReviewsRepository.getReviews(mMovieId,true)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(getDisposableObserver());
    }

    private void loadMoreReviews() {
        if ( mMovieId == 0 ) return;
        mReviewsRepository.getReviews(mMovieId,true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDisposableObserver());
    }

    private DisposableObserver<List<Review>> getDisposableObserver() {
        return new DisposableObserver<List<Review>>() {
            @Override
            public void onNext(List<Review> reviews) {
                if ( reviews.isEmpty() ) mView.showNoReviewsInfo();
                mView.showReviews(reviews);
                if ( mReviewsRepository.isMoreReviews() ){
               //     loadMoreReviews();
                    mView.hideProgressBar();
                } else{
                    mView.hideProgressBar();
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgressBar();
                mView.hideNoReviewsInfo();
            }

            @Override
            public void onComplete() {

            }
        };
    }


}
