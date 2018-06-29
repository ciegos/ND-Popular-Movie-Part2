package com.mycodingdesk.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Cesar Egoavil on 6/24/2018.
 */
public class MovieTrailerResult {
    private int id;
    @SerializedName("results")
    private List<MovieTrailer> result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieTrailer> getResult() {
        return result;
    }

    public void setResult(List<MovieTrailer> result) {
        this.result = result;
    }
}
