package com.example.arek.movies.moviesList;

import com.example.arek.movies.model.Movie;
import com.example.arek.movies.repository.MoviesRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Arkadiusz Wilczek on 04.03.18.
 * MoviesListPresenter
 */

public class MoviesListPresenter implements MoviesListContract.Presenter {

    private MoviesRepository mRepository;
    private MoviesListContract.View mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MoviesListPresenter(MoviesRepository repository){
        mRepository = repository;
    }

    @Override
    public void takeView(MoviesListContract.View view) {
        mView = view;
    }

    @Override    public void dropView() {
        if (mCompositeDisposable!= null && !mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
        }
        mView = null;
    }

    @Override
    public void loadMovies(int sortType) {
        DisposableObserver<List<Movie>> disposable = getDisposableObserver();
        mView.showProgressBar();
        mRepository.getMovies(sortType, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposable);
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMoreMovies(int sortType) {
        if ( mRepository.isMoreMovies() ) {
            DisposableObserver<List<Movie>> disposable = getDisposableObserver();
            mView.showProgressBar();
            mRepository.getMovies(sortType, false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(disposable);
            mCompositeDisposable.add(disposable);
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


}
