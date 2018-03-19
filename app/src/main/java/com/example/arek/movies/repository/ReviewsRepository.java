package com.example.arek.movies.repository;

import android.support.annotation.NonNull;

import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.model.Review;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 07.03.18.
 * ReviewsRepository
 */

public class ReviewsRepository {
    MovieDbApi mMovieDbApi;
    long mMovieId;
    int mPage;
    int  mTotalPages;

    public ReviewsRepository(@NonNull MovieDbApi movieDbApi){
        mMovieDbApi = movieDbApi;
    }

    public Observable<List<Review>> getReviews(long movieId, boolean start){
        if ( start ){
            mPage = 1;
            mMovieId = movieId;
        }  else if ( movieId == mMovieId && mPage<mTotalPages ){
            mPage++;
        }
        return getReviews(movieId, mPage);
    }

    private Observable<List<Review>> getReviews(long movieId, int page){
        return mMovieDbApi.getReviews(movieId, page, "")
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    mTotalPages=result.getTotalPages();
                    return result.getResults();
                });
    }

    public boolean isMoreReviews(){
        if ( mMovieId != 0 && mPage < mTotalPages ) return true;
        return false;
    }
}
