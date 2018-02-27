package com.example.arek.movies.api;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.arek.movies.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
                .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl httpUrl = request.url();

                            HttpUrl  newUrl = httpUrl.newBuilder()
                                    .addQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
                                    .build();

                            Request.Builder requestBuilder = request.newBuilder()
                                    .url(newUrl);

                            Request newRequest = requestBuilder.build();
                            return chain.proceed(newRequest);
                    }
                })
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
