
package com.example.arek.movies.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
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

    public List<Video> getResults() {
        return mVideos;
    }

    public void setResults(List<Video> videos) {
        mVideos = videos;
    }

}
