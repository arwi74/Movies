package com.example.arek.movies.moviesList;

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

import com.example.arek.movies.movieDetail.DetailActivity;
import com.example.arek.movies.MoviesApp;
import com.example.arek.movies.R;
import com.example.arek.movies.adapter.MoviesAdapter;
import com.example.arek.movies.api.MovieDbApi;
import com.example.arek.movies.databinding.ActivityMainBinding;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.repository.MoviesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        MoviesListContract.View {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;
    @SuppressWarnings("WeakerAccess")
    @Inject
    public MovieDbApi movieDbApi;
    @Inject
    public MoviesRepository mMoviesRepository;
    public MoviesListContract.Presenter mPresenter;

    private int mSortMode = Movie.SORT_MODE_POPULAR;

    private boolean mSwapData = false;
    private boolean mLoading = false;

    public static final String EXTRA_DETAIL_MOVIE = "detail_movie";

    private static final String STATE_SCROLL_POSITION = "state_recycler_position";
    private static final String STATE_SORT_MODE = "state_sort_mode";
    private int mScrollPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);

        if ( savedInstanceState!=null
                && savedInstanceState.containsKey(STATE_SCROLL_POSITION)
                && savedInstanceState.containsKey(STATE_SORT_MODE)) {
            mScrollPosition = savedInstanceState.getInt(STATE_SCROLL_POSITION);
            mSortMode = savedInstanceState.getInt(STATE_SORT_MODE);
            Log.d(LOG_TAG,"restore position: " + mScrollPosition);
        }

        showTitle();

        ((MoviesApp) getApplication()).getRepositoryComponent().inject(this);

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        setRecyclerView();
        mPresenter = new MoviesListPresenter(mMoviesRepository);
        mPresenter.takeView(this);
        mPresenter.loadMovies(mSortMode);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int position = ((GridLayoutManager)mRecycler.getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(LOG_TAG,"save position: "+position);
        outState.putInt(STATE_SCROLL_POSITION, position);
        outState.putInt(STATE_SORT_MODE, mSortMode);
        super.onSaveInstanceState(outState);
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
                    mPresenter.loadMoreMovies(mSortMode);
                    mLoading = true;
                }
            }
        });
    }

    public void showProgressBar() {
        mBinding.content.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mBinding.content.progressBar.setVisibility(View.GONE);
    }

    private void showTitle() {
        if (mSortMode == Movie.SORT_MODE_POPULAR) {
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_popular));
        } else {
            mBinding.mainTitleInfo.setText(getString(R.string.main_activity_title_top_rated));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
     //   MenuItem itemSort = menu.findItem(R.id.action_menu_popular_top_rated);
       // setOptionSortIcon(itemSort);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();

//        if (menuItem == R.id.action_menu_popular_top_rated) {
//            switchSortMode();
//            setOptionSortIcon(item);
//            mSwapData = true;
//            mPresenter.loadMovies(mSortMode);
//
//        }
        if (menuItem == R.id.action_menu_popular){
            if ( switchSortMode(Movie.SORT_MODE_POPULAR) ){
                mPresenter.loadMovies(mSortMode);
            }
        }else if (menuItem == R.id.action_menu_top_rated){
            if ( switchSortMode(Movie.SORT_MODE_TOP_RATED) ){
                mPresenter.loadMovies(mSortMode);
            }
        }else if (menuItem == R.id.action_menu_favorite){
            if ( switchSortMode(Movie.SORT_MODE_FAVORITES) ){
                mPresenter.loadMovies(mSortMode);
            }
        }

        showTitle();
        return super.onOptionsItemSelected(item);
    }

    private boolean switchSortMode(int sortMode) {
        if ( mSortMode == sortMode ) return false;

        mSortMode = sortMode;
        mSwapData = true;
        return true;
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

    public void showMovies(List<Movie> movies) {
        if (mSwapData) {
            mAdapter.swap(movies);
            mRecycler.scrollToPosition(1);
        } else {
            mAdapter.addMovies(movies);
        }
        mSwapData = false;
        mLoading = false;
        if ( mScrollPosition !=0 ){
            mRecycler.scrollToPosition(mScrollPosition);
            mScrollPosition = 0;
        }
    }

    public void showLoadErrorMessage() {
        Log.d(LOG_TAG, "error message");
        Toast.makeText(this, getString(R.string.load_error_message), Toast.LENGTH_LONG).show();
        mLoading = false;
    }

    @Override
    public void onMovieClick(Movie movie) {
        openDetailActivity(movie);
    }

}
