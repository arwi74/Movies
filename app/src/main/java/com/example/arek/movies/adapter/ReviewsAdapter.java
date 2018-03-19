package com.example.arek.movies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arek.movies.R;
import com.example.arek.movies.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 10.03.18.
 * ReviewsAdapter
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Review> mReviews = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reviews_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         Review review = mReviews.get(position);

         holder.author.setText(review.getAuthor());
         holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void addReviews(List<Review> reviews){
        if ( reviews == null || reviews.isEmpty() ) return;
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView)itemView.findViewById(R.id.reviews_item_author);
            content = (TextView)itemView.findViewById(R.id.reviews_item_content);
        }
    }
}
