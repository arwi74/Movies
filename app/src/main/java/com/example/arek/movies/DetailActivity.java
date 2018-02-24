package com.example.arek.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.arek.movies.databinding.ActivityDetailBinding;
import com.example.arek.movies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding mBinding;
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
        mBinding.content.detailOverwiew.setText(movie.getOverview());
        mBinding.content.detailVoteAvarge.setText(movie.getVoteAverage()+"");
        mBinding.content.releaseDate.setText(movie.getReleaseDate());


        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w185/"+movie.getPosterPath())
                .into(mBinding.content.detailPoster);

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w500/"+movie.getBackdropPath())
                .into(mBinding.toolbarImage);
//        Picasso.with(this)
//                .load("http://image.tmdb.org/t/p/w500/"+movie.getBackdropPath())
//                //.centerCrop()
//                //.resize(500,280)
//                //.fit()
//                .into(mBinding.content.detailBackdrop);

        Log.d(LOG_TAG,"http://image.tmdb.org/t/p/w185/"+movie.getPosterPath());
        Log.d(LOG_TAG,"http://image.tmdb.org/t/p/w185/"+movie.getBackdropPath());
    }




}
