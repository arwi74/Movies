package com.example.arek.movies.movieDetail;

import com.example.arek.movies.BasePresenter;
import com.example.arek.movies.model.Movie;

/**
 * Created by Arkadiusz Wilczek on 13.03.18.
 */

public interface MovieDetailContract {

    interface View {
        void setFavoriteButtonIcon(boolean favorite);

        void showMovieDetails(Movie movie);

        void showMovieGenres(String genres);
    }

    interface Presenter extends BasePresenter<MovieDetailContract.View>{

        void getMovieDetail();

        void changeStateFavoriteMovie();
    }
}
