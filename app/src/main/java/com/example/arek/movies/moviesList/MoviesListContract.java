package com.example.arek.movies.moviesList;

import com.example.arek.movies.BasePresenter;
import com.example.arek.movies.model.Movie;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 04.03.18.
 */

public interface MoviesListContract {

     interface View {

         void showProgressBar();

         void hideProgressBar();

         void showMovies(List<Movie> movies);

         void showLoadErrorMessage();
     }

     interface Presenter extends BasePresenter<MoviesListContract.View>{

         void loadMovies(int sortType);

         void loadMoreMovies(int SortType);

         void setFavorite(Movie movie);

         void unsetFavorite(Movie movie);
     }

}
