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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends Fragment implements ReviewsContract.View{
    private static final String ARG_MOVIE_ID = "param1";
    private FragmentReviewsBinding mBinding;
    private long mMovieId;
    private ReviewsPresenter mPresenter;
    private ReviewsAdapter mAdapter;
    @Inject
    public ReviewsRepository mReviewsRepository;

    private OnFragmentInteractionListener mListener;

    public ReviewsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        mBinding.reviewsNoReviewsImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoReviewsInfo() {
        mBinding.reviewsNoReviewsImage.setVisibility(View.GONE);
    }

    @Override
    public void showReviews(List<Review> reviews) {
        mAdapter.addReviews(reviews);
    }

    @Override
    public void showMoreReviews(List<Review> reviews) {
        mAdapter.addReviews(reviews);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
