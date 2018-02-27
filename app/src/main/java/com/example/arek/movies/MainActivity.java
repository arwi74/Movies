package com.example.arek.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;


    private static final int SORT_MODE_POPULAR = 0;
    private static final int SORT_MODE_TOP_RATED = 1;
    private int mSortMode = SORT_MODE_POPULAR;
    private int mPage = 1;
    private boolean mSwapData = false;
    private boolean mLoading = false;

    public static final String EXTRA_DETAIL_MOVIE = "detail_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        Toolbar toolbar = mBinding.toolbar;
        showTitle();




        loadMovies();


        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);

        mRecycler = mBinding.content.recyclerView;
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this,2));

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

    private void showProgressBar(){
        mBinding.content.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        mBinding.content.progressBar.setVisibility(View.GONE);
    }

    private void showTitle(){
        if ( mSortMode == SORT_MODE_POPULAR){
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_popular));
        }else{
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_top_rated));
        }

    }




    private void loadMovies(){
        mPage=1;
        showProgressBar();
        loadMoviesPage(mSortMode,mPage);
        mSwapData = true;
    }

    private void loadMoreMovies(){
        showProgressBar();
        mPage++;
        loadMoviesPage(mSortMode,mPage);
        mSwapData = false;
    }



    private void loadMoviesPage(int displayMode,int page){
        MovieDbApi movieDbApi = ApiClient.getInstance(this);
        Call<MovieDbResult> call;
        String language = Locale.getDefault().getLanguage();
        if ( displayMode == SORT_MODE_POPULAR){
            call = movieDbApi.getMoviesPopular(BuildConfig.THE_MOVIE_DB_API_KEY, page, language);
        }else{
            call = movieDbApi.getMoviesTopRated(BuildConfig.THE_MOVIE_DB_API_KEY, page, language);
        }
        call.enqueue(mCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem itemSort = menu.findItem(R.id.action_menu_popular_top_rated);
        setOptionSortIcon(itemSort);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();

        if ( menuItem == R.id.action_menu_popular_top_rated) {
            switchSortMode();
            setOptionSortIcon(item);
            loadMovies();
            showTitle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchSortMode(){
        if ( mSortMode == SORT_MODE_POPULAR )
            mSortMode=SORT_MODE_TOP_RATED;
        else
            mSortMode=SORT_MODE_POPULAR;
    }


    private void setOptionSortIcon(MenuItem item){
        if ( mSortMode == SORT_MODE_POPULAR ) {
            item.setIcon(R.drawable.ic_star_half_white_24dp);
        }else {
            item.setIcon(R.drawable.ic_star_white_24dp);
        }

    }

    private void openDetailActivity(Movie movie) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL_MOVIE,movie);
        startActivity(intent);
    }

    private void showMovies(List<Movie> movies){
        mLoading=false;
        hideProgressBar();
        if (mSwapData) {
            mAdapter.swap(movies);
            mRecycler.scrollToPosition(1);
        }else{
            mAdapter.addMovies(movies);
        }
    }


    private final Callback<MovieDbResult> mCallback = new Callback<MovieDbResult>() {
        @Override
        public void onResponse(@NonNull Call<MovieDbResult> call,@NonNull Response<MovieDbResult> response) {
            Log.d("**",response.toString());
            if (response.isSuccessful()){
                MovieDbResult result = response.body();

                if ( result!=null && result.getMovies()!= null ) {
                    List<Movie> movies = result.getMovies();
                    showMovies(movies);
                }

            } else{
                showLoadErrorMessage();
            }
        }

        @Override
        public void onFailure(@NonNull Call<MovieDbResult>  call,@NonNull Throwable t) {
            showLoadErrorMessage();
            t.printStackTrace();
        }
    };

    private void showLoadErrorMessage() {
        Log.d(LOG_TAG,"error message");
        Toast.makeText(this,getString(R.string.load_error_message),Toast.LENGTH_LONG).show();
        hideProgressBar();
    }

    @Override
    public void onMovieClick(Movie movie) {
        openDetailActivity(movie);
    }

}
