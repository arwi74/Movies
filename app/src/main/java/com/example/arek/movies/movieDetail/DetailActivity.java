package com.example.arek.movies.movieDetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.arek.movies.R;
import com.example.arek.movies.databinding.ActivityDetailBinding;
import com.example.arek.movies.moviesList.MainActivity;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.utils.GlideApp;
import com.example.arek.movies.utils.UtilsImage;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

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

        Uri posterUri = UtilsImage.buildImagePath(UtilsImage.SIZE_W185, movie.getPosterPath());
        Uri backdropUri = UtilsImage.buildImagePath(UtilsImage.SIZE_W500, movie.getBackdropPath());

        GlideApp.with(this)
                .load(posterUri)
                .error(R.drawable.ic_broken_image_grey_24dp)
                .into(mBinding.content.detailPoster);

        GlideApp.with(this)
                .load(backdropUri)
                .error(R.drawable.ic_broken_image_grey_24dp)
                .into(mBinding.toolbarImage);
    }


}
