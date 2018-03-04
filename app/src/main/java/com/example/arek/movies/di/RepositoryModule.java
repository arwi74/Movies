package com.example.arek.movies.di;

import android.app.Application;

import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.repository.MoviesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Arkadiusz Wilczek on 03.03.18.
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    MoviesRepository provideMoviesRepository(MovieDbApi movieDbApi){
        return new MoviesRepository(movieDbApi);
    }
}
