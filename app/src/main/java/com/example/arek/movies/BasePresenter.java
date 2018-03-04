package com.example.arek.movies;

/**
 * Created by Arkadiusz Wilczek on 04.03.18.
 */

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();

}
