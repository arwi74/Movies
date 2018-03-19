package com.example.arek.movies.movieDetail.reviews;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arek.movies.MoviesApp;
import com.example.arek.movies.R;
import com.example.arek.movies.adapter.ReviewsAdapter;
import com.example.arek.movies.databinding.FragmentReviewsBinding;
import com.example.arek.movies.model.Review;
import com.example.arek.movies.repository.ReviewsRepository;

import java.util.List;

import javax.inject.Inject;


public class ReviewsFragment extends Fragment implements ReviewsContract.View{
    private static final String ARG_MOVIE_ID = "param1";
    private FragmentReviewsBinding mBinding;
    private long mMovieId;
    private ReviewsPresenter mPresenter;
    private ReviewsAdapter mAdapter;
    @Inject
    public ReviewsRepository mReviewsRepository;

    public ReviewsFragment() {
    }


    public static ReviewsFragment newInstance(long param1) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_MOVIE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieId = getArguments().getLong(ARG_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_reviews,container,false);
        setRecyclerView();

        ((MoviesApp)getActivity().getApplication()).getRepositoryComponent().inject(this);

        mPresenter = new ReviewsPresenter(mReviewsRepository);
        mPresenter.takeView(this);
        mPresenter.loadReviews(mMovieId);
        setRecyclerView();
        return mBinding.getRoot();
    }

    private void setRecyclerView() {
        mAdapter = new ReviewsAdapter();
        RecyclerView recycler = mBinding.reviewsRecyclerView;
        recycler.setLayoutManager(
                new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(mAdapter);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.dropView();
    }

    @Override
    public void showProgressBar() {
        mBinding.reviewsProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mBinding.reviewsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoReviewsInfo() {
        mBinding.reviewsNoReviewsText.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoReviewsInfo() {
        mBinding.reviewsNoReviewsText.setVisibility(View.GONE);
    }

    @Override
    public void showReviews(List<Review> reviews) {
        mAdapter.addReviews(reviews);
    }

    @Override
    public void showMoreReviews(List<Review> reviews) {
        mAdapter.addReviews(reviews);
    }

}
