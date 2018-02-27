package com.example.arek.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.arek.movies.databinding.ActivityDetailBinding;
import com.example.arek.movies.model.Movie;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding mBinding;
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail);

        Toolbar toolbar = mBinding.toolbar;

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar!=null ) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MainActivity.EXTRA_DETAIL_MOVIE);


        showDetail(movie);
        Log.d("DetailActivity",movie.getGenreIds().toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showDetail(Movie movie) {
        mBinding.content.detailTitle.setText(movie.getTitle());
        mBinding.collapsingToolbar.setTitle(movie.getTitle());
        mBinding.content.detailOverview.setText(movie.getOverview());
        mBinding.content.detailVoteAverage.setText(String.format(Locale.getDefault(),"%.1f",movie.getVoteAverage()) );
        mBinding.content.releaseDate.setText(movie.getReleaseDate());

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w185/"+movie.getPosterPath())
                .into(mBinding.content.detailPoster);

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w500/"+movie.getBackdropPath())
                .into(mBinding.toolbarImage);

        Log.d(LOG_TAG,"http://image.tmdb.org/t/p/w185/"+movie.getPosterPath());
        Log.d(LOG_TAG,"http://image.tmdb.org/t/p/w185/"+movie.getBackdropPath());
    }


}
