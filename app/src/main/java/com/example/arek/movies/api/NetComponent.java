package com.example.arek.movies.api;

import com.example.arek.movies.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Arkadiusz Wilczek on 27.02.18.
 * NetComponent for dagger
 */
@Singleton
@Component(modules={NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
}
