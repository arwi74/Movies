
package com.example.arek.movies.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class GenreResult {

    @SerializedName("genres")
    private List<Genre> mGenres;

    public List<Genre> getGenres() {
        return mGenres;
    }

    public void setGenres(List<Genre> genres) {
        mGenres = genres;
    }

}
