package com.example.arek.movies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arek.movies.R;
import com.example.arek.movies.model.MovieDbResult;

import java.util.zip.Inflater;

/**
 * Created by Arkadiusz Wilczek on 19.02.18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    MovieDbResult mResult;

    public MoviesAdapter(MovieDbResult result){
        mResult = result;
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView id;
        public ViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_poster);
            id = itemView.findViewById(R.id.item_id);
        }
    }
}
