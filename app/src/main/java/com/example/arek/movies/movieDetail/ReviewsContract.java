package com.example.arek.movies.movieDetail;

import com.example.arek.movies.BasePresenter;
import com.example.arek.movies.model.Review;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 10.03.18.
 */

public interface ReviewsContract {

    interface View {
        void showProgressBar();

        void hideProgressBar();

        void showNoReviewsInfo();

        void hideNoReviewsInfo();

        void showReviews(List<Review> reviews);

        void showMoreReviews(List<Review> reviews);
    }

    interface Presenter extends BasePresenter<ReviewsContract.View> {
        void loadReviews(long movieId);
    }
}
