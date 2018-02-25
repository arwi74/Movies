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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;
    private List<Movie> mMovies = new ArrayList<>();


    public static final int DISPLAY_MODE_POPULAR = 0;
    public static final int DISPLAY_MODE_TOP_RATED = 1;
    public int mDisplayMode = DISPLAY_MODE_POPULAR;
    private int mPage = 1;
    private boolean mNotifyChanges = false;
    private boolean mLoading = false;

    public static final String EXTRA_DETAIL_MOVIE = "detail_movie";

    private final static String THE_MOVIE_DB_API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        Toolbar toolbar = mBinding.toolbar;
        showTitle();




        loadMovies();


        mAdapter = new MoviesAdapter(mMovies,this);

        mRecycler = mBinding.content.recyclerView;
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this,2));

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int itemCount = layoutManager.getItemCount();
                if ( itemCount>0 && (lastVisibleItem > itemCount - 4) && !mLoading ){
                    Log.d(LOG_TAG,"loading more request");
                        loadMoreMovies();
                        mLoading=true;
                }

            }
        });



        setSupportActionBar(toolbar);


    }

    public void showProgressBar(){
        mBinding.content.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        mBinding.content.progressBar.setVisibility(View.GONE);
    }

    public void showTitle(){
        if ( mDisplayMode ==  DISPLAY_MODE_POPULAR ){
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_popular));
        }else{
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_top_rated));
        }

    }




    public void loadMovies(){
        mPage=1;
        showProgressBar();
        loadMoviesPage(mDisplayMode,mPage);
        mNotifyChanges = true;
    }

    public void loadMoreMovies(){
        showProgressBar();
        mPage++;
        loadMoviesPage(mDisplayMode,mPage);
        mNotifyChanges = false;
    }



    private void loadMoviesPage(int displayMode,int page){
        MovieDbApi movieDbApi = ApiClient.getInstance(this);
        Call<MovieDbResult> call;
        if ( displayMode == DISPLAY_MODE_POPULAR ){
            call = movieDbApi.getMoviesPopular(THE_MOVIE_DB_API_KEY, page);
        }else{
            call = movieDbApi.getMoviesTopRated(THE_MOVIE_DB_API_KEY, page);
        }
        call.enqueue(mCallback);
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
        loadMovies();
        showTitle();
        return super.onOptionsItemSelected(item);
    }


    private void openDetailActivity(Movie movie) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL_MOVIE,movie);
        startActivity(intent);
    }

    public void showMovies(List<Movie> movies){
        mLoading=false;
        hideProgressBar();
        if ( mNotifyChanges ) {
            mAdapter.swap(movies);
          //  mAdapter.notifyDataSetChanged();
            mRecycler.scrollToPosition(1);
        }else{
            mAdapter.addMovies(movies);
        }

        Log.d(LOG_TAG,"Notify data set");
    //    mAdapter.notifyDataSetChanged();

      //  if (mPage<3) loadMoreMovies();
    }


    Callback<MovieDbResult> mCallback = new Callback<MovieDbResult>() {
        @Override
        public void onResponse(Call<MovieDbResult> call, Response<MovieDbResult> response) {
            Log.d("**",response.toString());
            if (response.isSuccessful()){
                MovieDbResult result = response.body();
                List<Movie> movies = result.getMovies();
                        //mAdapter.notifyDataSetChanged();
                showMovies(movies);
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
        openDetailActivity(movie);
    }

}
