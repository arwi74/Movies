package com.example.arek.movies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.arek.movies.adapter.MoviesAdapter;
import com.example.arek.movies.databinding.ActivityMainBinding;
import com.example.arek.movies.model.Movie;
import com.example.arek.movies.model.MovieDbResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;
    private MovieDbResult mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        Toolbar toolbar = mBinding.toolbar;

        fakeData();
        mAdapter = new MoviesAdapter(mResult);

        mRecycler = mBinding.content.recyclerView;
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(this,3));



        setSupportActionBar(toolbar);


    }

    private void fakeData() {
        mResult = new MovieDbResult();
        ArrayList<Movie> movies = new ArrayList();
        for(int i =0;i<20;i++){
            Movie movie = new Movie();
            movie.setTitle("Title "+i);
            movies.add(movie);
        }
        mResult.setMovies(movies);
    }

}
