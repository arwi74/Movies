package com.example.arek.movies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.arek.movies.adapter.MoviesAdapter;
import com.example.arek.movies.api.ApiClient;
import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.databinding.ActivityMainBinding;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.model.MovieDbResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;

    private final static String THE_MOVIE_DB_API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        Toolbar toolbar = mBinding.toolbar;


        MovieDbApi movieDbApi = ApiClient.getInstance();

        Call<MovieDbResult> call = movieDbApi.getMoviesPopular(THE_MOVIE_DB_API_KEY);
        call.enqueue(mCallback);


        mAdapter = new MoviesAdapter(null);

        mRecycler = mBinding.content.recyclerView;
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this,3));



        setSupportActionBar(toolbar);


    }




    Callback<MovieDbResult> mCallback = new Callback<MovieDbResult>() {
        @Override
        public void onResponse(Call<MovieDbResult> call, Response<MovieDbResult> response) {
            Log.d("**",response.toString());
            if (response.isSuccessful()){
                MovieDbResult result = response.body();
                List<Movie> movies = result.getMovies();
                Log.d("**","start");
                for(Movie movie:movies){
                    Log.d("**",movie.getTitle());
                }
                Log.d("**","stop");
                mAdapter.swap(result);
            }
        }

        @Override
        public void onFailure(Call<MovieDbResult> call, Throwable t) {
            t.printStackTrace();
        }
    };

}
