package com.example.arek.movies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arek.movies.R;
import com.example.arek.movies.model.Movie;

import java.util.List;
import java.util.Locale;

/**
 * Created by Arkadiusz Wilczek on 19.02.18.
 * adapter for displaying movie posters
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    private final List<Movie> mMovies;
    private final MoviesAdapterOnClickHandler mOnClickHandler;

    public MoviesAdapter(List<Movie> movies,MoviesAdapterOnClickHandler onClickHandler){
        mOnClickHandler = onClickHandler;
        mMovies = movies;
    }

    public interface MoviesAdapterOnClickHandler{
        void onMovieClick(Movie movie);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        holder.title.setText( movie.getTitle() );
        holder.vote.setText( String.format(Locale.getDefault(),"%.1f",movie.getVoteAverage()) );

        Glide.with(holder.itemView)
                .load("http://image.tmdb.org/t/p/w185/"+ mMovies.get(position).getPosterPath())
                .into(holder.poster);
    }

    public void swap(List<Movie> movies){
        if ( movies == null ) return;
            mMovies.clear();
            mMovies.addAll(movies);
            notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies){
            if ( movies == null ) return;
            mMovies.addAll(movies);
            notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if ( mMovies != null )
            return mMovies.size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView poster;
        final TextView title;
        final TextView vote;
        ViewHolder(final View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_poster);
            title = itemView.findViewById(R.id.item_movie_title);
            vote = itemView.findViewById(R.id.item_movie_vote);
            itemView.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mMovies.get(position);
            mOnClickHandler.onMovieClick(movie);
        }
    }


}
