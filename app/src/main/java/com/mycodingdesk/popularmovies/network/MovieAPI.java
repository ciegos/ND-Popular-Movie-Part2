package com.mycodingdesk.popularmovies.network;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycodingdesk.popularmovies.model.MovieResult;
import com.mycodingdesk.popularmovies.model.MovieReviewResult;
import com.mycodingdesk.popularmovies.model.MovieTrailerResult;
import com.mycodingdesk.popularmovies.util.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Cesar Egoavil on 5/1/2018.
 */
public class MovieAPI {
    public static final String BASE_TRAILER_IMAGE_URL = "https://img.youtube.com/vi/%s/0.jpg";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3/";
    private static final String TOP_RATED_URL = "movie/top_rated";
    private static final String POPULAR_URL = "movie/popular";
    private static final String TRAILER_URL = "movie/%s/videos";
    private static final String REVIEW_URL = "movie/%s/reviews";
    private static final String API_QUERY_KEY = "api_key";
    private static final String IMAGE_SMART = "w154";
    private static final String IMAGE_TABLET = "w185";
    private static final String IMAGE_BACKDROP_TABLET = "w500";
    private static final String IMAGE_BACKDROP_SMART = "w342";

    public static URL buildURL(int searchType){
        Uri.Builder builder = Uri.parse(BASE_MOVIE_URL).buildUpon();
        switch (searchType){
            case 0:
                builder.appendEncodedPath(POPULAR_URL);
                break;
            case 1:
                builder.appendEncodedPath(TOP_RATED_URL);
                break;
            default:
                builder.appendEncodedPath(POPULAR_URL);
                break;
        }
        builder.appendQueryParameter(API_QUERY_KEY, Constant.API_KEY);

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
        }

        return url;
    }

    public static URL buildImageURL(boolean isTablet){
        Uri.Builder builder = Uri.parse(BASE_IMAGE_URL).buildUpon();
        if(isTablet)
            builder.appendPath(IMAGE_TABLET);
        else
            builder.appendPath(IMAGE_SMART);
        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
        }

        return url;
    }

    public static URL buildMovieTrailerURL(int movieId){
        Uri.Builder builder = Uri.parse(BASE_MOVIE_URL).buildUpon();
        builder.appendEncodedPath(String.format(TRAILER_URL, movieId));
        builder.appendQueryParameter(API_QUERY_KEY, Constant.API_KEY);
        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
        }

        return url;
    }

    public static URL buildMovieReviewURL(int movieId){
        Uri.Builder builder = Uri.parse(BASE_MOVIE_URL).buildUpon();
        builder.appendEncodedPath(String.format(REVIEW_URL, movieId));
        builder.appendQueryParameter(API_QUERY_KEY, Constant.API_KEY);
        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
        }

        return url;
    }

    public static URL buildImageBackdropURL(boolean isTablet){
        Uri.Builder builder = Uri.parse(BASE_IMAGE_URL).buildUpon();
        if(isTablet)
            builder.appendPath(IMAGE_BACKDROP_TABLET);
        else
            builder.appendPath(IMAGE_BACKDROP_SMART);
        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
        }

        return url;
    }

    public static MovieResult getMovieList(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try{
            Gson gson = new GsonBuilder().create();
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            if(scanner.hasNext()) {
                String jsonData = scanner.useDelimiter("\\A").next();
                return gson.fromJson(jsonData, MovieResult.class);
            }
            else
                return null;
        }
        finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
    }

    public static MovieTrailerResult getMovieTrailerList(URL url) {
        HttpURLConnection urlConnection = null;
        MovieTrailerResult movieTrailerResult = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            Gson gson = new GsonBuilder().create();
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            if(scanner.hasNext()) {
                String jsonData = scanner.useDelimiter("\\A").next();
                movieTrailerResult = gson.fromJson(jsonData, MovieTrailerResult.class);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
        return movieTrailerResult;
    }

    public static MovieReviewResult getMovieReviewList(URL url) {
        HttpURLConnection urlConnection = null;
        MovieReviewResult movieReviewResult = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            Gson gson = new GsonBuilder().create();
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            if(scanner.hasNext()) {
                String jsonData = scanner.useDelimiter("\\A").next();
                movieReviewResult = gson.fromJson(jsonData, MovieReviewResult.class);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
        return movieReviewResult;
    }
}
