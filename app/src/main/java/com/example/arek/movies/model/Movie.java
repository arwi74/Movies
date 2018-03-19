
package com.example.arek.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Movie implements Parcelable{
    public static final int SORT_MODE_POPULAR = 0;
    public static final int SORT_MODE_TOP_RATED = 1;
    public static final int SORT_MODE_FAVORITES = 2;

    public Movie(){}

    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("id")
    private Long Id;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("title")
    private String title;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("vote_count")
    private Long voteCount;

    private boolean favorite = false;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Movie(Parcel in){
        boolean[] byteArray = new boolean[3];
        in.readBooleanArray(byteArray);
        adult = byteArray[0];
        video = byteArray[1];
        favorite = byteArray[2];
        backdropPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        title = in.readString();

        Id = in.readLong();
        voteCount = in.readLong();

        popularity = in.readDouble();
        voteAverage = in.readDouble();
        List<Integer> list = new ArrayList<>();
        in.readList(list,Long.class.getClassLoader());
        genreIds = list;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(new boolean[]{adult,video,favorite});

        dest.writeString(backdropPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(title);

        dest.writeLong(Id);
        dest.writeLong(voteCount);

        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);

        dest.writeList(genreIds);
    }

    public static final Creator CREATOR =new  Creator(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };
}
