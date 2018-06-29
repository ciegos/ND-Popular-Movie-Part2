package com.mycodingdesk.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mycodingdesk.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Cesar Egoavil on 6/13/2018.
 */
@Dao
public interface MovieDao {
    @Query("select * from favorite_movie")
    LiveData<List<Movie>> listAll();

    @Query("select * from favorite_movie where id = :id")
    LiveData<Movie> getMovieById(int id);

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Query("delete from favorite_movie where id = :id")
    void deleteMovie(int id);
}
