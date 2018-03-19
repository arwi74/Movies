
package com.example.arek.movies.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class ReviewsResult {

    @SerializedName("id")
    private Long mId;
    @SerializedName("page")
    private int mPage;
    @SerializedName("results")
    private List<Review> mReviews;
    @SerializedName("total_pages")
    private int mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public List<Review> getResults() {
        return mReviews;
    }

    public void setResults(List<Review> reviews) {
        mReviews = reviews;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        mTotalPages = totalPages;
    }

    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

}
