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
import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.databinding.ActivityMainBinding;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.model.MovieDbResult;
import com.example.arek.movies.repository.MoviesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;
    @SuppressWarnings("WeakerAccess")
    @Inject
    public MovieDbApi movieDbApi;
    @Inject
    public MoviesRepository mMoviesRepository;

    private int mSortMode = Movie.SORT_MODE_POPULAR;

    private int mPage = 1;
    private boolean mSwapData = false;
    private boolean mLoading = false;

    public static final String EXTRA_DETAIL_MOVIE = "detail_movie";

    private DisposableObserver<MovieDbResult> disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        showTitle();

        ((MoviesApp) getApplication()).getRepositoryComponent().inject(this);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        setRecyclerView();
        loadMovies();
        mMoviesRepository.test();
    }

    private void setRecyclerView(){
        int numRows = getResources().getInteger(R.integer.numRows);
        mRecycler = mBinding.content.recyclerView;
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this, numRows));
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int itemCount = layoutManager.getItemCount();
                if (itemCount > 0 && (lastVisibleItem > itemCount - 4) && !mLoading) {
                    Log.d(LOG_TAG, "loading more request");
                    loadMoreMovies();
                    mLoading = true;
                }
            }
        });
    }

    private void showProgressBar() {
        mBinding.content.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.content.progressBar.setVisibility(View.GONE);
    }

    private void showTitle() {
        if (mSortMode == Movie.SORT_MODE_POPULAR) {
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_popular));
        } else {
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_top_rated));
        }
    }

    private void loadMovies() {
        mPage = 1;
        loadMoviesPage(mSortMode, mPage);
        mSwapData = true;
    }

    private void loadMoreMovies() {
        mPage++;
        loadMoviesPage(mSortMode, mPage);
        mSwapData = false;
    }

    private void loadMoviesPage(int displayMode, int page) {
        showProgressBar();
        String language = Locale.getDefault().getLanguage();
        disposable = getDisposableObserver();
        if (displayMode == Movie.SORT_MODE_POPULAR) {
            movieDbApi.getMoviesPopular(page, language)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(disposable);
        } else {
            movieDbApi.getMoviesTopRated(page, language)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(disposable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    private DisposableObserver<MovieDbResult> getDisposableObserver(){
        return new DisposableObserver<MovieDbResult>() {
            @Override
            public void onNext(MovieDbResult movieDbResult) {
                hideProgressBar();
                mLoading = false;
                showMovies(movieDbResult.getMovies());
            }

            @Override
            public void onError(Throwable e) {
                hideProgressBar();
                mLoading = false;
                showLoadErrorMessage();
            }

            @Override
            public void onComplete() {
                hideProgressBar();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem itemSort = menu.findItem(R.id.action_menu_popular_top_rated);
        setOptionSortIcon(itemSort);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();

        if (menuItem == R.id.action_menu_popular_top_rated) {
            switchSortMode();
            setOptionSortIcon(item);
            loadMovies();
            showTitle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchSortMode() {
        if (mSortMode == Movie.SORT_MODE_POPULAR)
            mSortMode = Movie.SORT_MODE_TOP_RATED;
        else
            mSortMode = Movie.SORT_MODE_POPULAR;
    }

    private void setOptionSortIcon(MenuItem item) {
        if (mSortMode == Movie.SORT_MODE_POPULAR) {
            item.setIcon(R.drawable.ic_star_half_white_24dp);
        } else {
            item.setIcon(R.drawable.ic_star_white_24dp);
        }
    }

    private void openDetailActivity(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL_MOVIE, movie);
        startActivity(intent);
    }

    private void showMovies(List<Movie> movies) {
        if (mSwapData) {
            mAdapter.swap(movies);
            mRecycler.scrollToPosition(1);
        } else {
            mAdapter.addMovies(movies);
        }
    }

    private void showLoadErrorMessage() {
        Log.d(LOG_TAG, "error message");
        Toast.makeText(this, getString(R.string.load_error_message), Toast.LENGTH_LONG).show();
        hideProgressBar();
    }

    @Override
    public void onMovieClick(Movie movie) {
        openDetailActivity(movie);
    }

}
