package com.example.arek.movies;

import android.app.Application;

import com.example.arek.movies.di.AppModule;
import com.example.arek.movies.di.DaggerRepositoryComponent;
import com.example.arek.movies.di.RepositoryComponent;
import com.example.arek.movies.di.NetModule;
import com.example.arek.movies.di.RepositoryModule;

/**
 * Created by Arkadiusz Wilczek on 27.02.18.
 * dagger module for provide movieDbApi retrofit client
 */

@SuppressWarnings("WeakerAccess")
public class MoviesApp extends Application {
    private RepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mRepositoryComponent = DaggerRepositoryComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .repositoryModule(new RepositoryModule())
                .build();

    }

    public RepositoryComponent getRepositoryComponent(){ return mRepositoryComponent; }

}
