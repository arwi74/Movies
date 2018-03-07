package com.example.arek.movies.moviesList;

import com.example.arek.movies.model.Movie;
import com.example.arek.movies.repository.MoviesRepository;

import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 04.03.18.
 */

public class MoviesListPresenter implements MoviesListContract.Presenter {

    private MoviesRepository mRepository;
    private MoviesListContract.View mView;
    private DisposableObserver<List<Movie>> mDisposableObserver;

    public MoviesListPresenter(MoviesRepository repository){
        mRepository = repository;
    }

    @Override
    public void takeView(MoviesListContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void loadMovies(int sortType) {
        mView.showProgressBar();
        mRepository.getMovies(sortType, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDisposableObserver());
    }

    @Override
    public void loadMoreMovies(int sortType) {
        if ( mRepository.isMoreMovies() ) {
            mView.showProgressBar();
            mRepository.getMovies(sortType, false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getDisposableObserver());
        }
    }

    @Override
    public void setFavorite(Movie movie) {
        movie.setFavorite(!movie.isFavorite());
        if ( movie.isFavorite() )
            mRepository.saveFavoriteMovie(movie);
        else
            mRepository.deleteFavoriteMovie(movie);

    }

    @Override
    public void unsetFavorite(Movie movie) {

    }


    private DisposableObserver<List<Movie>> getDisposableObserver(){
        return new DisposableObserver<List<Movie>>() {
            @Override
            public void onNext(List<Movie> movies) {
                mView.hideProgressBar();
                mView.showMovies(movies);
            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgressBar();
                mView.showLoadErrorMessage();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void onMovieUpdate(Movie movie){

    }
}
