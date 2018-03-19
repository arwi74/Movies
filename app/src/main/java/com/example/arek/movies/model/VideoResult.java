
package com.example.arek.movies.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VideoResult {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private List<Video> mVideos;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
    }

}
