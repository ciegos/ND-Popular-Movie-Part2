package com.mycodingdesk.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mycodingdesk.popularmovies.database.AppDatabase;
import com.mycodingdesk.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Cesar Egoavil on 6/27/2018.
 */
public class DetailViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> movies;

    public DetailViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.movieDao().listAll();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
