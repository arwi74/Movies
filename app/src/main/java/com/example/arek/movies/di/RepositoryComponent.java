package com.example.arek.movies.di;

import com.example.arek.movies.movieDetail.DetailActivity;
import com.example.arek.movies.moviesList.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Arkadiusz Wilczek on 27.02.18.
 * RepositoryComponent for dagger
 */
@Singleton
@Component(modules={RepositoryModule.class,NetModule.class,AppModule.class })
public interface RepositoryComponent {
    void inject(MainActivity activity);
    void inject(DetailActivity activity);
}
