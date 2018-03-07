package com.example.arek.movies.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.example.arek.movies.R;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.utils.GlideApp;
import com.example.arek.movies.utils.UtilsImage;

import java.util.List;
import java.util.Locale;
import java.util.Observable;

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
        void onFavoriteClick(Movie movie);
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
        holder.setFavoriteIcon(movie);
//        Log.d(LOG_TAG, UtilsImage.buildImagePath(
//                UtilsImage.SIZE_W185,
//                mMovies.get(position).getPosterPath()).toString());

        Uri posterUri = UtilsImage.buildImagePath(UtilsImage.SIZE_W185, mMovies.get(position).getPosterPath());

        GlideApp.with(holder.itemView)
                .load(posterUri)
                .error(R.drawable.ic_broken_image_grey_24dp)
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
        final ImageView favorite;
        ViewHolder(final View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_poster);
            title = itemView.findViewById(R.id.item_movie_title);
            vote = itemView.findViewById(R.id.item_movie_vote);
            favorite = itemView.findViewById(R.id.item_favorite_icon);
            itemView.setOnClickListener(this);
            favorite.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mMovies.get(position);

            if ( v.getId() == R.id.item_favorite_icon){
                    favoriteClick(movie);
            } else {
                mOnClickHandler.onMovieClick(movie);
            }
        }

        void favoriteClick(Movie movie){
            mOnClickHandler.onFavoriteClick(movie);
            setFavoriteIcon(movie);
        }

        void setFavoriteIcon(Movie movie){
            if ( movie.isFavorite() )
                favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
            else
                favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }



}
