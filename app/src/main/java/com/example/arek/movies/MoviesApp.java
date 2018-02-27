package com.example.arek.movies;

import android.app.Application;

import com.example.arek.movies.api.DaggerNetComponent;
import com.example.arek.movies.api.NetComponent;
import com.example.arek.movies.api.NetModule;

/**
 * Created by Arkadiusz Wilczek on 27.02.18.
 * dagger module for provide movieDbApi retrofit client
 */

public class MoviesApp extends Application {
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(this))
                .build();
    }

    public NetComponent getNetComponent(){
        return mNetComponent;
    }
}
