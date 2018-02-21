package com.example.arek.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.arek.movies.adapter.MoviesAdapter;
import com.example.arek.movies.api.ApiClient;
import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.databinding.ActivityMainBinding;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.model.MovieDbResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private ActivityMainBinding mBinding;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;

    public static final int DISPLAY_MODE_POPULAR = 0;
    public static final int DISPLAY_MODE_TOP_RATED = 1;
    public int mDisplayMode = DISPLAY_MODE_POPULAR;

    public static final String EXTRA_DETAIL_MOVIE = "detail_movie";

    private final static String THE_MOVIE_DB_API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        Toolbar toolbar = mBinding.toolbar;





        loadMovies( mDisplayMode );


        mAdapter = new MoviesAdapter(null,this);

        mRecycler = mBinding.content.recyclerView;
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this,3));



        setSupportActionBar(toolbar);


    }

    public void loadMovies(int displayMode){
        MovieDbApi movieDbApi = ApiClient.getInstance();
        Call<MovieDbResult> call;
        if ( displayMode == DISPLAY_MODE_POPULAR ){
            call = movieDbApi.getMoviesPopular(THE_MOVIE_DB_API_KEY,1);
            call.enqueue(mCallback);
        }else if ( displayMode == DISPLAY_MODE_TOP_RATED ){
            call = movieDbApi.getMoviesTopRated(THE_MOVIE_DB_API_KEY,1);
            call.enqueue(mCallback);
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem){
            case R.id.action_menu_popular:
                mDisplayMode = DISPLAY_MODE_POPULAR;
                break;
            case R.id.action_menu_top_rated:
                mDisplayMode = DISPLAY_MODE_TOP_RATED;
                break;

        }
        loadMovies(mDisplayMode);
        return super.onOptionsItemSelected(item);
    }


    private void openDetail(Movie movie) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL_MOVIE,movie);
        startActivity(intent);
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

    @Override
    public void onMovieClick(Movie movie) {
        Toast.makeText(this,movie.getTitle(),Toast.LENGTH_LONG).show();
        openDetail(movie);
    }

}
