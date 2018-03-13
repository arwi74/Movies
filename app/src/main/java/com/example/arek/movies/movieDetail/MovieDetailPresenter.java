package com.example.arek.movies.movieDetail;

import android.support.annotation.NonNull;

import com.example.arek.movies.model.Genre;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.repository.MoviesRepository;
import com.example.arek.movies.utils.DbUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Arkadiusz Wilczek on 13.03.18.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private MoviesRepository mMoviesRepository;
    private Movie mMovie;
    private MovieDetailContract.View mView;
    private DisposableObserver mDisposable;

    public MovieDetailPresenter(@NonNull MoviesRepository movieRepository, @NonNull Movie movie){
        mMovie = movie;
        mMoviesRepository = movieRepository;
    }

    @Override
    public void takeView(MovieDetailContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        if ( mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
        mView = null;
    }

    @Override
    public void getMovieDetail() {
        mView.showMovieDetails(mMovie);
        mView.setFavoriteButtonIcon(mMovie.isFavorite());
        getGenres();
    }

    @Override
    public void changeStateFavoriteMovie() {
        mMovie.setFavorite(!mMovie.isFavorite());
        mView.setFavoriteButtonIcon(mMovie.isFavorite());
        if( mMovie.isFavorite() ) {
            mMoviesRepository.saveFavoriteMovie(mMovie);
        }else{
            mMoviesRepository.deleteFavoriteMovie(mMovie);
        }
    }

    private void getGenres(){
        mDisposable = getObserver();
        mMoviesRepository.getGenres()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDisposable);
    }

    private DisposableObserver<List<Genre>> getObserver(){
        return new DisposableObserver<List<Genre>>() {
            @Override
            public void onNext(List<Genre> genres) {
                displayGenres(mMovie, genres);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

    }

    private void displayGenres(Movie movie,List<Genre> genres) {
        String genresString = DbUtils.getGenresFromIds(movie.getGenreIds(), genres);
        mView.showMovieGenres(genresString);
    }

}
