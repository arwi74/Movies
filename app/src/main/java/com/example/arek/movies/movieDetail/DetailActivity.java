package com.example.arek.movies.movieDetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.arek.movies.MoviesApp;
import com.example.arek.movies.R;
import com.example.arek.movies.databinding.ActivityDetailBinding;
import com.example.arek.movies.movieDetail.reviews.ReviewsFragment;
import com.example.arek.movies.movieDetail.videos.VideosFragment;
import com.example.arek.movies.moviesList.MainActivity;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.repository.MoviesRepository;
import com.example.arek.movies.repository.ReviewsRepository;
import com.example.arek.movies.repository.VideosRepository;
import com.example.arek.movies.utils.GlideApp;
import com.example.arek.movies.utils.UtilsImage;
import java.util.Locale;
import javax.inject.Inject;


public class DetailActivity extends AppCompatActivity implements MovieDetailContract.View{
    private ActivityDetailBinding mBinding;
    @Inject
    public VideosRepository mVideosRepository;
    @Inject
    public ReviewsRepository mReviewsRepository;
    @Inject
    public MoviesRepository mMovieRepository;
    private MovieDetailContract.Presenter mPresenter;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        ((MoviesApp) getApplication()).getRepositoryComponent().inject(this);

        Toolbar toolbar = mBinding.toolbar;

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar!=null ) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(MainActivity.EXTRA_DETAIL_MOVIE);

        mPresenter = new MovieDetailPresenter(mMovieRepository, mMovie);
        mPresenter.takeView(this);
        mPresenter.getMovieDetail();

        getSupportFragmentManager()
               .beginTransaction()
               .replace(R.id.detail_videos_fragment, VideosFragment.newInstance(mMovie.getId()))
               .replace(R.id.detail_reviews_fragment, ReviewsFragment.newInstance(mMovie.getId()))
               .commit();

        setFloatingFavoritesButton();
    }

    private void setFloatingFavoritesButton() {
        mBinding.detailFavoriteButton
                .setOnClickListener(v -> mPresenter.changeStateFavoriteMovie());
    }

    @Override
    public void setFavoriteButtonIcon(boolean favorite){
        if ( favorite ) {
            mBinding.detailFavoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else{
            mBinding.detailFavoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    @Override
    public void showMovieDetails(Movie movie) {
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

    @Override
    public void showMovieGenres(String genres) {
        mBinding.content.detailGenres.setText(genres);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.EXTRA_DETAIL_MOVIE, mMovie);
        setResult(MainActivity.CODE_REQUEST_FROM_DETAIL, returnIntent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }
}
