package com.mycodingdesk.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Cesar Egoavil on 6/24/2018.
 */
public class MovieReviewResult {
    private int id;
    @SerializedName("results")
    private List<MovieReview> result;
    private int page;
    private int total_pages;
    private int total_results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieReview> getResult() {
        return result;
    }

    public void setResult(List<MovieReview> result) {
        this.result = result;
    }
}
