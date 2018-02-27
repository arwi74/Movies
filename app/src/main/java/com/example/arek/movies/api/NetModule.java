package com.example.arek.movies.api;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Arkadiusz Wilczek on 20.02.18.
 * Use dagger to provide retrofit client
 */

@Module
public class NetModule {
    private final Application mApp;

    public NetModule(Application app){
        mApp = app;
    }

    @Provides
    @Singleton
    Cache ProvideOkHttpCache(){
        int cacheSize = 10 * 1024 * 1024; //10MB
        return new Cache(mApp.getCacheDir(), cacheSize);
    }


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache){
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson(){
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    MovieDbApi provideRetrofitApiClient(Gson gson, OkHttpClient okHttpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbApi.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(MovieDbApi.class);
    }

}
