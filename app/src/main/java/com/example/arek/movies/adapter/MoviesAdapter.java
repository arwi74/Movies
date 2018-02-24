package com.example.arek.movies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arek.movies.R;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.model.MovieDbResult;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

/**
 * Created by Arkadiusz Wilczek on 19.02.18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    MovieDbResult mResult;
    MoviesAdapterOnClickHandler mOnClickHandler;

    public MoviesAdapter(MovieDbResult result,MoviesAdapterOnClickHandler onClickHandler){
        mResult = result;
        mOnClickHandler = onClickHandler;
    }

    public interface MoviesAdapterOnClickHandler{
        public void onMovieClick(Movie movie);
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
        holder.id.setText(mResult.getMovies().get(position).getTitle());


        Glide.with(holder.itemView)
                .load("http://image.tmdb.org/t/p/w185/"+mResult.getMovies().get(position).getPosterPath())
                .into(holder.poster);


    }

    public void swap(MovieDbResult result){
        if ( result != null ) {
            mResult = result;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if ( mResult != null )
            return mResult.getMovies().size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView poster;
        TextView id;
        public ViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_poster);
            id = itemView.findViewById(R.id.item_id);
            itemView.setOnClickListener(this);

//            ViewTreeObserver vto = poster.getViewTreeObserver();
//            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                public boolean onPreDraw() {
//                    poster.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                    int width = poster.getMeasuredWidth();
//
//                    int height = (int) (width * 1.5)+2;
//                    Log.d(LOG_TAG, "width=" + width + " height=" + height);
//                    poster.getLayoutParams().height = height;
//                    return true;
//                } });

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mResult.getMovies().get(position);
            mOnClickHandler.onMovieClick(movie);
        }
    }
}
