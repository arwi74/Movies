package com.example.arek.movies.movieDetail.reviews;

import android.support.annotation.NonNull;

import com.example.arek.movies.model.Review;
import com.example.arek.movies.movieDetail.reviews.ReviewsContract;
import com.example.arek.movies.repository.ReviewsRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Arkadiusz Wilczek on 10.03.18.
 */

public class ReviewsPresenter implements ReviewsContract.Presenter {
    private ReviewsRepository mReviewsRepository;
    private ReviewsContract.View mView;
    private long mMovieId;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public ReviewsPresenter(@NonNull ReviewsRepository reviewsRepository) {
        mReviewsRepository = reviewsRepository;
    }

    @Override
    public void takeView(ReviewsContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        if ( !mCompositeDisposable.isDisposed() )
        mCompositeDisposable.dispose();
        mView = null;
    }

    @Override
    public void loadReviews(long movieId) {
        DisposableObserver disposable = getDisposableObserver();
        mMovieId = movieId;
        mView.showProgressBar();
        mView.hideNoReviewsInfo();
        mReviewsRepository.getReviews(mMovieId,true)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposable);
        mCompositeDisposable.add(disposable);
    }

    private void loadMoreReviews() {
        DisposableObserver disposable = getDisposableObserver();
        if ( mMovieId == 0 ) return;
        mReviewsRepository.getReviews(mMovieId,false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposable);
        mCompositeDisposable.add(disposable);
    }

    private DisposableObserver<List<Review>> getDisposableObserver() {
        return new DisposableObserver<List<Review>>() {
            @Override
            public void onNext(List<Review> reviews) {
                if ( reviews.isEmpty() ) mView.showNoReviewsInfo();
                mView.showReviews(reviews);
                if ( mReviewsRepository.isMoreReviews() ){
                    loadMoreReviews();
               //     mView.hideProgressBar();
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
